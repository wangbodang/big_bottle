package com.vefuture.big_bottle.common.util;

/**
 * @author wangb
 * @date 2025/4/20
 * @description TODO: 类描述
 */
// ImageSourceDetector.java (Java 8‑compatible v1.4)
// ------------------------------------------------------------
// 1) ImageOrigin 判定（AI 生成 / 相机拍摄）
// 2) DeviceType 判定（SMARTPHONE / MIRRORLESS_DSLR / ACTION_CAM / AI_GENERATOR / UNKNOWN）
// 3) 新增 URL 支持：detectDeviceType(URL) – 直接从网络图片 URL 判定设备类型
//
// 依赖：metadata‑extractor 2.19.0 + Hutool‑core 5.8.x
// ------------------------------------------------------------
/*
<dependency>
  <groupId>com.drewnoakes</groupId>
  <artifactId>metadata-extractor</artifactId>
  <version>2.19.0</version>
</dependency>
*/

import cn.hutool.core.util.StrUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ImageSourceDetector {

    // ---------------- Enums ----------------
    public enum ImageOrigin {
        AI_GENERATED, CAMERA_CAPTURE, UNKNOWN
    }

    public enum DeviceType {
        SMARTPHONE, MIRRORLESS_DSLR, ACTION_CAM, AI_GENERATOR, UNKNOWN
    }

    // ---------------- Keyword dictionaries ----------------
    private static final Set<String> AI_KEYWORDS;
    private static final Set<String> SMARTPHONE_BRANDS;

    static {
        Set<String> ai = new HashSet<String>();
        Collections.addAll(ai,
                "midjourney", "stable diffusion", "dall-e", "dall·e", "dalle",
                "automatic1111", "sd-webui", "invokeai", "runwayml",
                "com.openai", "openai_dalle", "firefly", "bing image creator");
        AI_KEYWORDS = Collections.unmodifiableSet(ai);

        Set<String> phone = new HashSet<String>();
        Collections.addAll(phone,
                "apple", "iphone", "huawei", "xiaomi", "samsung", "oneplus",
                "google", "oppo", "vivo", "honor", "meizu", "sony ericsson", "realme");
        SMARTPHONE_BRANDS = Collections.unmodifiableSet(phone);
    }

    // =============== Public API ===============
    /** Detect image origin (AI vs Camera) from File */
    public static ImageOrigin detectOrigin(File file) throws IOException, ImageProcessingException {
        try (InputStream in = new java.io.FileInputStream(file)) {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return decideOrigin(meta);
        }
    }

    /** Detect image origin (AI vs Camera) from InputStream (stream will be closed) */
    public static ImageOrigin detectOrigin(InputStream in) throws IOException, ImageProcessingException {
        try {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return decideOrigin(meta);
        } finally {
            if (in != null) in.close();
        }
    }

    /** Detect device type from File */
    public static DeviceType detectDeviceType(File file) throws IOException, ImageProcessingException {
        try (InputStream in = new java.io.FileInputStream(file)) {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return classifyDevice(meta);
        }
    }

    /** Detect device type from InputStream (stream will be closed) */
    public static DeviceType detectDeviceType(InputStream in) throws IOException, ImageProcessingException {
        try {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return classifyDevice(meta);
        } finally {
            if (in != null) in.close();
        }
    }

    /** Detect device type directly from an image URL */
    public static DeviceType detectDeviceType(URL url) throws IOException, ImageProcessingException {
        try (InputStream in = url.openStream()) {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return classifyDevice(meta);
        }
    }

    // Convenience overload for String URL
    public static DeviceType detectDeviceType(String urlString) throws IOException, ImageProcessingException {
        return detectDeviceType(new URL(urlString));
    }

    // =============== Core decision logic ===============
    private static ImageOrigin decideOrigin(Metadata metadata) {
        int aiScore = 0;
        int camScore = 0;

        boolean hasMakeModel = false;
        boolean exposureValid = false;
        boolean hasGps = false;
        boolean hasMakerNote = false;
        boolean hasSubSec = false;

        for (Directory dir : metadata.getDirectories()) {
            for (Tag tag : dir.getTags()) {
                String value = tag.getDescription();
                if (value == null) continue;
                String lower = value.toLowerCase(Locale.ENGLISH);

                if (tag.getTagName().matches("(?i)(software|processing software|image description|user comment|toolkit)")) {
                    for (String kw : AI_KEYWORDS) {
                        if (lower.contains(kw)) {
                            aiScore += 1;
                            break;
                        }
                    }
                }
            }

            if (dir instanceof CanonMakernoteDirectory || dir.getName().toLowerCase(Locale.ENGLISH).contains("makernote")) {
                if (!dir.getTags().isEmpty()) hasMakerNote = true;
            }

            if (dir instanceof XmpDirectory) {
                try {
                    String dump = ((XmpDirectory) dir).getXMPMeta().dumpObject();
                    if (dump != null) {
                        String lowerXmp = dump.toLowerCase(Locale.ENGLISH);
                        if (lowerXmp.contains("c2pa") && lowerXmp.contains("createai")) aiScore += 2;
                    }
                } catch (Exception ignore) {}
            }
        }

        ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (ifd0 != null) {
            String make = ifd0.getString(ExifIFD0Directory.TAG_MAKE);
            String model = ifd0.getString(ExifIFD0Directory.TAG_MODEL);
            if (!StrUtil.isBlank(make) && !StrUtil.isBlank(model) && !"unknown".equalsIgnoreCase(make)) hasMakeModel = true;
        }

        ExifSubIFDDirectory sub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (sub != null) {
            Rational fNum = sub.getRational(ExifSubIFDDirectory.TAG_FNUMBER);
            Rational exp = sub.getRational(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            Integer iso = sub.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            exposureValid = fNum != null && exp != null && iso != null &&
                    fNum.doubleValue() >= 0.95 && exp.doubleValue() > 0 && iso > 0;
            String subSec = sub.getString(ExifSubIFDDirectory.TAG_SUBSECOND_TIME_ORIGINAL);
            if (!StrUtil.isBlank(subSec)) hasSubSec = true;
        }

        GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        hasGps = gpsDir != null && gpsDir.getGeoLocation() != null;

        if (hasMakeModel) camScore++;
        if (exposureValid) camScore++;
        if (hasGps || hasMakerNote || hasSubSec) camScore++;
        if (!hasMakeModel) aiScore++;

        if (aiScore >= 2 && aiScore > camScore) return ImageOrigin.AI_GENERATED;
        if (camScore >= 2 && camScore >= aiScore) return ImageOrigin.CAMERA_CAPTURE;
        return ImageOrigin.UNKNOWN;
    }

    private static DeviceType classifyDevice(Metadata meta) {
        ExifIFD0Directory ifd0 = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
        String make = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MAKE) : null;
        String model = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MODEL) : null;

        // 1) AI generator quick path – look at Software/XMP
        for (Directory dir : meta.getDirectories()) {
            for (Tag tag : dir.getTags()) {
                String value = tag.getDescription();
                if (value == null) continue;
                String l = value.toLowerCase(Locale.ENGLISH);
                if (tag.getTagName().matches("(?i)(software|toolkit|user comment|image description)")) {
                    for (String kw : AI_KEYWORDS) if (l.contains(kw)) return DeviceType.AI_GENERATOR;
                }
            }
        }

        // 2) Smartphone by Make brand
        if (!StrUtil.isBlank(make) && SMARTPHONE_BRANDS.stream().anyMatch(b -> make.toLowerCase(Locale.ENGLISH).contains(b))) {
            return DeviceType.SMARTPHONE;
        }

        // 3) Action camera keywords in Model
        if (!StrUtil.isBlank(model)) {
            String mdl = model.toLowerCase(Locale.ENGLISH);
            if (mdl.contains("gopro") || mdl.contains("osmo") || mdl.contains("insta360")) return DeviceType.ACTION_CAM;

            // 4) Mirrorless / DSLR heuristics
            if (mdl.startsWith("ilce") || mdl.startsWith("eos") || mdl.matches("d[0-9]{3,4}.*") || mdl.matches("z[5-9].*")) {
                return DeviceType.MIRRORLESS_DSLR;
            }
        }
        return DeviceType.UNKNOWN;
    }
}
