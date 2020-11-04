package org.fixt.fixcracker.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Images {
    private static final Logger LOG = LoggerFactory.getLogger(Images.class);
    private static final Map<String, String> actionToPathMap = new HashMap<>();

    public static final String STOP = "stop";

    public static final String APP_ICON = "app_icon";
    private static final String prefix32 = "/org/freedesktop/tango/32x32/";
    private static final String prefix16 = "/org/freedesktop/tango/16x16/";

    public static final String ABOUT = "about";
    public static final String START = "start";
    public static final String GO_BOTTOM = "go-bottom";
    public static final String EDIT = "edit";
    public static final String EDIT_PASTE = "edit-paste";
    public static final String SEND = "mail-send-receive";
    public static final String CLEAR = "clear";
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String GO_DOWN = "go-down";
    public static final String GO_UP = "go-up";
    public static final String OK = "ok";
    public static final String CANCEL = "cancel";
    public static final String CLOSE = "close";
    public static final String OPEN_FILE = "open-file";
    public static final String SAVE_FILE = "save-file";
    public static final String DIALOG_ERROR = "dialog-error";

    static {
        actionToPathMap.put(APP_ICON, prefix32 + "apps/utilities-system-monitor.png");
        actionToPathMap.put(OK, prefix16 + "actions/go-down.png");
        actionToPathMap.put(CANCEL, prefix16 + "emblems/emblem-unreadable.png");
        actionToPathMap.put(DIALOG_ERROR, prefix16 + "status/dialog-error.png");
        actionToPathMap.put(ABOUT, "about-icon.png");
        actionToPathMap.put(START, prefix16 + "actions/media-playback-start.png");
        actionToPathMap.put(STOP, prefix16 + "actions/media-playback-stop.png");
        actionToPathMap.put(GO_BOTTOM, prefix16 + "actions/go-bottom.png");
        actionToPathMap.put(EDIT, prefix16 + "apps/accessories-text-editor.png");
        actionToPathMap.put(EDIT_PASTE, prefix16 + "actions/edit-paste.png");
        actionToPathMap.put(SEND, prefix16 + "actions/mail-send-receive.png");
        actionToPathMap.put(CLEAR, prefix16 + "actions/edit-clear.png");
        actionToPathMap.put(ADD, prefix16 + "actions/list-add.png");
        actionToPathMap.put(REMOVE, prefix16 + "actions/list-remove.png");
        actionToPathMap.put(GO_DOWN, prefix16 + "actions/go-down.png");
        actionToPathMap.put(GO_UP, prefix16 + "actions/go-up.png");
        actionToPathMap.put(OPEN_FILE, prefix16 + "actions/document-open.png");
        actionToPathMap.put(SAVE_FILE, prefix16 + "devices/media-floppy.png");
    }

    private static final Map<String, ImageIcon> actionToIconMap = new HashMap<>();

    public static ImageIcon get(String actionName) {
        ImageIcon icon = actionToIconMap.get(actionName);
        if (icon != null) {
            return icon;
        }
        String path = actionToPathMap.get(actionName);
        if (path == null) {
            LOG.error("Could not find path for action: {}", actionName);
            return null;
        }
        ClassLoader cl = Images.class.getClassLoader();

        java.net.URL url = Images.class.getResource(path);
        if(url==null){
            url = cl.getResource(path);
        }
        if (url != null) {
            icon = new ImageIcon(url);
            actionToIconMap.put(actionName, icon);
            return icon;
        }
        LOG.error("Could not find the resource: {}", path);
        return null;
    }

    public static Image getImage(String iconName) {
        ImageIcon icon = get(iconName);
        if (icon != null) {
            BufferedImage bi = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            try {
                icon.paintIcon(null, g, 0, 0);
            } finally {
                g.dispose();
            }
            return bi;
        }
        return null;

    }
}
