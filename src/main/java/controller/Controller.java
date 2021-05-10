package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Client;
import model.Queue;
import model.SelectionPolicy;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller {
    @FXML
    TextField nbClients;
    @FXML
    TextField nbQueues;
    @FXML
    TextField simulationTime;
    @FXML
    TextField miniArrivalTime;
    @FXML
    TextField maxiArrivalTime;
    @FXML
    TextField minServiceTime;
    @FXML
    TextField maxServiceTime;
    @FXML
    ListView<String> evolutionList;
    @FXML
    ListView<String> waitingQueue;
    @FXML
    Button st;
    @FXML
    Button sq;
    @FXML
    TextField awt;
    @FXML
    TextField ast;
    @FXML
    TextField ph;

    SimulationManager simulationManager;
    SelectionPolicy selectionPolicy = SelectionPolicy.SHORTER_TIME;

    /**
     * initializing the simulation manager
     */
    @FXML
    public void initialize() throws FileNotFoundException {
        simulationManager = new SimulationManager(this);
    }

    /**
     * function for the "Shortest Time" button in case it is presses
     */
    @FXML
    public void strategyTime(){
        selectionPolicy = SelectionPolicy.SHORTER_TIME;
    }

    /**
     * function for the "Shortest Queue" button in case it is presses
     */
    @FXML
    public void strategyQueue(){
        selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
    }

    /**
     * verify if all the input data from the TextFields are integers
     * if true, the data will be trasmitted to the controller,strat the thread
     * if false, display an allert
     */
    @FXML
    public void getInput() {
        if (nbClients.getText().matches("[0-9]+") && nbQueues.getText().matches("[0-9]+") && simulationTime.getText().matches("[0-9]+") &&
                miniArrivalTime.getText().matches("[0-9]+") && maxiArrivalTime.getText().matches("[0-9]+") &&
                miniArrivalTime.getText().matches("[0-9]+") && maxiArrivalTime.getText().matches("[0-9]+")) {
            simulationManager.setInput(selectionPolicy, Integer.parseInt(simulationTime.getText()), Integer.parseInt(miniArrivalTime.getText()),
                    Integer.parseInt(maxiArrivalTime.getText()), Integer.parseInt(nbClients.getText()), Integer.parseInt(nbQueues.getText()),
                    Integer.parseInt(minServiceTime.getText()), Integer.parseInt(maxServiceTime.getText()));

            Thread thread = new Thread(simulationManager);
            thread.start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Ooops, looks like some inputs are NOT integers ");
            alert.show();
        }
    }

    /**
     *
     * @param clients
     * update the ListView where the clients which are NOT in a queue, are displayed
     */
    @FXML
    public void updateWaitingQueue(ArrayList<Client> clients) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        String time = "Time: " + String.valueOf(simulationManager.currentTime);
        observableList.add(time);
        observableList.add("Waiting clients: ");

        for (Client client : clients) {
            observableList.add(client.toString());
        }

        waitingQueue.setItems(observableList);
    }

    /**
     *
     * @param queues
     * update the ListView where the clients which are in a queue, are displayed
     */
    @FXML
    public void updateQueues(ArrayList<Queue> queues) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for (Queue queue : queues) {
            observableList.add(queue.toString());
        }
        evolutionList.setItems(observableList);
    }

    /**
     *
     * @param clients
     * @param queues
     * the function which call the methods that update the ListViews, in order to happen at the same time
     */
    @FXML
    public void update(ArrayList<Client> clients, ArrayList<Queue> queues) {
        this.updateWaitingQueue(clients);
        this.updateQueues(queues);
        updateStatistics();
    }

    /**
     * update the text fields at the end of the simulation with the statistics
     */
    @FXML
    public void updateStatistics(){
        awt.setText(String.valueOf(SimulationManager.average));
        ast.setText(String.valueOf(SimulationManager.averageServiceTime));
        ph.setText(String.valueOf(SimulationManager.peakHour));
    }
}
