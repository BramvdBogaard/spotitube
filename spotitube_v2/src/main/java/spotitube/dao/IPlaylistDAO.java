package spotitube.dao;

import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.domain.Playlist;

import java.util.HashMap;

public interface IPlaylistDAO {
    HashMap<Integer, Playlist> getAllPlaylists();

    void deletePlaylist(int id, boolean hasTracks);

    void addNewPlaylist(String user, AddPlaylistDTO playListDTO);

    HashMap<Integer, Playlist> getAllPlaylistsWithoutTracks();

    void editPlaylist(PlayListDTO playListDTO);
}
