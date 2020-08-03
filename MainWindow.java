import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainWindow is the Main Frame and has a Some SubFrames
 */
public class MainWindow {
    private JFrame mainFrame;
    private RequestListFrame requestListFrame;
    private RequestManageFrame requestManageFrame;
    private ResponsePanel responsePanel;
    private MenuBar menuBar;
    private Loader loader;
    private Controller controller;
    /**
     * Counstructor
     */
    public MainWindow(Controller controller){
        this.controller=controller;

        mainFrame = new JFrame("Insomnio");// make the Frame
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setMinimumSize(new Dimension(1100, 600));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLocation(100, 100);

        JPanel contentPanel = new JPanel(new BorderLayout(3, 3));// set the contentPane
        contentPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        contentPanel.setBackground(new Color(35, 35, 35));

        contentPanel.setFocusable(true);
        KeyHandler keyHandler = new KeyHandler();
        MouseHandler mouseHandler = new MouseHandler();
        contentPanel.addKeyListener(keyHandler);// set KeyListener
        // contentPanel.addMouseListener(mouseHandler);// set Mouse Handler
        contentPanel.requestDefaultFocus();
        mainFrame.setContentPane(contentPanel);

        requestListFrame = new RequestListFrame();
        requestManageFrame = new RequestManageFrame(controller);
        responsePanel = new ResponsePanel();

        // North Panel
        JPanel northPanel = new JPanel(new BorderLayout(0, 0));
        northPanel.setBackground(new Color(35, 35, 35));

        menuBar = new MenuBar(mainFrame, requestListFrame.getPanel(),controller);
        northPanel.add(menuBar.getMainMenu(), BorderLayout.NORTH);

        JLabel title = new JLabel("INSOMNIO (Click here to make Request !)");
        title.addMouseListener(new MouseHandler());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        Color purple = new Color(128, 0, 128);// make the purple Color
        title.setBackground(purple);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(title.getWidth(), title.getHeight() + 30));
        northPanel.add(title, BorderLayout.CENTER);

        requestListFrame.addObserver(requestManageFrame);
        requestListFrame.addObserver(responsePanel);

        mainFrame.add(northPanel, BorderLayout.NORTH);
        mainFrame.add(requestListFrame.getPanel(), BorderLayout.WEST);
        mainFrame.add(requestManageFrame.getPanel(), BorderLayout.CENTER);
        mainFrame.add(responsePanel.getPanel(), BorderLayout.EAST);

        loader = new Loader();
        loader.atStart(this);

    }

    public ResponsePanel getResponsePanel() {
        return responsePanel;
    }

    /**
     * @return the requestListFrame
     */
    public RequestListFrame getRequestListFrame() {
        return requestListFrame;
    }

    /**
     * @return the mainFrame
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * @return the menuBar
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * KeyHandler
     */
    private class KeyHandler extends KeyAdapter {
        private HashSet<Integer> keyEvents;

        /**
         * Counstructor
         */
        public KeyHandler() {
            keyEvents = new HashSet<Integer>();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            // System.out.println(e.getKeyCode());
            /**
             * 78 is code of N 17 is code of Control
             */
            switch (e.getKeyCode()) { // for multi KeyHandeling
                case KeyEvent.VK_N:
                    keyEvents.add(e.getKeyCode());
                    if (keyEvents.contains(17)) {
                        RequestMakeMenu requestMakeMenu = new RequestMakeMenu();
                    }
                    break;
                case KeyEvent.VK_CONTROL:
                    keyEvents.add(e.getKeyCode());
                    if (keyEvents.contains(78)) {
                        RequestMakeMenu requestMakeMenu = new RequestMakeMenu();
                    }
                    break;
                default:
                    keyEvents.clear();
                    break;
            }
        }

    }

    /**
     * MouseHandler
     */
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                RequestMakeMenu requestMakeMenu = new RequestMakeMenu();
            }
        }

    }

    /**
     * RequestMakeMenu is the menu that we can choose the Requests options
     */
    public class RequestMakeMenu {
        private JFrame r_frame; // is the frame window
        private JTextField rTextField;// is the name of the request
        private JComboBox type;// is the type of the request
        private JButton create;// the button

        /**
         * Cousntructor
         */
        public RequestMakeMenu() {
            r_frame = new JFrame("New Request");
            r_frame.setLocationRelativeTo(mainFrame);
            r_frame.setMinimumSize(new Dimension(700, 160));
            r_frame.setLocation(300, 300);
            r_frame.setResizable(false);

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
            mainPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
            mainPanel.addKeyListener(new KeyHandler2());
            mainPanel.setFocusable(true);
            mainPanel.requestDefaultFocus();
            r_frame.setContentPane(mainPanel);

            JLabel name = new JLabel("Name");
            name.setHorizontalAlignment(SwingConstants.LEFT);
            mainPanel.add(name, BorderLayout.NORTH);

            rTextField = new JTextField("MyRequest");
            int width = rTextField.getPreferredSize().width + 500;
            int height = rTextField.getPreferredSize().height - 30;
            rTextField.setPreferredSize(new Dimension(width, height));
            mainPanel.add(rTextField, BorderLayout.WEST);

            String[] types = { "GET", "POST", "PUT", "DELETE" };
            type = new JComboBox(types);
            mainPanel.add(type, BorderLayout.EAST);

            JPanel southPanel = new JPanel(new BorderLayout(20, 20));
            JLabel tip = new JLabel("Tip: Paste ");
            southPanel.add(tip, BorderLayout.WEST);

            create = new JButton("Create");
            create.addActionListener(new ButtonHandler());

            southPanel.add(create, BorderLayout.EAST);

            mainPanel.add(southPanel, BorderLayout.SOUTH);

            r_frame.setVisible(true);
        }

        /**
         * ButtonHandler for making request button
         */
        private class ButtonHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {// for button
                if (e.getSource().equals(create)) {
                    if (!checkExistedRequest(rTextField.getText())) {
                        Request tempRequest = new Request((String) type.getSelectedItem(), rTextField.getText());
                        requestListFrame.addRequest(tempRequest);
                        r_frame.setVisible(false);// hide the Making Menu Frame
                        return;
                    }
                    JOptionPane.showMessageDialog(r_frame, "Already Exist", "!!!!", JOptionPane.ERROR_MESSAGE);

                }
            }
        }

        /**
         * @return true if request with the name already existed
         * @param name
         */
        private boolean checkExistedRequest(String name) {
            ArrayList<Request> tempRequests = requestListFrame.getMyRequests();
            Iterator<Request> it = tempRequests.iterator();
            while (it.hasNext()) {
                Request temp = it.next();
                if (temp.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Key Handler for enter button
         */
        private class KeyHandler2 extends KeyAdapter {
            @Override
            public void keyReleased(KeyEvent e) {// for enter key
                // TODO Auto-generated method stub

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        Request tempRequest = new Request((String) type.getSelectedItem(), rTextField.getText());
                        requestListFrame.addRequest(tempRequest);
                        r_frame.setVisible(false);// hide the Making Menu Frame
                        break;

                }
            }
        }

    }

    /**
     * Set Frame Visible
     */
    public void show() {
        mainFrame.setVisible(true);
    }
}