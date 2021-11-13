package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {
    private final static int LIBRARY_CAPACITY = 21;

    public UserLibrary() {
        numberOfPlaylists = 1;
        playlistContent = new Playlist[LIBRARY_CAPACITY];
        playlistContent[0] = new UserPlaylist("Liked Content");
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (numberOfPlaylists >= LIBRARY_CAPACITY) throw new LibraryCapacityExceededException();
        playlistContent[numberOfPlaylists] = playlist;
        ++numberOfPlaylists;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (name.equals("Liked Content")) {
            throw new IllegalArgumentException();
        }
        if (this.playlistContent == null) {
            throw new EmptyLibraryException();
        }
        int i = 0;
        for (Playlist playlist : playlistContent) {
            if(playlist!=null) {
                if (playlist.getName().equals(name)) {
                    for (int j = i; j < numberOfPlaylists; ++j) {
                        playlistContent[j] = playlistContent[j + 1];
                    }
                    break;
                }
            }
            ++i;
        }
    }

    @Override
    public Playlist getLiked() {
        return playlistContent[0];
    }

//    private Playlist findLikedSongs() {
//        for (Playlist playlist : this.playlistContent) {
//            if (playlist != null) {
//                if (playlist.getName().equals("Liked Content")) {
//                    return playlist;
//                }
//            }
//        }
//        return null;
//    }

    private Playlist[] playlistContent;
    private int numberOfPlaylists;
}
