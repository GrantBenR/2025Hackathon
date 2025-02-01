package hackathon;

public class SpotifyRunner
{
    public static void main(String[] args)
    {
        System.out.println("Starting Server Runner");
        ServerRunner serverRunner = new ServerRunner();
        System.out.println("Spotify Runner Start!");
        SpotifyAuth spotifyAuth = new SpotifyAuth();
        String url = spotifyAuth.redirectToAuthCodeFlow("");
        System.out.println("Spotify Auth Instantiation Complete");
    }
}
