package model;

import java.util.ArrayList;

public class ConcreteStrategyQueue implements Strategy{

    /**
     *
     * @param queues
     * @param client
     * initialize the minimumClientsPerQueue with the maximum integer number and a queue, queueSeleted,  with null
     * go through all the queues and see if there is any queue which has the number of clients smaller than minimumClientsPerQueue
     * if true, this will be the new minimumClientsPerQueue and the queueSelected will be the queue with that minimum, then add the
     * client to that queue
     */
    @Override
    public void addClient(ArrayList<Queue> queues, Client client) {
        int minimumClientsPerQueue = Integer.MAX_VALUE;
        Queue queueSelected = null;
        for (Queue queue : queues) {
            if(queue.getClients().size() < minimumClientsPerQueue){
                minimumClientsPerQueue = queue.getClients().size();
                queueSelected = queue;
            }
        }
        assert queueSelected != null;
        queueSelected.addClient(client);
    }
}
