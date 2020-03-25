package spotitube;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotitube.api.Playlists;
import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.AllPlaylistsPlusTotalPlaytimeDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.api.dto.UserLoginRequest;
import spotitube.dao.IPlaylistDAO;
import spotitube.dao.IUserDAO;
import spotitube.domain.LocalStorage;
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

        Track track2 = new Track();
        track2.setId(2);
        track2.setOfflineAvailable(false);
        track2.setPublicationDate("25-3-2020");
        track2.setPlaycount(8);
        track2.setDuration(150);
        track2.setPerformer("artist2");
        track2.setTitle("song2");
        track2.setDescription("description 2");
        track2.setAlbum("album2");

        tracks.add(track);
        tracks.add(track2);
        playlist.setTracks(tracks);
        playlists.put(1, playlist);

        playlistsApi = new Playlists();
    }

    @Test
    public void getTotalPlaytimeOfAllPlaylistsTest() {
        int actualPlaytime = playlistsApi.getTotalPlaytimeOfAllPlaylists(playlists);
        assertEquals(150, actualPlaytime);
    }

    @Test
    public void getAllPlaylistsTest() {
        //Mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);

        when(playlistDAO.getAllPlaylists()).thenReturn(playlists);
        playlistsApi.setPlaylistDAO(playlistDAO);
        playlistsApi.setLocalStorage(localStorage);

        //Actual test
        Response response = playlistsApi.getAllPlaylists();
        AllPlaylistsPlusTotalPlaytimeDTO allPlaylistsPlusTotalPlaytimeDTO = (AllPlaylistsPlusTotalPlaytimeDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(2, allPlaylistsPlusTotalPlaytimeDTO.playlists.size());
    }

    @Test
    public void playlistsNotFoundTest() {
        //TODO: Fix nullpointerException ASAP
        //Mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);

        playlistsApi.setPlaylistDAO(playlistDAO);
        playlistsApi.setLocalStorage(localStorage);

        when(playlistDAO.getAllPlaylists()).thenReturn(null);

        //Actual test
        Response response = playlistsApi.getAllPlaylists();
        assertEquals(404, response.getStatus());

    }

    @Test
    public void deletePlaylistTest() {
        //mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);
        playlistsApi.setPlaylistDAO(playlistDAO);
        playlistsApi.setLocalStorage(localStorage);

        AllPlaylistsPlusTotalPlaytimeDTO playlistsDTO = new AllPlaylistsPlusTotalPlaytimeDTO();
        playlistsDTO.length = 0;
        playlistsDTO.playlists = new ArrayList<>();

        //actual test
        Response response = playlistsApi.deletePlaylist(1);
        AllPlaylistsPlusTotalPlaytimeDTO allPlaylistsPlusTotalPlaytimeDTO = (AllPlaylistsPlusTotalPlaytimeDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(1, allPlaylistsPlusTotalPlaytimeDTO.playlists.size());

    }

    @Test
    public void addPlaylistTest() {
        //mock
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        LocalStorage localStorage = mock(LocalStorage.class);
        playlistsApi.setLocalStorage(localStorage);
        playlistsApi.setPlaylistDAO(playlistDAO);

        AddPlaylistDTO addPlaylistDTO = new AddPlaylistDTO();
        addPlaylistDTO.name = "playlist1";
        addPlaylistDTO.id = 2;
        addPlaylistDTO.owner = true;

        //actual
        Response response = playlistsApi.addPlaylist("1234", addPlaylistDTO);
        AllPlaylistsPlusTotalPlaytimeDTO playlistDTO = (AllPlaylistsPlusTotalPlaytimeDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(3, playlistDTO.playlists.size());
        assertEquals(150, playlistDTO.length);

    }
//
//    @Test
//    public void editPlaylistNameTest() {
//
//    }

}
