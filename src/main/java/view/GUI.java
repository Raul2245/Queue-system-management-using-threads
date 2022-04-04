package view;

import controller.SelectionPolicy;
import controller.SimulationManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI extends JFrame {
    public JPanel contentPane;
    public JTextField clientsField;
    public JTextField serverField;
    public JTextField simulationField;
    public JTextField minArrivalField;
    public JTextField maxArrivalField;
    public JTextField maxServiceField;
    public JTextField minServiceField;
    public JLabel timeLabel;

    public int started = 0;

    public JTextPane queues;

    public void setTimeLabel (String s) {
        this.timeLabel.setText(s);
    }

    public JLabel getTimeLabel () {
        return this.timeLabel;
    }

    public GUI() {
        setTitle("Queue manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 741, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        clientsField = new JTextField();
        clientsField.setBounds(10, 28, 47, 19);
        contentPane.add(clientsField);
        clientsField.setColumns(10);

        serverField = new JTextField();
        serverField.setBounds(10, 72, 47, 19);
        contentPane.add(serverField);
        serverField.setColumns(10);

        simulationField = new JTextField();
        simulationField.setBounds(361, 28, 151, 19);
        contentPane.add(simulationField);
        simulationField.setColumns(10);

        minArrivalField = new JTextField();
        minArrivalField.setBounds(67, 28, 123, 19);
        contentPane.add(minArrivalField);
        minArrivalField.setColumns(10);

        maxArrivalField = new JTextField();
        maxArrivalField.setBounds(67, 72, 123, 19);
        contentPane.add(maxArrivalField);
        maxArrivalField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Clients");
        lblNewLabel.setBounds(10, 10, 96, 13);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Servers");
        lblNewLabel_1.setBounds(10, 57, 90, 13);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Simulation interval");
        lblNewLabel_2.setBounds(362, 10, 123, 13);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Minimum arrival time");
        lblNewLabel_3.setBounds(67, 10, 123, 13);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Maximum arrival time");
        lblNewLabel_4.setBounds(66, 57, 123, 13);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Minimum service time");
        lblNewLabel_5.setBounds(217, 10, 135, 13);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Maximum service time");
        lblNewLabel_6.setBounds(215, 57, 151, 13);
        contentPane.add(lblNewLabel_6);

        maxServiceField = new JTextField();
        maxServiceField.setBounds(215, 72, 136, 19);
        contentPane.add(maxServiceField);
        maxServiceField.setColumns(10);

        minServiceField = new JTextField();
        minServiceField.setBounds(215, 28, 136, 19);
        contentPane.add(minServiceField);
        minServiceField.setColumns(10);

        queues = new JTextPane();
        queues.setEditable(false);
        queues.setBounds(10, 119, 707, 334);
        contentPane.add(queues);

        JScrollPane scrollPane = new JScrollPane(queues);
        contentPane.add(scrollPane);
        scrollPane.setBounds(10, 119, 707, 334);

        JRadioButton queueCheck = new JRadioButton("Shortest Queue Strategy");
        queueCheck.setBounds(361, 53, 174, 21);
        contentPane.add(queueCheck);

        JRadioButton timeCheck = new JRadioButton("Shortest Time Strategy");
        timeCheck.setBounds(361, 71, 165, 21);
        contentPane.add(timeCheck);

        GUI g = this;
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulationManager sm = new SimulationManager(Integer.valueOf(simulationField.getText()),
                        Integer.valueOf(maxServiceField.getText()), Integer.valueOf(minServiceField.getText()),
                        Integer.valueOf(minArrivalField.getText()), Integer.valueOf(maxArrivalField.getText()),
                        Integer.valueOf(serverField.getText()), Integer.valueOf(clientsField.getText()));
                sm.setGui(g);
                sm.start();

                if (queueCheck.isSelected())
                    sm.getScheduler().changeStrategy(SelectionPolicy.SHORTEST_QUEUE);
                else if (timeCheck.isSelected())
                    sm.getScheduler().changeStrategy(SelectionPolicy.SHORTEST_TIME);

                synchronized (this) {
                    queues.setText(sm.toString());
                }
            }
        });
        startButton.setBounds(541, 28, 144, 61);
        contentPane.add(startButton);

        JLabel lblNewLabel_7 = new JLabel("Simulation Time: ");
        lblNewLabel_7.setBounds(540, 96, 111, 13);
        contentPane.add(lblNewLabel_7);

        timeLabel = new JLabel("0");
        timeLabel.setBounds(651, 96, 45, 13);
        contentPane.add(timeLabel);

    }

    public static void main(String[] args) {
            GUI frame = new GUI();
            frame.setVisible(true);
    }
}
