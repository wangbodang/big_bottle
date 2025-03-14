package com.vefuture.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * 测试 google 云存储
 * java API URL:
 * https://cloud.google.com/java/docs/setup?hl=zh-cn#install_a_jdk_java_development_kit
 * https://cloud.google.com/storage/docs/uploading-objects?hl=zh-CN#storage-upload-object-java
 */
@Slf4j
public class GoogleCloudStorageTest {

    //C:\Workspace\Idea_projects_2024\big_bottle\DOCS\json\galvanized-case-453511-n7-6bd15d67c233.json
    @Test
    public void testUpload02(){
        String projectId = "galvanized-case-453511-n7";
        String bucketName = "bigbootle";
        String objectName = "bill3.jpg";
        String filePath = "C:\\Workspace\\Test\\demo\\bill3.jpg";
        String contentType = "image/jpg";
        String credentialsFilePath = "C:\\Workspace\\Idea_projects_2024\\big_bottle\\DOCS\\json\\galvanized-case-453511-n7-6bd15d67c233.json";
        try {
            String imgUrl = uploadObjectByJsonKey(projectId, bucketName, objectName, filePath, credentialsFilePath, contentType);
            log.info("===> 上传图片成功 图片URL:{}", imgUrl);
            String signUrl = generateV4SignedUrlWithContentType(projectId,bucketName,objectName,credentialsFilePath);
            log.info("===> 签名图片URL:{}", signUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //上传文件返回可用图径
    public String uploadObjectByJsonKey(String projectId, String bucketName, String objectName, String filePath, String credentialsFilePath, String contentType) throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream(credentialsFilePath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        storage.createFrom(blobInfo, Paths.get(filePath));
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName + " with content-type " + contentType);
        return "https://storage.cloud.google.com/" + bucketName + "/" + objectName;
    }
    //生成签名图片地址
    public static String generateV4SignedUrlWithContentType(String projectId, String bucketName, String objectName, String credentialsFilePath) throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream(credentialsFilePath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build().getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = storage.get(blobId);

        URL signedUrl = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withContentType());

        return signedUrl.toString();
    }

    public String uploadObjectByJsonKey(String projectId, String bucketName, String objectName, String filePath, String credentialsFilePath) throws IOException {
        // 显式加载 JSON 密钥文件
        FileInputStream serviceAccountStream = new FileInputStream(credentialsFilePath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

        // 使用加载的凭据创建 Storage 实例
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build().getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/"+"png").build();
        storage.createFrom(blobInfo, Paths.get(filePath));
        System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);

        return "https://storage.cloud.google.com/" + bucketName + "/" + objectName;
    }



    //
    @Test
    public void testUpload01(){
        String projectId = "galvanized-case-453511-n7";
        String bucketName = "bigbootle";
        String objectName = "bill01.png";
        String filePath = "C:\\Workspace\\Test\\demo\\demo.png";
        try {
            uploadFile(projectId, bucketName, objectName, filePath);
            log.info("===> 上传图片成功!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //上传文件到goole cloud storage
    public void uploadFile(String projectId, String bucketName, String objectName, String filePath) throws IOException {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        // The ID of your GCS object
        // String objectName = "your-object-name";

        // The path to your file to upload
        // String filePath = "path/to/your/file"

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Optional: set a generation-match precondition to avoid potential race
        // conditions and data corruptions. The request returns a 412 error if the
        // preconditions are not met.
        Storage.BlobWriteOption precondition;
        if (storage.get(bucketName, objectName) == null) {
            // For a target object that does not yet exist, set the DoesNotExist precondition.
            // This will cause the request to fail if the object is created before the request runs.
            precondition = Storage.BlobWriteOption.doesNotExist();
        } else {
            // If the destination already exists in your bucket, instead set a generation-match
            // precondition. This will cause the request to fail if the existing object's generation
            // changes before the request runs.
            precondition =
                    Storage.BlobWriteOption.generationMatch(
                            storage.get(bucketName, objectName).getGeneration());
        }
        storage.createFrom(blobInfo, Paths.get(filePath), precondition);

        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
        log.info("===> File:{} uploaded to bucket:{} as objectName:{}", filePath, bucketName, objectName);
    }
}
