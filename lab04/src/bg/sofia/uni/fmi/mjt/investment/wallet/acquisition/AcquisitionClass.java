package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

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


}
