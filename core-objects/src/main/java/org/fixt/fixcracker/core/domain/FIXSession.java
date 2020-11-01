package org.fixt.fixcracker.core.domain;

import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import java.util.Map;

public class FIXSession {
    public static final String BEGIN_STRING_DEFAULT = "FIXT.1.1";
    public static final int APPL_VER_ID_DEFAULT = 7;
    public static final int HEARTBEAT_INTERVAL_DEFAULT = 30;
    public static final String[] BEGIN_STRING_ARRAY = new String[]{BEGIN_STRING_DEFAULT, "FIX.4.0", "FIX.4.1", "FIX.4.2", "FIX.4.3", "FIX.4.4"};
    public static final Map<Integer, String> APPL_VER_IDS =
            Map.of(0, "FIX2.7", 1, "FIX3.0", 2, "FIX4.0", 3, "FIX4.1", 4, "FIX4.2", 5, "FIX4.3",
                    6, "FIX44", 7, "FIX50", 8, "FIX5.0SP1");

    final String name;
    final String host;
    final int port;
    final String beginString;
    final int defaultApplVerID;
    final int heartBtInt;
    final boolean resetOnLogon;


    private final TagsString loginTags;


    public FIXSession(
            String name, String host, int port,
            String beginString, int defaultApplVerID, int heartBtInt,
            boolean resetOnLogon,
            String loginTags) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.beginString = beginString;
        this.defaultApplVerID = defaultApplVerID;
        this.heartBtInt = heartBtInt;
        this.resetOnLogon = resetOnLogon;
        this.loginTags = new TagsString(loginTags);
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getLoginTags() {
        return loginTags.toString();
    }

    public String getSenderCompID() {
        return loginTags.getTagValue(SenderCompID.FIELD);
    }

    public String getTargetCompID() {
        return loginTags.getTagValue(TargetCompID.FIELD);
    }

    public String getBeginString() {
        return beginString;
    }

    public int getDefaultApplVerID() {
        return defaultApplVerID;
    }

    public int getHeartBtInt() {
        return heartBtInt;
    }

    public boolean isResetOnLogon() {
        return resetOnLogon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FIXSession that = (FIXSession) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "FIXSession{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", beginString='" + beginString + '\'' +
                ", defaultApplVerID=" + defaultApplVerID +
                ", heartBtInt=" + heartBtInt +
                ", loginTags=" + loginTags +
                '}';
    }
}
