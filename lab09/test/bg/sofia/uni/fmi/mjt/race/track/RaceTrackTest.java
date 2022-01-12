package bg.sofia.uni.fmi.mjt.race.track;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RaceTrackTest {
    private RaceTrack track;

    @BeforeEach
    void setUp() throws InterruptedException {
        track = new RaceTrack(3);
    }

    @Test
    void testPitConstructorNegative() {
        assertThrows(IllegalArgumentException.class, () -> new RaceTrack(-3));
    }

    @Test
    void testWithMultipleCars() throws InterruptedException {
        List<Car> carList = new ArrayList<>();
        for (int i = 1; i <= 3; ++i) {
            carList.add(new Car(i, 3, track));
        }
        for (Car car : carList) {
            car.run();
        }

        Thread.sleep(160);
        track.getPit().finishRace();

        for (Car car : carList) {
            assertEquals(-1, car.getNPitStops());
        }

        assertEquals(9, track.getPit().getPitStopsCount());
        assertEquals(3, track.getNumberOfFinishedCars());

    }
}