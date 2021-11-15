package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Stock implements Asset {
    public Stock(String id, String name) {
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
        return AssetType.STOCK;
    }

    private final String name;
    private final String id;
}
