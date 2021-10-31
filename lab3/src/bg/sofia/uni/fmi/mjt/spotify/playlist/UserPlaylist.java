package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist{
    public UserPlaylist(String name, Playable[] playableContent){
        this.playlistName = name;
        this.playableContent=playableContent;
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if(this.playableContent.length >= 20){
            throw new PlaylistCapacityExceededException();
        }
        this.playableContent[this.playableContent.length]=playable;
    }

    @Override
    public String getName() {
        return this.playlistName;
    }

    private String playlistName;
    private Playable[] playableContent;
}
