package edu.uw.tcss.model;
import java.util.Map;

/**
 * Represents a Taxi vehicle in the city traffic simulation.
 * <br />
 * Taxis can navigate streets, lights, and crosswalks, with a specific behavior
 * for handling red crosswalk lights. They prioritize moving straight, left,
 * or right, only reversing direction if no other valid option is available.
 * <br />
 * Taxis will wait at red crosswalks for a set number of simulation cycles before proceeding.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Taxi extends AbstractVehicle {

    /** The death time for a Taxi after a collision, measured in simulation cycles. */
    private static final int TAXI_DEATH_TIME = 15;

    /** The number of ticks a Taxi waits at a red crosswalk before proceeding. */
    private static final int WAIT_TICKS_AT_RED = 3;

    /** Counter for the number of ticks a Taxi waits at a red crosswalk. */
    private int myWaitCounter;

    /**
     * Constructs a Taxi object with a specified initial position and direction.
     * <br />
     * The Taxi is initialized at the given coordinates, facing the specified
     * initial direction.
     *
     * @param theX the initial x-coordinate of the Taxi
     * @param theY the initial y-coordinate of the Taxi
     * @param theDir the initial direction the Taxi is facing
     */
    public Taxi(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, TAXI_DEATH_TIME);
    }

    /**
     * Determines if the Taxi can pass through a given terrain and light condition.
     * <br />
     * Taxis can navigate streets and lights, stopping at red lights. They also
     * navigate crosswalks but will wait at red crosswalk lights for a specified number
     * of simulation cycles before proceeding.
     *
     * @param theTerrain the type of terrain the Taxi is attempting to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the Taxi can pass the specified terrain and light, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean allowedToPass = switch (theTerrain) {
            case STREET, LIGHT -> true;
            case CROSSWALK -> theLight == Light.GREEN || theLight == Light.YELLOW;
            default -> false;
        };

        if (theTerrain == Terrain.LIGHT && theLight == Light.RED) {
            allowedToPass = false;
        }

        if (theTerrain == Terrain.CROSSWALK && theLight == Light.RED) {
            if (myWaitCounter >= WAIT_TICKS_AT_RED) {
                allowedToPass = true;
                myWaitCounter = 0;
            } else {
                myWaitCounter++;
            }
        }

        return allowedToPass;
    }

    /**
     * Chooses the direction the Taxi should move, prioritizing straight if possible,
     * then left, then right. If no valid terrain is found in those directions,
     * the Taxi will reverse direction.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction chosenDirection = getDirection().reverse();

        final Direction preferredDirection = getDirection();
        final Direction left = getDirection().left();
        final Direction right = getDirection().right();

        if (canMoveTo(theNeighbors, preferredDirection)) {
            chosenDirection = preferredDirection;
        } else if (canMoveTo(theNeighbors, left)) {
            chosenDirection = left;
        } else if (canMoveTo(theNeighbors, right)) {
            chosenDirection = right;
        }

        return chosenDirection;
    }

    /**
     * Helper method to determine if the Taxi can move in a specified direction based on
     * terrain type.
     * <br />
     * Taxis are restricted to moving to terrains that are streets, lights, or crosswalks.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @param theDirection the direction to check for movement
     * @return true if the Taxi can move in the specified direction, false otherwise
     */
    private boolean canMoveTo(final Map<Direction, Terrain> theNeighbors,
                              final Direction theDirection) {
        final Terrain terrain = theNeighbors.get(theDirection);
        return terrain == Terrain.STREET || terrain == Terrain.LIGHT
                || terrain == Terrain.CROSSWALK;
    }
}
