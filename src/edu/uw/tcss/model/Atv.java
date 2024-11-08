package edu.uw.tcss.model;

import java.util.Map;

/**
 * Represents an All-Terrain Vehicle (ATV) in the city traffic simulation.
 * <br />
 * The ATV has unique movement behavior, allowing it to travel on any terrain
 * except walls and ignore all traffic light colors. Each movement decision
 * is made randomly, with the ATV avoiding walls and its reverse direction.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public class Atv extends AbstractVehicle {

    /** The death time for an ATV after a collision, measured in simulation cycles. */
    private static final int ATV_DEATH_TIME = 25;

    /**
     * Constructs an ATV object with a specified initial position and direction.
     * <br />
     * The ATV is initialized at the given coordinates, facing the specified initial direction.
     *
     * @param theX the initial x-coordinate of the ATV
     * @param theY the initial y-coordinate of the ATV
     * @param theDir the initial direction the ATV is facing
     */
    public Atv(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, ATV_DEATH_TIME);
    }

    /**
     * Determines if the ATV can pass through a given terrain,
     * regardless of the light condition.
     * <br />
     * ATVs are allowed to navigate all terrains except walls, ignoring traffic light colors.
     *
     * @param theTerrain the type of terrain the ATV is attempting to pass
     * @param theLight the current light condition at the given terrain (ignored for ATVs)
     * @return true if the ATV can pass the specified terrain, false if it encounters a wall
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        return theTerrain != Terrain.WALL;
    }

    /**
     * Chooses a random direction for the ATV to move,
     * avoiding walls and the reverse direction.
     * <br />
     * The ATV selects a random direction that is neither its reverse direction
     * nor towards a wall, ensuring continued movement in open, accessible areas.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement, which is random but excludes walls
     * and reverse
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction atvDirection = Direction.random();

        while (atvDirection == getDirection().reverse()
                || theNeighbors.get(atvDirection) == Terrain.WALL) {
            atvDirection = Direction.random();
        }

        return atvDirection;
    }
}
