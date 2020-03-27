package spotitube.DAOTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.dao.PlaylistDAO;
import spotitube.domain.Playlist;
import spotitube.domain.Track;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class PlaylistDAOTest {
    private HashMap<Integer, Playlist> playlists = new HashMap<>();

    @BeforeEach
    public void init() {
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
    }

    @Test
    public void getAllPlaylistsTest() {
        try{
            String expectedSQL = "select p.id, p.name, p.owner, t.id as 'trackId', t.title, t.performer, t.duration, t.album, t.playcount, t.publicationDate, t.description, t.offlineAvailable\n" +
                    "FROM playlists p INNER JOIN PlaylistsTracks pt ON pt.playlistId = p.id INNER JOIN Tracks t ON pt.trackId = t.id";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.getAllPlaylists();

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).executeQuery();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAllPlaylistsWithoutTracksTest(){
        try{
            String expectedSQL = "select p.id, p.name, p.owner\n" +
                    "from playlists p INNER JOIN PlaylistsTracks pt ON pt.playlistId != p.id\n" +
                    "GROUP BY p.id";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.getAllPlaylistsWithoutTracks();

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).executeQuery();

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void editPlaylistTest() {
        try {
            String expectedSQL = "UPDATE Playlists\n" +
                    "SET name = ?\n" +
                    "WHERE id = ?";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);

            ArrayList<Track> tracks = new ArrayList<>();

            PlayListDTO playListDTO = new PlayListDTO();
            playListDTO.tracks = tracks;
            playListDTO.owner = "bram";
            playListDTO.name = "playlist1";
            playListDTO.id = 1;

            //Instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(false);

            //Run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.editPlaylist(playListDTO);

            //Asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).execute();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deletePlaylistWithTracksTest() {
        try {
            String expectedSQL = "DELETE p, pt\n" +
                    "from playlists p INNER JOIN PlaylistsTracks pt on pt.playlistId = p.id\n" +
                    "where p.id = ?";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int id = 1;

            //instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(false);

             //run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.deletePlaylist(id, true);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, id);
            verify(statement).execute();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deletePlaylistWithoutTracks() {
        try {
            String expectedSQL = "DELETE\n" +
                    "from playlists\n" +
                    "where id = ?";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int id = 1;

            //instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(false);

            // run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.deletePlaylist(id, false);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, id);
            verify(statement).execute();

        } catch(Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addNewPlaylistTest() {
        try {
            String expectedSQL = "INSERT INTO Playlists VALUES (null, ? , ?)";
            PlaylistDAO playlistDAO = new PlaylistDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            String user = "bram";
            AddPlaylistDTO playlistDTO = new AddPlaylistDTO();
            playlistDTO.owner = false;
            playlistDTO.id = 8;
            playlistDTO.name = "testing";

            //instruct mocks
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(false);

            //run tests
            playlistDAO.setDataSource(dataSource);
            playlistDAO.addNewPlaylist(user, playlistDTO);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setString(1, playlistDTO.name);
            verify(statement).setString(2, user);
            verify(statement).execute();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
