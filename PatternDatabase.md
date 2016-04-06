# Rush Hour Pattern Database #

## Definition ##
A Rush Hour pattern database, is a database that holds, for each legal pattern,
the minimal distance between itself and a solution, or -1 if it is not a solvable pattern.
The patterns are indexed according to a certain encoding, to be specified later.
The database entries consist of the distance value, indexed in the database by offset according the pattern's encoding. This distance has been shown to be about 93 in the worst case, so we represent this using one Byte.

## Generation ##
There are a couple of ways to fill the entries in the database:
  1. _solve the rush hour problem for each legal pattern_: Iterate over total possible number of vehicles in a pattern, and solve for all legal patterns with such number of vehicles. This means, that when solving the rush hour problem for any pattern with i vehicles, you already have a complete database on patterns with (i-1) vehicles. Given a specific pattern state, consider the heuristic of taking the maximum, over the database entries of patterns achieved from it by removing a single car. This heuristic is [consistent](http://en.wikipedia.org/wiki/Consistent_heuristic), rather accurate and fast to calculate, allowing to find a solution for any specific pattern with i vehicles in a very short time. Notice, that when solving the rush hour problem for a specific pattern, we find a path from itself to an optimal solution. This supplies us with the database entry values for every pattern appearing on this path.
  1. _find all shortest paths, one connected component at a time_: Iterate, using the supplied iterators, over all possible legal patterns, and for each unsolved pattern (empty entry in database), generate with BFS (A`*` with _null heuristic_) the entire connected component. While generating this connected component, we save a list of all wining states. Initialize a BFS search, starting with the winning states in the opened list, and distance zero. Fill the database with the resulting distance values.


## Pattern Encoding ##
There are several ways to encode a pattern:
  1. _save an index for each pattern_:  The encoding of a specific pattern, will save for each car, in ascending order as described in the [Patterns](http://code.google.com/p/no-rush/wiki/Patterns) section, the car type and location on the board. This can be done in one Byte per car, or 15 Bytes per pattern. A query to the database for a specific pattern consists of calculating this number, and performing a Binary search on the database to reach the desired entry. Since this significantly bloats the database size, we can choose to explicitly write the encoding of just one out of every, say, 1000 patterns. Then, given a pattern we can perform a binary search to find a close "written" pattern in the database, and start iterating from it to find the exact index of the searched pattern. Since the iterators allow starting the iteration from any arbitrary pattern, this provides a reasonable tradeoff between database size and query speed.
  1. _offset by Zobrist key_:  Since the patterns may be partitioned neatly, we may hold a database file for each partitioned set of patterns. For each such set, we may generate a [Zobrist](http://en.wikipedia.org/wiki/Zobrist_hashing) key of length comparable to _log(<set size>)_, repeating until a seed is found for which the generated Zobrist keys provide no collision. Both of these iterations can be done with the supplied iterators. This results in a fast-to-calculate perfect hash for each such set of patterns, into a continuous index set of comparable size. This allows to to forgo saving the index value for each database entry, as each entry can be accessed by the offset dictated by it's encoding. The price we pay is certain amount of empty entries we will have to hold.
  1. _offset by random projection encoding_: The method presented above may be applied to any method of randomly generating a fast-to-calculate hash on all possible patterns (including non-legal), that has a chance to be collision free on a sub-set of patterns (the legal patterns), and is into a continuous index set of size comparable to that of the sub-set. One such method is [Random Projection](http://en.wikipedia.org/wiki/Johnson-Lindenstrauss_lemma), which has a relatively low expectancy of collisions.

It is worth noting, that the main issue faced here is that of encoding the set of legal patterns into a continuous index set of comparable size, so that each specific pattern's index is fast to calculate. Certain elements from Coding Theory and Combinatorics, such as [Error Correcting Codes](http://en.wikipedia.org/wiki/Error_correcting_code) or processes involved in creating k-wise independent random variables (some examples given [here](http://www.cs.tau.ac.il/courses/combsem/09a/ppt/SimpleConstructions.ppt)), may be explored to yield a solution to this issue.

## Notes ##
We note that the database may be compressed. Certain facts, such as the first bit of every entry is always being zero (the distance from solution is always less than 128) lead us to believing the compression factor will be high.