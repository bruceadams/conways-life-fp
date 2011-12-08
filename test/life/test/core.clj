(ns life.test.core
  (:use [life.core])
  (:use [clojure.test]))

(deftest rule1
  (testing "Any live cell with fewer than two live neighbours dies,
            as if caused by under-population."
	   (is (not (stay-alive? 0)))
	   (is (not (stay-alive? 1)))))

(deftest rule2
  (testing "Any live cell with two or three live neighbours lives on
            to the next generation."
	   (is (stay-alive? 2))
	   (is (stay-alive? 3))))

(deftest rule3
  (testing "Any live cell with more than three live neighbours dies,
            as if by overcrowding."
	   (is (not (stay-alive? 4)))
	   (is (not (stay-alive? 5)))
	   (is (not (stay-alive? 6)))))

(deftest rule4
  (testing "Dead cells with less than three live neighbors remain dead."
	   (is (not (come-alive? 0)))
	   (is (not (come-alive? 1)))
	   (is (not (come-alive? 2))))
  (testing "Any dead cell with exactly three live neighbours becomes
            a live cell, as if by reproduction."
	   (is (come-alive? 3)))
  (testing "Dead cells with more than three live neighbors remain dead."
	   (is (not (come-alive? 4)))
	   (is (not (come-alive? 5)))
	   (is (not (come-alive? 6)))))

(deftest neighborhood-test
  (is (= #{[-1 -1] [-1 0] [-1 1]
	   [ 0 -1] [ 0 0] [ 0 1]
           [ 1 -1] [ 1 0] [ 1 1]}
	 (neighborhood [0 0])))
  (is (= #{[4 4] [4 5] [4 6]
	   [5 4] [5 5] [5 6]
           [6 4] [6 5] [6 6]}
	 (neighborhood [5 5]))))

(deftest neighbor-count-test
  (is (= 0 (neighbor-count [1 1] #{})))
  (is (= 0 (neighbor-count [1 1] #{[5 5] [5 6]})))
  (is (= 1 (neighbor-count [1 1] #{[1 1]})))
  (is (= 1 (neighbor-count [1 1] #{[2 2]})))
  (is (= 2 (neighbor-count [1 1] #{[0 1] [1 0]})))

  )

(deftest interesting-cells-test
  (is (= #{[4 4] [4 5] [4 6]
	   [5 4] [5 5] [5 6]
           [6 4] [6 5] [6 6]}
	 (interesting-cells #{[5 5]})))
  (is (= #{[4 4] [4 5] [4 6] [4 7]
	   [5 4] [5 5] [5 6] [5 7]
           [6 4] [6 5] [6 6] [6 7]}
	 (interesting-cells #{[5 5] [5 6]}))))

(deftest tick-test
  (testing "die off"
	   (is (= #{} (tick #{})))
	   (is (= #{} (tick #{[1 1]})))
	   (is (= #{} (tick #{[1 1] [1 2]})))
	   (is (= #{} (tick #{[1 1] [2 2]})))
	   (is (= #{} (tick #{[1 1] [2 2] [4 4] [5 5]}))))
  (testing "block"
	   (let [block #{[1 1] [1 2]
                         [2 1] [2 2]}]
			 (is (= block (tick block)))))
  (testing "beehive"
	   (let [beehive #{[1 1] [1 2]
		     [2 0]             [2 3]
                           [3 1] [3 2]}]
			 (is (= beehive (tick beehive)))))
  (testing "R-pentomino"
	   (is (= #{[1 0] [1 1] [1 2]
                    [2 0] [2 1] [2 2]
		    [3 0] [3 1]}
		  (tick #{      [1 1] [1 2]
                          [2 0] [2 1]
		                [3 1]}))))
  )
