package edu.uw.tcss.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Human vehicle in the city traffic simulation.
 * <br />
 * Humans can move across grass and crosswalks but have specific behavior at green crosswalk
 * lights,
 * where they wait before proceeding. They move randomly when possible, with a preference for
 * crosswalks if adjacent.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Human extends AbstractVehicle {

    /** The death time for a Human after a collision, measured in simulation cycles. */
    private static final int HUMAN_DEATH_TIME = 45;

    /** Random instance for shuffling direction choices. */
    private static final Random RANDOM = new Random();

    /**
     * Constructs a Human object with a specified initial position and direction.
     * <br />
     * The Human is initialized at the given coordinates, facing the specified initial
     * direction.
     *
     * @param theX the initial x-coordinate of the Human
     * @param theY the initial y-coordinate of the Human
     * @param theDir the initial direction the Human is facing
     */
    public Human(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, HUMAN_DEATH_TIME);
    }

    /**
     * Determines if the Human can pass through a given terrain and light condition.
     * <br />
     * Humans can navigate grass terrains freely. At crosswalks, they will stop if the light
     * is green,
     * and proceed otherwise.
     *
     * @param theTerrain the type of terrain the Human is attempting to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the Human can pass the specified terrain and light, false otherwise
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return switch (theTerrain) {
            case GRASS -> true;
            case CROSSWALK -> theLight != Light.GREEN;
            default -> false;
        };
    }

    /**
     * Chooses the direction the Human should move, prioritizing crosswalks if available,
     * then randomly selecting a direction on grass or crosswalk if no crosswalk is adjacent.
     * If no other valid movement options are available, the Human reverses direction.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction chosenDirection = getDirection().reverse();

        for (final Direction direction : Arrays.asList(getDirection(), getDirection().left(),
                getDirection().right())) {
            if (theNeighbors.get(direction) == Terrain.CROSSWALK) {
                chosenDirection = direction;
                break;
            }
        }

        if (chosenDirection == getDirection().reverse()) {
            final List<Direction> directions = Arrays.asList(getDirection(),
                    getDirection().left(), getDirection().right());
            Collections.shuffle(directions, RANDOM);

            for (final Direction direction : directions) {
                final Terrain terrain = theNeighbors.get(direction);
                if (terrain == Terrain.GRASS || terrain == Terrain.CROSSWALK) {
                    chosenDirection = direction;
                    break;
                }
            }
        }

        return chosenDirection;
    }
}
