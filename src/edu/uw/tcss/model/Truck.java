package edu.uw.tcss.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Truck vehicle in the city traffic simulation.
 * <br />
 * Trucks can navigate streets, lights, and crosswalks but stop at red crosswalk lights.
 * They choose their direction based on available passable terrain, prioritizing forward,
 * left, or right movement before considering turning around.
 * <br />
 * Trucks do not have a "death time," meaning they are always immediately available
 * after a collision.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Truck extends AbstractVehicle {

    /** The death time for a Truck after a collision, measured in simulation cycles. */
    private static final int TRUCK_DEATH_TIME = 0;

    /** Random instance for shuffling direction choices. */
    private static final Random RANDOM = new Random();

    /**
     * Constructs a Truck object with a specified initial position and direction.
     * <br />
     * The Truck is initialized at the given coordinates, facing the specified
     * initial direction.
     *
     * @param theX the initial x-coordinate of the Truck
     * @param theY the initial y-coordinate of the Truck
     * @param theDir the initial direction the Truck is facing
     */
    public Truck(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, TRUCK_DEATH_TIME);
    }

    /**
     * Determines if the Truck can pass through a given terrain and light condition.
     * <br />
     * Trucks are able to drive on streets and lights.
     * For crosswalks, the Truck will stop if the light is red, and otherwise it can proceed.
     *
     * @param theTerrain the type of terrain the Truck is attempting to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the Truck can pass the specified terrain and light, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return switch (theTerrain) {
            case STREET, LIGHT -> true;
            case CROSSWALK -> theLight != Light.RED;
            default -> false;
        };
    }

    /**
     * Chooses a direction for the Truck to move based on neighboring terrains.
     * <br />
     * The Truck prioritizes forward movement if possible and only considers turning
     * around if no other valid options exist. If the Truck is facing a red crosswalk
     * light, it will wait until it turns green.
     * <br />
     * Direction choices are shuffled randomly to simulate varying movement.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement, avoiding non-passable terrains
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        final List<Direction> directions = Arrays.asList(getDirection(),
                getDirection().left(), getDirection().right());
        Collections.shuffle(directions, RANDOM);

        Direction chosenDirection = getDirection().reverse();

        for (final Direction direction : directions) {
            final Terrain terrain = theNeighbors.get(direction);
            if (terrain == Terrain.STREET || terrain == Terrain.LIGHT
                    || terrain == Terrain.CROSSWALK) {
                chosenDirection = direction;
                break;
            }
        }

        return chosenDirection;
    }

    /**
     * Returns a string representation of the Truck, including its position and direction.
     * <br />
     * The string output provides a description of the Truck's current coordinates
     * and its facing direction.
     *
     * @return a string description of the Truck's current state
     */
    @Override
    public String toString() {
        return "Truck at (" + getX() + ", " + getY() + ") facing " + getDirection();
    }
}
