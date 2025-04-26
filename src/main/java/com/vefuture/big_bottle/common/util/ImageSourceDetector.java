// ImageSourceDetector.java (Java 8‑compatible v2.1)
// ------------------------------------------------------------
// • DetectionResult 一次输出 ImageOrigin + DeviceType
// • 两个枚举均增加 (code, desc) 字段，可方便序列化/国际化
// ------------------------------------------------------------
package com.vefuture.big_bottle.common.util;

import cn.hutool.core.util.StrUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ImageSourceDetector {

    // ---------------- Enums with (code, desc) ----------------
    public enum ImageOrigin {
        AI_GENERATED(0, "AI生成"),
        CAMERA_CAPTURE(1, "相机/手机拍摄"),
        UNKNOWN(2, "未知");

        private final int code; private final String desc;
        ImageOrigin(int code, String desc) { this.code = code; this.desc = desc; }
        public int getCode()  { return code; }
        public String getDesc() { return desc; }
    }

    public enum DeviceType {
        AI_GENERATOR(0, "AI生成器"),
        SMARTPHONE(1, "智能手机"),
        MIRRORLESS_DSLR(2, "单反/微单相机"),
        ACTION_CAM(3, "运动相机"),
        UNKNOWN(4, "未知");

        private final int code; private final String desc;
        DeviceType(int code, String desc) { this.code = code; this.desc = desc; }
        public int getCode()  { return code; }
        public String getDesc() { return desc; }
    }

    // ---------------- Result wrapper ----------------
    public static class DetectionResult {
        private final ImageOrigin origin;
        private final DeviceType deviceType;
        public DetectionResult(ImageOrigin origin, DeviceType deviceType) {
            this.origin = origin; this.deviceType = deviceType; }
        public ImageOrigin getOrigin()     { return origin;     }
        public DeviceType  getDeviceType() { return deviceType; }
        @Override public String toString() {
            return "DetectionResult{" +
                    "origin=" + origin +
                    "(" + origin.getCode() + "), " +
                    "deviceType=" + deviceType +
                    "(" + deviceType.getCode() + ")}"; }
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

    // =============== Public Detect API ===============
    public static DetectionResult detect(File file) throws IOException, ImageProcessingException {
        try (InputStream in = new FileInputStream(file)) {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return new DetectionResult(decideOrigin(meta), classifyDevice(meta));
        }
    }

    public static DetectionResult detect(InputStream in) throws IOException, ImageProcessingException {
        try {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return new DetectionResult(decideOrigin(meta), classifyDevice(meta));
        } finally { if (in != null) in.close(); }
    }

    public static DetectionResult detect(URL url) throws IOException, ImageProcessingException {
        try (InputStream in = url.openStream()) {
            Metadata meta = ImageMetadataReader.readMetadata(in);
            return new DetectionResult(decideOrigin(meta), classifyDevice(meta));
        }
    }

    public static DetectionResult detect(String url) throws IOException, ImageProcessingException {
        return detect(new URL(url));
    }

    // =============== Internal logic (unchanged) ===============
    private static ImageOrigin decideOrigin(Metadata metadata) {
        int aiScore = 0, camScore = 0;
        boolean hasMakeModel = false, exposureValid = false, hasGps = false, hasMakerNote = false, hasSubSec = false;

        for (Directory dir : metadata.getDirectories()) {
            for (Tag tag : dir.getTags()) {
                String val = tag.getDescription(); if (val == null) continue;
                String lower = val.toLowerCase(Locale.ENGLISH);
                if (tag.getTagName().matches("(?i)(software|processing software|image description|user comment|toolkit)"))
                    for (String kw : AI_KEYWORDS) if (lower.contains(kw)) { aiScore++; break; }
            }
            if (dir instanceof CanonMakernoteDirectory || dir.getName().toLowerCase(Locale.ENGLISH).contains("makernote"))
                if (!dir.getTags().isEmpty()) hasMakerNote = true;
            if (dir instanceof XmpDirectory) {
                try {
                    String dump = ((XmpDirectory) dir).getXMPMeta().dumpObject();
                    if (dump != null) {
                        String lx = dump.toLowerCase(Locale.ENGLISH);
                        if (lx.contains("c2pa") && lx.contains("createai")) aiScore += 2;
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
            Rational f = sub.getRational(ExifSubIFDDirectory.TAG_FNUMBER);
            Rational e = sub.getRational(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
            Integer iso = sub.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
            exposureValid = f != null && e != null && iso != null && f.doubleValue() >= 0.95 && e.doubleValue() > 0 && iso > 0;
            String subSec = sub.getString(ExifSubIFDDirectory.TAG_SUBSECOND_TIME_ORIGINAL);
            if (!StrUtil.isBlank(subSec)) hasSubSec = true;
        }

        hasGps = metadata.getFirstDirectoryOfType(com.drew.metadata.exif.GpsDirectory.class) != null;

        if (hasMakeModel) camScore++; if (exposureValid) camScore++; if (hasGps || hasMakerNote || hasSubSec) camScore++; if (!hasMakeModel) aiScore++;
        if (aiScore >= 2 && aiScore > camScore) return ImageOrigin.AI_GENERATED;
        if (camScore >= 2 && camScore >= aiScore) return ImageOrigin.CAMERA_CAPTURE;
        return ImageOrigin.UNKNOWN;
    }

    private static DeviceType classifyDevice(Metadata meta) {
        ExifIFD0Directory ifd0 = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
        String make = ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MAKE) : null;
        String model= ifd0 != null ? ifd0.getString(ExifIFD0Directory.TAG_MODEL): null;

        // AI generator hint
        for (Directory dir : meta.getDirectories())
            for (Tag tag : dir.getTags()) {
                String v = tag.getDescription(); if (v == null) continue;
                String l = v.toLowerCase(Locale.ENGLISH);
                if (tag.getTagName().matches("(?i)(software|toolkit|user comment|image description)"))
                    for (String kw : AI_KEYWORDS) if (l.contains(kw)) return DeviceType.AI_GENERATOR;
            }

        if (!StrUtil.isBlank(make) && SMARTPHONE_BRANDS.stream().anyMatch(b -> make.toLowerCase(Locale.ENGLISH).contains(b)))
            return DeviceType.SMARTPHONE;

        if (!StrUtil.isBlank(model)) {
            String mdl = model.toLowerCase(Locale.ENGLISH);
            if (mdl.contains("gopro") || mdl.contains("osmo") || mdl.contains("insta360")) return DeviceType.ACTION_CAM;
            if (mdl.startsWith("ilce") || mdl.startsWith("eos") || mdl.matches("d[0-9]{3,4}.*") || mdl.matches("z[5-9].*"))
                return DeviceType.MIRRORLESS_DSLR;
        }
        return DeviceType.UNKNOWN;
    }
}
