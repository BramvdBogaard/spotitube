package spotitube;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotitube.api.Playlists;
import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.AllPlaylistsPlusTotalPlaytimeDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.dao.IPlaylistDAO;
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
    private static Playlists playlistsApi = new Playlists();
    private static HashMap<Integer, Playlist> playlists = new HashMap<>();

    @BeforeEach
    public void setup() {
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
        assertEquals(250, actualPlaytime);
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
        assertEquals(1, allPlaylistsPlusTotalPlaytimeDTO.playlists.size());
    }

    @Test
    public void playlistsNotFoundTest() {
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

        Playlist playlist = new Playlist();
        playlist.setOwner("testOwner");
        playlist.setId(1);
        playlist.setName("playlist1");

        when(playlists.get(1)).thenReturn(playlist);
        doNothing().when(localStorage).setPlaylistsHashmap(playlists);
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

        User user = new User();
        user.setToken("1234");
        user.setUsername("bram");
        user.setPassword("password1234");

        //actual
        //TODO: Compleet andere functie wordt hier aangeroepen zonder reden, creator is null doordat localstorage geen user teruggeeft.
//        when(localStorage.getUser("1234")).thenReturn(user);

        Response response = playlistsApi.addPlaylist("1234", addPlaylistDTO);
        AllPlaylistsPlusTotalPlaytimeDTO playlistDTO = (AllPlaylistsPlusTotalPlaytimeDTO) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(3, playlistDTO.playlists.size());
        assertEquals(150, playlistDTO.length);

    }

    @Test
    public void editPlaylistNameTest() {
        //mock
        LocalStorage localStorage = mock(LocalStorage.class);
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        String newPlaylistName = "playlistTestEditPlaylist";

        PlayListDTO playListDTO = new PlayListDTO();
        playListDTO.id = 1;
        playListDTO.name = newPlaylistName;
        playListDTO.owner = "bram";
        playListDTO.tracks = new ArrayList<Track>();

        //actual test
        playlistsApi.setLocalStorage(localStorage);
        playlistsApi.setPlaylistDAO(playlistDAO);

//        when(playlistDAO.editPlaylist(playListDTO)).then(playlists.get(1).setName(newPlaylistName));
        //TODO: naam niet geupdatet
        Response response = playlistsApi.editPlaylistName(playListDTO);
        assertEquals(200, response.getStatus());
        assertEquals(newPlaylistName, playlists.get(1).getName());
    }

}
