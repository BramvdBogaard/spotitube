package spotitube.dao;

import spotitube.domain.Track;

import java.util.ArrayList;

public interface ITrackDAO {
    ArrayList<Track> getAllTracksNotInPlaylist(int playlistId, boolean playlistHasTracks);

    ArrayList<Track> getAllTracksInPlaylist(int playlistId);

    void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable);
}
