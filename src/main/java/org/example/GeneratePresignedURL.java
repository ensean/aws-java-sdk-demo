package org.example;

import java.time.Duration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
// snippet-end:[presigned.java2.generatepresignedurl.import]

/**
 * To run this AWS code example, ensure that you have setup your development environment, including your AWS credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 */

public class GeneratePresignedURL {

    public static void main(String[] args) {

        String bucketName = "bucket-xxxx";  // 桶名
        String keyName = "imgs/wl.jpg";     // S3桶中文件路径
        Region region = Region.AP_SOUTHEAST_1;  // S3 桶所在区域
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();
        // create signed url for upload
        signBucketForUpload(presigner, bucketName, keyName);

        // create signed url for download
        signBucketForDownload(presigner, bucketName, keyName);
        presigner.close();
    }

    // snippet-start:[presigned.java2.generatepresignedurl.main]
    // 生成下载链接
    public static void signBucketForDownload(S3Presigner presigner, String bucketName, String keyName) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);


            String myURL = presignedRequest.url().toString();
            System.out.println("Presigned URL to download a file to: " +myURL);


        } catch (S3Exception e) {
            e.getStackTrace();
        }
    }

    // 生成上传链接
    public static void signBucketForUpload(S3Presigner presigner, String bucketName, String keyName) {

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("image/jpeg")  // content type 需要根据实际文件类型指定
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);


            String myURL = presignedRequest.url().toString();
            System.out.println("Presigned URL to upload a file to: " +myURL);


        } catch (S3Exception e) {
            e.getStackTrace();
        }
    }
    // snippet-end:[presigned.java2.generatepresignedurl.main]
}


