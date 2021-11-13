package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.AccountType;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;

public class Spotify implements StreamingService {
    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (!checkAccount(account)) {
            throw new AccountNotFoundException();
        }
        if (!checkPlayable(title)) {
            throw new PlayableNotFoundException();
        }

        Playable toBePlayed = findByTitle(title);
        if (toBePlayed == null) {
            throw new PlayableNotFoundException();
        }

        account.listen(toBePlayed);
        System.out.println(toBePlayed.play());
    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if (account == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException("Illegal arguments were provided.");
        }
        if (!checkAccount(account)) {
            throw new AccountNotFoundException();
        }
        if (!checkPlayable(title)) {
            throw new PlayableNotFoundException();
        }

        Account currentAccount = null;
        for (Account acc : accounts) {
            if (acc.equals(account)) {
                currentAccount = acc;
                break;
            }
        }
        Playlist playlistOfLikedSongs = currentAccount.getLibrary().getLiked();
        try {
            playlistOfLikedSongs.add(findByTitle(title));
        } catch (PlaylistCapacityExceededException e) {
            e.printStackTrace();
            throw new StreamingServiceException();
        }

    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Illegal arguments were provided.");
        }
        if (!checkPlayable(title)) {
            throw new PlayableNotFoundException();
        }

        for (Playable playable : this.playableContent) {
            if (playable != null) {
                if (playable.getTitle().equals(title)) {
                    return playable;
                }
            }
        }
        return null;
    }

    @Override
    public Playable getMostPlayed() {
        if (playableContent.length == 0) {
            return null;
        }

        Playable mostPlayed = playableContent[0];
        for (Playable playable : playableContent) {
            if (playable != null) {
                if (playable.getTotalPlays() > mostPlayed.getTotalPlays()) {
                    mostPlayed = playable;
                }
            }
        }

        return mostPlayed.getTotalPlays() == 0 ? null : mostPlayed;
    }

    @Override
    public double getTotalListenTime() {
        double totalListenTime = 0;
        for (Account account : accounts) {
            totalListenTime += account.getTotalListenTime();
        }

        return totalListenTime;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double totalPlatformRevenue = 0;
        for (Account acc : accounts) {
            if (acc.getType() == AccountType.PREMIUM) {
                totalPlatformRevenue += 25;
            } else {
                totalPlatformRevenue += acc.getAdsListenedTo() * 0.10;
            }
        }

        return totalPlatformRevenue;
    }


    private boolean checkAccount(Account account) {
        for (Account acc : this.accounts) {
            if (acc.equals(account)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPlayable(String title) {
        for (Playable playable : this.playableContent) {
            if (playable != null) {
                if (playable.getTitle().equals(title)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final Account[] accounts;
    private final Playable[] playableContent;
}
