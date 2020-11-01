package org.fixt.fixcracker.core.settings;

import org.fixt.fixcracker.core.FIXCrackerConst;
import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.core.api.FIXSessionStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.fixt.fixcracker.core.FIXCrackerConst.SOH_STR;

public class PreferencesSessionStorage implements FIXSessionStorage {
    private static final String PREFERENCES_SESSIONS_ROOT = "Sessions";
    protected static final Preferences prefs = Preferences.userRoot().node(FIXCrackerConst.PREFERENCES_APP_NAME).node(PREFERENCES_SESSIONS_ROOT);

    private static final String SESSION_COUNT = "SessionCount";
    private static final String NAME = "Name_";
    private static final String HOST = "Host_";
    private static final String PORT = "Port_";
    private static final String LOGIN_TAGS = "LoginTags_";
    private static final String BEGIN_STRING = "BeginString_";
    private static final String APPL_VER_ID = "ApplVerID_";
    private static final String HEARTBEAT_INTERVAL = "HeartbeatInterval_";
    private static final String RESET_ON_LOGON = "ResetOnLogon_";


    private final List<FIXSession> list = new ArrayList<>();


    public PreferencesSessionStorage() {
        reload();
    }

    @Override
    public List<FIXSession> getAll() {
        return Collections.unmodifiableList(list);
    }

    private void reload() {
        list.clear();
        int sessionCount = prefs.getInt(SESSION_COUNT, 0);
        for (int i = 0; i < sessionCount; i++) {
            list.add(new FIXSession(
                    prefs.get(NAME + i, "Session" + i),
                    prefs.get(HOST + i, "localhost"),
                    prefs.getInt(PORT + i, 7777),
                    prefs.get(BEGIN_STRING + i, FIXSession.BEGIN_STRING_DEFAULT),
                    prefs.getInt(APPL_VER_ID + i, FIXSession.APPL_VER_ID_DEFAULT),
                    prefs.getInt(HEARTBEAT_INTERVAL + i, FIXSession.HEARTBEAT_INTERVAL_DEFAULT),
                    prefs.getBoolean(RESET_ON_LOGON + i, true),
                    prefs.get(LOGIN_TAGS + i, "").replace("|", SOH_STR)
            ));
        }
    }

    @Override
    public void add(FIXSession session) throws BackingStoreException {
        saveSession(list.size(), session);
        list.add(session);
        prefs.putInt(SESSION_COUNT, list.size());
        prefs.flush();
    }

    private void saveSession(int idx, FIXSession session) {
        prefs.put(NAME + idx, session.getName());
        prefs.put(HOST + idx, session.getHost());
        prefs.putInt(PORT + idx, session.getPort());
        prefs.put(BEGIN_STRING + idx, session.getBeginString());
        prefs.putInt(APPL_VER_ID + idx, session.getDefaultApplVerID());
        prefs.putInt(HEARTBEAT_INTERVAL + idx, session.getHeartBtInt());
        prefs.putBoolean(RESET_ON_LOGON + idx, session.isResetOnLogon());
        prefs.put(LOGIN_TAGS + idx, session.getLoginTags().replace(SOH_STR, "|"));
    }

    @Override
    public void update(FIXSession oldstate, FIXSession newState) throws BackingStoreException {
        int idx = list.indexOf(oldstate);
        if (idx >= 0) {
            saveSession(idx, newState);
            prefs.flush();
        }
    }

    @Override
    public void remove(FIXSession session) throws BackingStoreException {
        list.remove(session);
        for (int i = 0; i < list.size(); i++) {
            saveSession(i, list.get(i));
        }
        prefs.putInt(SESSION_COUNT, list.size());
        prefs.flush();
    }
}
