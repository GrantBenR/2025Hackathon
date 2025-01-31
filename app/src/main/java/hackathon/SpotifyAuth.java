package hackathon;

import io.github.cdimascio.dotenv.Dotenv;

public class SpotifyAuth
{
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    public SpotifyAuth()
    {
        Dotenv dotenv = Dotenv.load();
        CLIENT_ID = dotenv.get("CLIENT_ID");
        CLIENT_SECRET = dotenv.get("CLIENT_ID");

    }
}
