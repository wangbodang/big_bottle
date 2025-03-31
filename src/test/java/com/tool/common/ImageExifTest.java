package com.tool.common;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
        //
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

}
