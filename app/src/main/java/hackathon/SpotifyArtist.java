package hackathon;

import java.util.ArrayList;

public class SpotifyArtist
{
    private String id;
    private String href;

    private String type = "artist";
    private String name;

    //Artist Constructor
    public SpotifyArtist(String id, String href, String type, String name){

        //Initialization
        this.id = id;
        this.href = href;
        this.type = type;
        this.name = name;

    }
}
