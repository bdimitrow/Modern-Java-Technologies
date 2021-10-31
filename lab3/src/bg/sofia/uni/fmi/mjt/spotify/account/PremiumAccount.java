package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class PremiumAccount extends Account{
    public PremiumAccount(String email, Library library) {
        super(email, library);
        this.accType = AccountType.PREMIUM;
    }

    @Override
    public int getAdsListenedTo() {
        return 0;
    }

    @Override
    public AccountType getType() {
        return accType;
    }

    private AccountType accType;
}
