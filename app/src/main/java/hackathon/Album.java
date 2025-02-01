package hackathon;

import java.util.ArrayList;

public class Album
{
    private ArrayList<String> albumCovers;
    private String AlbumName;
    private String ReleaseDate;

    public Album(ArrayList<String> albumCovers, String releaseDate, String albumName) {
        this.albumCovers = albumCovers;
        ReleaseDate = releaseDate;
        AlbumName = albumName;
    }


}
