package org.example.clients.gui.view;

import org.example.clients.ClientConstants;
import org.example.clients.gui.controller.ClientGuiController;
import org.example.clients.gui.model.ClientGuiModel;

import javax.swing.*;
import java.awt.*;

public class ClientGuiView {

    private final ClientGuiController controller;

    private JFrame frame = new JFrame(ClientConstants.TITLE);
    private JTextField textField = new JTextField(50);
    private JTextArea messages = new JTextArea(10, 50);
    private JTextArea users = new JTextArea(10, 10);

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        textField.setEditable(false);
        messages.setEditable(false);
        users.setEditable(false);

        frame.getContentPane().add(textField, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(messages), BorderLayout.WEST);
        frame.getContentPane().add(new JScrollPane(users), BorderLayout.EAST);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener((action) -> {
            controller.sendTextMessage(textField.getText());
            textField.setText("");
        });
    }

    public String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                ClientConstants.ENTER_SERVER_ADDRESS,
                ClientConstants.CLIENT_CONFIGURATION,
                JOptionPane.QUESTION_MESSAGE);
    }

    public int getServerPort() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    ClientConstants.ENTER_SERVER_PORT,
                    ClientConstants.CLIENT_CONFIGURATION,
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        ClientConstants.INCORRECT_PORT,
                        ClientConstants.CLIENT_CONFIGURATION,
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame,
                ClientConstants.ENTER_USERNAME,
                ClientConstants.CLIENT_CONFIGURATION,
                JOptionPane.QUESTION_MESSAGE);
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    ClientConstants.CONNECTION_IS_ESTABLISHED,
                    frame.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    ClientConstants.CLIENT_IS_NOT_CONNECTED,
                    frame.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void refreshMessages() {
        messages.append(controller.getModel().getNewMessage() + "\n");
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();
        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        users.setText(sb.toString());
    }

}
