#!/usr/bin/env ruby

require 'test/unit'
require 'set'
require_relative 'test_helper'
require 'conways'

include Test::Unit

class ConwaysTest < TestCase
  def test_rule1
    assert(!stay_alive?(0))
    assert(!stay_alive?(1))
  end

  def test_rule2
    assert(stay_alive?(2))
    assert(stay_alive?(3))
  end

  def test_rule3
    assert(!stay_alive?(4))
    assert(!stay_alive?(5))
    assert(!stay_alive?(6))
  end

  def test_rule4_1
    assert(!come_alive?(0))
    assert(!come_alive?(1))
    assert(!come_alive?(2))
  end

  def test_rule4_2
    assert(come_alive?(3))
  end

  def test_rule4_3
    assert(!come_alive?(4))
    assert(!come_alive?(5))
    assert(!come_alive?(6))
  end

  def test_neighborhood
    assert_equal(Set.new([[-1,-1],[-1,0],[-1,1],
                          [ 0,-1],       [ 0,1],
                          [ 1,-1],[ 1,0],[ 1,1]]),
                 neighborhood([0,0]))
    assert_equal(Set.new([[4,4],[4,5],[4,6],
                          [5,4],      [5,6],
                          [6,4],[6,5],[6,6]]),
                 neighborhood([5,5]))
  end

  def test_live_neighbor_count
    assert_equal(0, live_neighbor_count([1, 1], Set.new()))
    assert_equal(0, live_neighbor_count([1, 1], Set.new([[5, 5], [5, 6]])))
    assert_equal(0, live_neighbor_count([1, 1], Set.new([[1, 1]])))
    assert_equal(1, live_neighbor_count([1, 1], Set.new([[2, 2]])))
    assert_equal(2, live_neighbor_count([1, 1], Set.new([[0, 1], [1, 0]])))
  end

  def test_nearby_dead_cells
    assert_equal(Set.new([[4,4],[4,5],[4,6],
                          [5,4],      [5,6],
                          [6,4],[6,5],[6,6]]),
                 nearby_dead_cells(Set.new([[5,5]])))
    assert_equal(Set.new([[4,4],[4,5],[4,6],[4,7],
                          [5,4],            [5,7],
                          [6,4],[6,5],[6,6],[6,7]]),
                 nearby_dead_cells(Set.new([[5,5],[5,6]])))
  end

  BLOCK = Set.new([[1,1],[1,2],
                   [2,1],[2,2]])

  BEEHIVE = Set.new([[1,1],[1,2],
                     [2,0],[2,3],
                     [3,1],[3,2]])

  R_PENTOMINO = Set.new([[1,1],[1,2],
                         [2,0],[2,1],
                         [3,1]])

  def test_live_on
    assert_equal(BLOCK, live_on(BLOCK))
    assert_equal(BEEHIVE, live_on(BEEHIVE))
    assert_equal(Set.new([[1, 1], [1, 2],
                          [2, 0],
                          [3, 1]]), 
                 live_on(R_PENTOMINO))
  end

  def test_new_life
    assert_equal(Set.new, new_life(BLOCK))
    assert_equal(Set.new, new_life(BEEHIVE))
    assert_equal(Set.new([[1,0],[3,0]]), 
                 new_life(R_PENTOMINO))
  end

  def test_tick
    assert_equal(BLOCK, tick(BLOCK))
    assert_equal(BEEHIVE, tick(BEEHIVE))
    assert_equal(Set.new([[1,0], [1, 1], [1, 2],
                          [2, 0],
                          [3, 0], [3, 1]]), 
                 tick(R_PENTOMINO))
  end
end
