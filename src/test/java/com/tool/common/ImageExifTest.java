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

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Slf4j
public class ImageExifTest {

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
}
