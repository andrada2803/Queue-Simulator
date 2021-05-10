package model;

import controller.SimulationManager;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private AtomicInteger state;
    private int id;

    /**
     *
     * @param id
     * Queue constructor
     */
    public Queue(int id) {
        this.clients = new LinkedBlockingQueue<Client>();
        this.waitingPeriod = new AtomicInteger(0);
        this.state = new AtomicInteger(1);
        this.id = id;
    }

    /**
     *
     * @return the client from the queue
     */
    public BlockingQueue<Client> getClients() {
        return clients;
    }

    /**
     *
     * @param clients
     * set the client from that queue
     */
    public void setClients(BlockingQueue<Client> clients) {
        this.clients = clients;
    }

    /**
     *
     * @return the waiting time for that queue
     */
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    /**
     *
     * @param waitingPeriod
     * set the waiting period
     */
    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    /**
     *
     * @return the id of the queue
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param newClient
     * add the client to the queue, set its finish time and increase the waiting time of the queue
     */
    public void addClient(Client newClient) {
        clients.add(newClient);
        newClient.setFinishTime(waitingPeriod.get() + newClient.arrivalT + newClient.serviceT);
        waitingPeriod.getAndAdd(newClient.serviceT);
    }

    /**
     * take the next client, the thread stops for a second, it decrements the waiting time, and I remove the client from
     * the waiting clients list, also here I compute the average waiting time (the value before the division to the number
     * of clients), all this as long as there are still clients or the simulation time is not over
     */
    @Override
    public void run() {
        while (state.get() == 1) {
            try {
                if (!clients.isEmpty()) {
                    Client client = clients.element();
                    for (int i = 0; i < client.serviceT; i++) {
                        Thread.sleep(1000);
                        waitingPeriod.getAndAdd(-1);
                    }
                    clients.remove(client);
                    SimulationManager.average += (client.finishTime - client.arrivalT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * stops the run method
     */
    public void breakMethod() {
        state.set(0);
    }

    @Override
    public String toString() {
        return "Queue " + id +
                ": " + clients;
    }
}
