package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.time.Duration;
import java.util.ArrayList;

public class BedrockC3 {
    public static void main(String[] agrs){

        String prompt = "Who is the president of US? Why does he/she can be the president? Please give a narrative explanation，2000 words";
        String claudeModelId = "anthropic.claude-3-sonnet-20240229-v1:0";

        SdkHttpClient sdkHttpClient = ApacheHttpClient.builder().socketTimeout(Duration.ofSeconds(300)).build();
        BedrockRuntimeClient client = BedrockRuntimeClient.builder().httpClient(sdkHttpClient)
                .region(Region.US_WEST_2)
                .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(300)).apiCallAttemptTimeout(Duration.ofSeconds(300)))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();


        // Claude requires you to enclose the prompt as follows:
        ArrayList<JSONObject> contents = new ArrayList<JSONObject>();
        contents.add(new JSONObject().put("type", "text").put("text", prompt));

        JSONObject session1 = new JSONObject()
                .put("role", "user")
                .put("content", contents);
        ArrayList<JSONObject> enclosedPrompt = new ArrayList<JSONObject>();

        enclosedPrompt.add(session1);

        String payload = new JSONObject()
                .put("anthropic_version", "bedrock-2023-05-31")
                .put("max_tokens", 2000)
                .put("temperature", 0.5)
//                .put("system", "")  // add on demand
//                .put("top_p", 0.999)
//                .put("top_k", 250)
                .put("messages", enclosedPrompt)
                .toString();
        System.out.println(payload);
        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(claudeModelId)
                .contentType("application/json")
                .accept("application/json")
                .build();
        long start = System.currentTimeMillis();
        InvokeModelResponse response = client.invokeModel(request);

        JSONObject responseBody = new JSONObject(response.body().asUtf8String());

        JSONArray generatedText = responseBody.getJSONArray("content");

        long end = System.currentTimeMillis();
        System.out.println(generatedText.getJSONObject(0).get("text"));
        System.out.println("Time cost: " + (end - start)/1000 + " seconds");
    }
}
