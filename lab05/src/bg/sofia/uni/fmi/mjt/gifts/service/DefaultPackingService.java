package bg.sofia.uni.fmi.mjt.gifts.service;

import bg.sofia.uni.fmi.mjt.gifts.gift.BirthdayGift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;
import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;
import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DefaultPackingService<T extends Priceable> implements PackingService<T> {
    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T item) {
        if (sender == null || receiver == null || item == null) {
            throw new IllegalArgumentException();
        }

        Gift<T> packed = new BirthdayGift<T>(sender, receiver, new ArrayList<>());
        packed.addItem(item);
        return packed;
    }

    @Override
    public Gift<T> pack(Person<?> sender, Person<?> receiver, T... items) {
        if (sender == null || receiver == null || items == null) {
            throw new IllegalArgumentException();
        }

        Gift<T> packed = new BirthdayGift<T>(sender, receiver, new ArrayList<>());
        for (T curr : items) {
            packed.addItem(curr);
        }

        return packed;
    }

    @Override
    public Collection<T> unpack(Gift<T> gift) {
        if (gift == null) {
            throw new IllegalArgumentException();
        }

        return List.copyOf(gift.getItems());
    }
}
