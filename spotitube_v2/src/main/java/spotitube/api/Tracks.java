package spotitube.api;

import spotitube.api.dto.AddTrackToPlaylistDTO;
import spotitube.api.dto.TracksDTO;
import spotitube.dao.ITrackDAO;
import spotitube.domain.LocalStorage;
import spotitube.domain.Playlist;
import spotitube.domain.Track;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;

@Path("")
public class Tracks {
    private ITrackDAO trackDAO;

    private LocalStorage localStorage;

    @GET
    @Path("tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableTracks(@QueryParam("forPlaylist") int playlistId) {

        HashMap<Integer, Playlist> allPlaylists = localStorage.getPlaylistsHashmap();
        ArrayList<Track> tracks = new ArrayList<>();

        Playlist selectedPlaylist = allPlaylists.get(playlistId);

        if (selectedPlaylist.getTracks().size() > 0) {
            tracks = trackDAO.getAllTracksNotInPlaylist(playlistId, true);
        }

        else if (selectedPlaylist.getTracks().size() == 0) {
            tracks = trackDAO.getAllTracksNotInPlaylist(playlistId, false);
        }

        if (tracks == null) {
            return Response.status(404).build();
        }

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(200).entity(tracksDTO).build();
    }

    @GET
    @Path("playlists/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTracksInPlaylist(@PathParam("id") int playlistId) {
        ArrayList<Track> tracks = trackDAO.getAllTracksInPlaylist(playlistId);

        if (tracks == null) {
            return Response.status(404).build();
        }

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(200).entity(tracksDTO).build();
    }

    @POST
    @Path("playlists/{id}/tracks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("id") int id, AddTrackToPlaylistDTO trackObject) {
        trackDAO.addTrackToPlaylist(id, trackObject.id, trackObject.offlineAvailable);
        return getAllAvailableTracks(id);
    }

    @DELETE
    @Path("playlists/{playlistId}/tracks/{trackId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@PathParam("playlistId") int playlistId, @PathParam("trackId") int trackId) {
        trackDAO.deleteTrackFromPlaylist(playlistId, trackId);

        return getAllTracksInPlaylist(playlistId);
    }

    @Inject
    public void setTrackDAO(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @Inject
    public void setLocalStorage(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }
}
