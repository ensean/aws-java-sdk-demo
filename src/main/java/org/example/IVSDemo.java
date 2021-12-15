package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;
import software.amazon.awssdk.services.ivs.model.CreateChannelRequest;
import software.amazon.awssdk.services.ivs.model.CreateChannelResponse;

import java.util.HashMap;

/**
 * Hello world for aws ivs jdk!
 *
 */
public class IVSDemo
{
    public static void main( String[] args )
    {
        Region region = Region.US_WEST_2;   // Oregon
        IvsClient ivsClient = IvsClient.builder().region(region).build();
        // 存放标签
        HashMap<String, String> cTags = new HashMap<>();
        cTags.put("test", "yes");
        cTags.put("project", "vn");

        CreateChannelRequest createChannelRequest = CreateChannelRequest
                .builder()
                .latencyMode("LOW") // LOW or NORMAL，频道延迟
                .name("testFromJAVASDK-v3") // 频道名称，自定义
                .type("STANDARD")   // BASIC or STANDARD，频道类型
                .tags(cTags)
                .build();
        CreateChannelResponse resp = ivsClient.createChannel(createChannelRequest);
        System.out.println(resp.toString());
        System.out.println( "Go to aws console to verify" );
    }
}
