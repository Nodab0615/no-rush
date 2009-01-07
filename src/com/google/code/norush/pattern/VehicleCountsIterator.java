package com.google.code.norush.pattern;

import com.google.code.norush.RushHourConsts;
import com.google.code.norush.VehicleType;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates all unique combinations of vehicle type counts, given a total number
 * of vehicles.
 * 
 * @author DL, GT
 */
public class VehicleCountsIterator implements Iterator<int[]> {
    /**
     * Constructor.
     * 
     * @param vehicleTotalCount  Total number of cars and trucks, excluding
     *                           the Red Car.
     */
    public VehicleCountsIterator(int vehicleTotalCount) {
        assert(vehicleTotalCount >= 0);
        assert(vehicleTotalCount <= RushHourConsts.MaxCars + RushHourConsts.MaxTrucks);

        // initialize car type counts
        vehicleCounts = new int[VehicleType.NumVehicles];
        vehicleCounts[VehicleType.RedCar.ordinal()] = 1;
        
        int numCars = Math.min(RushHourConsts.MaxCars, vehicleTotalCount);
        vehicleCounts[VehicleType.HorizontalCar.ordinal()] = numCars;
        vehicleCounts[VehicleType.VerticalCar.ordinal()] = 0;
        vehicleCounts[VehicleType.HorizontalTruck.ordinal()] =
            vehicleTotalCount - numCars;
        vehicleCounts[VehicleType.VerticalTruck.ordinal()] = 0;

        reachedEnd = false;
    }

    /** @see java.util.Iterator#hasNext() */
    public boolean hasNext() {
        return !reachedEnd;
    }
    
    /** @see java.util.Iterator#next() */
    public int[] next() {
        if (reachedEnd) {
            throw new NoSuchElementException();
        }
        
        int[] copy = new int[vehicleCounts.length];
        System.arraycopy(vehicleCounts, 0, copy, 0, vehicleCounts.length);
        advanceToNextCounts();
        return copy;
    }

    /** @see java.util.Iterator#remove() */
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    private void advanceToNextCounts() {
        int horCars = vehicleCounts[VehicleType.HorizontalCar.ordinal()];
        int verCars = vehicleCounts[VehicleType.VerticalCar.ordinal()];
        int horTrucks = vehicleCounts[VehicleType.HorizontalTruck.ordinal()];
        int verTrucks = vehicleCounts[VehicleType.VerticalTruck.ordinal()];
        
        if (horTrucks > 0) {
            // next truck combination (total trucks remains the same)
            --vehicleCounts[VehicleType.HorizontalTruck.ordinal()];
            ++vehicleCounts[VehicleType.VerticalTruck.ordinal()];
        } else if (horCars > 0) {
            // next car combination (total cars remains the same)
            --vehicleCounts[VehicleType.HorizontalCar.ordinal()];
            ++vehicleCounts[VehicleType.VerticalCar.ordinal()];
            vehicleCounts[VehicleType.HorizontalTruck.ordinal()] = verTrucks;
            vehicleCounts[VehicleType.VerticalTruck.ordinal()] = 0;
        } else if ((horCars + verCars > 0)
                && (horTrucks + verTrucks < RushHourConsts.MaxTrucks)){
            // next total cars
            vehicleCounts[VehicleType.HorizontalCar.ordinal()] =
                horCars + verCars - 1;
            vehicleCounts[VehicleType.VerticalCar.ordinal()] = 0;
            vehicleCounts[VehicleType.HorizontalTruck.ordinal()] =
                horTrucks + verTrucks + 1;
            vehicleCounts[VehicleType.VerticalTruck.ordinal()] = 0;
        } else {
            reachedEnd = true;
        }
    }
    
    private boolean reachedEnd;
    private final int[] vehicleCounts;
}