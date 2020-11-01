package org.fixt.fixcracker.gui.swing.utils;

import javax.swing.*;
import java.util.Map;
import java.util.function.Function;

public class DefaultMapComboBoxModel<K,V> extends DefaultComboBoxModel<MapEntryViewItem<K,V>> {
    public DefaultMapComboBoxModel(Map<K,V> map) {
        super(map.entrySet().stream().map((Function<Map.Entry<K, V>, MapEntryViewItem>) MapEntryViewItem::new).toArray(MapEntryViewItem[]::new));
    }
}
