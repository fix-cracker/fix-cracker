package org.fixt.fixcracker.core.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
//TODO: add JMX: https://www.quickfixj.org/usermanual/2.1.0/usage/jmx.html

public class FIXServer implements Application {
    private static final Logger LOG = LoggerFactory.getLogger(FIXServer.class);
    private final Acceptor acceptor;

    public static void main(String... args) throws ConfigError, FileNotFoundException {
        new FIXServer(args).start();
    }

    public FIXServer(String[] args) throws ConfigError, FileNotFoundException {
        SessionSettings settings = new SessionSettings(new FileInputStream(args[0]));
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        acceptor = new SocketAcceptor
                (this, storeFactory, settings, logFactory, messageFactory);

    }

    private void start() throws ConfigError {
        acceptor.start();
    }

    @Override
    public void onCreate(SessionID sessionID) {
        LOG.info("OnCreate({})", sessionID);

    }

    @Override
    public void onLogon(SessionID sessionID) {
        LOG.info("onLogon({})", sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        LOG.info("onLogout({})", sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        LOG.info("toAdmin({})", sessionID);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {
        LOG.info("fromAdmin({})", sessionID);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) {
        LOG.info("toApp({},{})", sessionID, message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) {
        LOG.info("fromApp({},{})", sessionID, message);
    }
}
