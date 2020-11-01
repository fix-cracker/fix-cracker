package org.fixt.fixcracker.core.connector;

import quickfix.Log;
import quickfix.LogFactory;
import quickfix.SessionID;

public class SessionErrorInterceptorFactory implements LogFactory {
    private final QuickFIXConnector connector;

    public SessionErrorInterceptorFactory(QuickFIXConnector connector) {
        this.connector = connector;
    }

    @Override
    public Log create(SessionID sessionID) {
        return new SessionErrorInterceptor();
    }

    private class SessionErrorInterceptor implements Log {

        @Override
        public void clear() {

        }

        @Override
        public void onIncoming(String s) {

        }

        @Override
        public void onOutgoing(String s) {

        }

        @Override
        public void onEvent(String s) {

        }

        @Override
        public void onErrorEvent(String s) {
            connector.onSessionError(s);
        }
    }
}
