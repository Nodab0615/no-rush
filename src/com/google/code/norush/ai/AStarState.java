package com.google.code.norush.ai;

/**
 * A state with its A-Star specific meta-data (scores, previous state, etc).
 * Supports comparing two states according to their G+H scores.
 * 
 * @author DL, GT
 */
public class AStarState implements Comparable<AStarState> {
    public AStarState(State state) { 
        this.state = state;
    }
    
    public State getState() {
        return state;
    }
    
    public int getGScore() {
        return gScore;
    }
    
    public void setGScore(int gScore) {
        this.gScore = gScore;
    }
    
    public int getHScore() {
        return hScore;
    }
    
    public void setHScore(int hScore) {
        this.hScore = hScore;
    }
    
    public AStarState getPrevState() {
        return prevAStarState;
    }
    
    public Move getPrevMove() {
        return prevStateMove;
    }
    
    public void setPrevState(AStarState prevAStarState, Move prevStateMove) {
        this.prevAStarState = prevAStarState;
        this.prevStateMove = prevStateMove;
    }
    
    /** @see Comparable#compareTo */
    @Override
    public int compareTo(AStarState other) {
        return (gScore + hScore - other.gScore - other.hScore);
    }
    
    /** The actual, wrapped, state object. */
    private State state;
    
    /** A-Star actual distance from initial state. */
    private int gScore;
    
    /** A-Star heuristic distance to goal state. */
    private int hScore;
    
    /** The previous State. */
    private AStarState prevAStarState;
    
    /** The Move used to transform the previous state to this state. */
    private Move prevStateMove;
}
