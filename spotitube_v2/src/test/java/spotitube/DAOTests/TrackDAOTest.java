package spotitube.DAOTests;

import org.junit.jupiter.api.Test;
import spotitube.dao.TrackDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TrackDAOTest {

    @Test
    public void getAllTracksNotInPlaylistPlaylistHasTracksTest() {
        try {
            String expectedSQL = "SELECT *\n" +
                    "FROM Tracks t\n" +
                    "WHERE t.id NOT IN (SELECT t.id\n" +
                    "FROM PlaylistsTracks pt INNER JOIN Tracks t ON pt.trackId = t.id\n" +
                    "WHERE pt.playlistId = ?\n" +
                    "GROUP BY pt.trackId)";
            TrackDAO trackDAO = new TrackDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int playlistId = 1;

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            trackDAO.setDataSource(dataSource);
            trackDAO.getAllTracksNotInPlaylist(playlistId, true);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, playlistId);
            verify(statement).executeQuery();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAllTracksNotInPlaylistPlaylistHasNoTracksTest(){
        try {
            String expectedSQL = "select * from Tracks";
            TrackDAO trackDAO = new TrackDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int playlistId = 1;

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            trackDAO.setDataSource(dataSource);
            trackDAO.getAllTracksNotInPlaylist(playlistId, false);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).executeQuery();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getAllTracksInPlaylistTest() {
        try{
            String expectedSQL = "SELECT t.id, t.title, t.performer, t.duration, t.album, t.playcount, t.publicationDate, t.description, t.offlineAvailable\n" +
                    "FROM Playlists p INNER JOIN PlaylistsTracks pt ON p.id = pt.playlistId INNER JOIN Tracks t ON pt.trackId = t.id\n" +
                    "WHERE p.id = ?";
            TrackDAO trackDAO = new TrackDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int playlistId = 1;

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            trackDAO.setDataSource(dataSource);
            trackDAO.getAllTracksInPlaylist(playlistId);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, playlistId);
            verify(statement).executeQuery();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void addTrackToPlaylistTest() {
        try{
            String expectedSQL = "INSERT INTO PlaylistsTracks VALUES (? , ?)";
            TrackDAO trackDAO = new TrackDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int playlistId = 1;
            int trackId = 2;

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);

            //run tests
            trackDAO.setDataSource(dataSource);
            trackDAO.addTrackToPlaylist(playlistId, trackId, true);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, playlistId);
            verify(statement).setInt(2, trackId);
            verify(statement).execute();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void deleteTrackFromPlaylistTest() {
        try{
            String expectedSQL = "DELETE FROM PlaylistsTracks WHERE playlistId = ? AND trackId = ?";
            TrackDAO trackDAO = new TrackDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            int playlistId = 1;
            int trackId = 2;

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);

            //run tests
            trackDAO.setDataSource(dataSource);
            trackDAO.deleteTrackFromPlaylist(playlistId, trackId);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setInt(1, playlistId);
            verify(statement).setInt(2, trackId);
            verify(statement).execute();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
