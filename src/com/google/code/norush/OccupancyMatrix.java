package com.google.code.norush;

/**
 * Helper class to keep track of a Rush Hour board's occupied cells.
 * 
 * @author DL, GT
 */
public class OccupancyMatrix {
    
    /** Initializes the board to be all empty. */
    public OccupancyMatrix() {
        // default initialization to false
        occupancy = new boolean[RushHourConsts.NumPositions];
    }
    
    /** Initializes the board according to a given car formation. */
    public OccupancyMatrix(Vehicle[] vehicles) {
        // default initialization to false
        occupancy = new boolean[RushHourConsts.NumPositions];
        
        for (int i = 0; i < vehicles.length; ++i) {
            int position = vehicles[i].getPosition();
            switch (vehicles[i].getType()) {
                case RedCar:
                case HorizontalCar:
                    setHorizontal2(position, true);
                    break;
                case HorizontalTruck:
                    setHorizontal3(position, true);
                    break;
                case VerticalCar:
                    setVertical2(position, true);
                    break;
                case VerticalTruck:
                    setVertical3(position, true);
                    break;
                default:
                    throw new RuntimeException("Invalid car type.");
            }
        }
    }
    
    /** @return whether a given cell is occupied. */
    public boolean isOccupied(int index) {
        return occupancy[index];
    }
    
    /** @return whether a given cell can accommodate a horizontal car. */
    public boolean isHorizontal2Occupied(int index) {
        return (occupancy[index] || occupancy[index+1]);
    }
    
    /** @return whether a given cell can accommodate a horizontal truck. */
    public boolean isHorizontal3Occupied(int index) {
        return (occupancy[index] || occupancy[index+1] || occupancy[index+2]);
    }
    
    /** @return whether a given cell can accommodate a vertical car. */
    public boolean isVertical2Occupied(int index) {
        return (occupancy[index] || occupancy[index+RushHourConsts.BoardSize]);
    }
    
    /** @return whether a given cell can accommodate a horizontal truck. */
    public boolean isVertical3Occupied(int index) {
        return (occupancy[index] || occupancy[index+RushHourConsts.BoardSize] || occupancy[index+2*RushHourConsts.BoardSize]);
    }
    
    /** @return the number of free cells to the right. */
    public int getFreeCellsRight(int index) {
        int col = index % RushHourConsts.BoardSize;
        if (col == RushHourConsts.BoardSize - 1) {
            return 0;
        }
        
        int numFree = 0;
        for (int i = 1; i <= RushHourConsts.BoardSize - col - 1; ++i) {
            if (occupancy[index + i]) {
                break;
            }
            ++numFree;
        }
        return numFree;
    }
  
    /** @return the number of free cells to the left. */
    public int getFreeCellsLeft(int index) {
        int col = index % RushHourConsts.BoardSize;
        if (col == 0) {
            return 0;
        }
        
        int numFree = 0;
        for (int i = 1; i <= col; ++i) {
            if (occupancy[index - i]) {
                break;
            }
            ++numFree;
        }
        return numFree;
    }

    /** @return the number of free cells to the top. */
    public int getFreeCellsUp(int index) {
        int row = index / RushHourConsts.BoardSize;
        if (row == 0) {
            return 0;
        }
        
        int numFree = 0;
        for (int i = 1; i <= row; ++i) {
            if (occupancy[index - i * RushHourConsts.BoardSize]) {
                break;
            }
            ++numFree;
        }
        return numFree;
    }

    /** @return the number of free cells to the bottom. */
    public int getFreeCellsDown(int index) {
        int row = index / RushHourConsts.BoardSize;
        if (row == RushHourConsts.BoardSize - 1) {
            return 0;
        }
        
        int numFree = 0;
        for (int i = 1; i <= RushHourConsts.BoardSize - row - 1; ++i) {
            if (occupancy[index + i * RushHourConsts.BoardSize]) {
                break;
            }
            ++numFree;
        }
        return numFree;
    }
    
    /** Parks (or unparks) a horizontal car in a given cell. */
    public void setHorizontal2(int index, boolean occupied) {
        occupancy[index] = occupancy[index+1] = occupied;
    }
    
    /** Parks (or unparks) a horizontal truck in a given cell. */
    public void setHorizontal3(int index, boolean occupied) {
        occupancy[index] = occupancy[index+1] = occupancy[index+2] = occupied;
    }
    
    /** Parks (or unparks) a vertical car in a given cell. */
    public void setVertical2(int index, boolean occupied) {
        occupancy[index] = occupancy[index+RushHourConsts.BoardSize] = occupied;
    }
    
    /** Parks (or unparks) a vertical truck in a given cell. */
    public void setVertical3(int index, boolean occupied) {
        occupancy[index] = occupancy[index+RushHourConsts.BoardSize] = occupancy[index+2*RushHourConsts.BoardSize] = occupied;
    }
    
    /** Represents the board (true=occupied, false=free). */
    private final boolean[] occupancy;
}
