package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;
import software.amazon.awssdk.services.ivs.model.CreateChannelRequest;
import software.amazon.awssdk.services.ivs.model.CreateChannelResponse;
import software.amazon.awssdk.services.ivs.model.GetChannelRequest;
import software.amazon.awssdk.services.ivs.model.GetChannelResponse;
import software.amazon.awssdk.services.ivs.model.GetStreamKeyRequest;
import software.amazon.awssdk.services.ivs.model.GetStreamKeyResponse;
import software.amazon.awssdk.services.ivs.model.ListStreamKeysRequest;
import software.amazon.awssdk.services.ivs.model.ListStreamKeysResponse;
import software.amazon.awssdk.services.ivs.model.StreamKey;

import java.util.HashMap;

/**
 * Hello world for aws ivs jdk!
 *
 */
public class IVSDemo
{
    public static void main( String[] args )
    {
        // 指定接口区域。控制接口在如下地区可用，选取离服务端位置近的即可
        // 俄勒冈 US_WEST_2
        // 弗吉尼亚北 US_EAST_1
        // 法兰克福 EU_CENTRAL_1
        // 爱尔兰 EU_WEST_1
        // 东京 AP_NORTHEAST_1
        // 首尔 AP_NORTHEAST_2
        // 孟买 AP_SOUTH_1
        Region region = Region.AP_NORTHEAST_1;
        
        // 创建客户端
        IvsClient ivsClient = IvsClient.builder().region(region).build();

        // 存放标签，按需要设置
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
        String ingestEndpoint = resp.channel().ingestEndpoint();
        String streamKey = resp.streamKey().value();
        System.out.println("The ingest address is " + "rtmp://"+ ingestEndpoint+"/app/" + streamKey);
    }
}
