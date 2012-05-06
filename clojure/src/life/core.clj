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
  "Return the set of cell coordinates of the Moore neighbors of the
   given cell."
  (difference (set (for [x (range (dec (first p)) (inc (inc (first p))))
                         y (range (dec (last  p)) (inc (inc (last  p))))]
                     [x y]))
              #{p}))

(defn live-neighbor-count [p live-cells]
  "Return the number of live neighbors of the given cell."
  (count (intersection (neighborhood p) live-cells)))

(defn nearby-dead-cells [live-cells]
  "For a set of live cells, return a set of all neighboring dead
   cells."
  (difference (apply union (map neighborhood live-cells)) live-cells))

(defn live-on [live-cells]
  "Return the set of live cells that will remain alive in the next
   tick."
  (select #(stay-alive? (live-neighbor-count % live-cells))
          live-cells))

(defn new-life [live-cells]
  "Return the set of currently dead cells that will come to life in the
   next tick."
  (select #(come-alive? (live-neighbor-count % live-cells))
          (nearby-dead-cells live-cells)))

(defn tick [live-cells]
  "Return the set of live cells in the next tick of Conway's Game of
   Life."
  (union (live-on live-cells) (new-life live-cells)))

(defn ascii-format [live-tests]
  (let [xmin () ymin () xmax () ymax ()]
    ))