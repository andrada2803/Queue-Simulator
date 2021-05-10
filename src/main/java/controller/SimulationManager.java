package controller;

import javafx.application.Platform;
import model.Client;
import model.Queue;
import model.Scheduler;
import model.SelectionPolicy;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class SimulationManager implements Runnable {

    public int timeLimit;
    public int minimumServiceTime;
    public int maximumServiceTime;
    public int nbOfQueues;
    public int nbOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    public static float average = 0;
    public int currentTime;
    public static float averageServiceTime = 0;
    public static int peakHour = 0;
    public int maxClients = 0;

    private Scheduler scheduler;
    private final ArrayList<Client> generatedClients;
    private final Controller controller;
    public PrintWriter outFile = new PrintWriter("out1.txt");

    /**
     *
     * @param controller
     * the constructor of the SimulationManager
     */
    public SimulationManager(Controller controller) throws FileNotFoundException {
        this.controller = controller;
        generatedClients = new ArrayList<>();
    }

    /**
     *
     * @param selectionPolicy
     * @param timeLimit
     * @param minArrivalTime
     * @param maxArrivalTime
     * @param nbOfClients
     * @param nbOfQueues
     * @param minimumServiceTime
     * @param maximumServiceTime
     * the input from the "Controller" was set to this class' attributes, in order to manipulate the data
     * here I also call the function which generates the clients randomly, initiate the scheduler and the strategy
     * this all happens after pressing the button "Strategy"
     *
     */
    public void setInput(SelectionPolicy selectionPolicy, int timeLimit, int minArrivalTime, int maxArrivalTime, int nbOfClients, int nbOfQueues, int minimumServiceTime, int maximumServiceTime) {
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.nbOfClients = nbOfClients;
        this.nbOfQueues = nbOfQueues;
        this.minimumServiceTime = minimumServiceTime;
        this.maximumServiceTime = maximumServiceTime;

        generateNRandomClients();
        scheduler = new Scheduler(nbOfQueues);
        scheduler.changeStrategy(selectionPolicy);
    }

    /**
     * generate the N random clients and also computing the average service time
     * at the and I sorted the ArrayList with respect to the arrivalTime
     */
    private void generateNRandomClients() {
        Random rand = new Random();
        int index = 0;

        while (index < nbOfClients) {
            int clientServiceTime = rand.nextInt((maximumServiceTime - minimumServiceTime) + 1) + minimumServiceTime;
            int arriveClientTime = rand.nextInt((maxArrivalTime - minArrivalTime) + 1) + minArrivalTime;
            Client newClient = new Client(index, arriveClientTime, clientServiceTime);

            averageServiceTime += clientServiceTime;
            generatedClients.add(newClient);
            index++;
        }
        averageServiceTime = averageServiceTime / nbOfClients;
        generatedClients.sort(Comparator.comparingInt(Client::getArrivalT));
    }

    /**
     *verify if the simulation time is not over, then create a new ArrayList of clients which have the arrival time equal
     * with the current time, erase them from the waiting clients list (“generatedClients”) and dispatch the clients
     * then I go through all the queues and compute the number of clients for every moment in order to know the peak hour
     * the update the UI (real-time) with the help of “Platform.runLater()”, then the thread will sleep for 1 second,
     * the current time will increase, then I verify if there are still clients in the waiting queue in order to continue the simulation or not
     * after we reach the end of the simulation we have to call the break method from Queue to stop the run method from there
     */
    @Override
    public void run() {
        currentTime = 0;
        while (currentTime < timeLimit) {
            outFile.println("Time: " + currentTime);
            outFile.print("Waiting clients: ");
            int finalCurrentTime = currentTime;
            ArrayList<Client> clients = (ArrayList<Client>) generatedClients.stream().filter(b -> b.getArrivalT() == finalCurrentTime).collect(Collectors.toList());
            for (Client client : clients) {
                generatedClients.remove(client);
            }
            for (Client generatedClient : generatedClients) {
                outFile.print(generatedClient);
            }
            outFile.println();
            for (Client client : clients) {
                scheduler.dispatchClient(client); }
            int allQueues = 0;
            for (Queue queue : scheduler.getQueues()) {
                allQueues += queue.getClients().size();
                outFile.println(queue);}
            if (allQueues > maxClients) {
                peakHour = currentTime;
                maxClients = allQueues; }
            Platform.runLater(() -> controller.update(generatedClients, scheduler.getQueues()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;
            if (generatedClients.isEmpty()) {
                boolean allClosed = true;
                for (Queue queue : scheduler.getQueues()) {
                    if (!queue.getClients().isEmpty()) {
                        allClosed = false;
                        break; } }
                if (allClosed) {
                    for (Queue queue : scheduler.getQueues()) {
                        queue.breakMethod(); }
                    break; } }
        }
        for (Queue queue : scheduler.getQueues()) {
            queue.breakMethod(); }
        Platform.runLater(() -> controller.update(generatedClients, scheduler.getQueues()));
        average /= nbOfClients;
        Platform.runLater(controller::updateStatistics);
        outFile.println("Average waiting is: " + average + ", average service time is: " + averageServiceTime + ", peak hour is: " + peakHour);
        outFile.close();
    }
}
