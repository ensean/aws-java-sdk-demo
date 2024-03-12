package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.util.concurrent.atomic.AtomicReference;

public class BedrockClaude3StreamResp {
    public static String invokeClaude3_sonnet(String prompt, boolean silent) {

        BedrockRuntimeAsyncClient client = BedrockRuntimeAsyncClient.builder()
                .region(Region.US_WEST_2)
                .build();

        AtomicReference finalCompletion = new AtomicReference<>("");

        JSONObject content = new JSONObject();
        content.put("type", "text");
        content.put("text", prompt);

        JSONArray contentArray = new JSONArray();
        contentArray.put(content);

        JSONObject messages = new JSONObject();
        messages.put("role", "user");
        messages.put("content", contentArray);

        JSONArray arrayElementOneArray = new JSONArray();
        arrayElementOneArray.put(messages);
        String payload = new JSONObject()
                .put("anthropic_version", "bedrock-2023-05-31")
                .put("max_tokens", 2000)
                .put("messages", arrayElementOneArray)
                .toString();

        InvokeModelWithResponseStreamRequest request = InvokeModelWithResponseStreamRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId("anthropic.claude-3-sonnet-20240229-v1:0")
                .contentType("application/json")
                .accept("application/json")
                .build();

        InvokeModelWithResponseStreamResponseHandler.Visitor visitor = InvokeModelWithResponseStreamResponseHandler.Visitor.builder()
                .onChunk(chunk -> {
                    JSONObject json = new JSONObject(chunk.bytes().asUtf8String());
                    if (json.getString("type").equals("content_block_delta")) {
                        String completion = json.getJSONObject("delta").getString("text");
                        finalCompletion.set(finalCompletion.get() + completion);
                        if (!silent) {
                            System.out.print(completion);
                        }
                    }

                })
                .build();

        InvokeModelWithResponseStreamResponseHandler handler = InvokeModelWithResponseStreamResponseHandler.builder()
                .onEventStream(stream -> stream.subscribe(event -> event.accept(visitor)))
                .onComplete(() -> {
                })
                .onError(e -> System.out.println("\n\nError: " + e.getMessage()))
                .build();

        client.invokeModelWithResponseStream(request, handler).join();

        return finalCompletion.get().toString();
    }

    public static void main(String[] agrs) {
        // 使用 silent false 参数控制 Bedrock 流式返回结果
        String resp = invokeClaude3_sonnet("以科普的形式讲解一下阿法狗的工作原理。", false);

        // 一次性返回结果
        System.out.println(resp);
    }
}
