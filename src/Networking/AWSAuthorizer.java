/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import java.util.Iterator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 *
 * @author Hjorthen
 */
class CryptoInformation {

    String identity;
    String token;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

/**
 *
 * @author hjorthen
 */
public class AWSAuthorizer {
    final static String authpoint = "https://70r7hyxz72.execute-api.eu-west-1.amazonaws.com/development/login";
    final static String roleArn = "arn:aws:iam::619517212226:role/Cognito_WEWOUsersAuth_Role";
    final static String version = "2011-06-15";
    final static String action = "AssumeRoleWithWebIdentity";
    final static String stsUrl = "https://sts.amazonaws.com/";
    final static String tmpAccessUrl = stsUrl + "?Action=" + action + "&RoleArn=" + roleArn + "&Version=" + version;
    private String tmpAccessUrlFull;
    private String OAuth;
    private String username;
    private String password;
    
    private String accessKey;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecret() {
        return secret;
    }

    public String getToken() {
        return token;
    }
    private String secret;
    private String token;
    public AWSAuthorizer(String username, String password)
    {
        this.username = username;
        this.password = password;
        OAuth = GetOAuthToken();
        tmpAccessUrlFull = tmpAccessUrl + "&RoleSessionName=" + username + "&WebIdentityToken=" + OAuth;
        AquireTemporaryAccessKey();
    }
    
    private void AquireTemporaryAccessKey()
    {
        Client client = ClientBuilder.newClient();
        String result = client.target(tmpAccessUrlFull).request(MediaType.APPLICATION_JSON).get(String.class);
        JSONObject jsonResult = new JSONObject(result);
        JSONObject credentials = jsonResult.getJSONObject("AssumeRoleWithWebIdentityResponse").getJSONObject("AssumeRoleWithWebIdentityResult").getJSONObject("Credentials");
        secret = credentials.getString("SecretAccessKey");
        accessKey = credentials.getString("AccessKeyId");
        token = credentials.getString("SessionToken");
    }
    
    //Connects to the javabog authorizer and requests token
    private String GetOAuthToken()
    {
         Client client = ClientBuilder.newClient();
         JSONObject loginCredentials = new JSONObject();
         loginCredentials.put("username", username);
         loginCredentials.put("password", password);
         try{
            CryptoInformation crypto = new CryptoInformation();
            crypto = client.target(authpoint).request(MediaType.APPLICATION_JSON).header("Content-Type", "application/json")
                     .post(Entity.json(loginCredentials.toString()), CryptoInformation.class); 
            return crypto.token;
         }
           catch(javax.ws.rs.ForbiddenException e)
           {
               e.printStackTrace();
               System.out.println(e.getResponse().getHeaderString("x-amzn-errortype"));
               return null;
           }
    }
}
