package com.google.code.norush.heuristics;

import com.google.code.norush.ai.AStarHeuristic;
import com.google.code.norush.ai.State;

/**
 * The null heuristic: returns 0 for all states.
 * The A-Star algorithm effectively becomes BFS.
 * 
 * @author DL, GT
 */
public class NullHeuristic implements AStarHeuristic {
    public int distanceToGoal(State state) {
        return 0;
    }
}
