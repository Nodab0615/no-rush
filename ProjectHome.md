**NoRush!** is a brave attempt to fully solve the 6x6 [Rush Hour](http://en.wikipedia.org/wiki/Rush_Hour_(board_game)) game, using a complete (pre-made) pattern database.

## Background ##
Solving Rush Hour games is a relatively easy task: given an initial board, the complete graph of reachable boards is usually quite small (in the lower 100,000s), making even a basic BFS search feasible and indeed quite fast (see [Performance](Performance.md)).

A slightly better approach is to use the [A\* path finding algorithm](http://en.wikipedia.org/wiki/A*_search_algorithm), accompanied by some heuristic function (given a board, the heuristic returns an estimate of how far this board is from a solution). If the heuristic is both [admissible](http://en.wikipedia.org/wiki/Admissible_heuristic) and [consistent](http://en.wikipedia.org/wiki/Consistent_heuristic), the algorithm is guaranteed to find an optimal solution, if one exists. The more accurate the heuristic, the faster a solution is found.

## Vision ##
**NoRush!** is an attempt to manufacture _the best possible_ heuristic function. Given any Rush Hour board, the heuristic would return the real, accurate, distance to a solution.

This can be done by building (once) a huge database that matches each possible Rush Hour board (or [Board Pattern](Patterns.md)) to its solution-distance.

## Code ##
Source code can be downloaded via the SVN web-interface, and a brief [High Level Design](HighLevelDesign.md) is in the Wiki.