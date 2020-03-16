package spotitube.api.dto;

import java.util.ArrayList;

public class AllPlaylistsPlusTotalPlaytimeDTO {
    public ArrayList<PlayListDTO> playlists = new ArrayList<>();
    public int length;
}
