package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist {
    private final static int PLAYLIST_CAPACITY = 20;

    public UserPlaylist(String name) {
        this.playlistName = name;
        this.numberofPlayables = 0;
        this.playableContent = new Playable[PLAYLIST_CAPACITY];
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if (numberofPlayables >= PLAYLIST_CAPACITY) {
            throw new PlaylistCapacityExceededException();
        }
        this.playableContent[numberofPlayables] = playable;
        ++numberofPlayables;
    }

    @Override
    public String getName() {
        return this.playlistName;
    }

    private String playlistName;
    private Playable[] playableContent;
    private int numberofPlayables;
}
