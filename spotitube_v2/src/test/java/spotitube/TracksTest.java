package spotitube;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotitube.api.Tracks;
import spotitube.api.dto.AddTrackToPlaylistDTO;
import spotitube.api.dto.TracksDTO;
import spotitube.dao.ITrackDAO;
import spotitube.domain.LocalStorage;
import spotitube.domain.Playlist;
import spotitube.domain.Track;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TracksTest {
    private static Tracks tracksApi = new Tracks();
    private static ArrayList<Track> tracks = new ArrayList<>();
    private static HashMap<Integer, Playlist> playlists = new HashMap<>();

    @BeforeAll
    public static void init() {
        Playlist playlist = new Playlist();
        playlist.setOwner("testOwner");
        playlist.setId(1);
        playlist.setName("playlist1");

        Track track = new Track();
        track.setId(1);
        track.setOfflineAvailable(true);
        track.setPublicationDate("12-3-2020");
        track.setPlaycount(5);
        track.setDuration(100);
        track.setPerformer("artist1");
        track.setTitle("song1");
        track.setDescription("description 1");
        track.setAlbum("album1");

        tracks.add(track);

        playlist.setTracks(tracks);
        playlists.put(1, playlist);
    }

    @Test
    public void getAllAvailableTracksTest() {
        //mock
        ITrackDAO trackDAO = mock(ITrackDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);
        tracksApi.setTrackDAO(trackDAO);
        tracksApi.setLocalStorage(localStorage);
        when(trackDAO.getAllTracksNotInPlaylist(1, true)).thenReturn(tracks);
        when(localStorage.getPlaylistsHashmap()).thenReturn(playlists);

        //actual test
        Response response = tracksApi.getAllAvailableTracks(1);

        TracksDTO responseDTO = (TracksDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(1, responseDTO.tracks.size());
    }

    @Test
    public void getAllTracksInPlaylistTest() {
        //mock
        ITrackDAO trackDAO = mock(ITrackDAO.class);
        tracksApi.setTrackDAO(trackDAO);

        //actual test
        when(trackDAO.getAllTracksInPlaylist(1)).thenReturn(tracks);
        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        Response response = tracksApi.getAllTracksInPlaylist(1);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void addTrackToPlaylistTest() {
        //mock
        ITrackDAO trackDAO = mock(ITrackDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);
        tracksApi.setTrackDAO(trackDAO);
        tracksApi.setLocalStorage(localStorage);

        AddTrackToPlaylistDTO trackDTO = new AddTrackToPlaylistDTO();
        trackDTO.offlineAvailable = true;
        trackDTO.id = 8;
        trackDTO.duration = 100;
        trackDTO.performer = "artiest";
        trackDTO.title = "liedje";

        Track track = new Track();
        track.setOfflineAvailable(true);
        track.setId(8);
        track.setTitle("liedje");

        ArrayList<Track> newTracks = new ArrayList<>();
        newTracks.add(track);
        newTracks.addAll(tracks);

        //actual tests
        when(localStorage.getPlaylistsHashmap()).thenReturn(playlists);

        Response response = tracksApi.addTrackToPlaylist(1, trackDTO);
        assertEquals(200, response.getStatus());
        assertEquals(2, newTracks.size());
    }

    @Test
    public void deleteTrackFromPlaylistTest() {
        //mock
        ITrackDAO trackDAO = mock(ITrackDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);
        tracksApi.setLocalStorage(localStorage);
        tracksApi.setTrackDAO(trackDAO);

        //actual test
        when(localStorage.getPlaylistsHashmap()).thenReturn(playlists);
        Response response = tracksApi.deleteTrackFromPlaylist(1, 1);
        assertEquals(200, response.getStatus());
        assertEquals(1, tracks.size());
    }
}
