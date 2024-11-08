package edu.uw.tcss.app;

import edu.uw.tcss.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

class CarTest {

    private Car car;
    private final int initialX = 10;
    private final int initialY = 20;
    private final Direction initialDir = Direction.NORTH;
    private Map<Direction, Terrain> neighbors;

    @BeforeEach
    void setUp() {
        car = new Car(initialX, initialY, initialDir);
        neighbors = new HashMap<>();
    }

    @Test
    void testCarConstructor() {
        assertEquals(initialX, car.getX());
        assertEquals(initialY, car.getY());
        assertEquals(initialDir, car.getDirection());
        assertTrue(car.isAlive());
        assertEquals(15, car.getDeathTime());
    }

    @Test
    void testCanPassStreet() {
        assertTrue(car.canPass(Terrain.STREET, Light.RED));
    }

    @Test
    void testCanPassLightGreen() {
        assertTrue(car.canPass(Terrain.LIGHT, Light.GREEN));
        assertFalse(car.canPass(Terrain.LIGHT, Light.RED));
    }

    @Test
    void testCanPassCrosswalk() {
        assertTrue(car.canPass(Terrain.CROSSWALK, Light.GREEN));
        assertFalse(car.canPass(Terrain.CROSSWALK, Light.RED));
    }

    @Test
    void testChooseDirection_GoesStraight() {
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        assertEquals(Direction.NORTH, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesLeft() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        assertEquals(Direction.WEST, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesRight() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        assertEquals(Direction.EAST, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesReverse() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        assertEquals(Direction.SOUTH, car.chooseDirection(neighbors));
    }

    @Test
    void testCollideWithTruck() {
        Truck truck = new Truck(0, 0, Direction.NORTH);
        car.collide(truck);
        assertFalse(car.isAlive());
    }

    @Test
    void testPokeAndRevive() {
        car.collide(new Truck(0, 0, Direction.NORTH));
        for (int i = 0; i < 15; i++) {
            car.poke();
        }
        assertTrue(car.isAlive());
    }

    @Test
    void testReset() {
        car.setX(5);
        car.setY(5);
        car.setDirection(Direction.WEST);
        car.reset();
        assertEquals(initialX, car.getX());
        assertEquals(initialY, car.getY());
        assertEquals(initialDir, car.getDirection());
        assertTrue(car.isAlive());
    }

    @Test
    void testCanPassDefaultCase() {
        assertFalse(car.canPass(Terrain.GRASS, Light.GREEN), "Car should not pass on "
                + "GRASS terrain");
    }
    @Test
    void testChooseDirection_PreferLight() {
        neighbors.put(Direction.NORTH, Terrain.LIGHT);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.NORTH, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_PreferCrosswalk() {
        neighbors.put(Direction.NORTH, Terrain.CROSSWALK);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.NORTH, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_LeftLight() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.LIGHT);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.WEST, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_LeftCrosswalk() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.CROSSWALK);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.WEST, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_RightLight() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.LIGHT);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.EAST, car.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_RightCrosswalk() {
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.CROSSWALK);
        neighbors.put(Direction.SOUTH, Terrain.GRASS);

        assertEquals(Direction.EAST, car.chooseDirection(neighbors));
    }

}
