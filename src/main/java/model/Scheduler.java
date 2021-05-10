package model;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Queue> queues;
    private Strategy strategy;

    /**
     *
     * @param maxNbOfQueues
     * Scheduler constructor
     */
    public Scheduler(int maxNbOfQueues){
        queues = new ArrayList<>();
        for (int nbOfQueues = 0; nbOfQueues < maxNbOfQueues; nbOfQueues++) {
            Queue newQueue = new Queue(nbOfQueues);
            Thread queueThread = new Thread(newQueue);
            queues.add(newQueue);
            queueThread.start();
        }
    }

    /**
     *
     * @return the queues
     */
    public ArrayList<Queue> getQueues(){
        return queues;
    }

    /**
     *
     * @param policy
     * change the strategy if one is choosen
     */
    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTER_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    /**
     *
     * @param client
     * add the clients for the strategy chosen
     */
    public void dispatchClient(Client client){
        strategy.addClient(queues, client);
    }
}
