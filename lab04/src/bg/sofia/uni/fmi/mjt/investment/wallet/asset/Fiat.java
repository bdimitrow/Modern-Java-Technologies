package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Fiat implements Asset {
    public Fiat(String id, String name) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AssetType getType() {
        return AssetType.FIAT;
    }

    private final String name;
    private final String id;
}
