package org.fixt.fixcracker.gui.swing.utils;

import java.util.Map;

public class MapEntryViewItem<K,V> {
    private Map.Entry<K,V> entry;

    public MapEntryViewItem(Map.Entry<K,V> entry) {
        this.entry = entry;
    }

    public Map.Entry<K,V> getEntry() {
        return entry;
    }

    @Override
    public String toString() {
        return entry.getValue().toString();
    }
}
