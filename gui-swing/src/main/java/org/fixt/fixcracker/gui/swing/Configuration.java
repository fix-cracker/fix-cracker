package org.fixt.fixcracker.gui.swing;

import org.fixt.fixcracker.core.api.FIXSessionStorage;
import org.fixt.fixcracker.core.settings.PreferencesSessionStorage;
/*
Here some DI might happen, but we are reluctant to get more dependencies
Lets keep it simple while it possible
 */
public class Configuration {
    public static final FIXSessionStorage FIX_SESSION_STORAGE = new PreferencesSessionStorage();
}
