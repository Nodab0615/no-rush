package com.google.code.norush.ai;

/**
 * Inteface for an A-Star algorithm heuristic function.
 * 
 * If an optimal solution is required, this function must be
 *  1) admissable, meaning it should never overestimate the actual minimal
 *     distance to the goal.
 *  2) monotonic (or consistent), meaning that for any pair of adjacent states
 *     x and y, we must have h(x) <= 1 + h(y).
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
