package org.fixt.fixcracker.core.api;

import org.fixt.fixcracker.core.domain.FIXSession;
import quickfix.ConfigError;

public interface FIXConnector {

    void sendMessage(String fixMessage) throws Exception;

    boolean isConnected();

    void disconnect();

    void setMessageMonitor(MessagesListView messageMonitor);

    void connect(FIXSession session) throws ConfigError;
}
