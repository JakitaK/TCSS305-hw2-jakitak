package edu.uw.tcss.app;

import static org.junit.jupiter.api.Assertions.*;

import edu.uw.tcss.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AtvTest {

    private Atv atv;
    private Map<Direction, Terrain> neighbors;

    @BeforeEach
    public void setUp() {
        atv = new Atv(0, 0, Direction.NORTH);
        neighbors = new HashMap<>();
    }


    @Test
    public void testChooseDirectionRandomMovement() {
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.STREET);

        Direction chosenDirection = atv.chooseDirection(neighbors);
        assertTrue(chosenDirection == Direction.NORTH ||
                chosenDirection == Direction.SOUTH ||
                chosenDirection == Direction.EAST ||
                chosenDirection == Direction.WEST);
    }

    @Test
    public void testCanPassAnyTerrainExceptWall() {
        assertTrue(atv.canPass(Terrain.STREET, Light.GREEN));
        assertTrue(atv.canPass(Terrain.GRASS, Light.RED));
        assertFalse(atv.canPass(Terrain.WALL, Light.RED));
    }

    @Test
    public void testIgnoresAllLights() {
        assertTrue(atv.canPass(Terrain.CROSSWALK, Light.RED));
        assertTrue(atv.canPass(Terrain.CROSSWALK, Light.YELLOW));
        assertTrue(atv.canPass(Terrain.CROSSWALK, Light.GREEN));
    }

    @Test
    public void testCollisionWithTruckCausesDeath() {
        Vehicle truck = new Truck(0, 0, Direction.NORTH);
        atv.collide(truck);
        assertFalse(atv.isAlive());
        assertEquals(25, atv.getDeathTime());
    }

    @Test
    public void testRevivalAfterDeathCycles() {
        atv.collide(new Truck(0, 0, Direction.NORTH));
        for (int i = 0; i < 25; i++) {
            atv.poke();
        }
        assertTrue(atv.isAlive());
    }

    @Test
    public void testChooseDirectionAvoidsReverse() {
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.STREET);

        for (int i = 0; i < 10; i++) {
            Direction chosen = atv.chooseDirection(neighbors);
            assertNotEquals(Direction.SOUTH, chosen, "ATV chose reverse direction SOUTH");
        }
    }

    @Test
    public void testChooseDirectionAvoidsWalls() {
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.STREET);

        for (int i = 0; i < 10; i++) {
            Direction chosen = atv.chooseDirection(neighbors);
            assertNotEquals(Direction.SOUTH, chosen, "ATV chose direction SOUTH with wall");
        }
    }

    @Test
    public void testChooseDirectionSelectsValidDirectionWhenNoReverseOrWall() {
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.STREET);

        Direction chosen = atv.chooseDirection(neighbors);
        assertTrue(chosen == Direction.NORTH || chosen == Direction.EAST || chosen == Direction.WEST,
                "ATV did not choose a valid direction");
    }

    @Test
    public void testGetImageFileNameWhenAlive() {
        assertEquals("atv.gif", atv.getImageFileName());
    }

    @Test
    public void testGetImageFileNameWhenDead() {
        atv.collide(new Truck(0, 0, Direction.NORTH));
        assertEquals("atv_dead.gif", atv.getImageFileName());
    }
    @Test
    public void testChooseDirectionStartsWithWall() {
        neighbors.put(Direction.NORTH, Terrain.WALL);
        neighbors.put(Direction.SOUTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.WALL);

        atv.setDirection(Direction.EAST);

        Direction chosenDirection = atv.chooseDirection(neighbors);

        assertEquals(Direction.EAST, chosenDirection, "ATV should eventually choose a non-wall direction");
    }
    @Test
    public void testChooseDirectionAvoidsReverseAndWalls() {
        Map<Direction, Terrain> neighbors = new EnumMap<>(Direction.class);
        neighbors.put(Direction.NORTH, Terrain.STREET);
        neighbors.put(Direction.SOUTH, Terrain.WALL);
        neighbors.put(Direction.EAST, Terrain.STREET);
        neighbors.put(Direction.WEST, Terrain.STREET);

        Direction chosenDirection = atv.chooseDirection(neighbors);

        assertNotEquals(Direction.SOUTH, chosenDirection);
        assertNotEquals(Terrain.WALL, neighbors.get(chosenDirection));
    }


}