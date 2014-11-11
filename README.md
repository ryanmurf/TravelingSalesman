TravelingSalesman
=================

An algorithm to solve a traveling salesman problem


Compile
--------
cd TravelingSalesman
mkdir bin
javac -d ./bin/ -cp "jgrapht-0.9.0/lib/*:src/algorithms/designProblem3/" src/algorithms/designProblem3/*


Run  
----
//First arg is map
//Second arg is the method A for annealing B for bruteforce
java -cp "./jgrapht-0.9.0/lib/*:./bin/" algorithms.designProblem3.TravelingSalesman maps/map8 A

