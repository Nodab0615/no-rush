package com.google.code.norush;

import com.google.code.norush.ai.Move;
import com.google.code.norush.ai.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a Rush Hour board: car types, positions and names.
 * 
 * @author DL, GT
 */
public class RushHourBoard implements State {

    /**
     * Constructs the board from standard string representation.
     * 
     * For example, the string "AA...OP..Q.OPXXQ.OP..Q..B...CCB.RRR." encodes
     * a board with 1 red car (X), 2 horizontal cars (A,C), 1 vertical car (B),
     * 1 horizontal truck (R) and 3 vertical trucks (O,P,Q):
     * 
     *      AA...O
     *      P..Q.O
     *      PXXQ.O
     *      P..Q..
     *      B...CC
     *      B.RRR.
     * 
     * @param boardStr the encoded board string.
     */
    public RushHourBoard(String boardStr) {
        assert(boardStr.length() == RushHourConsts.NumPositions);
        
        boolean isGoalTmp = true;
        int numVehicles = 0;
        Set<Character> foundNames = new HashSet<Character>();
        for (int i = 0; i < boardStr.length(); ++i) {
            char c = boardStr.charAt(i);
            if ((c == EmptyChar) || (foundNames.contains(c))) {
                continue;
            }
            foundNames.add(c);
            ++numVehicles;
        }

        vehicles = new Vehicle[numVehicles];
        
        int vehicleIndex = 0;
        foundNames.clear();
        for (int i = 0; i < boardStr.length(); ++i) {
            char c = boardStr.charAt(i);
            
            if ((c == EmptyChar) || (foundNames.contains(c))) {
                continue;
            }
            foundNames.add(c);

            int position = i;
            char name = c;
            VehicleType type;
            if (c == RedCarChar) {
                type = VehicleType.RedCar;
                isGoalTmp = false;
            } else if (boardStr.charAt(i+1) == c) {
                if ((i + 2 < RushHourConsts.NumPositions) &&
                    (boardStr.charAt(i+2) == c)) {
                    type = VehicleType.HorizontalTruck;
                } else {
                    type = VehicleType.HorizontalCar;
                }
            } else if ((i + 2*RushHourConsts.BoardSize < RushHourConsts.NumPositions)
                    && (boardStr.charAt(i + 2*RushHourConsts.BoardSize) == c)) {
                type = VehicleType.VerticalTruck;
            } else {
                type = VehicleType.VerticalCar;
            }
            
            vehicles[vehicleIndex] = new Vehicle(type, position, name);
            ++vehicleIndex;
        }
        
        this.isGoal = isGoalTmp;
    }
    
    /**
     * Constructs the board from explicit data, following a given move.
     * 
     * @pararm vehicles   Vehicles' formation to use. ust be ordered by
     *                    position, in ascending order.
     * @pararm isGoal     Whether or not this is a final board (no Red Car).
     */
    public RushHourBoard(Vehicle[] vehicles, boolean isGoal) {
        this.vehicles = vehicles;
        this.isGoal = isGoal;
    }
    
    /** @see State#getMoves() */
    public List<Move> getMoves() {
        List<Move> moves = new ArrayList<Move>();
        if (!isGoal) {
            // We could have generated the occupancy matrix in the constructor
            // and make it a member (perhaps even publicly exposing it, to allow
            // heuristics to use it). However, this seems to cause about 20%
            // speed degradation, so we only do it here, when actually needed).
            OccupancyMatrix occupancy = new OccupancyMatrix(vehicles);
            for (int vehicleIndex = 0; vehicleIndex < vehicles.length; ++vehicleIndex) {
                addMovesForVehicle(vehicleIndex, moves, occupancy);
            }
        }
                
        return moves;
    }

    /** @see State#applyMove() */
    public State applyMove(Move move) {
        RushHourMove rhMove = (RushHourMove) move;
        int vehicleIndex = rhMove.getVehicleIndex();
        
        // Handle winning move
        if (rhMove.isGoalMove()) {
            int numVehicles = vehicles.length - 1;
            
            Vehicle[] newVehicles = new Vehicle[numVehicles];
            System.arraycopy(vehicles, 0, newVehicles, 0, vehicleIndex);
            System.arraycopy(vehicles, vehicleIndex+1,
                newVehicles, vehicleIndex, numVehicles-vehicleIndex);
            
            return new RushHourBoard(newVehicles, true);
        }
        
        VehicleType vehicleType = vehicles[vehicleIndex].getType();
        
        // Handle non-winning horizontal move
        if ((vehicleType == VehicleType.HorizontalCar) ||
            (vehicleType == VehicleType.HorizontalTruck) ||
            (vehicleType == VehicleType.RedCar)) {
            
            // Since car ordering is guaranteed to remain the same, we can reuse
            // some of this object's internal arrays (everything is immutable
            // anyway)
            Vehicle[] newVehicles = vehicles.clone();
            newVehicles[vehicleIndex] = new Vehicle(
                vehicleType, rhMove.getNewPosition(), vehicles[vehicleIndex].getName());
            return new RushHourBoard(newVehicles, false);
        } else {
            // Handle vertical move
            
            // Find the moved car's new index (in the new positions array).
            // The positions are sorted, and we know for sure that the new
            // position is not in the current positions array - so we can binary
            // search it.
            int numVehicles = vehicles.length;
            int oldPos = vehicles[vehicleIndex].getPosition();
            int newPos = rhMove.getNewPosition();
            Vehicle newVehicle = new Vehicle(
                vehicleType, newPos, vehicles[vehicleIndex].getName());
            int newVehicleIndex =
                -(Arrays.binarySearch(vehicles, newVehicle) + 1);
            
            // Moving down?
            if (newPos > oldPos) {
                Vehicle[] newVehicles = new Vehicle[numVehicles];
                System.arraycopy(vehicles, 0, newVehicles, 0, vehicleIndex);
                System.arraycopy(vehicles, vehicleIndex+1,
                    newVehicles, vehicleIndex, newVehicleIndex - vehicleIndex - 1);
                newVehicles[newVehicleIndex-1] = newVehicle; // remember the moved car itself!
                System.arraycopy(vehicles, newVehicleIndex,
                    newVehicles, newVehicleIndex, numVehicles - newVehicleIndex);

                return new RushHourBoard(newVehicles, false);
            } else {
                // Moving up
                Vehicle[] newVehicles = new Vehicle[numVehicles];
                System.arraycopy(vehicles, 0, newVehicles, 0, newVehicleIndex);
                newVehicles[newVehicleIndex] = newVehicle;
                System.arraycopy(vehicles, newVehicleIndex,
                    newVehicles, newVehicleIndex+1, vehicleIndex - newVehicleIndex);
                System.arraycopy(vehicles, vehicleIndex+1,
                    newVehicles, vehicleIndex+1, numVehicles - vehicleIndex - 1);

                return new RushHourBoard(newVehicles, false);
            }
        }
    }

    /** @see State#isGoal() */
    public boolean isGoal() {
        return isGoal;
    }
    
    /**
     * @return The vehicles' formation for this board, ordered by position in
     *         ascending order.
     */
    public Vehicle[] getVehicles() {
        return vehicles;
    }
    
    /**
     * Adds to a given list all moves that can be applies to this board, that
     * affect a given vehicle.
     * 
     * @param vehicleIndex   Vehicle index to move.
     * @param moves          List to add moves to.
     * @param occupancy      The OccupancyMatrix for this board.
     */
    private void addMovesForVehicle(
        int vehicleIndex, List<Move> moves, OccupancyMatrix occupancy) {
        
        VehicleType type = vehicles[vehicleIndex].getType();
        int pos = vehicles[vehicleIndex].getPosition();
        StringBuffer moveStr = new StringBuffer(3);
        moveStr.setLength(3);
        moveStr.setCharAt(0, vehicles[vehicleIndex].getName());
        
        // Horizontal vehicle?
        if ((type == VehicleType.RedCar) ||
            (type == VehicleType.HorizontalCar) ||
            (type == VehicleType.HorizontalTruck)) {

            // Handle moves to the right
            int size = ((type == VehicleType.HorizontalTruck) ? 3 : 2);
            int numFreeRight = occupancy.getFreeCellsRight(pos+size-1);
            moveStr.setCharAt(1, 'R');
            
            //  1. Handle possible winning move
            if (type == VehicleType.RedCar) {
                int col = pos % RushHourConsts.BoardSize;
                if (numFreeRight == RushHourConsts.BoardSize - col - 2) {
                    moveStr.setCharAt(2, Character.forDigit(
                        RushHourConsts.BoardSize - col, 10));
                    
                    moves.add(new RushHourMove(vehicleIndex, moveStr.toString()));
                }
            }
            
            //  2. Handle regular moves
            for (int i = 1; i <= numFreeRight; ++i) {
                moveStr.setCharAt(2, Character.forDigit(i, 10));
                moves.add(
                    new RushHourMove(vehicleIndex, pos + i, moveStr.toString()));
            }
            
            // Handle moves to the left
            int numFreeLeft = occupancy.getFreeCellsLeft(pos);
            moveStr.setCharAt(1, 'L');
            for (int i = 1; i <= numFreeLeft; ++i) {
                moveStr.setCharAt(2, Character.forDigit(i, 10));
                moves.add(
                    new RushHourMove(vehicleIndex, pos - i, moveStr.toString()));
            }
        } else { // Vertical vehicles
            // Handle down moves
            int size = (type == VehicleType.VerticalTruck) ? 3 : 2;
            int numFreeDown = occupancy.getFreeCellsDown(
                pos + (size-1) * RushHourConsts.BoardSize);
            moveStr.setCharAt(1, 'D');
            for (int i = 1; i <= numFreeDown; ++i) {
                moveStr.setCharAt(2, Character.forDigit(i, 10));
                moves.add(new RushHourMove(
                    vehicleIndex,
                    pos + i * RushHourConsts.BoardSize,
                    moveStr.toString()));
            }
            
            // Handle up moves
            int numFreeUp = occupancy.getFreeCellsUp(pos);
            moveStr.setCharAt(1, 'U');
            for (int i = 1; i <= numFreeUp; ++i) {
                moveStr.setCharAt(2, Character.forDigit(i, 10));
                moves.add(new RushHourMove(
                    vehicleIndex,
                    pos - i * RushHourConsts.BoardSize,
                    moveStr.toString()));
            }
        }
    }
    
    /** @see Object#toString() */
    @Override
    public String toString() { 
        StringBuffer str = new StringBuffer(RushHourConsts.NumPositions);
        str.setLength(RushHourConsts.NumPositions);
        for (int i = 0; i < str.length(); ++i) {
            str.setCharAt(i, EmptyChar);
        }
        for (int vehicleIndex = 0; vehicleIndex < vehicles.length; ++vehicleIndex) {
            VehicleType type = vehicles[vehicleIndex].getType();
            char name = vehicles[vehicleIndex].getName();
            int pos = vehicles[vehicleIndex].getPosition();
            if ((type == VehicleType.RedCar) || (type == VehicleType.HorizontalCar)) {
                str.setCharAt(pos, name);
                str.setCharAt(pos+1, name);
            } else if (type == VehicleType.HorizontalTruck ) {
                str.setCharAt(pos, name);
                str.setCharAt(pos+1, name);
                str.setCharAt(pos+2, name);
            } else if (type == VehicleType.VerticalCar) {
                str.setCharAt(pos, name);
                str.setCharAt(pos+RushHourConsts.BoardSize, name);
            } else {
                str.setCharAt(pos, name);
                str.setCharAt(pos+RushHourConsts.BoardSize, name);
                str.setCharAt(pos+2*RushHourConsts.BoardSize, name);
            }
        }
        return str.toString();
    }

    /** @see Object#equals() */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof RushHourBoard)) {
            return false;
        }
        RushHourBoard otherBoard = (RushHourBoard)other;
        
        return Arrays.equals(vehicles, otherBoard.vehicles);
    }
    
    /** @see Object#hashCode() */
    @Override
    public int hashCode() {
        return Arrays.hashCode(vehicles);
    }
    
    /** Char encoding for an empty cell (@see constructor from String). */
    static final char EmptyChar = '.';
    
    /** Char encoding for the red car (@see constructor from String). */
    static final char RedCarChar = 'X';

    /**
     * The vehicles' formation for this board, ordered by position in ascending
     * order.
     */
    private final Vehicle[] vehicles;
    
    /** Whether or not this is a winning board (no Red Car). */
    private final boolean isGoal;
}
