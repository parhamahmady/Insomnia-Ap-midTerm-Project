import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MenuBar is the menu bar in the top of the app
 */
public class MenuBar {

    private JMenuBar mainMenu;
    private JFrame mainFrame;// is the mainWindow Frame
    private JPanel sidePanel;
    private ApplicationMenu applicationMenu;
    private ViewMenu viewMenu;
    private HelpMenu helpMenu;
    private boolean fRedirect;
    private Controller controller;//for MVC

    public MenuBar(JFrame input, JPanel input2,Controller controller){
        controller=new Controller();

        mainFrame = input;
        sidePanel = input2;

        mainMenu = new JMenuBar();
        mainMenu.setBackground(new Color(35, 35, 35));
        mainMenu.setForeground(Color.WHITE);
        mainMenu.setFocusable(true);
        mainMenu.requestDefaultFocus();

        applicationMenu = new ApplicationMenu();
        mainMenu.add(applicationMenu.getApplicationMenu());

        viewMenu = new ViewMenu();
        mainMenu.add(viewMenu.getViewMenu());

        helpMenu = new HelpMenu();
        mainMenu.add(helpMenu.getHelpMenu());

        this.controller=controller;//set the Controler
    }

    /**
     * @return the mainMenu
     */
    public JMenuBar getMainMenu() {
        return mainMenu;
    }

    /**
     * @param fRedirect the fRedirect to set
     */
    public void setfRedirect(boolean fRedirect) {
        this.fRedirect = fRedirect;
        controller.setFollowRedDirect(fRedirect);
    }

    public boolean getFRedirect() {
        return fRedirect;
    }

    /**
     * AppicationMenu
     */
    private class ApplicationMenu implements ActionListener {
        private JMenu menu;
        private JMenuItem options;
        private JMenuItem exit;

        /**
         * Counstructor
         */
        public ApplicationMenu() {
            menu = new JMenu("Application");
            menu.setForeground(Color.WHITE);
            menu.setMnemonic(KeyEvent.VK_A);

            options = new JMenuItem("Options");
            KeyStroke ctrlOKeyStroke = KeyStroke.getKeyStroke("control O");
            options.setAccelerator(ctrlOKeyStroke);
            options.addActionListener(this);

            exit = new JMenuItem("Exit");
            KeyStroke ctrlEKeyStroke = KeyStroke.getKeyStroke("control E");
            exit.setAccelerator(ctrlEKeyStroke);
            exit.addActionListener(this);

            menu.add(options);
            menu.add(exit);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(options)) {
                OptionFrame optionFrame = new OptionFrame();
            }
            if (e.getSource().equals(exit)) {
                Saver saver = new Saver();
                saver.atStart(mainFrame.getDefaultCloseOperation(), fRedirect);
                if (mainFrame.getDefaultCloseOperation() == 3) {// terminate
                    System.exit(0);

                } else {

                    if (SystemTray.isSupported()) {

                        SystemTray tray = SystemTray.getSystemTray();

                        Image image = Toolkit.getDefaultToolkit().getImage("./Files/p.jpg");
                        PopupMenu popup = new PopupMenu();
                        MenuItem show = new MenuItem("Show");
                        ActionListener showListener = new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // System.out.println("Exiting....");
                                // System.exit(0);
                                mainFrame.setVisible(true);
                                mainFrame.setExtendedState(JFrame.NORMAL);
                            }
                        };
                        show.addActionListener(showListener);
                        popup.add(show);
                        MenuItem exit = new MenuItem("Exit");
                        ActionListener exitListener = new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                System.exit(0);

                            }
                        };
                        exit.addActionListener(exitListener);
                        popup.add(exit);
                        TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
                        trayIcon.setImageAutoSize(true);
                        mainFrame.addWindowStateListener(new WindowStateListener() {
                            public void windowStateChanged(WindowEvent e) {
                                if (e.getNewState() == JFrame.ICONIFIED) {
                                    try {
                                        tray.add(trayIcon);
                                        mainFrame.setVisible(false);

                                    } catch (AWTException ex) {
                                        System.out.println("unable to add to tray");
                                    }
                                }
                            }
                        });
                    } else {
                        System.out.println("System tray not suported");
                    }
                    mainFrame.setState(JFrame.ICONIFIED); // To minimize a frame
                    mainFrame.setVisible(false);
                }

            }
        }

        /**
         * @return the applicationMenu
         */
        public JMenu getApplicationMenu() {
            return menu;
        }

        /**
         * OptionFrame
         */
        private class OptionFrame implements ActionListener {
            private JFrame frame;
            private JButton close;
            private JCheckBox followRedirect;
            private JCheckBox hideOnTray;

            public OptionFrame() {
                frame = new JFrame("Options");
                frame.setLocationRelativeTo(mainFrame);
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                frame.setResizable(false);
                frame.setSize(new Dimension(300, 300));

                JPanel contenetPane = new JPanel(new GridLayout(3, 1));
                frame.setContentPane(contenetPane);

                followRedirect = new JCheckBox("Follow Redirect");
                if (fRedirect)
                    followRedirect.setSelected(true);
                frame.add(followRedirect);

                hideOnTray = new JCheckBox("Hide On Tray");
                if (mainFrame.getDefaultCloseOperation() == 1)
                    hideOnTray.setSelected(true);
                frame.add(hideOnTray);

                close = new JButton("Close");

                close.addActionListener(this);
                frame.add(close);

                frame.setVisible(true);

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(close)) {
                    // set Close options
                    if (hideOnTray.isSelected()) {
                        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    } else {
                        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                    // Set Follow Redirect and 
                    fRedirect = followRedirect.isSelected();
                    if (fRedirect) {
                        controller.setFollowRedDirect(fRedirect);
                    }
                    frame.setVisible(false);
                }

            }
        }
    }

    /**
     * ViewMenu
     */
    private class ViewMenu implements ActionListener {
        private JMenu viewMenu;
        private JMenuItem fullScreen;
        private JMenuItem sideBar;

        /**
         * Counstructor
         */
        public ViewMenu() {
            viewMenu = new JMenu("View");
            viewMenu.setForeground(Color.WHITE);
            viewMenu.setMnemonic(KeyEvent.VK_V);

            fullScreen = new JMenuItem("Toggle Full Screen");
            KeyStroke ctrlFKeyStroke = KeyStroke.getKeyStroke("control F");
            fullScreen.setAccelerator(ctrlFKeyStroke);
            fullScreen.addActionListener(this);

            sideBar = new JMenuItem("Toggle SideBar");
            KeyStroke ctrlSKeyStroke = KeyStroke.getKeyStroke("control S");
            sideBar.setAccelerator(ctrlSKeyStroke);
            sideBar.addActionListener(this);

            viewMenu.add(fullScreen);
            viewMenu.add(sideBar);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(fullScreen)) {
                // Set Frame Size (Full Screen or Not)
                if (mainFrame.getExtendedState() == 0) {
                    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    mainFrame.setExtendedState(0);
                }
            }

            if (e.getSource().equals(sideBar)) {

                if (sidePanel.isVisible()) {
                    sidePanel.setVisible(false);
                } else {

                    sidePanel.setVisible(true);
                    mainFrame.repaint();

                }
            }
        }

        /**
         * @return the viewMenu
         */
        public JMenu getViewMenu() {
            return viewMenu;
        }
    }

    /**
     * HelpMenu
     */
    private class HelpMenu implements ActionListener {
        private JMenu helpMenu;
        private JMenuItem about;
        private JMenuItem help;

        /**
         * Counstructor
         */
        public HelpMenu() {

            helpMenu = new JMenu("Help");
            helpMenu.setForeground(Color.WHITE);
            helpMenu.setMnemonic(KeyEvent.VK_H);

            about = new JMenuItem("About");
            KeyStroke ctrlTKeyStroke = KeyStroke.getKeyStroke("control T");
            about.setAccelerator(ctrlTKeyStroke);
            about.addActionListener(this);
            helpMenu.add(about);

            help = new JMenuItem("Help");
            KeyStroke ctrlHKeyStroke = KeyStroke.getKeyStroke("control H");
            help.setAccelerator(ctrlHKeyStroke);
            help.addActionListener(this);
            helpMenu.add(help);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(about)) {
                AboutFrame aboutFrame = new AboutFrame();
            }
            if (e.getSource().equals(help)) {
                HelpFrame helpFrame = new HelpFrame();
            }
        }

        /**
         * @return the helpMenu
         */
        public JMenu getHelpMenu() {
            return helpMenu;
        }

        /**
         * HelpFrame
         */
        private class HelpFrame implements ActionListener {
            private JFrame frame;
            private JTextArea text;
            private JButton close;

            public HelpFrame() {
                frame = new JFrame("Help");
                frame.setLocationRelativeTo(mainFrame);
                frame.setResizable(false);
                frame.setSize(new Dimension(300, 300));

                JPanel pane = new JPanel(new BorderLayout(10, 10));
                frame.setContentPane(pane);

                text = new JTextArea();
                text.setText(controller.help());
                text.setFocusable(false);

                frame.add(text, BorderLayout.CENTER);
                close = new JButton("Close");
                close.addActionListener(this);
                frame.add(close, BorderLayout.SOUTH);

                frame.setVisible(true);

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(close)) {
                    frame.setVisible(false);
                }

            }

            /**
             * @param text the text to set
             */
            public void setText(JTextArea text) {
                this.text = text;
            }
        }

        /**
         * HelpFrame
         */
        private class AboutFrame implements ActionListener {
            private JFrame frame;
            private JTextArea text;
            private JButton close;

            public AboutFrame() {
                frame = new JFrame("About");
                frame.setLocationRelativeTo(mainFrame);
                frame.setResizable(false);
                frame.setSize(new Dimension(300, 150));
                JPanel pane = new JPanel(new BorderLayout(10, 10));
                frame.setContentPane(pane);
                String t = "Insomnia\nMade By Parham Ahmadi (9831071)\nContact me: Parhamahmady998@gmail.com";

                text = new JTextArea(t);

                text.setFocusable(false);
                frame.add(text, BorderLayout.CENTER);

                close = new JButton("Close");
                close.addActionListener(this);

                frame.add(close, BorderLayout.SOUTH);

                frame.setVisible(true);

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(close)) {
                    frame.setVisible(false);
                }

            }

            /**
             * @param text the text to set
             */
            public void setText(JTextArea text) {
                this.text = text;
            }
        }
    }
}
