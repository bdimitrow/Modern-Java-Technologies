package server.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.LruCache;
import server.dto.Food;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheFoodTest {
    private LRUCacheFood lruCache;


    @BeforeEach
    void setUp() {
        lruCache = new LRUCacheFood(4);
        Food one = new Food(1, "des1", "Branded", "123");
        Food two = new Food(2, "des2", "Normal", "321");
        Food three = new Food(3, "des3", "Special", "222");
        lruCache.set(one);
        lruCache.set(two);
        lruCache.set(three);
    }

    @Test
    void getByKeywords() {
        Food one = new Food(1, "des1", "Branded", "123");
        Food one2 = new Food(122, "des1", "Branded2", "12334");
        Food two = new Food(2, "des2", "Normal", "321");
        lruCache.set(one2);
        assertTrue(lruCache.getByKeywords("des1").getFoods().contains(one));
        assertTrue(lruCache.getByKeywords("des1").getFoods().contains(one2));
        assertFalse(lruCache.getByKeywords("des1").getFoods().contains(two));
    }

    @Test
    void getByUpcCode() {
        assertEquals(lruCache.getByUpcCode("123").getGtinUpc(), "123");
        assertNull(lruCache.getByUpcCode("333"));
    }

    @Test
    void set() {
        Food four = new Food(12, "test", "type", "12333");
        assertFalse(lruCache.get(four));
        lruCache.set(four);
        assertTrue(lruCache.get(four));
    }

    @Test
    void size() {
        assertEquals(lruCache.size(),3);
        lruCache.set(new Food(11,"11","type","1222313"));
        assertEquals(lruCache.size(),4);
    }

    @Test
    void clear() {
        assertNotEquals(lruCache.size(), 0);
        lruCache.clear();
        assertEquals(lruCache.size(), 0);
    }

    @Test
    void isEmpty() {
        assertFalse(lruCache.isEmpty());
        lruCache.clear();
        assertTrue(lruCache.isEmpty());
    }
}