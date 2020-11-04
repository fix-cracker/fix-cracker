package org.fixt.fixcracker.core.server;

import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.core.domain.TagsString;
import org.fixt.fixcracker.core.settings.PreferencesSessionStorage;
import quickfix.ConfigError;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.field.SenderCompID;
import quickfix.field.TargetCompID;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.prefs.BackingStoreException;

public class PreferencesInit {
    public static void main(String[] args) throws FileNotFoundException, ConfigError, BackingStoreException {
        SessionSettings sessionSettings = new SessionSettings(new FileInputStream(args[0]));
        PreferencesSessionStorage storage = new PreferencesSessionStorage();
        List<FIXSession> configured = new ArrayList<>(storage.getAll());
        for (FIXSession session : configured) {
            storage.remove(session);
        }
        Iterator<SessionID> it = sessionSettings.sectionIterator();
        while (it.hasNext()) {
            SessionID id = it.next();
            Properties props = sessionSettings.getSessionProperties(id);
            TagsString tagsString= new TagsString(null);
            tagsString.setTagValue(TargetCompID.FIELD,id.getSenderCompID());
            tagsString.setTagValue(SenderCompID.FIELD,id.getTargetCompID());
            FIXSession session = new FIXSession(
                    id.getTargetCompID() + "->" + id.getSenderCompID(),
                    "localhost", 1234,
                    sessionSettings.getString(id, "BeginString"),
                    Integer.parseInt(props.getProperty("DefaultApplVerID", "7")),
                    30,
                    "Y".equals(props.getProperty("ResetOnLogon", "N")),
                    tagsString.toString()
            );

            storage.add(session);
        }

    }
}
