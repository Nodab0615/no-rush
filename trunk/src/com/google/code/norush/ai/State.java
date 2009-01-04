package com.google.code.norush.ai;

import java.util.List;

/**
 * An immutable state in the problem-space.
 * 
 * @author DL, GT
 */
public interface State {
    /** @return a list of moves that can be applied to this state. */
    public List<Move> getMoves();
    
    /**
     * Applies a given move to this state.
     * @param move   The move to apply (as returned by the getMoves method).
     * @return the new (post-move) state.
     */
    public State applyMove(Move move);
    
    /** @return whether or not this state is a goal state. */
    public boolean isGoal();
}
