**NoRush!** is written in Java, and consists of four major packages. Unit Testing is done with the [JUnit](http://en.wikipedia.org/wiki/JUnit) framework.

### com.google.code.norush ###
Implements the basic Rush Hour data structures: a Rush Hour board, vehicle types, etc.

### com.google.code.norush.ai ###
Generic, relatively fast, implementation of the A`*` path finding algorithm, providing interfaces for States, Moves and Heuristic functions.
This package is completely independent of the rest of the project and contains no Rush Hour specific logic. It can be plugged as-is to other, unrelated, projects.

### com.google.code.norush.heuristics ###
Implements the Rush Hour specific Heuristic functions (see [Performance](Performance.md) for details).

### com.google.code.norush.pattern ###
Defines the Rush Hour board pattern data structure, and implements logic for multi-level iteration over all Rush Hour board patterns (see [Patterns](Patterns.md)).

The most important class in this package is RushHourPatternIterator.
Given an ordered list of vehicle types, the class iterates all legal Rush Hour patterns that match it. Implementation is fairly optimized, significantly reducing the possible search-space using position tables for the various vehicle types.