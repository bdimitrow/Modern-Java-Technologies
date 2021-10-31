package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.AccountType;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class Spotify implements StreamingService {
    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    public static int numberOfListenedContent = 0;

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account.getEmail() == null || title == null || title.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (!checkAccount(account)) {
            throw new AccountNotFoundException();
        }
        if (!checkPlayable(title)) {
            throw new PlayableNotFoundException();
        }

        for (Playable playable : this.playableContent) {
            if (title.equals(playable.getTitle())) {
                playable.play();
            }
        }
    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {

    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (title == null || title.isBlank()) throw new IllegalArgumentException();
        if (!checkPlayable(title)) throw new PlayableNotFoundException();
        for (Playable playable : this.playableContent) {
            if (playable.getTitle().equals(title) {
                return playable;
            }
        }
    }

    @Override
    public Playable getMostPlayed() {
        return null;
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
            if (playable.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    private Account[] accounts;
    private Playable[] playableContent;
}
