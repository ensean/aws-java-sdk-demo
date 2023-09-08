package org.example;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResponse;

import java.util.HashMap;

//
public class CognitoDeveloperProvider {

    public static void main(String[] args) {
        String a = genCred("jack", "123456");
        System.out.println(a);
    }

    public static boolean validAccount(String userName, String passwd){
        return userName.equals("jack") && passwd.equals("123456");
    }

    public static String genCred(String userName, String passwd) {
        // authenticate your end user as appropriate
        // ....
        if (!validAccount(userName, passwd))
            return "Username or password invalid";

        // if authenticated, initialize a cognito client with your AWS developer credentials
        // 本样例权限凭证从 ~/.aws/crendentials获取
        CognitoIdentityClient identityClient = CognitoIdentityClient.builder()
                .region(Region.US_WEST_2)
                .build();

        // set up your logins map with the username of your end user
        HashMap<String, String> logins = new HashMap<>();
        logins.put("self_back_end", "jim-from-android");

        // create a new request to retrieve the token for your end user
        GetOpenIdTokenForDeveloperIdentityRequest request = GetOpenIdTokenForDeveloperIdentityRequest.builder()
                .identityPoolId("us-west-2:350c9bac-d808-401f-8edf-01edf827xxxxx")
                .logins(logins)
                .tokenDuration(60* 15L).build();

        GetOpenIdTokenForDeveloperIdentityResponse response = identityClient.getOpenIdTokenForDeveloperIdentity(request);

        // obtain identity id and token to return to your client
        String identityId = response.identityId();
        String token = response.token();
        System.out.println("identityId: " + identityId);
        System.out.println("token:" + token);
        return identityId + ", " + token;
    }
}
