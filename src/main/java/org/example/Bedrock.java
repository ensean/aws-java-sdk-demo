package org.example;

import com.sun.tools.javac.util.List;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.internal.impl.ApacheSdkHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.time.Duration;

public class Bedrock {
    public static void main(String[] agrs){
//        BedrockRuntimeClient client = BedrockRuntimeClient.builder()
//                .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(300)))
//                .region(Region.US_EAST_1)
//                .credentialsProvider(ProfileCredentialsProvider.create())
//                .build();
        String prompt = "Who is the president of US? Why does he/she can be the president? Please give a narrative explanation，2000 words";
        String claudeModelId = "anthropic.claude-v2";

        SdkHttpClient sdkHttpClient = ApacheHttpClient.builder().socketTimeout(Duration.ofSeconds(3)).build();



        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:";


        BedrockRuntimeClient client = BedrockRuntimeClient.builder().httpClient(sdkHttpClient)
                .region(Region.US_WEST_2)
                .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(300)).apiCallAttemptTimeout(Duration.ofSeconds(300)))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        String payload = new JSONObject()
                .put("prompt", enclosedPrompt)
                .put("max_tokens_to_sample", 2000)
                .put("temperature", 0.5)
                .put("stop_sequences", List.of("\n\nHuman:"))
                .toString();

        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(claudeModelId)
                .contentType("application/json")
                .accept("application/json")
                .build();
        InvokeModelResponse response = client.invokeModel(request);

        JSONObject responseBody = new JSONObject(response.body().asUtf8String());

        String generatedText = responseBody.getString("completion");
        System.out.println(generatedText);
    }
}
