package com.google.code.norush.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implements the standard A-Star (A*) path finding algorithm.
 * 
 * @author DL, GT
 */
public class AStarPathFinder {
    public AStarPathFinder(AStarHeuristic heuristic) {
        this.heuristic = heuristic;
    }
    
    public List<Move> solve(State state) {
        // Final state?
        if (state.isGoal()) {
            return new ArrayList<Move>();
        }

        // A-Star open and closed state lists
        Set<State> closed = new HashSet<State>();
        Map<State, AStarState> open = new HashMap<State, AStarState>();
        PriorityQueue<AStarState> sortedOpen = new PriorityQueue<AStarState>();

        // Add initial state to open list, unless unsolvable for sure
        AStarState startState = new AStarState(state);
        startState.setGScore(0);
        int nextStateHScore = heuristic.distanceToGoal(state);
        if (nextStateHScore == Integer.MAX_VALUE) {
            return null; // No possible solutions
        }
        startState.setHScore(nextStateHScore);
        open.put(state, startState);
        sortedOpen.add(startState);
        
        // Iterate open list till a solution is found
        while (!open.isEmpty()) {
            // Try best known state
            AStarState bestAStarState = sortedOpen.poll();
            State bestState = bestAStarState.getState();
            if (bestState.isGoal()) {
                return reconstructSolution(bestAStarState);
            }
            open.remove(bestState);
            closed.add(bestState);
            
            // Best known state failed, expand it
            for (Move move : bestState.getMoves()) {
                State nextState = bestState.applyMove(move);
                if (closed.contains(nextState)) {
                    // Ignore already-checked states
                    continue;
                }
                
                int nextStateGScore = bestAStarState.getGScore() + 1;
                AStarState nextAStarState = open.get(nextState);
                if (nextAStarState != null) {
                    // State already in open list, update shortest possible path
                    if (nextAStarState.getGScore() > nextStateGScore) {
                        sortedOpen.remove(nextAStarState);
                        nextAStarState.setGScore(nextStateGScore);
                        nextAStarState.setPrevState(bestAStarState, move);
                        sortedOpen.add(nextAStarState);
                    }
                } else {
                    // Add new state to open list
                    nextStateHScore = heuristic.distanceToGoal(nextState);
                    if (nextStateHScore != Integer.MAX_VALUE) {
                        nextAStarState = new AStarState(nextState);
                        nextAStarState.setGScore(nextStateGScore);
                        nextAStarState.setHScore(nextStateHScore);
                        nextAStarState.setPrevState(bestAStarState, move);
                        sortedOpen.add(nextAStarState);
                        open.put(nextState, nextAStarState);
                    } else {
                        // Ignore dead-ends
                        closed.add(nextState);
                    }
                }
            }
        }
        
        // No possible solutions
        return null;
    }
 
    /**
     * Reconstructs the list of moves that needs to be applied to an initial
     * state in order to reach a given goal state.
     * 
     * @param goalState   The reached goal state.
     * @return the moves that can be applied, in order, to the initial state.
     */
    private List<Move> reconstructSolution(AStarState goalState) {
        List<Move> solution = new ArrayList<Move>();
        
        AStarState current = goalState;
        while (true) {
            AStarState prev = current.getPrevState();
            if (prev == null) {
                break;
            }
            solution.add(0, current.getPrevMove());
            current = prev;
        }
        
        return solution;
    }

    /** The heuristic function. */
    private final AStarHeuristic heuristic;
}