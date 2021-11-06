package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

import java.util.Objects;

public abstract class AbstractAsset implements Asset{
    protected AbstractAsset(String id, String name){
        this.id = id;
        this.name = name;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract AssetType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAsset that = (AbstractAsset) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    protected String id;
    protected String name;
}
