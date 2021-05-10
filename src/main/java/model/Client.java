package model;

public class Client {
    public int ID;
    public int arrivalT;
    public int serviceT;
    public int finishTime;

    /**
     *
     * @param ID
     * @param arrivalT
     * @param serviceT
     * Client constructor
     */
    public Client(int ID, int arrivalT, int serviceT) {
        this.ID = ID;
        this.arrivalT = arrivalT;
        this.serviceT = serviceT;
    }

    /**
     *
     * @return the arrival time of the client
     */
    public int getArrivalT() {
        return arrivalT;
    }

    /**
     *
     * @param finishTime
     * set the finish time
     */
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "(" + ID +
                "," + arrivalT +
                "," + serviceT +
                ')';
    }
}
