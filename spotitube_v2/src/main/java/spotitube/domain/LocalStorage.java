package spotitube.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalStorage {
    private static List<User> usersList = new ArrayList<>();
    private static String currentUser;
    private HashMap<Integer, Playlist> playlistsHashmap = new HashMap<>();

    public HashMap<Integer, Playlist> getPlaylistsHashmap() {
        return playlistsHashmap;
    }

    public void setPlaylistsHashmap(HashMap<Integer, Playlist> playlistsHashmap) {
        this.playlistsHashmap = playlistsHashmap;
    }

    public static User getUser(String token) {
        for(User user: usersList) {
            if (user.getToken().equals(token)) {
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user) {
        currentUser = user.getToken();
        usersList.add(user);
    }

    public String getCurrentUser() {
        return currentUser;
    }
}

