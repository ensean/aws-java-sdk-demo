package org.example;


import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;
import software.amazon.awssdk.services.cloudfront.url.SignedUrl;

public class CloudFrontPreSignURL {
    private static final CloudFrontUtilities cloudFrontUtilities = CloudFrontUtilities.create();

    public static void main(String[] args){

        // 对外访问域名，可以是 Cloudfront 提供的域名也可以是绑定到 Cloudfront 的自定义域名
        String distributionDomain = "d2hthbfg2tekim0.cloudfront.net";
        // 证书生成后需要转换为 der 格式供 Java 使用
        // https://docs.aws.amazon.com/zh_cn/AmazonCloudFront/latest/DeveloperGuide/private-content-trusted-signers.html#private-content-creating-cloudfront-key-pairs
        String privateKeyFilePath = "/tmp/cloudfront_sign/private_key.der";
        //  文件路径
        String s3ObjectKey = "imgs/Tom-And-Jerry.jpg";

        // Cloudfront 中 key id
        String publicKeyId = "K2PJJ4JHN30NU";
        String protocol = "https";
        String resourcePath = "/" + s3ObjectKey;
        String cloudFrontUrl = null;
        try {
            cloudFrontUrl = new URL(protocol, distributionDomain, resourcePath).toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Instant expirationDate = Instant.now().plus(7, ChronoUnit.DAYS);
        Path path = Paths.get(privateKeyFilePath);

        CustomSignerRequest csr = null;
        try {
            csr = CustomSignerRequest.builder()
                    .resourceUrl(cloudFrontUrl)
                    .privateKey(path)
                    .keyPairId(publicKeyId)
                    .expirationDate(expirationDate)
                    //.ipRange("1.2.3.4/32")    //  根据需要开启是否限制访问 IP
                    .build();

            SignedUrl signedUrl = cloudFrontUtilities.getSignedUrlWithCustomPolicy(csr);
            System.out.println(signedUrl.url());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}

