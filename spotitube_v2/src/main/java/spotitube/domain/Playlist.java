package spotitube.domain;

import java.util.ArrayList;

public class Playlist {
    private int id;
    private String owner;
    private String name;
    private ArrayList<Track> tracks = new ArrayList<>();
    private int totalPlayTime;

    public int getTotalPlayTime() {
        return totalPlayTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrackToList(Track track){
        this.tracks.add(track);
    }

    //TODO: implement getDuration method, calculate there
    public int getTotalDurationOfPlaylist() {
        tracks.forEach(track -> totalPlayTime += track.getDuration());
        return totalPlayTime;
    }
}
