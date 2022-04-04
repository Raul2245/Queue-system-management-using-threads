package controller;

import model.Client;
import model.Server;
import java.util.ArrayList;

public class SizeStrategy implements Strategy{
    @Override
    public void addTask(ArrayList<Server> servers, Client c) {
        int minSize = 99999;
        for (Server i: servers) {
            if (i.getClients().size() < minSize)
                minSize = i.getProcessTime().intValue();
        }

        for (Server i: servers) {
            if (i.getClients().size() == minSize) {
                i.addClient(c);
                return;
            }
        }
    }
}
