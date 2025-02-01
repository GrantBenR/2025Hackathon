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

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

public class SpotifyAuth {
    //THIS IS DEFINITELY WRONG ---->>>
    private final static String BASE_URL = "https://accounts.spotify.com/api/token";

    public SpotifyAuth() {
        //Get spotify dev client data from .env
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        String CLIENT_SECRET = dotenv.get("CLIENT_ID");
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
        getAccessToken(CLIENT_ID, codeChallenger, codeVerifier);

        HttpGet httpGet = new HttpGet("https://accounts.spotify.com/authorize");
        try
        {
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameter("client_id", clientId)
                    .addParameter("response_type", "code")
                    .addParameter("redirect_uri", "http://localhost:5500/callback")
                    .addParameter("scope", "user-read-private user-read-email")
                    .addParameter("code_challenge_method", "S256")
                    .addParameter("code_challenge", codeChallenger)
                    .build();
            return 1;
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
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
    public String getAccessToken(String clientId, String code, String codeVerifier)
    {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpGet httpGet = new HttpGet(BASE_URL);

        try {
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameter("client_id", clientId)
                    .addParameter("grant_type", "authorization_code")
                    .addParameter("code", code)
                    .addParameter("redirect_uri", "http://localhost:5500/callback")
                    .addParameter("code_verifier", codeVerifier)
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(uri.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //String authToken = objectMapper.readValue(response.body(), new TypeReference<>() {});
            String authToken = response.body();
            System.out.println("Authentication request response: " + authToken);
            return authToken;
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
