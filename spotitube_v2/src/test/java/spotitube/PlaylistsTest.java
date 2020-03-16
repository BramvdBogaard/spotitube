package spotitube;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spotitube.api.Playlists;
import spotitube.api.dto.AllPlaylistsPlusTotalPlaytimeDTO;
import spotitube.api.dto.UserLoginRequest;
import spotitube.dao.IPlaylistDAO;
import spotitube.dao.IUserDAO;
import spotitube.domain.Playlist;
import spotitube.domain.Track;
import spotitube.domain.User;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlaylistsTest {
    private static Playlists playlistsApi;
    private static HashMap<Integer, Playlist> playlists = new HashMap<>();

    @BeforeAll
    public static void setup() {
        Playlist playlist = new Playlist();
        playlist.setOwner("testOwner");
        playlist.setId(1);
        playlist.setName("playlist1");

        ArrayList<Track> tracks = new ArrayList<Track>();
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

        playlistsApi = new Playlists();
    }

    @Test
    public void getTotalPlaytimeOfAllPlaylistsTest() {
        int actualPlaytime = playlistsApi.getTotalPlaytimeOfAllPlaylists(playlists);
        assertEquals(100, actualPlaytime);
    }

    @Test
    public void getAllPlaylistsTest() {
        //Setup mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        when(playlistDAO.getAllPlaylists()).thenReturn(playlists);
        playlistsApi.setPlaylistDAO(playlistDAO);

        //Actual test
        Response response = playlistsApi.getAllPlaylists();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void playlistsNotFoundTest() {
        //Setup mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        when(playlistDAO.getAllPlaylists()).thenReturn(null);
        playlistsApi.setPlaylistDAO(playlistDAO);

        //Actual test
        Response response = playlistsApi.getAllPlaylists();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void deletePlaylistTest() {
        //Setup mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        when(playlistDAO.getAllPlaylists()).thenReturn(playlists);
        playlistsApi.setPlaylistDAO(playlistDAO);
        Response response = playlistsApi.getAllPlaylists();

        //Actual test
        assertEquals(true, playlistDAO.getAllPlaylists().containsKey(1));
        assertEquals(200, response.getStatus());
    }
}
