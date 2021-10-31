package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class FreeAccount extends Account {
    public FreeAccount(String email, Library library){
        super(email,library);
        accType = AccountType.FREE;
    }

    @Override
    public int getAdsListenedTo() {
        return numberOfListenedContent/5;
    }

    @Override
    public AccountType getType() {
        return accType;
    }

    private AccountType accType;
}
