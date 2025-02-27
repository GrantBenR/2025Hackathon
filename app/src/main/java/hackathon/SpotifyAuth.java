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
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;


public class SpotifyAuth {
    private final static String BASE_URL = "https://accounts.spotify.com/api/token";
    public static String DiscordUserId;
    public SpotifyAuth() {
        //Get spotify dev client data from .env
//        Dotenv dotenv = Dotenv.load();
//        String CLIENT_ID = dotenv.get("CLIENT_ID");
//        System.out.println(CLIENT_ID);
//        String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
//        String url = redirectToAuthCodeFlow();
//        if (url != null)
//        {
//            System.out.println("SUCCESS");
//        }
    }

    public String redirectToAuthCodeFlow(String discordUserId)
    {
        DiscordUserId = discordUserId;
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
                    .addParameter("client_id", CLIENT_ID)
                    .addParameter("scope", "user-read-private user-read-email user-library-read playlist-read-private user-read-currently-playing user-top-read user-follow-read")
                    .addParameter("redirect_uri", "http://localhost:8888/callback")
//                  .addParameter("code_challenge_method", "S256")
//                  .addParameter("code_challenge", codeChallenger)
                    .addParameter("state", state)
                    .build();
            System.out.println(uri.toString());
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(uri.toString()))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return uri.toString();
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


    //Part of PKCE Auth flow that is not in use
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
    //Also part of PKCE auth flow
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
    public static void getAccessToken(String code)
    {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
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
            String authTokenObject = EntityUtils.toString(response);
            System.out.println("Authentication request response: " + authTokenObject);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(authTokenObject);
            String AUTHENTICATION_TOKEN = jsonNode.get("access_token").asText();
            System.out.println("Authentication Token: " + AUTHENTICATION_TOKEN);
            String REFRESH_TOKEN = jsonNode.get("refresh_token").asText();
            System.out.println("Refresh Token: " + REFRESH_TOKEN);
            String StartDate = Instant.now().toString();
            if (DiscordUserId == null)
            {
                System.out.println("Discord User ID was found null when preparing Authentication Token");
            }
            else
            {
                if (DatabaseConnection.CheckUserStatusInDatabase(DiscordUserId) == false)
                {
                    System.out.println("Inserting new user into the database with id: " + DiscordUserId);
                    DatabaseConnection.InsertNewUserIntoDatabase(DiscordUserId, AUTHENTICATION_TOKEN, REFRESH_TOKEN, StartDate);
                }
                else
                {
                    System.out.println("Updating user in the database with id: " + DiscordUserId);
                    DatabaseConnection.UpdateUserData(DiscordUserId, AUTHENTICATION_TOKEN, REFRESH_TOKEN, StartDate);
                }
            }
            //getCurrentUserTopTracks(AUTHENTICATION_TOKEN);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void getRefreshToken(String refresh_token)
    {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        Dotenv dotenv = Dotenv.load();
        String CLIENT_ID = dotenv.get("CLIENT_ID");
        String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
        String clientData = CLIENT_ID + ":" + CLIENT_SECRET;
        byte[] encodedBytes = Base64.getUrlEncoder().encode(clientData.getBytes());
        String encodedString = new String(encodedBytes);
        System.out.println(encodedString);
        try {
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("refresh_token", refresh_token));
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            HttpPost httpPost = new HttpPost(BASE_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("Authorization", "Basic " + encodedString);
            HttpEntity response = client.execute(httpPost).getEntity();
            String authTokenObject = EntityUtils.toString(response);
            System.out.println("Authentication request response: " + authTokenObject);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(authTokenObject);
            String AUTHENTICATION_TOKEN = jsonNode.get("access_token").asText();
            System.out.println("Authentication Token: " + AUTHENTICATION_TOKEN);
            String REFRESH_TOKEN = jsonNode.get("refresh_token").asText();
            System.out.println("Refresh Token: " + REFRESH_TOKEN);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void getCurrentUserProfile(String token)
    {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = "https://api.spotify.com/v1/me";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", ("Bearer " + token))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void getCurrentUserTopArtists(String token)
    {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = "https://api.spotify.com/v1/me/top/artists";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", ("Bearer " + token))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<SpotifyTrack> getCurrentUserTopTracks(String token)
    {
        HttpClient client = HttpClient.newHttpClient();
        HttpGet httpGet = new HttpGet("https://api.spotify.com/v1/me/top/tracks?time_range=long_term&limit=10&offset=0");
        URI uri = null;
        try
        {
            uri = new URIBuilder(httpGet.getURI())
                    .addParameter("scope", "user-top-read")
                    .build();
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", ("Bearer " + token))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode fullJsonNode = mapper.readTree(response.body());
            ArrayNode itemsJsonNode = (ArrayNode) fullJsonNode.get("items");
            System.out.println(itemsJsonNode.toString());
            ArrayList<SpotifyTrack> spotifyTracks = new ArrayList<SpotifyTrack>();
            for (int i = 0; i < itemsJsonNode.size(); i++ )
            {
                JsonNode albumNode = itemsJsonNode.get(i).get("album");
                String albumName = albumNode.get("name").toString();
                String releaseDate = albumNode.get("release_date").toString();
                ArrayNode imagesNode = (ArrayNode) albumNode.get("images");
                ArrayList<String> albumCovers = new ArrayList<String>();
                for (int j = 0; j < imagesNode.size(); j++ )
                {
                    albumCovers.add(imagesNode.get(j).get("url").toString());
                }
                Album album = new Album(albumCovers, releaseDate, albumName);

                ArrayNode artistsNode = (ArrayNode) itemsJsonNode.get(i).get("artists");
                ArrayList<SpotifyArtist> artistsArray = new ArrayList<SpotifyArtist>();
                for (int j = 0; j < artistsNode.size(); j++ )
                {
                    String artistName = artistsNode.get(j).get("name").toString();
                    String artistType = artistsNode.get(j).get("type").toString();
                    String artistHref = artistsNode.get(j).get("href").toString();
                    String artistId = artistsNode.get(j).get("id").toString();
                    SpotifyArtist newArtist = new SpotifyArtist(artistId, artistHref, artistType, artistName);
                    artistsArray.add(newArtist);
                }
                String trackName = itemsJsonNode.get(i).get("name").toString();
                String trackUrl = itemsJsonNode.get(i).get("external_urls").get("spotify").toString();
                spotifyTracks.add(new SpotifyTrack(artistsArray, album, trackName, trackUrl));
            }
            return spotifyTracks;
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void getCurrentUserFollowedArtists(String token)
    {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = "https://api.spotify.com/v1/me/following?type=artist";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", ("Bearer " + token))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void getDoesCurrentUserFollowArtist(String token, String artistId)
    {
        HttpClient client = HttpClient.newHttpClient();
        String endpoint = "https://api.spotify.com/v1/me/following/contains?type=artist&ids=" + artistId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", ("Bearer " + token))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
