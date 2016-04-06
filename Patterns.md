# Rush Hour Patterns #

## Definition ##
A Rush Hour pattern defines a unique board formation in which individual vehicle identities are ignored. That is, a pattern only cares about vehicle types and positions ("Horizontal Car in first row, columns 3-4") and ignores car names ("GREEN Horizontal Car in first row, columns 3-4"). Since vehicle identities have no affect in the game world, a heuristic function can operate on patterns.

## Partitioning ##
Rush Hour board patterns can be multi-level partitioned:

First, patterns can be partitioned according to the total number of vehicles on the board (excluding the Red Car, which is always present).

When this number is known, further partitioning can be done based on the _Vehicle Counts_: the number of vehicles from each unique type (4 total: Horizontal/Vertical Cars, Horizontal/Vertical Trucks). In Rush Hour, the game rules limit the total number of Cars (excluding the Red Car) to 11 and the total number of Trucks to 4.

When unique vehicle counts are known, a third level of partitioning can be applied according to the _Vehicle Orders_ (from here on, the board is treated as a one-dimensional vector of length 36, where rows are concatenated in ascending order). For example, patterns that contain just 2 Horizontal Cars (and the Red Car) can have one of three distinct orderings: RHH, HRH, HHR.

Finally, given a specific ordering of the 4 vehicle types, one can simply iterate all legal Rush Hour patterns that match it.

## Statistics ##
**NoRush!** provides Java iterators for each level above. The following partial table is based on running them on a 2.33GHz CPU (utilizing just one of its two cores):

| **Total Vehicles** | **Vehicle Counts** | **Vehicle Orders** | **Patterns** | **Orders Iteration (ms)** | **Patterns Iteration (ms)** |
|:-------------------|:-------------------|:-------------------|:-------------|:--------------------------|:----------------------------|
|0                   |1                   |1                   |5             |4                          |4                            |
|1                   |4                   |8                   |463           |0                          |1                            |
|2                   |10                  |48                  |18360         |1                          |6                            |
|3                   |20                  |256                 |411873        |3                          |43                           |
|4                   |35                  |1280                |5817423       |2                          |702                          |
|5                   |50                  |5952                |54174754      |12                         |8298                         |
|6                   |65                  |25536               |340649436     |37                         |71319                        |
|7                   |80                  |101376              |1465092227    |149                        |458746                       |
|8                   |95                  |375552              |4328632113    |554                        |2310181                      |
|9                   |110                 |1310720             |8747585886    |1962                       |9579390                      |
|10                  |125                 |4347904             |11923910742   |6678                       |34353494                     |
|11                  |140                 |13811712            |10688660835   |21898                      |113420311                    |
|12                  |142                 |42225664            |              |68843                      |                             |
|13                  |130                 |123748352           |              |207027                     |                             |
|14                  |103                 |335462400           |              |577536                     |                             |
|15                  |60                  |715653120           |              |1238005                    |                             |