package org.example;

import java.time.Duration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
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

        String bucketName = "copy-of";
        String keyName = "athena/summary_data.txt";
        Region region = Region.AP_NORTHEAST_1;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();

        signBucket(presigner, bucketName, keyName);
        presigner.close();
    }

    // snippet-start:[presigned.java2.generatepresignedurl.main]
    public static void signBucket(S3Presigner presigner, String bucketName, String keyName) {

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
            System.out.println("Presigned URL to upload a file to: " +myURL);


        } catch (S3Exception e) {
            e.getStackTrace();
        }
    }
    // snippet-end:[presigned.java2.generatepresignedurl.main]
}


