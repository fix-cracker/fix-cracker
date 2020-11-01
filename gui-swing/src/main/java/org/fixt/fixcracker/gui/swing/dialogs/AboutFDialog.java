package org.fixt.fixcracker.gui.swing.dialogs;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.fixt.fixcracker.core.FIXCrackerConst;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.IDialogFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class AboutFDialog extends JPanel implements IDialogFrame {
    private static final Logger LOG = LoggerFactory.getLogger(AboutFDialog.class);

    public AboutFDialog() {
        super(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(FIXCrackerConst.PREFERENCES_APP_NAME, createMainTab());
        tabs.addTab("License", createLicenseTab());
        add(tabs, BorderLayout.CENTER);

    }


    private JPanel createLicenseTab() {
        JPanel pnl = new JPanel(new BorderLayout());
        JEditorPane view = new JEditorPane();
        view.setContentType("text/html");
        view.setEditable(false);
        try {
            view.setPage(AboutFDialog.class.getClassLoader().getResource("LICENSE"));
        } catch (IOException e) {
            LOG.error("error", e);
            view.setText("LICENSE file is not found");
        }
        pnl.add(new JScrollPane(view), BorderLayout.CENTER);
        return pnl;
    }


    private JPanel createMainTab() {
        JPanel pnl = new JPanel();
        BoxLayout layout = new BoxLayout(pnl, BoxLayout.Y_AXIS);
        pnl.setLayout(layout);
        try {
            final BufferedImage myLogo = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png")));
            pnl.add(new JLabel(new ImageIcon(myLogo)), BorderLayout.NORTH);
        } catch (IOException e) {
            LOG.error("logo.png is not found");
        }
        JLabel versionLabel = new JLabel(getVersion());
        versionLabel.setFont(new Font(versionLabel.getFont().getName(), Font.PLAIN, 32));
        pnl.add(versionLabel);
        JEditorPane view = new JEditorPane();
        view.setContentType("text/html");
        view.setEditable(false);
        view.addHyperlinkListener(new LinkListener());
        try {
            view.setPage(AboutFDialog.class.getClassLoader().getResource("about.html"));
        } catch (IOException e) {
            LOG.error("error", e);
            view.setText("about.html is not found");
        }

        pnl.add(new JScrollPane(view));
        return pnl;
    }

    private String getVersion() {
        try (FileReader freader = new FileReader("pom.xml")) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(freader);
            return model.getVersion();
        } catch (Exception ex) {
            LOG.error("Error while getting the version", ex);
            return "Unknown";
        }
    }

    @Override
    public void init(FramedDialog dlg) {
        dlg.getBtnCancel().setVisible(false);
        dlg.setMinimumSize(new Dimension(500, 500));
    }

    @Override
    public String getValidationMessage() {
        return null;
    }

    @Override
    public boolean performDialogAction(FramedDialog dlg) {
        return true;
    }

    @Override
    public Frame getOwnerFrame() {
        return null;
    }


    static class LinkListener implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent he) {
            if (!Desktop.isDesktopSupported() || he.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
                return;
            try {
                Desktop.getDesktop().browse(he.getURL().toURI());
            } catch (IOException | URISyntaxException e) {
                LOG.error("error", e);
            }
        }
    }
}
