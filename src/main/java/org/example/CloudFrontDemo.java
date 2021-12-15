package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudfront.CloudFrontClient;
import software.amazon.awssdk.services.cloudfront.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CloudFrontDemo {

    public static void main(String[] args){
        CloudFrontClient cloudFrontClient = CloudFrontClient.builder()
                .region(Region.AWS_GLOBAL)
                .build();
        Collection<String> paths = new ArrayList<String>();
        paths.add("/mp4/What-is-AWS.mp4");
        paths.add("/imgs/*");
        createInvalidation(cloudFrontClient, "E370EQFIREOXXX", paths);
    }

    /**
     * 获取clodufront分发列表
     * @param cloudFrontClient
     */
    public static void getCFDistrubutions(CloudFrontClient cloudFrontClient) {
        try {

            ListDistributionsResponse response = cloudFrontClient.listDistributions();
            DistributionList list = response.distributionList();
            List<DistributionSummary> dists = list.items();

            for(DistributionSummary dist : dists) {
                System.out.println("The Distribution ARN is "+dist.arn());
            }

        } catch (CloudFrontException e){
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * 创建缓存失效（刷新缓存）任务
     * @param cloudFrontClient cloudfrontClient
     * @param cloudFrontDistId Cloudfront 分发 ID，如E370EQFIREOXXX
     * @param paths 路径
     */
    public static void createInvalidation(CloudFrontClient cloudFrontClient, String cloudFrontDistId, Collection<String> paths){
        Paths invalidationPaths = Paths.builder()
                .items(paths)
                .quantity(paths.size())
                .build();

        InvalidationBatch invalidationBatch = InvalidationBatch.builder()
                .paths(invalidationPaths)
                .callerReference("arcones")
                .build();

        CreateInvalidationRequest createInvalidationRequest = CreateInvalidationRequest.builder()
                .distributionId(cloudFrontDistId)
                .invalidationBatch(invalidationBatch)
                .build();

        cloudFrontClient.createInvalidation(createInvalidationRequest);
    }
}
