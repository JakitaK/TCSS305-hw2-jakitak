package edu.uw.tcss.model;

import java.util.Map;

/**
 * Represents a Car vehicle in the city traffic simulation.
 * <br />
 * The Car has specific movement and collision behavior, allowing it to pass certain
 * terrains based on traffic light conditions. It prioritizes movement directions,
 * with a fallback to reverse if no valid terrain is found.
 * <br />
 * The Car can navigate streets and traffic lights, but will only cross a crosswalk
 * if the light is green. Its directional preference is straight, left, and then right.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Car extends AbstractVehicle {

    /** The death time for a Car after a collision, measured in simulation cycles. */
    private static final int CAR_DEATH_TIME = 15;

    /**
     * Constructs a Car object with a specified initial position and direction.
     * <br />
     * The Car is initialized at the given coordinates, facing the specified initial direction.
     *
     * @param theX the initial x-coordinate of the Car
     * @param theY the initial y-coordinate of the Car
     * @param theDir the initial direction the Car is facing
     */
    public Car(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, CAR_DEATH_TIME);
    }

    /**
     * Determines if the Car can pass through a given terrain and light condition.
     * <br />
     * Cars can navigate streets freely. At traffic lights, they will stop if the light is red.
     * At crosswalks, the Car will only proceed if the light is green.
     *
     * @param theTerrain the type of terrain the Car is attempting to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the Car can pass the specified terrain and light, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return switch (theTerrain) {
            case STREET -> true;
            case LIGHT -> theLight != Light.RED;
            case CROSSWALK -> theLight == Light.GREEN;
            default -> false;
        };
    }

    /**
     * Chooses the direction the Car should move, prioritizing straight if possible,
     * then left, then right. If no valid terrain is found in those directions,
     * the Car will reverse direction.
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

        if (theNeighbors.get(preferredDirection) == Terrain.STREET
                || theNeighbors.get(preferredDirection) == Terrain.LIGHT
                || theNeighbors.get(preferredDirection) == Terrain.CROSSWALK) {
            chosenDirection = preferredDirection;
        } else if (theNeighbors.get(left) == Terrain.STREET
                || theNeighbors.get(left) == Terrain.LIGHT
                || theNeighbors.get(left) == Terrain.CROSSWALK) {
            chosenDirection = left;
        } else if (theNeighbors.get(right) == Terrain.STREET
                || theNeighbors.get(right) == Terrain.LIGHT
                || theNeighbors.get(right) == Terrain.CROSSWALK) {
            chosenDirection = right;
        }

        return chosenDirection;
    }
}
