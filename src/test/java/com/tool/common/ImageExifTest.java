package com.tool.common;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.vefuture.big_bottle.common.util.ImageSourceDetector;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Slf4j
public class ImageExifTest {

    private String imgPath ="C:\\Workspace\\Test\\demo\\";

    //带EXIF的照片
    @Test
    public void test01() throws ImageProcessingException, IOException {

        File imageFile = new File("C:\\Workspace\\Test\\demo\\papasu.jpg");

        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }



    //微信传过来
    @Test
    public void test02() throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File("C:\\Workspace\\Test\\demo\\papasu.jpg"));

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().equals("Make") || tag.getTagName().equals("Model")) {
                    System.out.println(tag.getTagName() + ": " + tag.getDescription());
                }
            }
        }
    }



    //带EXIF的照片
    @Test
    public void test03() throws ImageProcessingException, IOException {

        File imageFile = new File(imgPath+"DSC02529_3k.jpg");

        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }

    //从AWS的图片来查询EXIF信息
    //https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/DSC02529_3k.jpg

    @Test
    public void test04(){
        try {
            //String imageUrl = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/DSC02529_3k.jpg"; // 替换为你的图片 URL
            //String imageUrl = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/DSC02529_3k.jpg"; // 索尼相机的
            //String imageUrl = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/4acb46f7-77ae-4073-ac7f-cff2740a58df.jpg"; // 苹果手机的
            //String imageUrl = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/f4827a8c-5bd9-44e7-b833-7d18cf594655.JPG"; // 替换为你的图片 URL
            String imageUrl = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/IMG_20250331_230309.jpg"; // 小米手机的
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag);
                    }
                }

                // 可选：输出错误信息
                for (Directory directory : metadata.getDirectories()) {
                    for (String error : directory.getErrors()) {
                        System.err.println("ERROR: " + error);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //AI图片
    @Test
    public void test05() throws ImageProcessingException, IOException {

        //String  imgFileName = "ai_02.png";
        //String  imgFileName = "IMG_20250331_230309.jpg"; //小米的
        String  imgFileName = "S__5734418.jpg";
        Metadata metadata = ImageMetadataReader.readMetadata(new File("C:\\Workspace\\Test\\demo\\" + imgFileName));
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
        System.out.println("===================================================================================");
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().equals("Make") || tag.getTagName().equals("Model")) {
                    System.out.println(tag.getTagName() + ": " + tag.getDescription());
                }
            }
        }
    }
    /* 判断本地文件 */
    @Test
    public void testImgAi(){

        String  imgFileName = "ai_02.png";
        //String  imgFileName = "IMG_20250331_230309.jpg"; //小米的
        //String  imgFileName = "papasu.jpg";
        /*ImageSourceDetector.ImageOrigin detect = ImageSourceDetector.detectOrigin(new File(imgPath + imgFileName));
        log.info("---> imgFileName:[{}] === [{}]", imgFileName, detect);*/

    }

    /* 判断网络文件 */
    @Test
    public void testImgStream(){
        String imageUrl1 = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/DSC02529_3k.jpg"; // 替换为你的图片 URL
        String imageUrl2 = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/DSC02529_3k.jpg"; // 索尼相机的
        String imageUrl3 = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/4acb46f7-77ae-4073-ac7f-cff2740a58df.jpg"; // 苹果手机的
        String imageUrl4 = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/f4827a8c-5bd9-44e7-b833-7d18cf594655.JPG"; // 替换为你的图片 URL
        String imageUrl5 = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/IMG_20250331_230309.jpg"; // 小米手机的

        List<String> imgList = Arrays.asList(new String[]{
                imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5,
                "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/df0aac23-9bad-4e90-a7e3-911ccbdbc7cf.jpg",

        });
        imgList.stream().forEach(imageUrl ->{
            try {
                URL url = new URL(imageUrl);
//                ImageSourceDetector.ImageOrigin detect = ImageSourceDetector.detectOrigin(url.openStream());
//                ImageSourceDetector.DeviceType deviceType = ImageSourceDetector.detectDeviceType(url.openStream());
//                log.info("-->{} - {} - {}", imageUrl.substring(imageUrl1.lastIndexOf("/")+1), detect, deviceType);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
