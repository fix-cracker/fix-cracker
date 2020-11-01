package org.fixt.fixcracker.core.settings;

import org.fixt.fixcracker.core.FIXCrackerConst;
import org.fixt.fixcracker.core.api.MessageTemplateStorage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesTemplateStorage implements MessageTemplateStorage {
    private static final String PREFERENCES_TEMPLATES_ROOT = "Templates";
    protected static final Preferences prefs = Preferences.userRoot().node(FIXCrackerConst.PREFERENCES_APP_NAME).node(PREFERENCES_TEMPLATES_ROOT);

    @Override
    public String getMessageTemplate(String protocolName, String templateName) {
        return decodeString(prefs.node(protocolName).get(templateName, ""));
    }

    @Override
    public void putMessageTemplate(String protocolName, String templateName, String messageString) throws BackingStoreException {
        prefs.node(protocolName).put(templateName, encodeString(messageString));
        prefs.flush();
    }

    // SOH symbol is not supported by Preferences class
    private String encodeString(String str) {
        return str.replace("|", "%pipe%").replace(FIXCrackerConst.SOH_STR, "|");
    }

    private String decodeString(String str) {
        return str.replace("|", FIXCrackerConst.SOH_STR).replace("%pipe%", "|");
    }


    @Override
    public void removeMessageTemplate(String protocolName, String templateName) {
        prefs.node(protocolName).remove(templateName);
    }

    @Override
    public String[] getMessageTemplateArray(String protocolName) throws BackingStoreException {
        return prefs.node(protocolName).keys();
    }


}
