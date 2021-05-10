package model;

import java.util.ArrayList;

public class ConcreteStrategyTime implements Strategy {
    /**
     *
     * @param queues
     * @param client
     *  initialize the minimumWaiting with the maximum integer number and a queue, queueSeleted,  with null
     *  go through all the queues and see if there is any queue which has the waiting time smaller than minimumWaiting
     * if true, this will be the new minimumWaiting and the queueSelected will be the queue with that minimum, then add the
     * client to that queue
     */
    @Override
    public void addClient(ArrayList<Queue> queues, Client client) {
        int minimumWaiting = Integer.MAX_VALUE;
        Queue queueSelected = null;
        for (Queue queue : queues) {
            if (queue.getWaitingPeriod().get() < minimumWaiting) {
                minimumWaiting = queue.getWaitingPeriod().get();
                queueSelected = queue;
            }
        }
        assert queueSelected != null;
        queueSelected.addClient(client);
    }
}
