package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;

public class BirthdayGift<T extends Priceable> implements Gift<T> {
    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;
        this.items = items;
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        double totalPrice = 0;
        for (T current : items) {
            totalPrice += current.getPrice();
        }

        return totalPrice;
    }

    @Override
    public void addItem(T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }

        this.items.add(t);
    }

    @Override
    public boolean removeItem(T t) {
        if (t == null) {
            return false;
        }

        return items.removeIf(t::equals);
    }

    @Override
    public Collection<T> getItems() {
        return this.items;
    }

    @Override
    public T getMostExpensiveItem() {
        double currentPrice = 0;
        double maxPrice = 0;
        T mostExpensiveItems = null;
        for (T current : items) {
            currentPrice = current.getPrice();
            if (currentPrice > maxPrice) {
                maxPrice = currentPrice;
                mostExpensiveItems = current;
            }
        }

        return mostExpensiveItems;
    }

    private Person<?> sender;
    private Person<?> receiver;
    private Collection<T> items;
}
