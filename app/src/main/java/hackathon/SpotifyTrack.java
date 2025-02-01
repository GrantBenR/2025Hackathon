package hackathon;

import java.util.ArrayList;

public class SpotifyTrack
{
    private ArrayList<SpotifyArtist> artists;
    private Album album;
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
