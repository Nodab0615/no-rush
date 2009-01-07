package com.google.code.norush;

import com.google.code.norush.ai.Move;

/**
 * A single Rush Hour move, that can be applied to a specific board.
 * 
 * @author DL, GT
 */
public class RushHourMove implements Move {
    /**
     * Constructor for a winning move (i.e. a move that simply removes the
     * specified vehicle from the board). Should only be used on the Red Car.
     * 
     * @param vehicleIndex   The vehicle index to remove from the board.
     * @param moveStr        The move's encoded string representation.
     */
    public RushHourMove(int vehicleIndex, String moveStr) {
        this.vehicleIndex = vehicleIndex;
        this.isGoalMove = true;
        this.newPosition = -1;
        this.moveStr = moveStr;
    }
    
    /**
     * Constructor for a regular non-winning move.
     * @param vehicleIndex  The vehicle index to move.
     * @param newPosition   The new vehicle's position
     * @param moveStr       The move's encoded string representation.
     */
    public RushHourMove(int vehicleIndex, int newPosition, String moveStr) {
        this.vehicleIndex = vehicleIndex;
        this.isGoalMove = false;
        this.newPosition = newPosition;
        this.moveStr = moveStr;
    }
    
    /** @return the board-specific vehicle's index this move applies to. */
    public int getVehicleIndex() {
        return vehicleIndex;
    }
    
    /** @return whether or not this move is a winning move. */
    public boolean isGoalMove() {
        return isGoalMove;
    }
    
    /**
     * @return the new vehicle's position (i.e. after the move). Only valid for
     * non-goal moves. For goal moves, returns -1.
     */
    public int getNewPosition() {
        return newPosition;
    }
    
    /** @see Object#toString() */
    @Override
    public String toString() { 
        return moveStr;
    }
 
    private final int vehicleIndex;
    private final boolean isGoalMove;
    private final int newPosition;
    private final String moveStr;
}