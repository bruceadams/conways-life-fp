(ns life.core
  (:use [clojure.set]))

(defn stay-alive? [live-neighbors]
  "At each step in time, the following transitions occur:
1. Any live cell with fewer than two live neighbours dies,
   as if caused by under-population.
2. Any live cell with two or three live neighbours lives on
   to the next generation.
3. Any live cell with more than three live neighbours dies,
   as if by overcrowding."
  (or (= 2 live-neighbors)
      (= 3 live-neighbors)))

(defn come-alive? [live-neighbors]
  "At each step in time, the following transitions occur:
4. Any dead cell with exactly three live neighbours becomes a live cell,
   as if by reproduction."
  (= 3 live-neighbors))

(defn neighborhood [p]
  "Return the set of cell coordinates that make up the Moore
   neighborhood around and including the given cell."
  (set (for [x (range (- (first p) 1) (+ (first p) 2))
	     y (range (- (last  p) 1) (+ (last  p) 2))]
         [x y])))

(defn neighbor-count [p live-cells]
  (count (intersection (neighborhood p) live-cells)))

(defn interesting-cells [live-cells]
  "For a set of live cells, return a superset that adds all neighbors
   of the live cells."
  (apply union (map neighborhood live-cells)))

(defn tick [live-cells]
  (apply union
         (select #(stay-alive? (- (neighbor-count % live-cells) 1))
                 live-cells)
         (select #(come-alive? (neighbor-count % live-cells))
                 (difference (interesting-cells live-cells) live-cells))))

