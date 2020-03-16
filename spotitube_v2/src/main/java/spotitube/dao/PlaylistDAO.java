package spotitube.dao;

import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.domain.Playlist;
import spotitube.domain.Track;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;

public class PlaylistDAO implements IPlaylistDAO {
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public HashMap<Integer, Playlist> getAllPlaylists() {
        HashMap<Integer, Playlist> playlists = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "select p.id, p.name, p.owner, t.id as 'trackId', t.title, t.performer, t.duration, t.album, t.playcount, t.publicationDate, t.description, t.offlineAvailable\n" +
                    "FROM playlists p INNER JOIN PlaylistsTracks pt ON pt.playlistId = p.id INNER JOIN Tracks t ON pt.trackId = t.id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playlistId = resultSet.getInt(("id"));

                if (!playlists.containsKey(playlistId)) {
                    Playlist playlist = new Playlist();
                    playlist.setId(resultSet.getInt("id"));
                    playlist.setName(resultSet.getString("name"));
                    playlist.setOwner(resultSet.getString("owner"));

                    playlists.put(playlistId, playlist);

                }

                Track track = new Track();
                track.setId(resultSet.getInt("trackId"));
                track.setTitle(resultSet.getString("title"));
                track.setPerformer(resultSet.getString("performer"));
                track.setDuration(resultSet.getInt("duration"));
                track.setAlbum(resultSet.getString("album"));
                track.setPlaycount(resultSet.getInt("playcount"));
                track.setPublicationDate(resultSet.getString("publicationDate"));
                track.setDescription(resultSet.getString("description"));
                track.setOfflineAvailable(resultSet.getBoolean("offlineAvailable"));

                Playlist playlist = playlists.get(playlistId);
                playlist.addTrackToList(track);
            }

            return playlists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    @Override
    public HashMap<Integer, Playlist> getAllPlaylistsWithoutTracks() {
        HashMap<Integer, Playlist> playlists = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "select p.id, p.name, p.owner\n" +
                    "from playlists p INNER JOIN PlaylistsTracks pt ON pt.playlistId != p.id\n" +
                    "GROUP BY p.id";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int playlistId = resultSet.getInt(("id"));

                if (!playlists.containsKey(playlistId)) {
                    Playlist playlist = new Playlist();
                    playlist.setId(resultSet.getInt("id"));
                    playlist.setName(resultSet.getString("name"));
                    playlist.setOwner(resultSet.getString("owner"));

                    playlists.put(playlistId, playlist);
                }
            }
            return playlists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }

    @Override
    public void editPlaylist(PlayListDTO playListDTO) {
        try (Connection connection = dataSource.getConnection()){
            String sql = "UPDATE Playlists\n" +
                    "SET name = ?\n" +
                    "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playListDTO.name);
            statement.setInt(2, playListDTO.id);
            statement.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePlaylist(int id, boolean hasTracks) {
        //TODO: Delete only works on projects that have tracks!!
        try (Connection connection = dataSource.getConnection()) {
            String sql;

            if (hasTracks) {
                sql = "DELETE p, pt\n" +
                        "from playlists p INNER JOIN PlaylistsTracks pt on pt.playlistId = p.id\n" +
                        "where p.id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                preparedStatement.execute();
            }

            sql = "DELETE\n" +
                    "from playlists\n" +
                    "where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNewPlaylist(String user, AddPlaylistDTO playListDTO) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO Playlists VALUES (null, ? , ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playListDTO.name);
            statement.setString(2, user);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
