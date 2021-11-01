import java.util.ArrayList;
import java.util.Collections;

import distanceMatrix.DistanceMatrix;

// função simulated-annealing(problema, decaimento, temperatura_inicial, cria_solução_inicial, n_iter, var_n_iter, vizinho, ...) retorna um estado solução

// inputs:
//      problema, um problema
//      decaimento, uma função de decaimento da temperatura
//      temperatura_inicial, uma função que determina a temperatura inicial a partir do problema
//      cria_solução_inicial, uma função que determina uma solução inicial a partir do problema
//      var_n_iter, uma função de variação do número de iterações a cada temperatura
//      vizinho, uma função que retorna um vizinho a partir de uma solução
//      criterio_de_paragem, uma função que retorna true no caso de se ter atingido o critério de paragem
//      n_iter, o número de iterações à temperatura inicial

// variáveis locais:
//      corrente, uma solução
//      próximo, uma solução
//      melhor, uma solução
//      T, uma "temperatura" que controla a probabilidade de aceitação de soluções piores

// corrente <- cria_solução_inicial(problema)
// melhor <- corrente
// T <- temperatura_inicial(problema)
// repeat
//     for n = 1 to n_iter do
//         próximo <- vizinho(corrente)
//         d <- distância(próximo) - distância(corrente)
//         if d < 0 then
//             corrente <- próximo
//             if distância(corrente) < distância(melhor) then melhor <- corrente
//         else corrente <- próximo apenas com probabilidade exp(-d/T)
//     if criterio_de_paragem(...) then retorna melhor
//     n_iter <- var_n_iter(n_iter)
//     T <- decaimento(T)

public class SimulatedAnnealing {

    private final DistanceMatrix initialSolutionMatrix;
    private final DistanceMatrix fullMatrix;
    private final float decay;
    private final float initialTemp;

    public SimulatedAnnealing(DistanceMatrix initialSolutionMatrix, DistanceMatrix fullMatrix, float decay,
            float initialTemp) {
        this.initialSolutionMatrix = initialSolutionMatrix;
        this.fullMatrix = fullMatrix;
        this.decay = decay;
        this.initialTemp = initialTemp;
    }

    public void run() {

        System.out.println();
        long start = System.nanoTime();
        ArrayList<String> result = simulatedAnnealingAlgorithm();
        long finish = System.nanoTime();
        long duration = finish - start;
        double timeElapsed = (double) duration / 1000000000;

        System.out.println();
        System.out.println("Elapsed Time : " + timeElapsed);
        System.out.println("The best solution is : " + result.toString() + " || " + fullMatrix.getInitials(result));
        System.out.println("-------------------------------------------------------------------------------------");

        int totalCost = calculateDistance(result);
        System.out.println("The lowest cost is : " + totalCost);

        int returnCost = fullMatrix.distance(result.get(0), result.get(result.size() - 1));
        System.out.println("The return distance from the goal to the start is : " + returnCost);
        System.out.println("The total cost is : " + (int) (returnCost + totalCost));

    }

    private ArrayList<String> simulatedAnnealingAlgorithm() {
        float currentTemp = initialTemp;

        ArrayList<String> bestSolution = new ArrayList<>(initialSolutionMatrix.getCities());
        ArrayList<String> currentSolution = new ArrayList<>(initialSolutionMatrix.getCities());
        ArrayList<String> nextSolution;

        int currentSolutionCost = calculateDistance(currentSolution);
        int bestSolutionCost = currentSolutionCost;
        int nextSolutionCost;

        
        int totalIterations = 0;
        int acceptanceCriteria = 0;

         while (acceptanceCriteria < 550) {
       // while (currentTemp > 1) {
            int nIterations = 20;
            for (int iteration = 0; iteration < nIterations; iteration++) {
                acceptanceCriteria++;
                nextSolution = swap(currentSolution); // get neighbour solution
                nextSolutionCost = calculateDistance(nextSolution);
                if (nextSolutionCost - currentSolutionCost < 0) {
                    currentSolution = nextSolution; // if the neighbour solution has a lower cost , the algorithm always
                                                    // accepts it
                    acceptanceCriteria = 0;
                    currentSolutionCost = nextSolutionCost;
                    if (currentSolutionCost < bestSolutionCost)
                        bestSolution = currentSolution; // if the current solution has a lower cost than the best
                                                        // solution
                                                        // found so far, update the best solution
                } else {
                    double chance = acceptanceProbability(currentTemp, currentSolutionCost, nextSolutionCost);
                  //  System.out.println(chance);
                    if (chance > Math.random()) {
                        currentSolution = nextSolution;
                        acceptanceCriteria = 0;
                    }
                }
                totalIterations++;

            }
            nIterations *= 1.2;
            currentTemp *= decay;

        }
        System.out.println("Total number of itarations :" + totalIterations);
        return bestSolution;
    }

    // d = d(i,j) + d(i+1,j+1) - d(i,i+1) - d(j,j+1)
    // P(d) = exp(-d/T)
    private double acceptanceProbability(float currentTemp, int currentSolutionCost, int nextSolutionCost) {

        return Math.exp(-( nextSolutionCost - currentSolutionCost ) / currentTemp);
    }

    private ArrayList<String> swap(ArrayList<String> currentSolution) {
        ArrayList<String> nextSolution = new ArrayList<>(currentSolution);

        int firstPick = (int) (Math.random() * currentSolution.size());
        int secondPick = firstPick;

        // so we can make sure the picked cities are different and not next to each
        // other
        while (firstPick == secondPick || Math.abs(firstPick - secondPick) > 1) {
            secondPick = (int) (Math.random() * currentSolution.size());
        }

        // System.out.println("---------------------------------------------------------------------------------------");
        // System.out.println("The first pick was : " + firstPick + " " + "\nThe
        // secondpick was : " + secondPick);
        // System.out.println("The original solution is : " +
        // currentSolution.toString());
        Collections.swap(nextSolution, firstPick, secondPick);
        // System.out.println("The new solution is : " + nextSolution.toString());
        // System.out.println("----------------------------------------------------------------------------------------");
        return nextSolution;
    }

    private int calculateDistance(ArrayList<String> solution) {
        DistanceMatrix distanceMatrix = new DistanceMatrix(fullMatrix, solution);
        int totalCost = 0;

        // sum of all the distances between the cities
        for (int i = 0; i < solution.size() - 1; i++) {
            totalCost += distanceMatrix.distance(solution.get(i), solution.get(i + 1));

        }

        return totalCost;
    }

}
