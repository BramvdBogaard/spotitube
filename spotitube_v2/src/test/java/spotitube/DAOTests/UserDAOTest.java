package spotitube.DAOTests;

import org.junit.jupiter.api.Test;
import spotitube.dao.PlaylistDAO;
import spotitube.dao.UserDAO;
import spotitube.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class UserDAOTest {

    @Test
    public void getUserTest() {
        try{
            String expectedSQL = "select * from user where user = ? AND password = ?";
            UserDAO userDAO = new UserDAO();
            DataSource dataSource = mock(DataSource.class);
            Connection connection = mock(Connection.class);
            PreparedStatement statement = mock(PreparedStatement.class);
            ResultSet resultSet = mock(ResultSet.class);
            String username = "bram";
            String password = "password";

            //instruct mocks
            when(statement.executeQuery()).thenReturn(resultSet);
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(statement);
            when(resultSet.next()).thenReturn(true).thenReturn(false);

            //run tests
            userDAO.setDataSource(dataSource);
            userDAO.getUser(username, password);

            //asserts
            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(statement).setString(1, username);
            verify(statement).setString(2, password);
            verify(statement).executeQuery();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
