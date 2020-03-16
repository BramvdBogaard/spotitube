package spotitube.api;

import spotitube.api.dto.TracksDTO;
import spotitube.dao.ITrackDAO;
import spotitube.domain.Track;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("")
public class Tracks {
    private ITrackDAO trackDAO;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableTracks(@QueryParam("forPlaylist") int playlistId) {
        System.out.println(playlistId);

        //TODO: Only works on playlists that have tracks!
        ArrayList<Track> tracks = trackDAO.getAllTracksNotInPlaylist(playlistId);

        if (tracks == null) {
            return Response.status(404).build();
        }

        TracksDTO tracksDTO = new TracksDTO();
        tracksDTO.tracks = tracks;

        return Response.status(200).entity(tracksDTO).build();
    }

    @Inject
    public void setTrackDAO(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}
