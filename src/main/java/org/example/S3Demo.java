package org.example;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;
import software.amazon.awssdk.services.ivs.model.CreateChannelRequest;
import software.amazon.awssdk.services.ivs.model.CreateChannelResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.util.HashMap;
import java.util.Properties;

public class S3Demo {

    public static void main( String[] args )
    {
        Properties p = System.getProperties();
        p.put("aws.accessKeyId", "AKIA3L...");  // AK
        p.put("aws.secretAccessKey", "7IIrjY...");  // SK
        System.setProperties(p);
        SystemPropertyCredentialsProvider sysPopsProvider = SystemPropertyCredentialsProvider.create();

        Region region = Region.US_WEST_2;
        S3Client s3 = S3Client.builder().credentialsProvider(sysPopsProvider).region(region).build();

        ListBucketsResponse s3Resp = s3.listBuckets();
        // print to console for test
        System.out.println(s3Resp);

    }
}

