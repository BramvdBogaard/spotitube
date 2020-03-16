package spotitube.dao;

import spotitube.domain.Track;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrackDAO implements ITrackDAO {
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public ArrayList<Track> getAllTracksNotInPlaylist(int playlistId) {
        try (Connection connection = dataSource.getConnection()){
            ArrayList<Track> tracks = new ArrayList<>();
            String sql = "SELECT pt.playlistId, pt.trackId, t.title\n" +
                    "FROM PlaylistsTracks pt INNER JOIN Tracks t ON pt.trackId != t.id\n" +
                    "WHERE pt.playlistId = ?\n" +
                    "ORDER BY pt.trackId";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, playlistId);
            ResultSet resultset = statement.executeQuery();

            //TODO: Add track to list and return list (after query is fixed)
//            while (resultset.next()) {
//                Track track = new Track();
//            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
