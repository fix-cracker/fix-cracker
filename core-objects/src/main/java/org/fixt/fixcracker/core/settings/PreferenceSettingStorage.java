package org.fixt.fixcracker.core.settings;

import org.fixt.fixcracker.core.FIXCrackerConst;
import org.fixt.fixcracker.core.api.GeneralSettingStorage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenceSettingStorage implements GeneralSettingStorage {
    private static final String PREFERENCES_SESSIONS_ROOT = "General";
    protected static final Preferences prefs = Preferences.userRoot().node(FIXCrackerConst.PREFERENCES_APP_NAME).node(PREFERENCES_SESSIONS_ROOT);

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return prefs.getBoolean(name, defaultValue);
    }

    @Override
    public void putBoolean(String name, boolean value) throws BackingStoreException {
        prefs.putBoolean(name,value);
        prefs.flush();
    }
}
