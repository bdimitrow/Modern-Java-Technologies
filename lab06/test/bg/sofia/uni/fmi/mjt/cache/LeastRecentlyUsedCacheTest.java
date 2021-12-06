package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeastRecentlyUsedCacheTest {

    @Mock
    private static Storage<Integer, String> storage;

    private static LeastRecentlyUsedCache<Integer, String> lru;

    @BeforeAll
    public static void setUp() {
        lru = new LeastRecentlyUsedCache<Integer, String>(storage, 5);
    }

    @AfterEach
    public void tearDown() {
        lru.clear();
    }

    @Test
    void size() {
        assertEquals(lru.size(), 0);

        lru.put(1, "edno");
        lru.put(2, "dve");

        assertEquals(lru.size(), 2);

        lru.put(3, "tri");

        assertEquals(lru.size(), 3);
    }

    @Test
    void clear() {
        lru.put(1, "edno");
        assertFalse(lru.values().isEmpty());

        lru.clear();
        assertTrue(lru.values().isEmpty());
    }

    @Test
    void values() {
        lru.put(1, "edno");
        lru.put(2, "dve");
        lru.put(3, "tri");
        lru.put(4, "chetiri");
        assertIterableEquals(lru.values(), List.of("edno", "dve", "tri", "chetiri"));
    }

    @Test
    void getFromCache() {
        lru.put(1, "edno");
        lru.put(2, "dve");

        assertEquals(lru.getFromCache(2), "dve");
        assertNull(lru.getFromCache(4));
    }

    @Test
    void put() {
        lru.put(1, "edno");
        assertEquals(lru.getFromCache(1), "edno");
    }

    @Test
    void containsKey() {
        lru.put(1, "edno");
        assertTrue(lru.containsKey(1));
        assertFalse(lru.containsKey(2));
    }

    @Test
    void evictFromCache() {
        lru.put(1, "edno");
        lru.put(2, "dve");
        lru.evictFromCache();
        assertFalse(lru.containsKey(1));
        assertTrue(lru.containsKey(2));
    }

    @Test
    void addToCache() {
        CacheBase<Integer, String> cb = new LeastRecentlyUsedCache<Integer, String>(storage, 2);
        cb.addToCache(1, "edno");
        cb.addToCache(2, "dve");
        cb.addToCache(3, "tri");

        assertFalse(cb.containsKey(1));
        assertTrue(cb.containsKey(3));
    }
}