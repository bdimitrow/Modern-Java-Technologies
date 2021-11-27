package bg.sofia.uni.fmi.mjt.twitch.user;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;

import java.util.Set;

public class UserImpl implements User{
    public UserImpl(String username, UserStatus status){
        this.username = username;
        this.status = status;
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public UserStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    private UserStatus status;
    private String username;
    private Set<Content> listened;

    public Set<Content> getListened() {
        return listened;
    }

    public void setListened(Set<Content> listened) {
        this.listened = listened;
    }
}
