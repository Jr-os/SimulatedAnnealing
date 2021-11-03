import java.util.ArrayList;
import java.util.Collections;

import distanceMatrix.DistanceMatrix;

public class Main {

    public static void main(String[] args) {

        // set the DistanceMatrix with all the cities in a give file
        DistanceMatrix dm = new DistanceMatrix("distancias.txt");

        // create a new Distance Matrix given the Initials of the cities of the given
        // file

        // DistanceMatrix path = new DistanceMatrix(dm, "ADPTUV"); // best solution so
        // far : [Teixoso, Pinhal, Atroeira, Vilar, Ulgueira, Douro] 459
        // [Douro, Ulgueira, Vilar, Atroeira, Pinhal, Teixoso] 459

       // DistanceMatrix path = new DistanceMatrix(dm, "CDGILNOQRSTU"); // The best solution is : [Quebrada, Lourel,
                                                                      // Teixoso, Cerdeira, Ulgueira, Douro, Infantado,
                                                                      // Oura, Gonta, Nelas, Serra, Roseiral] ||
                                                                      // QLTCUDIOGNSR || 1546
        // get the initial temperature, by finding the biggest distance between any two
        // cities of a given DistanceMatrix

        DistanceMatrix path = new DistanceMatrix(dm,"ABCDEFGHIJLMOPQRSTUV");
        float initialTemp = getInitialTemp(path);

        // set the SimulatedAnnealing solver
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(path, dm, 0.9F, initialTemp);

        // run the SA algorithm
        simulatedAnnealing.run();
    }

    // find the biggest distance between any two cities
    private static float getInitialTemp(DistanceMatrix path) {

        Integer maxDistance = 0;
        for (ArrayList<Integer> i : path.getDistances()) {

            if (maxDistance < Collections.max(i))
                maxDistance = Collections.max(i);
        }

        return (float) (maxDistance / Math.log(1.01));

    }
}