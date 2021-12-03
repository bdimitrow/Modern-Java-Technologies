//package bg.sofia.uni.fmi.mjt.cache;
//
//import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class CacheBaseTest<K, V> {
//    @Mock
//    private Storage<K, V> storage;
//
//
//    @InjectMocks
//    private CacheBase cacheBase;
//
//    @Test
//    void testGetNull() {
//        assertThrows(IllegalArgumentException.class, () -> cacheBase.get(null), "The argument can not be null");
//    }
//
//}