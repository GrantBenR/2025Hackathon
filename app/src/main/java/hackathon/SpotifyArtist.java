package hackathon;

import java.util.ArrayList;

public class SpotifyArtist
{
    private String id;
    private String href;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
