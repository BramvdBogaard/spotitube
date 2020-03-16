package spotitube.dao;

import spotitube.domain.User;

public interface IUserDAO {
    User getUser(String user, String password);
}
