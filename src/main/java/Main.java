import controller.SimulationManager;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        SimulationManager simulationManager = new SimulationManager(null);
        Thread thread = new Thread(simulationManager);
        thread.start();
    }

}
