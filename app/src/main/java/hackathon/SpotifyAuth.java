package hackathon;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;


public class SpotifyAuth {
    //THIS IS DEFINITELY WRONG ---->>>
    private final static String BASE_URL = "https://accounts.spotify.com/api/token";

    public SpotifyAuth() {
        //Get spotify dev client data from .env
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        System.out.println(CLIENT_ID);
        String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
        Integer returnStatus = redirectToAuthCodeFlow(CLIENT_ID);
        if (returnStatus == 1)
        {
            System.out.println("SUCCESS");
        }
    }

    public Integer redirectToAuthCodeFlow(String clientId)
    {
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        String CLIENT_SECRET = dotenv.get("CLIENT_ID");
        //Length indicated by spotify documentation
        String codeVerifier = generateCodeVerifier(128);
        String codeChallenger = generateCodeChallenge(codeVerifier);

        HttpGet httpGet = new HttpGet("https://accounts.spotify.com/authorize");
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            String state = generateCodeVerifier(16);
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameter("response_type", "code")
                    .addParameter("client_id", clientId)
                    .addParameter("scope", "user-read-private user-read-email")
                    .addParameter("redirect_uri", "http://localhost:8888/callback")
//                    .addParameter("code_challenge_method", "S256")
//                    .addParameter("code_challenge", codeChallenger)
                    .addParameter("state", state)
                    .build();
            System.out.println(uri.toString());
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(uri.toString()))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return 1;
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        } 
        catch (IOException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
                
        } 
        catch (InterruptedException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public String generateCodeVerifier(int length)
    {
        String text = "";
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int possibleLength = possible.length();
        for (int i = 0; i < length; i++) {
            text += possible.charAt((int) Math.floor(Math.random() * possibleLength));
        }
        return text;
    }
    public String generateCodeChallenge(String codeVerifier)
    {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedVerifier = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedVerifier.length);
            for (byte b : encodedVerifier) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String outputString = hexString.toString();
            System.out.println("SHA-256 Hash: " + outputString);
            return outputString;
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("Error in Message Digest Get Instance SHA 256 in SpotifyAuth.java");
            throw new RuntimeException(e);
        }
    }
    private static RequestConfig requestConfig = RequestConfig.custom().build();
    public static String getAccessToken(String code)
    {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpGet httpGet = new HttpGet(BASE_URL);
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
        String clientData = CLIENT_ID + ":" + CLIENT_SECRET;
        byte[] encodedBytes = Base64.getUrlEncoder().encode(clientData.getBytes());
        String encodedString = new String(encodedBytes);
        System.out.println(encodedString);
        try {
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("code", code));
            urlParameters.add(new BasicNameValuePair("redirect_uri", "http://localhost:8888/callback"));
            urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            HttpPost httpPost = new HttpPost(BASE_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization", "Basic " + encodedString);
            HttpEntity response = client.execute(httpPost).getEntity();
            String authToken = EntityUtils.toString(response);
            System.out.println("Authentication request response: " + authToken);
            return authToken;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
