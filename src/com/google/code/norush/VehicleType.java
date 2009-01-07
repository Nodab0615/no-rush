package com.google.code.norush;

/**
 * Rush Hour vehicle types.
 * 
 * @author DL, GT
 */
public enum VehicleType {
    RedCar,
    HorizontalCar, VerticalCar,
    HorizontalTruck, VerticalTruck,
    InvalidVehicle;
            
    public static final int NumVehicles = values().length-1 ;
}
