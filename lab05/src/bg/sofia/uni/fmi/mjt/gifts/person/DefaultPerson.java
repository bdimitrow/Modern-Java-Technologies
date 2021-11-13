package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.*;

public class DefaultPerson<I> implements Person<I> {
    public DefaultPerson(I id) {
        this.id = id;
    }


    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        List<Gift<?>> mostExpensive = new ArrayList<>();
        while (n > 0) {
            mostExpensive.add(findMostExpensiveReceivedGift());
            --n;
        }

        return Collections.unmodifiableCollection(mostExpensive);
    }

    @Override
    public I getId() {
        return this.id;
    }

    @Override
    public void receiveGift(Gift<?> gift) throws WrongReceiverException {
        if (gift == null) {
            throw new IllegalArgumentException();
        }
        if (!this.equals(gift.getReceiver())) {
            throw new WrongReceiverException();
        }
        receivedGifts.add(gift);
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultPerson<?> that = (DefaultPerson<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private final I id;
    private Collection<Gift<?>> receivedGifts;

    private Gift<?> findMostExpensiveReceivedGift() {
        double maxPrice = 0;
        Gift<?> mostExpensive = null;
        for (var current : receivedGifts) {
            if (current.getPrice() > maxPrice) {
                maxPrice = current.getPrice();
                mostExpensive = current;
            }
        }

        return mostExpensive;
    }
}
