package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;
import java.util.Objects;

public class AcquisitionClass implements Acquisition {
    private Asset asset;
    private double price;
    private LocalDateTime time;
    private int quantity;

    public AcquisitionClass(Asset asset, double price, LocalDateTime time, int quantity) {
        this.asset = asset;
        this.price = price;
        this.time = time;
        this.quantity = quantity;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return this.time;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public Asset getAsset() {
        return this.asset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcquisitionClass that = (AcquisitionClass) o;
        return Double.compare(that.price, price) == 0 &&
                quantity == that.quantity &&
                Objects.equals(asset, that.asset) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset, price, time, quantity);
    }
}
