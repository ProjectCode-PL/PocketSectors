package pl.projectcode.pocketsectors.nukkit.user;

import cn.nukkit.Player;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private final Map<String, User> users = new HashMap<>();

    public void createUser(Player player) {
        this.createUser(player.getName());
    }

    public void createUser(String name) {
        if(!this.userExists(name)) {
            this.users.put(name, new User(name));
        }
    }

    public User getUser(Player player) {
        return this.getUser(player.getName());
    }

    public User getUser(String name) {
        return this.users.get(name);
    }

    public boolean userExists(Player player) {
        return this.userExists(player.getName());
    }

    public boolean userExists(String name) {
        return this.users.containsKey(name);
    }
}
