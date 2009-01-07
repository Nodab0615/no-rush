package com.google.code.norush.pattern;

import com.google.code.norush.VehicleType;
import com.google.code.norush.OccupancyMatrix;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates all legal board configurations containing a given number of cars
 * from each type, in a given order.
 * 
 * @author DL, GT
 */
public class RushHourPatternIterator implements Iterator<RushHourPattern> {

    public RushHourPatternIterator(VehicleType[] carTypes) {
        this.carTypes = carTypes;
        
        // Initialize car legal position pointers
        carLegalPositions = new int[carTypes.length][];
        carLegalPositionsIndices = new int[carTypes.length];
                
        boolean preRedCar = true;
        for (int i = 0; i < carTypes.length; ++i) {
            carLegalPositionsIndices[i] = -1;
            switch (carTypes[i]) {
                case RedCar:
                    carLegalPositions[i] = RedCarPositions;
                    preRedCar = false;
                    break;
                case HorizontalCar:
                    carLegalPositions[i] =
                        (preRedCar ? HorizontalCarPreRedPositions : HorizontalCarPostRedPositions);
                    break;
                case HorizontalTruck:
                    carLegalPositions[i] =
                        (preRedCar ? HorizontalTruckPreRedPositions : HorizontalTruckPostRedPositions);
                    break;
                case VerticalCar:
                    carLegalPositions[i] =
                        (preRedCar ? VerticalCarPreRedPositions : VerticalCarPostRedPositions);
                    break;
                case VerticalTruck:
                    carLegalPositions[i] =
                        (preRedCar ? VerticalTruckPreRedPositions : VerticalTruckPostRedPositions);
                    break;
                default:
                    throw new RuntimeException("Invalid car type");
            }
        }
        
        pattern = new RushHourPattern(carTypes, new int[carTypes.length]);
        
        reachedEnd = !advanceToNextLegalPattern(0);
    }

    private void clearCarPos(int carIndex, int pos) {
        switch (carTypes[carIndex]) {
            case RedCar:
            case HorizontalCar:
                occupancy.setHorizontal2(pos, false);
                break;
            case HorizontalTruck:
                occupancy.setHorizontal3(pos, false);
                break;
            case VerticalCar:
                occupancy.setVertical2(pos, false);
                break;
            case VerticalTruck:
                occupancy.setVertical3(pos, false);
                break;
            default:
                throw new RuntimeException("Invalid car type.");
        }
    }
        
    private boolean setCarPosIfFree(int carIndex, int pos) {
        switch (carTypes[carIndex]) {
            case RedCar:
            case HorizontalCar:
                if (!occupancy.isHorizontal2Occupied(pos)) {
                    occupancy.setHorizontal2(pos, true);
                    return true;
                }
                break;
            case HorizontalTruck:
                if (!occupancy.isHorizontal3Occupied(pos)) {
                    occupancy.setHorizontal3(pos, true);
                    return true;
                }
                break;
            case VerticalCar:
                if (!occupancy.isVertical2Occupied(pos)) {
                    occupancy.setVertical2(pos, true);
                    return true;
                }
                break;
            case VerticalTruck:
                if (!occupancy.isVertical3Occupied(pos)) {
                    occupancy.setVertical3(pos, true);
                    return true;
                }
                break;
            default:
                throw new RuntimeException("Invalid car type.");
        }
        return false;
    }
    
    private void advanceToNextLegalPattern() {
        for (int carIndex = carLegalPositionsIndices.length-1; carIndex >= 0; --carIndex) {
            if (advanceToNextLegalPattern(carIndex)) {
                return;
            }
        }
        reachedEnd = true;
    }
    
    private boolean advanceToNextLegalPattern(int carIndex) {
        while (true) {
            if (carLegalPositionsIndices[carIndex] != -1) {
                clearCarPos(carIndex,
                    carLegalPositions[carIndex][carLegalPositionsIndices[carIndex]]);
            }
            if (!advanceToNextLegalPosition(carIndex)) {
                return false;
            } else {
                if ((carIndex == carLegalPositionsIndices.length-1) ||
                    (advanceToNextLegalPattern(carIndex+1))) {
                    return true;
                }
            }
        }
    }
    
    private boolean advanceToNextLegalPosition(int carIndex) {
        int carPosIndex = carLegalPositionsIndices[carIndex];
        
        if (carPosIndex == -1) {
            if (carIndex == 0) {
                carPosIndex = 0;
            } else {
                int prevCarPos =
                    carLegalPositions[carIndex-1][carLegalPositionsIndices[carIndex-1]];
                
                carPosIndex = Arrays.binarySearch(
                    carLegalPositions[carIndex], prevCarPos+1);
                if (carPosIndex < 0) {
                    carPosIndex = -1 - carPosIndex;
                }
            }
        } else {
            ++carPosIndex;
        }
        
        while (true) {
            if (carPosIndex == carLegalPositions[carIndex].length) {
                
                carLegalPositionsIndices[carIndex] = -1;
                return false;
            }
            
            int pos = carLegalPositions[carIndex][carPosIndex];
            if (setCarPosIfFree(carIndex, pos)) {
                carLegalPositionsIndices[carIndex] = carPosIndex;
                return true;
            }
            
            ++carPosIndex;
        }
    }
        
    /** @see java.util.Iterator#hasNext() */
    public boolean hasNext() {
        return !reachedEnd;
    }
    
    /** @see java.util.Iterator#next() */
    public RushHourPattern next() {
        if (reachedEnd) {
            throw new NoSuchElementException();
        }

        for (int i = 0; i < carLegalPositionsIndices.length; ++i) {
            pattern.vehiclePositions[i] =
                carLegalPositions[i][carLegalPositionsIndices[i]];
        }
        advanceToNextLegalPattern();
        return pattern;
    }

    /** @see java.util.Iterator#remove() */
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    /** Holds the car types to iterate (in a given order). */
    private final VehicleType[] carTypes;
    
    /** Whether or not the iteration has ended. */
    private boolean reachedEnd;
    
    /** Reused by next(). */
    private final RushHourPattern pattern;
    
    /** Keeps track of occupied board positions. */
    private final OccupancyMatrix occupancy = new OccupancyMatrix();
    
    /**
     * For each car, points to the corresponding array of legal board
     * positions.
     */
    private final int[][] carLegalPositions;
    
    /**
     * For each car, hold an index to the corresponding array of legal board
     * positions (or -1 if undefined).
     */
    private final int[] carLegalPositionsIndices;
    
    /** Legal board positions for the red car (0-based). */
    private static final int[] RedCarPositions = new int[]
        { 12, 13, 14, 15, 16 }; // 5 total
    
    /**
     * Legal board positions for a horizontal car which is parked before the
     * red car (0-based).
     */
    private static final int[] HorizontalCarPreRedPositions = new int[]
        { 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 13, 14 }; // 13 total
    
    /**
     * Legal board positions for a horizontal car which is parked after the
     * red car (0-based).
     */
    private static final int[] HorizontalCarPostRedPositions = new int[]
        { 14, 15, 16, 18, 19, 20, 21, 22, 24, 25, 26, 27, 28,
          30, 31, 32, 33, 34 }; // 18 total
    
    /**
     * Legal board positions for a horizontal truck which is parked before the
     * red car (0-based).
     */
    private static final int[] HorizontalTruckPreRedPositions = new int[]
        { 0, 1, 2, 3, 6, 7, 8, 9, 12, 13 }; // 10 total
    
    /**
     * Legal board positions for a horizontal truck which is parked after the
     * red car (0-based).
     */
    private static final int[] HorizontalTruckPostRedPositions = new int[]
        { 14, 15, 18, 19, 20, 21, 24, 25, 26, 27, 30, 31, 32,
          33 }; // 14 total
    
    /**
     * Legal board positions for a vertical car which is parked before the
     * red car (0-based).
     */
    private static final int[] VerticalCarPreRedPositions = new int[]
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }; // 16 total
    
    /**
     * Legal board positions for a vertical car which is parked after the
     * red car (0-based).
     */
    private static final int[] VerticalCarPostRedPositions = new int[]
        { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
          29 }; // 16 total
    
    /**
     * Legal board positions for a vertical truck which is parked before the
     * red car (0-based).
     */
    private static final int[] VerticalTruckPreRedPositions = new int[]
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }; // 16 total
    
    /**
     * Legal board positions for a horizontal truck which is parked after the
     * red car (0-based).
     */
    private static final int[] VerticalTruckPostRedPositions = new int[]
        { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 }; // 9 total
}
