package model;

public class Client implements Comparable{
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public boolean sent;

    public Client(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Client() { }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public int compareTo(Object o) {
        Client c = new Client();
        c = (Client)o;

        if (this.getArrivalTime() < c.getArrivalTime())
            return -1;
        else if (this.getArrivalTime() > c.getArrivalTime())
            return 1;
        return 0;
    }
}
