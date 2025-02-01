package hackathon;

import java.util.ArrayList;

public class SpotifyTrack
{
    private ArrayList<SpotifyArtist> artists;
    private Album album;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public ArrayList<SpotifyArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<SpotifyArtist> artists) {
        this.artists = artists;
    }

    private String trackName;
    private String trackUrl;

    public SpotifyTrack(ArrayList<SpotifyArtist> artists, Album album, String trackName, String trackUrl)
    {
        this.artists = artists;
        this.album = album;
        this.trackName = trackName;
        this.trackUrl = trackUrl;
    }
    @Override
    public String toString(){
        return this.album + ":" + this.trackName;
    }
}
