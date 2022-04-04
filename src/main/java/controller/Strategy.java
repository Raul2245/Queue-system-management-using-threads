package controller;

import model.Client;
import model.Server;

import java.util.ArrayList;

public interface Strategy {
    public void addTask(ArrayList<Server> servers, Client c);
}