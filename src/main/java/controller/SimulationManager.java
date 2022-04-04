package controller;

import model.Client;
import model.Server;
import view.GUI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager extends Thread{
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int numberOfServers;
    public int numberOfClients;
    public AtomicInteger simulationTime;

    private Scheduler scheduler;
    public GUI gui;
    private ArrayList<Client> generatedClients;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public ArrayList<Client> getGeneratedClients() {
        return generatedClients;
    }

    public void setGeneratedClients(ArrayList<Client> generatedClients) {
        this.generatedClients = generatedClients;
    }


    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime,
                             int minArrivalTime, int maxArrivalTime, int numberOfServers, int numberOfClients) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;

        this.scheduler = new Scheduler(numberOfServers, numberOfClients, SelectionPolicy.SHORTEST_TIME, this);
        this.generatedClients = generateNRandomClients();
        this.simulationTime = new AtomicInteger(0);
    }

    public ArrayList<Client> generateNRandomClients() {
        ArrayList<Client> result = new ArrayList<Client>();

        for (int i = 0; i < numberOfClients; i++) {
            Random rand = new Random();
            int process = rand.nextInt(maxProcessingTime);
            Client c = new Client();

            if (process < minProcessingTime && process + minProcessingTime < maxProcessingTime)
                process += minProcessingTime;
            c.setServiceTime(process);

            int arrival = rand.nextInt(maxArrivalTime);
            if (arrival < minArrivalTime && arrival + minArrivalTime < maxArrivalTime)
                arrival += minArrivalTime;
            c.setArrivalTime(arrival);
            c.setID(i);

            result.add(c);
        }

        Collections.sort(result);
        return result;
    }

    public String clientsToString() {
        String result = new String();

        for (Client c: this.getGeneratedClients()) {
            if (c.sent == false)
                result = result + "(ID: " + c.getID() + ", Service time: " + c.getServiceTime() + ", Arrival Time: " + c.getArrivalTime() + ") ";
        }

        return result;
    }

    public String loggerToString() {
        String result = new String();

        for (Server s: scheduler.getServers()) {
            result = result + "\nServer " + s.getServerID() + ":";
            for (Client c: s.getClients())
                if (c.getServiceTime() != 0)
                result = result + "(ID: " + c.getID() + ", Service time: " + c.getServiceTime() + ", Arrival Time: " + c.getArrivalTime() + ") ";
        }
        result = result + "\n";

        return result;
    }

    @Override
    public void run() {
        int currentTime = 0;
        int initialSize = this.getGeneratedClients().size();
        this.scheduler = new Scheduler(numberOfServers, numberOfClients, SelectionPolicy.SHORTEST_TIME, this);

        ArrayList<Client> toBeRemoved = new ArrayList<>();
        int[] frequency = new int[5000];
        int service = 0;
        int waiting = 0;
        double d = 0.0;
        double d1 = 0.0;
        int max = 0;

        while (currentTime < timeLimit) {
                for (Client c : this.getGeneratedClients())
                    if (c.getArrivalTime() == currentTime) {
                        frequency[c.getArrivalTime()] += 1;
                        service += c.getServiceTime();
                        waiting += c.getArrivalTime();

                        this.getScheduler().dispatchClient(c);
                        c.sent = true;
                        toBeRemoved.add(c);
                    }

            this.generatedClients.removeAll(toBeRemoved);

            currentTime = simulationTime.incrementAndGet();
            gui.getTimeLabel().setText(Integer.toString(currentTime));

            d = (double)(waiting) / initialSize;
            d1 = (double)(service) / initialSize;

            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter("log.txt", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myWriter.write("Time: " + currentTime +
                        "\nWaiting clients: " + this.clientsToString() + this.loggerToString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (this) {
                gui.queues.setText(this.toString());
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        synchronized (this) {
            for (int i = 0; i < frequency.length; i++)
                if (frequency[i] >= max)
                    max = i;

            gui.queues.setText(this.toString() + "\nPeak Time: " + Integer.toString(max)
                    + "\nAverage waiting time: " + Double.toString(d) + "\nAverage service time: " + Double.toString(d1) + "\n" );

            FileWriter myWriter = null;
            try {
                myWriter = new FileWriter("log.txt", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myWriter.write("\nPeak Time: " + Integer.toString(max)
                        + "\nAverage waiting time: " + Double.toString(d) + "\nAverage service time: " + Double.toString(d1) + "\n" );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        String result = new String();

        result = "Clients: " + numberOfClients + " Servers: " + numberOfServers + " Simulation time: " + timeLimit
                + "\nMinimum arrival time: " + minArrivalTime + " Maximum arrival time: " + maxArrivalTime
                + "\nMinimum service time: " + minProcessingTime + " Maximum service time: " + maxProcessingTime + "\n\n";

        synchronized (this.generatedClients) {
            for (Server s : scheduler.getServers()) {
                result = result + "\nServer " + s.getServerID() + ":";
                for (Client c : s.getClients())
                    if (c.getServiceTime() != 0)
                        result = result + "(ID: " + c.getID() + ", Service time: " + c.getServiceTime() + ", Arrival Time: " + c.getArrivalTime() + ") ";
            }
        }

        return result;
    }
}
