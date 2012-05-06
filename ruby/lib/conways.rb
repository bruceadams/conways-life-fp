require 'set'

# At each step in time, the following transitions occur:
# 1. Any live cell with fewer than two live neighbours dies,
#    as if caused by under-population.
# 2. Any live cell with two or three live neighbours lives on
#    to the next generation.
# 3. Any live cell with more than three live neighbours dies,
#    as if by overcrowding.
def stay_alive?(live_neighbors)
  live_neighbors == 2 || live_neighbors == 3
end

# At each step in time, the following transitions occur:
# 4. Any dead cell with exactly three live neighbours becomes a live cell,
#    as if by reproduction.
def come_alive?(live_neighbors)
  live_neighbors == 3
end

# Return the set of cell coordinates of the Moore neighbors of the
# given cell.
def neighborhood(cell)
  Set.new((cell[0]-1..cell[0]+1).map{|x| (cell[1]-1..cell[1]+1).map{|y| [x,y]}}.
          flatten(1)) - Set.new([cell])
end

# Return the number of live neighbors of the given cell.
def live_neighbor_count(cell, live_cells)
  (neighborhood(cell) & live_cells).count
end

# For a set of live cells, return a set of all neighboring dead cells.
def nearby_dead_cells(live_cells)
  live_cells.map { |p| neighborhood(p) }.reduce(:|) - live_cells
end

# Return the set of live cells that will remain alive in the next tick.
def live_on(live_cells)
  Set.new(live_cells.select { |p| 
            stay_alive?(live_neighbor_count(p, live_cells)) })
end

# Return the set of currently dead cells that will come to life in the
# next tick.
def new_life(live_cells)
  Set.new(nearby_dead_cells(live_cells).select { |p| 
            come_alive?(live_neighbor_count(p,  live_cells)) })
end

# Return the set of live cells in the next tick of Conway's Game of Life.
def tick(live_cells)
  live_on(live_cells) | new_life(live_cells)
end
