package hackathon;

import java.util.ArrayList;

public class Album
{
    private ArrayList<String> albumCovers;
    private String AlbumName;

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public ArrayList<String> getAlbumCovers() {
        return albumCovers;
    }

    public void setAlbumCovers(ArrayList<String> albumCovers) {
        this.albumCovers = albumCovers;
    }

    private String ReleaseDate;

    public Album(ArrayList<String> albumCovers, String releaseDate, String albumName) {
        this.albumCovers = albumCovers;
        ReleaseDate = releaseDate;
        AlbumName = albumName;
    }


}
