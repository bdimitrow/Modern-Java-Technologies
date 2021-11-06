package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Fiat extends AbstractAsset{
    protected Fiat(String id, String name) {
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
        return AssetType.FIAT;
    }
}
