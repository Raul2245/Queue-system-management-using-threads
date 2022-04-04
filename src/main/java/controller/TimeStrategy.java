package controller;

import model.Client;
import model.Server;
import java.util.ArrayList;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(ArrayList<Server> servers, Client c) {
        int minTime = 99999;
        for (Server i: servers) {
            if (i.getProcessTime().intValue() < minTime)
                minTime = i.getProcessTime().intValue();
        }

        for (Server i: servers) {
            if (i.getProcessTime().intValue() == minTime) {
                i.addClient(c);

                return;
            }
        }
    }
}
