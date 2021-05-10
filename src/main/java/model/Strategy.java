package model;

import java.util.ArrayList;

public interface Strategy {
    public void addClient(ArrayList<Queue> queues, Client client);
}
