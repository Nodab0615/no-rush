package com.google.code.norush.pattern;

import com.google.code.norush.VehicleType;

/**
 * Represents a Rush Hour board pattern, i.e. a board configuration that ignores
 * vehicle identities.
 * 
 * @author DL, GT
 */
public class RushHourPattern {
    /**
     * Constructs the pattern from explicit data.
     * @param vehicleTypes      Vehicle types (ordered by position, ascending).
     * @param vehiclePositions  Vehicle positions.
     */
    public RushHourPattern(VehicleType[] vehicleTypes, int[] vehiclePositions) {
        this.vehicleTypes = vehicleTypes;
        this.vehiclePositions = vehiclePositions;
    }
    
    public final VehicleType[] vehicleTypes;
    public final int[] vehiclePositions;
}
