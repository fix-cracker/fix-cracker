package org.fixt.fixcracker.core.api;


import org.fixt.fixcracker.core.MainController;

public interface MessageSenderView {
    void setMainController(MainController controller);
    String getFIXMessage();
}
