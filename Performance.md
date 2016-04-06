# Solving Rush Hour #
Performance is measured by solving the 40 standard Rush Hour puzzles using different heuristic functions.

Currently, one of three heuristics is employed:
  1. _Null Heuristic_: returns 0 for all boards, equivalent to running a BFS search.
  1. _Blocking Vehicles Heuristic_: returns the number of occupied squares to the right of the Red Car (i.e. the number of vehicles blocking the Red Car's exit route).
  1. _Oracle Heuristic_: returns the real minimal distance to a solution. This is the best possible heuristic, which simulates how the game would be solved given a complete pre-calculated pattern database. The implementation internally solves the board using A`*` and one of the previous heuristics, saves the results in a hash table and reuses them for subsequent queries. We use this to get lower bounds for performance.

Since all three heuristics are [consistent](http://en.wikipedia.org/wiki/Consistent_heuristic) (hence also [admissible](http://en.wikipedia.org/wiki/Admissible_heuristic)), the algorithm always generates optimal solutions.

## Time Performance ##
We use a 2.33GHz CPU, utilizing just one of its two cores, and measure the total time to solve all 40 puzzles (results are in milliseconds, averaged over 100 trials):

| **Null Heuristic** | **Blocking Cars Heuristic** | **Oracle Heuristic** |
|:-------------------|:----------------------------|:---------------------|
|1385                |934                          |43                    |


## Path Finding Performance ##
For each puzzle, we measure the number of traversed board states, the number of calls to the heuristic function (which is also the number of _unique_ board states) and the [effective branching factor](http://en.wikipedia.org/wiki/Branching_factor).

| **Puzzle** | **Null Heuristic** | **Blocking Cars Heuristic** | **Oracle Heuristic** |
|:-----------|:-------------------|:----------------------------|:---------------------|
|1           |11618 , 1086 , 3.222|6332 , 878 , 2.987           |291 , 176 , 2.032     |
|2           |29812 , 3961 , 3.625|4405 , 1011 , 2.854          |225 , 144 , 1.968     |
|3           |7884 , 839 , 1.898  |4073 , 605 , 1.811           |388 , 199 , 1.531     |
|4           |3474 , 413 , 2.474  |1166 , 274 , 2.192           |79 , 54 , 1.625       |
|5           |21157 , 2318 , 3.024|5271 , 1035 , 2.591          |430 , 250 , 1.962     |
|6           |16850 , 1857 , 2.949|7062 , 1054 , 2.677          |147 , 104 , 1.741     |
|7           |57003 , 6031 , 2.322|20347 , 2484 , 2.145         |561 , 341 , 1.627     |
|8           |6461 , 952 , 2.077  |5580 , 912 , 2.052           |218 , 128 , 1.566     |
|9           |6145 , 932 , 2.069  |2993 , 573 , 1.948           |603 , 223 , 1.705     |
|10          |17204 , 2295 , 1.775|12742 , 1660 , 1.744         |327 , 215 , 1.406     |
|11          |6876 , 869 , 1.424  |5633 , 785 , 1.413           |299 , 182 , 1.256     |
|12          |11686 , 1357 , 1.735|5815 , 905 , 1.665           |539 , 279 , 1.448     |
|13          |76568 , 9511 , 2.02 |30494 , 4603 , 1.907         |321 , 191 , 1.434     |
|14          |115516 , 14618 , 1.985|37669 , 5176 , 1.859         |991 , 617 , 1.501     |
|15          |3192 , 533 , 1.42   |3171 , 524 , 1.42            |552 , 228 , 1.316     |
|16          |22429 , 2857 , 1.611|17317 , 2136 , 1.592         |707 , 379 , 1.367     |
|17          |19525 , 2152 , 1.509|16080 , 1939 , 1.497         |722 , 339 , 1.316     |
|18          |13711 , 1615 , 1.464|11900 , 1550 , 1.456         |373 , 225 , 1.267     |
|19          |3648 , 560 , 1.452  |3437 , 493 , 1.448           |350 , 185 , 1.305     |
|20          |11866 , 2016 , 2.555|4011 , 978 , 2.293           |366 , 191 , 1.804     |
|21          |1670 , 269 , 1.424  |1531 , 253 , 1.418           |191 , 116 , 1.284     |
|22          |33821 , 4279 , 1.493|21155 , 3065 , 1.467         |590 , 327 , 1.278     |
|23          |19284 , 2744 , 1.405|10542 , 1596 , 1.376         |711 , 327 , 1.254     |
|24          |45428 , 4452 , 1.536|42542 , 4172 , 1.532         |765 , 412 , 1.304     |
|25          |81803 , 8775 , 1.52 |60851 , 7059 , 1.504         |758 , 463 , 1.278     |
|26          |40402 , 4855 , 1.461|33738 , 4236 , 1.451         |780 , 460 , 1.268     |
|27          |21308 , 2941 , 1.428|17495 , 2381 , 1.418         |296 , 185 , 1.225     |
|28          |15730 , 2186 , 1.38 |10129 , 1618 , 1.36          |410 , 232 , 1.222     |
|29          |38363 , 4356 , 1.406|37788 , 4309 , 1.405         |670 , 410 , 1.234     |
|30          |8616 , 1173 , 1.327 |7768 , 1131 , 1.323          |910 , 399 , 1.237     |
|31          |32316 , 4005 , 1.324|30497 , 3830 , 1.322         |465 , 302 , 1.181     |
|32          |3292 , 606 , 1.245  |2821 , 542 , 1.24            |592 , 284 , 1.188     |
|33          |37263 , 4168 , 1.301|20782 , 2856 , 1.282         |496 , 310 , 1.168     |
|34          |37433 , 4419 , 1.277|35337 , 4280 , 1.276         |3186 , 1306 , 1.206   |
|35          |34170 , 4132 , 1.275|32966 , 3887 , 1.274         |1125 , 658 , 1.177    |
|36          |22939 , 2943 , 1.256|17494 , 2300 , 1.249         |2090 , 818 , 1.19     |
|37          |15255 , 1951 , 1.227|14319 , 1938 , 1.226         |1118 , 557 , 1.161    |
|38          |28753 , 4003 , 1.238|23669 , 3329 , 1.233         |1702 , 679 , 1.168    |
|39          |24740 , 3688 , 1.224|24226 , 3563 , 1.224         |1344 , 640 , 1.155    |
|40          |24691 , 3249 , 1.219|20183 , 2814 , 1.215         |1545 , 773 , 1.155    |