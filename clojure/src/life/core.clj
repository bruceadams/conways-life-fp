(ns life.core
  (:use [clojure.set]))

(defn stay-alive? 
  "At each step in time, the following transitions occur:
1. Any live cell with fewer than two live neighbours dies,
   as if caused by under-population.
2. Any live cell with two or three live neighbours lives on
   to the next generation.
3. Any live cell with more than three live neighbours dies,
   as if by overcrowding."
  [live-neighbors]
  (or (= 2 live-neighbors)
      (= 3 live-neighbors)))

(defn come-alive? 
  "At each step in time, the following transitions occur:
4. Any dead cell with exactly three live neighbours becomes a live cell,
   as if by reproduction."
  [live-neighbors]
  (= 3 live-neighbors))

(defn neighborhood 
  "Return the set of cell coordinates of the Moore neighbors of the
   given cell."
  [p]
  (difference (set (for [x (range (dec (first p)) (inc (inc (first p))))
                         y (range (dec (last  p)) (inc (inc (last  p))))]
                     [x y]))
              #{p}))

(defn live-neighbor-count 
  "Return the number of live neighbors of the given cell."
  [p live-cells]
  (count (intersection (neighborhood p) live-cells)))

(defn nearby-dead-cells 
  "For a set of live cells, return a set of all neighboring dead
   cells."
  [live-cells]
  (difference (apply union (map neighborhood live-cells)) live-cells))

(defn live-on 
  "Return the set of live cells that will remain alive in the next
   tick."
  [live-cells]
  (select #(stay-alive? (live-neighbor-count % live-cells))
          live-cells))

(defn new-life 
  "Return the set of currently dead cells that will come to life in the
   next tick."
  [live-cells]
  (select #(come-alive? (live-neighbor-count % live-cells))
          (nearby-dead-cells live-cells)))

(defn tick 
  "Return the set of live cells in the next tick of Conway's Game of
   Life."
  [live-cells]
  (union (live-on live-cells) (new-life live-cells)))
