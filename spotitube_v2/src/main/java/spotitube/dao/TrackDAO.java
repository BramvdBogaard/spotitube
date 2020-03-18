package spotitube.dao;

import spotitube.domain.Track;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class TrackDAO implements ITrackDAO {
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public ArrayList<Track> getAllTracksNotInPlaylist(int playlistId, boolean playlistHasTracks) {
        ArrayList<Track> tracks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()){

            String sql = playlistHasTracks
                    ? "SELECT *\n" +
                    "FROM Tracks t\n" +
                    "WHERE t.id NOT IN (SELECT t.id\n" +
                    "FROM PlaylistsTracks pt INNER JOIN Tracks t ON pt.trackId = t.id\n" +
                    "WHERE pt.playlistId = ?\n" +
                    "GROUP BY pt.trackId)"
                    : "select * from Tracks";

            PreparedStatement statement = connection.prepareStatement(sql);

            if (playlistHasTracks) {
                statement.setInt(1, playlistId);
            }

            ResultSet resultset = statement.executeQuery();

            while (resultset.next()) {
                Track t = new Track();
                t.setId(resultset.getInt("id"));
                t.setTitle(resultset.getString("title"));
                t.setPerformer(resultset.getString("performer"));
                t.setDuration(resultset.getInt("duration"));
                t.setAlbum(resultset.getString("album"));
                t.setPlaycount(resultset.getInt("playcount"));
                t.setPublicationDate(resultset.getString("publicationDate"));
                t.setDescription(resultset.getString("description"));
                t.setOfflineAvailable(resultset.getBoolean("offlineAvailable"));

                tracks.add(t);
            }
            return tracks;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    @Override
    public ArrayList<Track> getAllTracksInPlaylist(int playlistId) {
        ArrayList<Track> tracks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            String sql = "SELECT t.id, t.title, t.performer, t.duration, t.album, t.playcount, t.publicationDate, t.description, t.offlineAvailable\n" +
                    "FROM Playlists p INNER JOIN PlaylistsTracks pt ON p.id = pt.playlistId INNER JOIN Tracks t ON pt.trackId = t.id\n" +
                    "WHERE p.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                Track t = new Track();
                t.setId(resultSet.getInt("id"));
                t.setTitle(resultSet.getString("title"));
                t.setPerformer(resultSet.getString("performer"));
                t.setDuration(resultSet.getInt("duration"));
                t.setAlbum(resultSet.getString("album"));
                t.setPlaycount(resultSet.getInt("playcount"));
                t.setPublicationDate(resultSet.getString("publicationDate"));
                t.setDescription(resultSet.getString("description"));
                t.setOfflineAvailable(resultSet.getBoolean("offlineAvailable"));

                tracks.add(t);
            }
            return tracks;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    @Override
    public void addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO PlaylistsTracks VALUES (? , ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            statement.setInt(2, trackId);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
