package com.google.code.norush.heuristics;

import com.google.code.norush.OccupancyMatrix;
import com.google.code.norush.RushHourBoard;
import com.google.code.norush.Vehicle;
import com.google.code.norush.VehicleType;
import com.google.code.norush.ai.AStarHeuristic;
import com.google.code.norush.ai.State;

/**
 * Guesses distance to goal based on the number of vehicles between the Red Car
 * and the exit (this is indeed an admissable and consistent heuristic).
 * 
 * @author DL, GT
 */
public class BlockingVehiclesHeuristic implements AStarHeuristic {
    public int distanceToGoal(State state) {
        RushHourBoard board = (RushHourBoard) state;
        if (board.isGoal()) {
            return 0;
        }

        // Find Red Car
        Vehicle[] vehicles = board.getVehicles();
        int redCarIndex = -1;
        while (vehicles[++redCarIndex].getType() != VehicleType.RedCar) {}
        int redCarPos = vehicles[redCarIndex].getPosition();

        // Find number of blocking cars
        OccupancyMatrix occupancy = new OccupancyMatrix(vehicles);
        int numBlocking = 0;
        for (int i = redCarPos + 2; i < 18; ++i) {
            if (occupancy.isOccupied(i)) {
                ++numBlocking;
            }
        }
        return numBlocking + 1;
    }
}