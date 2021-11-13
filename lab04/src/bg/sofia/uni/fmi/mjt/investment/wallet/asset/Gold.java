package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Gold extends AbstractAsset {
    protected Gold(String id, String name) {
        super(id, name);
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
        return AssetType.GOLD;
    }
}
