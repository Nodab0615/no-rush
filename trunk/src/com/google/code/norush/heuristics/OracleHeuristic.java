package com.google.code.norush.heuristics;

import com.google.code.norush.ai.AStarHeuristic;
import com.google.code.norush.ai.AStarPathFinder;
import com.google.code.norush.ai.Move;
import com.google.code.norush.ai.State;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Oracle heuristic for Rush Hour, simulating how A-Star would solve Rush
 * Hour given a complete pattern databse.
 * 
 * This heuristic solves any given state using A-Star (with the blocking
 * vehicles heuristic) and returns the optimal path's length. This can be used
 * to assess the effective branching factor of using a pattern databse (time
 * estimates are obviously meaningless).
 * 
 * @author DL, GT
 */
public class OracleHeuristic implements AStarHeuristic {
    public OracleHeuristic() {}

    public int distanceToGoal(State state) {
        // Known state?
        Integer knownDistance = knownStates.get(state);
        if (knownDistance != null) {
            return knownDistance;
        }
        
        // Unknown state, solve it
        List<Move> moves = aStar.solve(state).solution;
        if (moves == null) {
            // Unsolvable state
            knownStates.put(state, Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
        }
        
        // State solved, add entire path to Oracle's memory
        int numMoves = moves.size();
        knownStates.put(state, numMoves);
        
        int movesLeft = numMoves;
        for (Move move : moves) {
            --movesLeft;
            state = state.applyMove(move);
            knownStates.put(state, movesLeft);
        }
            
        return numMoves;
    }
    
    private AStarPathFinder aStar =
        new AStarPathFinder(new BlockingVehiclesHeuristic());
    private Map<State, Integer> knownStates = new HashMap<State, Integer>();
}
