package hackathon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SpotifyAuth
{
    //THIS IS DEFINITELY WRONG ---->>>
    private final static String BASE_URL = "https://accounts.spotify.com/api/token";
    public SpotifyAuth()
    {
        //Get spotify dev client data from .env
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        String CLIENT_SECRET = dotenv.get("CLIENT_ID");
        //Length indicated by spotify documentation
        String codeVerifier = generateCodeVerifier(128);
        String codeChallenger = generateCodeChallenge(codeVerifier);
        String accessToken = getAccessToken(CLIENT_ID, codeChallenger, codeVerifier);

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
            String authToken = objectMapper.readValue(response.body(), new TypeReference<>() {});
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
//    const params = new URLSearchParams();
//        params.append("client_id", clientId);
//        params.append("grant_type", "authorization_code");
//        params.append("code", code);
//        params.append("redirect_uri", "http://localhost:5173/callback");
//        params.append("code_verifier", verifier!);
//
//    const result = await fetch("https://accounts.spotify.com/api/token", {
//                method: "POST",
//                headers: { "Content-Type": "application/x-www-form-urlencoded" },
//        body: params
//    });
//
//    const { access_token } = await result.json();
//        return access_token;
//    }
    }
}
