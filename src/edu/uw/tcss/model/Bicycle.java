package edu.uw.tcss.model;

import java.util.Map;

/**
 * Represents a Bicycle vehicle in the city traffic simulation.
 * <br />
 * The Bicycle prioritizes trails for movement but can also travel on streets,
 * lights, and crosswalks, stopping at red and yellow crosswalk lights and only
 * proceeding when the light is green.
 * <br />
 * This class defines specific movement and collision behavior for the Bicycle,
 * implementing directional preferences for trail and other passable terrains.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Bicycle extends AbstractVehicle {

    /** The death time for a Bicycle after a collision, measured in simulation cycles. */
    private static final int BICYCLE_DEATH_TIME = 35;

    /**
     * Constructs a Bicycle object with a specified initial position and direction.
     * <br />
     * The Bicycle is initialized at the given coordinates, facing the specified
     * initial direction.
     *
     * @param theX the initial x-coordinate of the Bicycle
     * @param theY the initial y-coordinate of the Bicycle
     * @param theDir the initial direction the Bicycle is facing
     */
    public Bicycle(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, BICYCLE_DEATH_TIME);
    }

    /**
     * Determines if the Bicycle can pass through a given terrain and light condition.
     * <br />
     * Bicycles can navigate trails and streets freely. At crosswalks and lights,
     * they will proceed only when the light is green and stop if the light is red or yellow.
     *
     * @param theTerrain the type of terrain the Bicycle is attempting to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the Bicycle can pass the specified terrain and light, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return switch (theTerrain) {
            case STREET, TRAIL -> true;
            case LIGHT, CROSSWALK -> theLight == Light.GREEN;
            default -> false;
        };
    }

    /**
     * Chooses the direction the Bicycle should move, prioritizing trails if available.
     * <br />
     * If no trail is available, the Bicycle will select street, light, or crosswalk terrains
     * in the following order of preference: straight, left, then right. If no valid
     * options are
     * found, the Bicycle will reverse direction.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        final Direction chosenDirection;

        if (theNeighbors.get(getDirection()) == Terrain.TRAIL) {
            chosenDirection = getDirection();
        } else {
            final Direction left = getDirection().left();
            final Direction right = getDirection().right();

            if (theNeighbors.get(left) == Terrain.TRAIL) {
                chosenDirection = left;
            } else if (theNeighbors.get(right) == Terrain.TRAIL) {
                chosenDirection = right;
            } else if (canMoveTo(theNeighbors, getDirection())) {
                chosenDirection = getDirection();
            } else if (canMoveTo(theNeighbors, left)) {
                chosenDirection = left;
            } else if (canMoveTo(theNeighbors, right)) {
                chosenDirection = right;
            } else {
                chosenDirection = getDirection().reverse();
            }
        }

        return chosenDirection;
    }

    /**
     * Helper method to determine if the Bicycle can move in a specified direction based on
     * terrain type.
     * <br />
     * Bicycles are restricted to moving to terrains that are streets, lights, or crosswalks.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @param theDirection the direction to check for movement
     * @return true if the Bicycle can move in the specified direction, false otherwise
     */
    private boolean canMoveTo(final Map<Direction, Terrain> theNeighbors,
                              final Direction theDirection) {
        final Terrain terrain = theNeighbors.get(theDirection);
        return terrain == Terrain.STREET || terrain == Terrain.LIGHT
                || terrain == Terrain.CROSSWALK;
    }
}
