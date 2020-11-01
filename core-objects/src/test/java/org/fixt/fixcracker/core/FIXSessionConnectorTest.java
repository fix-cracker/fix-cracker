package org.fixt.fixcracker.core;

import org.fixt.fixcracker.core.connector.QuickFIXConnector;
import org.fixt.fixcracker.core.domain.FIXSession;
import quickfix.ConfigError;

import java.util.concurrent.TimeUnit;

public class FIXSessionConnectorTest {

    private static QuickFIXConnector connector;

    public static void main(String... args) throws ConfigError, InterruptedException {
        FIXSession session = new FIXSession("test", "localhost", 1234, FIXSession.BEGIN_STRING_DEFAULT, FIXSession.APPL_VER_ID_DEFAULT,
                FIXSession.HEARTBEAT_INTERVAL_DEFAULT,
                true,
                "49=FIX_CRACKER" + FIXCrackerConst.SOH_STR + "FIXSERVER");
        connector = new QuickFIXConnector();
        connector.connect(session);

        TimeUnit.SECONDS.sleep(20);
    }
}
