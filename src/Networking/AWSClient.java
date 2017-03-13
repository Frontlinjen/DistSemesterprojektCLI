/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.glassfish.jersey.filter.LoggingFilter;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.filter.LoggingFilter;
import org.json.JSONObject;

/*
 *
 * @author Hjorthen
 * 
 */

class Crypto{
        /**
	 
	 Begin copy from: http://docs.aws.amazon.com/general/latest/gr/signature-v4-examples.html#signature-v4-examples-java
	 Dato: 14/3/2017 kl. 00:34
         * 
	 */
	
	public static byte[] HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return mac.doFinal(data.getBytes("UTF8"));
	}

	public static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
	    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
	    byte[] kDate = HmacSHA256(dateStamp, kSecret);
	    byte[] kRegion = HmacSHA256(regionName, kDate);
	    byte[] kService = HmacSHA256(serviceName, kRegion);
	    byte[] kSigning = HmacSHA256("aws4_request", kService);
	    return kSigning;
	}
	/**
	 
	  End copy from: http://docs.aws.amazon.com/general/latest/gr/signature-v4-examples.html#signature-v4-examples-java
	 
	 */
        
        public static String byteToHexString(byte[] bytes)
	{
		StringBuilder hexes = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			hexes.append(String.format("%02x", bytes[i]));
		}
		return hexes.toString();
	}
        
        public static byte[] sha256(String s) throws NoSuchAlgorithmException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(s.getBytes(StandardCharsets.UTF_8));
		
	}

}

/**
 *
 * @author Hjorthen
 */
public class AWSClient {
       AWSAuthorizer authorizer;
       public boolean login(String username, String password){
           authorizer = new AWSAuthorizer(username, password);
           System.out.println(authorizer.toString());
           return true;
       }
       public static void main(String[] args){
           AWSClient client = new AWSClient();
           client.login("s153255", "kode");
           client.SetTarget(new URL(" https://70r7hyxz72.execute-api.eu-west-1.amazonaws.com/development/tasks"));
           client.POST("{\n" +
"    \"price\" : 22,\n" +
"    \"ETC\" : 1,\n" +
"    \"views\" : 122,\n" +
"    \"zipaddress\" : 50,\n" +
"    \"ID\" : \"Testing\"\n" +
"    \n" +
"}");
       }
       
       
	
	private URL target;
	public void SetTarget(URL url)
        {
           target = url;
        }
	public void Connect(){
		//url = new URL(uri);
	}
	public void POST(String data){
            	Client client = ClientBuilder.newClient();     
                String uri = target.getFullPath();
                Invocation.Builder target = client.target("https://" + uri).request(MediaType.APPLICATION_JSON);
                try{
                    System.out.println(authorizer.toString());
                      System.out.println("Identity: " + setAWSHeader(target, data, "POST").post(Entity.json(data), String.class));
                }
                catch(javax.ws.rs.ForbiddenException e)
                {
                    System.out.println("Error description: " + e.getResponse().getHeaders().get("x-amzn-errortype"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
        }
	public String GET(){
		Client client = ClientBuilder.newClient();     
                String uri = target.getFullPath();
                Invocation.Builder target = client.target("https://" + uri).request(MediaType.APPLICATION_JSON);
                try{
                      return setAWSHeader(target, "", "GET").get(String.class);
                }
                catch(javax.ws.rs.ForbiddenException e)
                {
                    System.out.println("Error description: " + e.getResponse().getHeaders().get("x-amzn-errortype"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return null;
	}
        
	

	
	private static String getDateStamp(Date d)
	{
		//return "20170312";
		SimpleDateFormat datestamp = new SimpleDateFormat("YYYYMMdd");
		datestamp.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return datestamp.format(d);
	}
	private static String getAmzDate(Date d)
	{
		//return "20170312T133959Z";
		SimpleDateFormat amzdate = new SimpleDateFormat("YYYYMMdd'T'HHmmss'Z'");
		amzdate.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return amzdate.format(d);
	}
	
      
	public Invocation.Builder setAWSHeader(Invocation.Builder client, String payload, String type) throws Exception
	{
		
		String canonical_uri = target.getPath();
		//TODO: URL-encode
		String canonical_query = target.getCanonicalQuery();
		Date d = new Date();
                String dateStamp = getDateStamp(d);
                String amzDate = getAmzDate(d);
		String host = target.getHostname();
		String canonicalHeaders = "content-type:" + "application/json" + "\n" + "host:" + host + "\nx-amz-date:" + getAmzDate(d) + "\n";
		String signedHeaders = "content-type;host;x-amz-date";
		String bodyHash = Crypto.byteToHexString(Crypto.sha256(payload));
		String canonicalRequest = type + "\n" + canonical_uri + "\n" + canonical_query + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + bodyHash;
		String algorithm = "AWS4-HMAC-SHA256";
		String credential_scope = getDateStamp(d) + "/" + "eu-west-1" + "/" + "execute-api" + "/" + "aws4_request";
		String signString = algorithm + "\n" + getAmzDate(d) + "\n" + credential_scope + "\n" + Crypto.byteToHexString(Crypto.sha256(canonicalRequest));
		byte[] signKey = Crypto.getSignatureKey(authorizer.getSecret(), getDateStamp(d), "eu-west-1", "execute-api");
		String signature = Crypto.byteToHexString(Crypto.HmacSHA256(signString, signKey));
		String authHeader = algorithm + ' ' + "Credential=" + authorizer.getAccessKey() + "/" + 
							credential_scope + ", " + "SignedHeaders=" + signedHeaders + ", Signature=" + signature;
                client.header("x-amz-date", getAmzDate(d)).
                       header("content-type", "application/json").
                       header("Authorization", authHeader).
                       header("X-Amz-Security-Token", authorizer.getToken());                        
		          System.out.println(signature);
                        
		return client;
	}
}
