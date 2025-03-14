package com.vechain.upload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 测试Amzon S3上传服务
 */
@Slf4j
public class AmazonS3UploadTest {


    /**
     * 测试AmzonS3存储服务
     *
     * @param
     * @return  返回值说明
     */

    @Test
    public void testAmzonS3Upload(){

        Regions clientRegion = Regions.DEFAULT_REGION;
        /*String bucketName = "*** Bucket name ***";
        String stringObjKeyName = "*** String object key name ***";
        String fileObjKeyName = "*** File object key name ***";
        String fileName = "*** Path to file to upload ***";*/

        String bucketName = "bvefuturebigbottle";
        String stringObjKeyName = "";

        /*
        String fileObjKeyName = "images/2025/03/15/_MG_1425_3k.jpg";
        String fileName = "E:\\CANON2025A\\2025_03_14\\3k\\_MG_1425_3k.jpg";
        */

        String fileObjKeyName = "images/2025/03/15/_MG_1425_3k.jpg";
        String fileName = "E:\\CANON2025A\\2025_03_14\\3k\\xxxx.png";
        String objectKey = "uploads/xxxx.png";
        String contentType = "image/png";

        // https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/_MG_1368_3k.jpg
        // String url = "https://" + bucketName + ".s3.amazonaws.com/" + objectKey;

        try {
            // This code expects that you have AWS credentials set up per:
            // https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html
            /*
            AKIASE5KRNSZ44TF27OH
            p6ZE/wmq50C9A4b42donsb0qJ7NYWWtSQMXaTvBm
             */
            //BasicAWSCredentials awsCreds = new BasicAWSCredentials("你的AccessKey", "你的SecretKey");
            BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIASE5KRNSZ44TF27OH", "p6ZE/wmq50C9A4b42donsb0qJ7NYWWtSQMXaTvBm");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    //.withRegion(Regions.AP_NORTHEAST_1) // 你的 S3 区域
                    .withRegion(Regions.AP_SOUTHEAST_2) // 你的 S3 区域
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();


            /*
            // Upload a text string as a new object.
            s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");

            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/"+"jpeg");
            metadata.addUserMetadata("title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
            */
            // 1. 创建文件对象
            File file = new File(fileName);
            //String key = "uploads/image.jpg";
            // 2. 设置元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType); // 确保 Content-Type 正确
            metadata.addUserMetadata("title", "UserUploadedImage"); // Key 需要是小写

            // 3. 上传文件
            PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, file);
            request.setMetadata(metadata);

            s3Client.putObject(request);
            // https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/_MG_1368_3k.jpg
            String targetUrl = "https://" + bucketName + ".s3.ap-southeast-2.amazonaws.com/" + objectKey;;
            log.info("===> 上传成功 url:[{}]", targetUrl);

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

    }
}
