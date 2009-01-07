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
    
    /**
     * The result of running the A-Star path finder: the found solution and
     * related statistics.
     */
    public class AStarResult {
        /**
         * Constructor.
         * @param solution    The list of moves that needs to be applied to the
         *                    initial state in order to reach a goal state, or
         *                    null if no solution exists.
         * @param states      The number of expanded states (a unique state can
         *                    be be expanded more than once).
         * @param uniqueStates   The number of unique states traversed (this is
         *                       the number of times the heuristic function was
         *                       called).
         */
        public AStarResult(List<Move> solution, int states, int uniqueStates) {
            this.solution = solution;
            this.states = states;
            this.uniqueStates = uniqueStates;

            // Calculate effective branching factor (or explicitly set to 0, if
            // no solution was found or initial state was a goal state).
            if ((solution != null) && (solution.size() != 0)) {
                ebf = Math.pow(states, 1.0/solution.size());
            } else {
                ebf = 0;
            }
        }
        
        public final List<Move> solution;
        public final int states;
        public final int uniqueStates;
        
        /** The effective branching factor. */
        public final double ebf;
    }
    
    public AStarResult solve(State state) {
        // Final state?
        if (state.isGoal()) {
            return new AStarResult(new ArrayList<Move>(), 1, 1);
        }

        // A-Star open and closed state lists
        Set<State> closed = new HashSet<State>();
        Map<State, AStarState> open = new HashMap<State, AStarState>();
        PriorityQueue<AStarState> sortedOpen = new PriorityQueue<AStarState>();

        // Add initial state to open list, unless unsolvable for sure
        int numStates = 1;
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
                return new AStarResult(reconstructSolution(bestAStarState),
                    numStates, open.size() + closed.size());
            }
            open.remove(bestState);
            closed.add(bestState);
            
            // Best known state failed, expand it
            for (Move move : bestState.getMoves()) {
                ++numStates;
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
        return new AStarResult(null, numStates, open.size() + closed.size());
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