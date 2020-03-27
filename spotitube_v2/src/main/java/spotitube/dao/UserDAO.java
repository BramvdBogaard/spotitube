package spotitube.dao;

import spotitube.domain.User;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUserDAO{
    @Resource(name = "jdbc/spotitube")
    DataSource dataSource;

    @Override
    public User getUser(String user, String password) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "select * from user where user = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                User newUser = new User();
                newUser.setToken(resultSet.getString("token"));
                newUser.setUsername(resultSet.getString("user"));
                newUser.setPassword(resultSet.getString("password"));

                return newUser;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
