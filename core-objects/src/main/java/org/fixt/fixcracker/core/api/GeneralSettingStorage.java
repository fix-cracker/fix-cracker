package org.fixt.fixcracker.core.api;

public interface GeneralSettingStorage {
    boolean getBoolean(String name, boolean defaultValue);

    void putBoolean(String name, boolean value) throws Exception;
}
