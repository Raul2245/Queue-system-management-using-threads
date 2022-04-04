package controller;

import model.Client;
import model.Server;
import java.util.ArrayList;

public class Scheduler{
    public SimulationManager sm;
    private ArrayList<Server> servers = new ArrayList<Server>();
    private int maxNoServers;
    private int maxClientsPerServer;
    private Strategy strategy;

    public Scheduler (int maxNoServers, int maxClientsPerServer, SelectionPolicy sp, SimulationManager sm) {
        for (int i = 0; i < maxNoServers; i++) {
            Server s = new Server(i, this);
            servers.add(s);
            s.start();
        }
        this.sm = sm;
        this.changeStrategy(sp);
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new SizeStrategy();
        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new TimeStrategy();
    }

    public void dispatchClient (Client c) {
        strategy.addTask(servers, c);
    }

    public ArrayList<Server> getServers() {
        return servers;
    }

}
