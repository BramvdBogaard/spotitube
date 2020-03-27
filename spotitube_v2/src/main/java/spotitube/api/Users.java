package spotitube.api;

import spotitube.api.dto.UserLoginRequest;
import spotitube.dao.IUserDAO;
import spotitube.dao.UserDAO;
import spotitube.domain.LocalStorage;
import spotitube.domain.User;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class Users {

    private IUserDAO userDAO;
    private LocalStorage localStorage;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginRequest input) {
        User user = userDAO.getUser(input.getUser(), input.getPassword());

        if (user == null ) {
            return Response.status(404).build();
        }

        LocalStorage.addUser(user);
        return Response.status(200).entity(user).build();
    }

    @Inject
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Inject
    public void setLocalStorage(LocalStorage localStorage) { this.localStorage = localStorage; }
}
