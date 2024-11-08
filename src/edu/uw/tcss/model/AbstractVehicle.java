package edu.uw.tcss.model;

import java.util.Map;
import java.util.Random;

/**
 * An abstract class representing a vehicle in the city traffic simulation.
 * <br />
 * The `AbstractVehicle` provides shared behaviors and properties such as position,
 * direction, death status, and image representation for each vehicle type.
 * Subclasses must implement the specific movement and collision behavior.
 *
 * @version Autumn 2024
 * @author Jakita Kaur
 */
public abstract class AbstractVehicle implements Vehicle {

    /** Random instance for generating random directions. */
    private static final Random RANDOM = new Random();

    /** Current x-coordinate of the vehicle. */
    private int myX;

    /** Current y-coordinate of the vehicle. */
    private int myY;

    /** Current direction the vehicle is facing. */
    private Direction myDirection;

    /** The status of the vehicle; true if alive, false if dead. */
    private boolean myAlive;

    /** The death time after which the vehicle revives, measured in simulation cycles. */
    private final int myDeathTime;

    /** Initial x-coordinate of the vehicle, used for resetting. */
    private final int myInitialX;

    /** Initial y-coordinate of the vehicle, used for resetting. */
    private final int myInitialY;

    /** Initial direction of the vehicle, used for resetting. */
    private final Direction myInitialDirection;

    /** Counter tracking cycles since the vehicle died. */
    private int myDeathCounter;

    /**
     * Constructs an `AbstractVehicle` object with a specified initial position,
     * direction, and death time.
     *
     * @param theX the initial x-coordinate of the vehicle
     * @param theY the initial y-coordinate of the vehicle
     * @param theDir the initial direction the vehicle is facing
     * @param theDeathTime the time in simulation cycles after which the vehicle revives
     */
    protected AbstractVehicle(final int theX, final int theY,
                              final Direction theDir, final int theDeathTime) {
        super();
        myX = theX;
        myY = theY;
        myInitialX = theX;
        myInitialY = theY;
        myDirection = theDir;
        myInitialDirection = theDir;
        myDeathTime = theDeathTime;
        myAlive = true;
        myDeathCounter = 0;
    }

    /**
     * Returns the x-coordinate of the vehicle.
     *
     * @return the current x-coordinate of the vehicle
     */
    @Override
    public int getX() {
        return myX;
    }

    /**
     * Returns the y-coordinate of the vehicle.
     *
     * @return the current y-coordinate of the vehicle
     */
    @Override
    public int getY() {
        return myY;
    }

    /**
     * Returns the current direction the vehicle is facing.
     *
     * @return the current direction
     */
    @Override
    public Direction getDirection() {
        return myDirection;
    }

    /**
     * Returns whether the vehicle is alive.
     *
     * @return true if the vehicle is alive; false otherwise
     */
    @Override
    public boolean isAlive() {
        return myAlive;
    }

    /**
     * Returns the death time of the vehicle in simulation cycles.
     *
     * @return the number of cycles the vehicle stays dead before reviving
     */
    @Override
    public int getDeathTime() {
        return myDeathTime;
    }

    /**
     * Advances the vehicle’s death counter and revives it if necessary.
     */
    @Override
    public void poke() {
        if (!myAlive) {
            myDeathCounter++;
            if (myDeathCounter >= myDeathTime) {
                revive();
            }
        }
    }

    /**
     * Revives the vehicle by resetting its alive status and choosing a random direction.
     */
    protected void revive() {
        myAlive = true;
        myDirection = Direction.random();
        myDeathCounter = 0;
    }

    /**
     * Handles a collision with another vehicle by setting the current vehicle
     * to dead if the other vehicle has a shorter death time.
     *
     * @param theOther the other vehicle in the collision
     */
    @Override
    public void collide(final Vehicle theOther) {
        if (myAlive && theOther.isAlive() && this.getDeathTime() > theOther.getDeathTime()) {
            myAlive = false;
        }
    }

    /**
     * Returns the file name of the image representing the vehicle.
     * <br />
     * The file name indicates whether the vehicle is alive or dead.
     *
     * @return the image file name for this vehicle
     */
    @Override
    public String getImageFileName() {
        final String baseName = getClass().getSimpleName().toLowerCase();
        final String suffix;
        if (myAlive) {
            suffix = ".gif";
        } else {
            suffix = "_dead.gif";
        }
        return baseName + suffix;
    }


    /**
     * Resets the vehicle to its initial state, including position, direction, and alive
     * status.
     */
    @Override
    public void reset() {
        myX = myInitialX;
        myY = myInitialY;
        myDirection = myInitialDirection;
        myAlive = true;
        myDeathCounter = 0;
    }

    /**
     * Sets the x-coordinate of the vehicle.
     *
     * @param theX the new x-coordinate
     */
    @Override
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets the y-coordinate of the vehicle.
     *
     * @param theY the new y-coordinate
     */
    @Override
    public void setY(final int theY) {
        myY = theY;
    }

    /**
     * Sets the direction of the vehicle.
     *
     * @param theDir the new direction for the vehicle
     */
    @Override
    public void setDirection(final Direction theDir) {
        myDirection = theDir;
    }

    /**
     * Returns a string representation of the vehicle, including its type,
     * position, direction, and status.
     *
     * @return a string describing the vehicle's state
     */
    @Override
    public String toString() {
        final String className = getClass().getSimpleName();
        final String position = " at (" + myX + "," + myY + ")";
        final String direction = ", facing " + myDirection;
        final String status;
        if (myAlive) {
            status = ", alive";
        } else {
            status = ", dead";
        }
        return className + position + direction + status;
    }
    /**
     * Determines if the vehicle can pass a specified terrain and light condition.
     * <br />
     * Each subclass must implement this method to define its specific behavior.
     *
     * @param theTerrain the type of terrain to pass
     * @param theLight the current light condition at the given terrain
     * @return true if the vehicle can pass, false otherwise
     */
    @Override
    public abstract boolean canPass(Terrain theTerrain, Light theLight);

    /**
     * Chooses a direction for the vehicle to move based on neighboring terrains.
     * <br />
     * Each subclass must implement this method to define its specific movement logic.
     *
     * @param theNeighbors a map of the neighboring terrains in each direction
     * @return the chosen direction for movement
     */
    @Override
    public abstract Direction chooseDirection(Map<Direction, Terrain> theNeighbors);
}
