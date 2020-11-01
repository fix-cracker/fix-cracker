package org.fixt.fixcracker.gui.swing.frames.dialogs;



import org.fixt.fixcracker.core.settings.PreferencesSessionStorage;
import org.junit.Test;

import java.util.prefs.BackingStoreException;


public class PrefsTest {
    @Test
    public void base() throws BackingStoreException {
//        Preferences prefs = Preferences.userRoot().node("Test");
//        int count = prefs.getInt("Count", 0);
//        assertEquals(0,count);
//        prefs.putInt("Count",1);
//        prefs.flush();
//
//        prefs = Preferences.userRoot().node("Test");
//        count = prefs.getInt("Count", 0);
//        assertEquals(1,count);

        PreferencesSessionStorage stg = new PreferencesSessionStorage();
        System.out.println(stg.getAll());
    }
}
