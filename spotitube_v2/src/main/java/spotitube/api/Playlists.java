package spotitube.api;

import spotitube.api.dto.AddPlaylistDTO;
import spotitube.api.dto.AllPlaylistsPlusTotalPlaytimeDTO;
import spotitube.api.dto.PlayListDTO;
import spotitube.dao.IPlaylistDAO;
import spotitube.domain.LocalStorage;
import spotitube.domain.Playlist;
import spotitube.domain.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("")
public class Playlists {
    private IPlaylistDAO playlistDAO;
    private HashMap<Integer, Playlist> playlists = new HashMap<>();

    private LocalStorage localStorage;

    @GET
    @Path("playlists")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists() {
        //TODO: doesn't return playtime after each call (length = 0)!
        playlists = playlistDAO.getAllPlaylists();
        HashMap<Integer, Playlist> playlistsWithoutTracks = playlistDAO.getAllPlaylistsWithoutTracks();

        if (playlists == null) {
            return Response.status(404).build();
        }

        playlists.putAll(playlistsWithoutTracks);

        //set localstorage
        localStorage.setPlaylistsHashmap(playlists);

        AllPlaylistsPlusTotalPlaytimeDTO allPlaylistsDTO = new AllPlaylistsPlusTotalPlaytimeDTO();

        playlists.values().stream().forEach(playlist -> {
            PlayListDTO playlistDTO = new PlayListDTO();
            playlistDTO.id = playlist.getId();
            playlistDTO.name = playlist.getName();
            playlistDTO.tracks = playlist.getTracks();
            playlistDTO.owner = playlist.getOwner();

            allPlaylistsDTO.playlists.add(playlistDTO);
        });

        allPlaylistsDTO.length = getTotalPlaytimeOfAllPlaylists(playlists);

        return Response.status(200).entity(allPlaylistsDTO).build();
    }

    @DELETE
    @Path("playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int id) {
        getAllPlaylists();
        Playlist selectedPlaylist = playlists.get(id);

        if (selectedPlaylist.getTracks().size() == 0) {
            playlistDAO.deletePlaylist(id, false);
        }

        playlistDAO.deletePlaylist(id, true);

        return getAllPlaylists();

    }

    @POST
    @Path("playlists")
    @QueryParam("token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, AddPlaylistDTO playListDTO) {

        User creator = LocalStorage.getUser(token);

        if(creator == null || playListDTO == null) {
            Response.status(404).build();
        }

        playlistDAO.addNewPlaylist(creator.getUser(), playListDTO);
        return getAllPlaylists();
    }

    @PUT
    @Path("playlists/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPlaylistName(PlayListDTO playListDTO) {
        playlistDAO.editPlaylist(playListDTO);
        Response allPlaylists = getAllPlaylists();
        return allPlaylists;
    }

    public int getTotalPlaytimeOfAllPlaylists(HashMap<Integer, Playlist> playlists) {
        int totalTime =
                playlists
                        .values()
                        .stream()
                        .mapToInt(playlist -> playlist.getTotalDurationOfPlaylist())
                        .sum();
        return totalTime;
    }

    @Inject
    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    @Inject
    public void setLocalStorage(LocalStorage localStorage) { this.localStorage = localStorage; }
}
