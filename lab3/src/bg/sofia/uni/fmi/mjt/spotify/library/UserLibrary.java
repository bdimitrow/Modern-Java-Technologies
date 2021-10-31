package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library {
    public UserLibrary() {
        this.playlistContent[0] = new UserPlaylist("Liked Content", null);
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (playlistContent.length >= 21) throw new LibraryCapacityExceededException();
        playlistContent[playlistContent.length] = playlist;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (this.playlistContent == null) {
            throw new EmptyLibraryException();
        }
        int i = 0;
        for (Playlist playlist : playlistContent) {
            if (playlist.getName().equals(name)) {
                for (int j = i; j < playlistContent.length; ++j) {
                    playlistContent[j] = playlistContent[j + 1];
                }
                break;
            }
            ++i;
        }
    }

    @Override
    public Playlist getLiked() {
        return findLikedSongs();
    }

    private Playlist findLikedSongs() {
        for (Playlist playlist : this.playlistContent) {
            if (playlist.getName().equals("Liked Content")) {
                return playlist;
            }
        }
        return null;
    }

    private Playlist[] playlistContent;
}
