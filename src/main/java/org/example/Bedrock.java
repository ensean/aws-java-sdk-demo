package org.example;

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

public class Bedrock {
    public static void main(String[] agrs){
        String prompt = "Who is the president of US? Why does he/she can be the president? Please give a narrative explanation，2000 words";
        String claudeModelId = "anthropic.claude-v2";

        // https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/best-practices.html#bestpractice5
        SdkHttpClient sdkHttpClient = ApacheHttpClient.builder().socketTimeout(Duration.ofSeconds(300)).build();

        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:";

        BedrockRuntimeClient client = BedrockRuntimeClient.builder().httpClient(sdkHttpClient)
                .region(Region.US_WEST_2)
                .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(300)).apiCallAttemptTimeout(Duration.ofSeconds(300)))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
        ArrayList<String> stop = new ArrayList<String>();
        stop.add("\n\nHuman:");
        String payload = new JSONObject()
                .put("prompt", enclosedPrompt)
                .put("max_tokens_to_sample", 2000)
                .put("temperature", 0.5)
                .put("stop_sequences", stop)
                .toString();

        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(claudeModelId)
                .contentType("application/json")
                .accept("application/json")
                .build();
        long start = System.currentTimeMillis();
        InvokeModelResponse response = client.invokeModel(request);

        JSONObject responseBody = new JSONObject(response.body().asUtf8String());

        String generatedText = responseBody.getString("completion");
        long end = System.currentTimeMillis();
        System.out.println(generatedText);
        System.out.println("Time cost: " + (end - start)/1000 + " seconds");
    }
}
