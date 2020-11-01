package org.fixt.fixcracker.core;

import org.fixt.fixcracker.core.api.FIXConnector;
import org.fixt.fixcracker.core.api.MainWindow;
import org.fixt.fixcracker.core.api.MessageSenderView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    private FIXConnector connector;
    private MainWindow mainWindow;
    private MessageSenderView senderView;


    public void init(
            MainWindow mainWindow,
            MessageSenderView senderView,
            FIXConnector connector
    ){
        this.mainWindow=mainWindow;
        this.senderView=senderView;
        this.connector=connector;
    }

    public void sendMessage() {
        String fixMessage = senderView.getFIXMessage();
        if (fixMessage.length() > 0) {
            try {
                connector.sendMessage(fixMessage);
            } catch (Exception ex) {
                LOG.error("Error while sending message:",ex);
                mainWindow.showErrorMessageBox("Error while sending message",ex);
            }
        }
    }

}
