package org.fixt.fixcracker.core.api;

import quickfix.Message;

public interface MessagesListView {
    void onMessage(boolean incoming, Message msg);

    void clearView();

    void onSessionError(String error);

    void suppressHB(boolean suppressHB);

    void keepLastSelected(boolean selected);
}
