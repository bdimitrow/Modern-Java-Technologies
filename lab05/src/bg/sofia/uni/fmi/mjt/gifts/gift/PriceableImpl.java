package bg.sofia.uni.fmi.mjt.gifts.gift;

public class PriceableImpl implements Priceable {

    @Override
    public double getPrice() {
        return this.price;
    }

    private double price;
}
