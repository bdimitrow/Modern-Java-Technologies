package bg.sofia.uni.fmi.mjt.cache.factory;

import bg.sofia.uni.fmi.mjt.cache.Cache;
import bg.sofia.uni.fmi.mjt.cache.LeastFrequentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.LeastRecentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class CacheFactoryTest {

    @Mock
    private Storage<Integer, String> storage;

    private static final int TEST_CAPACITY = 5;

    @Test
    void getInstance() {
        assertThrows(IllegalArgumentException.class,
                () -> CacheFactory.getInstance(storage, -1, EvictionPolicy.LEAST_FREQUENTLY_USED));
        Cache<Integer, String> test1 =
                CacheFactory.getInstance(storage, TEST_CAPACITY, EvictionPolicy.LEAST_FREQUENTLY_USED);
        assertTrue(test1 instanceof LeastFrequentlyUsedCache);
    }

    @Test
    void testGetInstance() {
        Cache<Integer, String> test2 = CacheFactory.getInstance(storage, EvictionPolicy.LEAST_RECENTLY_USED);
        assertTrue(test2 instanceof LeastRecentlyUsedCache);
    }
}