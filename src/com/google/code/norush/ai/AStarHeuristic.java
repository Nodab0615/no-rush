package com.google.code.norush.ai;

/**
 * Inteface for an A-Star algorithm (admissable) heuristic function.
 * 
 * @author DL, GT
 */
public interface AStarHeuristic {
    /**
     * @param state  A state.
     * @return the assumed, admissable, distance (i.e. minimal number of moves)
     *         between the given state and a goal state.
     *         Returning Integer.MAX_VALUE indicates that the given state can
     *         never reach a goal state.
     */
    public int distanceToGoal(State state);
}
