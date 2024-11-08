package edu.uw.tcss.app;

import edu.uw.tcss.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TruckTest {

    private Truck truck;
    private final int initialX = 15;
    private final int initialY = 25;
    private final Direction initialDir = Direction.SOUTH;
    private Map<Direction, Terrain> neighbors;

    @BeforeEach
    void setUp() {
        truck = new Truck(initialX, initialY, initialDir);
        neighbors = new HashMap<>();
    }
    @Test
    public void testCanPass() {
        final List<Terrain> validTerrain = new ArrayList<>();
        validTerrain.add(Terrain.STREET);
        validTerrain.add(Terrain.LIGHT);
        validTerrain.add(Terrain.CROSSWALK);

        final Truck truck = new Truck(0, 0, Direction.NORTH);
        for (final Terrain destinationTerrain : Terrain.values()) {
            for (final Light currentLightCondition : Light.values()) {
                if (destinationTerrain == Terrain.CROSSWALK && currentLightCondition == Light.RED) {
                    assertFalse(truck.canPass(destinationTerrain, currentLightCondition),
                            "Truck should NOT be able to pass " + destinationTerrain
                                    + " with red light");
                } else if (validTerrain.contains(destinationTerrain)) {
                    assertTrue(truck.canPass(destinationTerrain, currentLightCondition),
                            "Truck should be able to pass " + destinationTerrain
                                    + " with light " + currentLightCondition);
                } else {
                    assertFalse(truck.canPass(destinationTerrain, currentLightCondition),
                            "Truck should NOT be able to pass " + destinationTerrain
                                    + " with light " + currentLightCondition);
                }
            }
        }
    }

    @Test
    void testTruckConstructor() {
        assertEquals(initialX, truck.getX());
        assertEquals(initialY, truck.getY());
        assertEquals(initialDir, truck.getDirection());
        assertTrue(truck.isAlive());
        assertEquals(0, truck.getDeathTime());
    }

    @Test
    void testCanPassStreet() {
        assertTrue(truck.canPass(Terrain.STREET, Light.RED));
    }

    @Test
    void testCanPassCrosswalkLight() {
        assertTrue(truck.canPass(Terrain.CROSSWALK, Light.GREEN));
        assertFalse(truck.canPass(Terrain.CROSSWALK, Light.RED));
    }

    @Test
    void testCanPassDefaultCaseForTruck() {
        assertFalse(truck.canPass(Terrain.GRASS, Light.GREEN), "Truck should not pass on GRASS terrain");
    }

    @Test
    void testChooseDirection_StayPutOnRedCrosswalk() {
        neighbors.put(Direction.SOUTH, Terrain.CROSSWALK);

        assertEquals(Direction.SOUTH, truck.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesStraight() {
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.GRASS);

        assertEquals(Direction.SOUTH, truck.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesLeft() {
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.GRASS);

        assertEquals(Direction.EAST, truck.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesRight() {
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.STREET);
        neighbors.put(Direction.NORTH, Terrain.GRASS);

        assertEquals(Direction.WEST, truck.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_GoesReverse() {
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.STREET);

        assertEquals(Direction.NORTH, truck.chooseDirection(neighbors));
    }


    @Test
    void testCollideWithCar() {
        Car car = new Car(0, 0, Direction.NORTH);
        truck.collide(car);
        assertTrue(truck.isAlive());
    }

    @Test
    void testPokeNoEffect() {
        truck.poke();
        assertTrue(truck.isAlive());
    }

    @Test
    void testReset() {
        truck.setX(7);
        truck.setY(7);
        truck.setDirection(Direction.EAST);
        truck.reset();
        assertEquals(initialX, truck.getX());
        assertEquals(initialY, truck.getY());
        assertEquals(initialDir, truck.getDirection());
        assertTrue(truck.isAlive());
    }

    @Test
    public void testToString() {
        Truck truck = new Truck(10, 11, Direction.NORTH);

        String result = truck.toString();

        assertEquals("Truck at (10, 11) facing NORTH", result, "The toString output should match the "
              + "truck's position and direction.");
    }
    @Test
    void testChooseDirection_MovesToLight() {
        neighbors.put(Direction.SOUTH, Terrain.LIGHT);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.GRASS);

        assertEquals(Direction.SOUTH, truck.chooseDirection(neighbors));
    }

    @Test
    void testChooseDirection_PicksCrosswalkAsFallback() {
        neighbors.put(Direction.SOUTH, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.CROSSWALK);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.NORTH, Terrain.GRASS);

        assertEquals(Direction.WEST, truck.chooseDirection(neighbors));
    }

}
