package model;

import controller.Scheduler;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread{
    private ArrayBlockingQueue<Client> clients = new ArrayBlockingQueue<Client>(100);
    private int serverID;
    private AtomicInteger processTime;
    private Scheduler scheduler;

    public ArrayBlockingQueue<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayBlockingQueue<Client> clients) {
        this.clients = clients;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public AtomicInteger getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime.set(processTime);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler s) {
        this.scheduler = s;
    }

    public Server(int serverID, Scheduler s) {
        this.serverID = serverID;
        this.processTime = new AtomicInteger();
        this.scheduler = s;
    }

    public void addClient(Client c) {
        this.clients.add(c);
        this.getProcessTime().set(this.getProcessTime().intValue() + c.getServiceTime());
    }

    @Override
    public void run() {
        ArrayList<Client> toBeRemoved = new ArrayList<>();

        while (this.getProcessTime().get() > 0) {
            Client c = null;

            if (!this.getClients().isEmpty()) {
                try {
                    c = this.getClients().take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (c != null) {
                try {
                    sleep(c.getServiceTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                this.getProcessTime().set(this.getProcessTime().get() - c.getServiceTime());
                c.setServiceTime(0);
                toBeRemoved.add(c);
            }

            this.getClients().removeAll(toBeRemoved);

            synchronized (this) {
                scheduler.sm.gui.queues.setText(scheduler.sm.toString());
            }
        }
    }
}
