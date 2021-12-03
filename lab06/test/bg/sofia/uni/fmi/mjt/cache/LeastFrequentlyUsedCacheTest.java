package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class LeastFrequentlyUsedCacheTest<K, V> {
    @Mock
    private static Storage<Integer, String> storage;

    private static LeastFrequentlyUsedCache<Integer, String> lfu;

    @BeforeAll
    public static void setUp() {
        lfu = new LeastFrequentlyUsedCache<>(storage, 5);
    }

    @AfterEach
    public void tearDown() {
        lfu.clear();
    }

    @Test
    void size() {
        assertEquals(lfu.size(), 0);

        lfu.put(1, "edno");
        lfu.put(2, "dve");

        assertEquals(lfu.size(), 2);

        lfu.put(3, "tri");

        assertEquals(lfu.size(), 3);
    }

    @Test
    void values() {
        lfu.put(1, "edno");
        lfu.put(2, "dve");
        lfu.put(3, "tri");
//        assertIterableEquals(lfu.values(), Array("edno","edve","tri"));
    }

    @Test
    void clear() {
        lfu.put(1, "edno");
        assertFalse(lfu.values().isEmpty());

        lfu.clear();
        assertTrue(lfu.values().isEmpty());
    }

    @Test
    void getFromCache() {
        lfu.put(2, "dve");
        lfu.put(3, "tri");

        assertEquals(lfu.getFromCache(2), "dve");
        assertNull(lfu.getFromCache(4));
    }

    @Test
    void put() throws ItemNotFound {
        lfu.put(2, "dve");
        assertEquals(lfu.get(2), "dve");
    }

    @Test
    void containsKey() {
        lfu.put(2, "dve");
        assertTrue(lfu.containsKey(2));
        assertFalse(lfu.containsKey(3));
    }

    @Test
    void evictFromCache() throws ItemNotFound {
        lfu.put(1, "edno");
        lfu.put(1, "dve");
        lfu.put(1, "tri");
        lfu.put(2, "dve");
        lfu.evictFromCache();
        assertTrue(lfu.containsKey(1));
        assertFalse(lfu.containsKey(2));
    }

    @Test
    void getHitRate() throws ItemNotFound {
        assertEquals(lfu.getHitRate(),0);
        lfu.put(1,"edno");
        when(storage.retrieve(3)).thenReturn("tri");
        lfu.get(3);
        assertEquals(lfu.getHitRate(),0);
        lfu.get(1);
        assertEquals(lfu.getHitRate(),1);
    }

    @Test
    void testGetWithNull() {
        assertThrows(IllegalArgumentException.class,
                () -> lfu.get(null),
                "key can not be null");
    }

    @Test
    void testGetSucc() throws ItemNotFound {
        lfu.put(1, "edno");
        assertNotNull(lfu.get(1));
        assertEquals(lfu.get(1), "edno");
    }

    @Test
    void testGetFromStorageNull() {
//        when(storage.retrieve(2)).thenThrow(ItemNotFound.class);
//        assertThrows(ItemNotFound.class, ()->lfu.get(2));
    }

    @Test
    void testGetFromStorage() {
//    when(storage.retrieve(2)).thenReturn("dve");

    }


}