package org.fixt.fixcracker.core.connector;

import org.fixt.fixcracker.core.api.FIXConnector;
import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.core.api.MessagesListView;
import org.fixt.fixcracker.core.domain.TagsString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;

import java.util.concurrent.ForkJoinPool;

public class QuickFIXConnector implements Application, FIXConnector {
    private final Logger LOG = LoggerFactory.getLogger(QuickFIXConnector.class);
    private FIXSession session;
    private volatile SocketInitiator initiator;
    private MessagesListView messageMonitor;
    private TagsString loginTags;


    public QuickFIXConnector() {
    }

    /**
     * Documentation : https://www.quickfixj.org/usermanual/2.1.0/usage/configuration.html
     * http://www.quickfixengine.org/quickfix/doc/html/configuration.html
     *
     * @param session session to connect
     */
    public void connect(FIXSession session) throws ConfigError {
        this.session = session;
        LOG.debug("Connecting to {}", session);
        loginTags = new TagsString(session.getLoginTags());
        SessionSettings sessionSettings = new SessionSettings();
        SessionID sessionID = new SessionID(session.getBeginString(), session.getSenderCompID(), session.getTargetCompID());

        sessionSettings.setString(sessionID, "ConnectionType", "initiator");
        sessionSettings.setString(sessionID, "SocketConnectHost", session.getHost());
        sessionSettings.setLong(sessionID, "SocketConnectPort", session.getPort());
        sessionSettings.setString(sessionID, "NonStopSession", "Y");
        sessionSettings.setLong(sessionID, "DefaultApplVerID", session.getDefaultApplVerID());//required for FIXT.1.1
        sessionSettings.setLong(sessionID, "HeartBtInt", session.getHeartBtInt());//required for initiator
//        sessionSettings.setLong(sessionID, "ReconnectInterval", 0);
        sessionSettings.setString(sessionID, "ContinueInitializationOnError", "N");
        sessionSettings.setString(sessionID, "EnableNextExpectedMsgSeqNum", "Y");

        sessionSettings.setString(sessionID, "ResetOnLogon", session.isResetOnLogon() ? "Y" : "N");


        MessageStoreFactory storeFactory = new MemoryStoreFactory();
        LogFactory logFactory = new CompositeLogFactory(new LogFactory[]{
                new SLF4JLogFactory(sessionSettings),
                new SessionErrorInterceptorFactory(this)
        });

        MessageFactory messageFactory = new DefaultMessageFactory();
        SocketInitiator.Builder builder = SocketInitiator.newBuilder();
        initiator = builder.withApplication(this)
                .withMessageStoreFactory(storeFactory)
                .withSettings(sessionSettings)
                .withLogFactory(logFactory)
                .withMessageFactory(messageFactory)
                .build();

        initiator.start();

    }

    @Override
    public void onCreate(SessionID sessionID) {
        LOG.info("OnCreate");
    }

    @Override
    public void onLogon(SessionID sessionID) {
        LOG.info("onLogon");
    }

    @Override
    public void onLogout(SessionID sessionID) {
        LOG.info("onLogout");
        if(!disconnectIsInProgress){
            disconnect();
        }
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        LOG.info("toAdmin");
        monitor(false, message);
    }

    private void monitor(boolean incoming, Message message) {
        if (messageMonitor != null) {
            messageMonitor.onMessage(incoming, message);
        }
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID){
        LOG.debug("fromAdmin");
        monitor(true, message);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) {
        LOG.debug("toApp");
        monitor(false, message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID){
        LOG.debug("fromApp");
        monitor(true, message);
    }

    @Override
    public void setMessageMonitor(MessagesListView messageMonitor) {
        this.messageMonitor = messageMonitor;
    }

    private volatile boolean disconnectIsInProgress;

    @Override
    public void disconnect() {
        if (initiator != null) {
            disconnectIsInProgress = true;
            try {
                initiator.stop(!initiator.isLoggedOn());
            } finally {
                disconnectIsInProgress = false;
            }
        }
    }

    public FIXSession getSession() {
        return session;
    }

    @Override
    public void sendMessage(String fixMessage) throws InvalidMessage, SessionNotFound {
        Message msg = new Message(fixMessage, false);
        Message.Header header = msg.getHeader();
        header.setField(new BeginString(session.getBeginString()));
        header.setField(new SenderCompID(session.getSenderCompID()));
        if (loginTags.getTagValue(SenderSubID.FIELD) != null) {
            header.setField(new SenderSubID(loginTags.getTagValue(SenderSubID.FIELD)));
        }
        if (loginTags.getTagValue(SenderLocationID.FIELD) != null) {
            header.setField(new SenderLocationID(loginTags.getTagValue(SenderLocationID.FIELD)));
        }
        header.setField(new TargetCompID(session.getTargetCompID()));
        if (loginTags.getTagValue(TargetSubID.FIELD) != null) {
            header.setField(new TargetSubID(loginTags.getTagValue(TargetSubID.FIELD)));
        }
        if (loginTags.getTagValue(TargetLocationID.FIELD) != null) {
            header.setField(new TargetLocationID(loginTags.getTagValue(TargetLocationID.FIELD)));
        }
        Session.sendToTarget(msg);
    }

    @Override
    public boolean isConnected() {
        return initiator != null && initiator.isLoggedOn();
    }


    public void onSessionError(String error) {
        if(!disconnectIsInProgress) {
            ForkJoinPool.commonPool().submit(this::disconnect);
        }
        messageMonitor.onSessionError(error);
    }
}
