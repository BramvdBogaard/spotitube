package spotitube.api.dto;

import spotitube.domain.Track;

import java.util.ArrayList;

public class PlayListDTO {
    public int id;
    public String owner;
    public String name;
    public ArrayList<Track> tracks;
}
