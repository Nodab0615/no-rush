package com.google.code.norush.pattern;

import com.google.code.norush.VehicleType;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates all unique vehicle type orederings, given the number of vehicles
 * from each type.
 * 
 * @author DL, GT
 */
public class VehicleOrdersIterator implements Iterator<VehicleType[]> {
    /**
     * Constructor.
     * 
     * @param vehicleCounts  Number of vehicles from each vehicle type. Each
     *                       member of this array corresponds to the vehicle
     *                       type with the same ordinal value.
     */
    public VehicleOrdersIterator(int[] vehicleCounts) {
        assert(vehicleCounts.length == VehicleType.NumVehicles);
        
        this.vehicleCounts = vehicleCounts;

        int totalVehicles = 0;
        for (int vehicleCount : vehicleCounts) {
            totalVehicles += vehicleCount;
        }
        
        vehicleNumFreeIndices = new int[VehicleType.NumVehicles];
        int numFreeIndices = totalVehicles;
        for (int i = 0; i < VehicleType.NumVehicles; ++i) {
            vehicleNumFreeIndices[i] = numFreeIndices;
            numFreeIndices -= vehicleCounts[i];
        }
        
        binomialIterators = new BinomialIterator[VehicleType.NumVehicles];
        for (int i = 0; i < VehicleType.NumVehicles; ++i) {
            binomialIterators[i] = new BinomialIterator(
                vehicleNumFreeIndices[i], vehicleCounts[i]);
        }
        
        vehiclesOrder = new VehicleType[totalVehicles];
        for (int i = 0; i < totalVehicles; ++i) {
            vehiclesOrder[i] = VehicleType.InvalidVehicle;
        }
        
        reachedEnd = !advanceToNextOrder(0);
    }

    /** @see java.util.Iterator#hasNext() */
    public boolean hasNext() {
        return !reachedEnd;
    }
    
    /** @see java.util.Iterator#next() */
    public VehicleType[] next() {
        if (reachedEnd) {
            throw new NoSuchElementException();
        }
        
        VehicleType[] copy = new VehicleType[vehiclesOrder.length];
        System.arraycopy(vehiclesOrder, 0, copy, 0, vehiclesOrder.length);
        
        advanceToNextOrder();
        return copy;
    }

    /** @see java.util.Iterator#remove() */
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    private void writeIndices(int carTypeIndex) {
        if (!binomialIterators[carTypeIndex].hasNext()) {
            binomialIterators[carTypeIndex] = new BinomialIterator(
                vehicleNumFreeIndices[carTypeIndex],
                vehicleCounts[carTypeIndex]);
        }
        
        int[] indices = binomialIterators[carTypeIndex].next();
        if (indices.length == 0) {
            return; // TODO: maybe handle otherwise?
        }
        
        int indiceIndex = 0;
        int numFree = 0;
        for (int i = 0; i < vehiclesOrder.length; ++i) {
            if (vehiclesOrder[i] == VehicleType.InvalidVehicle) {
                if (indices[indiceIndex] == numFree) {
                    vehiclesOrder[i] = VehicleType.values()[carTypeIndex];
                    ++indiceIndex;
                    if (indiceIndex >= indices.length) {
                        break;
                    }
                }
                ++numFree;
            }
        }
    }
    
    private boolean advanceToNextOrder(int carTypeIndex) {
        if (!binomialIterators[carTypeIndex].hasNext()) {
            return false;
        }
        
        // Clear next car types
        for (int i = 0; i < vehiclesOrder.length; ++i) {
            if (vehiclesOrder[i].ordinal() >= carTypeIndex) {
                vehiclesOrder[i] = VehicleType.InvalidVehicle;
            }
        }
        
        for (int i = carTypeIndex; i < VehicleType.NumVehicles; ++i) {
            writeIndices(i);
        }
        return true;
    }
    
    private void advanceToNextOrder() {
        for (int carTypeIndex = VehicleType.NumVehicles-1; carTypeIndex >= 0; --carTypeIndex) {
            if (advanceToNextOrder(carTypeIndex)) {
                return;
            }
        }
        reachedEnd = true;
    }
 
    private boolean reachedEnd;
    private final int[] vehicleNumFreeIndices;
    private final VehicleType[] vehiclesOrder;
    private final int[] vehicleCounts;
    private final BinomialIterator[] binomialIterators;
}