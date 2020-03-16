package spotitube;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spotitube.api.Users;
import spotitube.api.dto.UserLoginRequest;
import spotitube.dao.IUserDAO;
import spotitube.domain.User;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersTest {
    private static Users users;

    @BeforeAll
    public static void setup() {
        users = new Users();
    }

    @Test
    public void loginUserTest() {
        //Setup mock
        IUserDAO userDAO = mock(IUserDAO.class);
        User user = new User();
        UserLoginRequest loginUser = new UserLoginRequest();

        String username = "abc";
        String password = "def";
        String token = "1234";

        user.setPassword(password);
        user.setUsername(username);
        user.setToken(token);

        loginUser.setUser(username);
        loginUser.setPassword(password);

        when(userDAO.getUser(username, password)).thenReturn(user);
        users.setUserDAO(userDAO);

        //Actual test
        Response response = UsersTest.users.loginUser(loginUser);
        spotitube.domain.User responseUser = (spotitube.domain.User) response.getEntity();

        assertEquals(200, response.getStatus());
        assertEquals(username, responseUser.getUser());
        assertEquals(password, responseUser.getPassword());
        assertEquals(token, responseUser.getToken());
    }

    @Test
    public void userNotFoundTest() {
        //Setup Mock
        IUserDAO userDAO = mock(IUserDAO.class);
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setPassword("1234");
        userLoginRequest.setUser("abcd");

        when(userDAO.getUser("user", "12345")).thenReturn(null);
        users.setUserDAO(userDAO);

        //Actual test
        Response response = users.loginUser(userLoginRequest);
        assertEquals(404, response.getStatus());
    }
}
