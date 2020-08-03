
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * RequestListFrame
 */
public class RequestListFrame {
    private JPanel panel;// is the main panel
    private ArrayList<Request> mRequests;
    private ArrayList<RequestPanel> mRequestPanels;
    private JScrollPane scrollPane;
    private JPanel itemPanel;// is the requestItems' Panel
    private StatusObservable responseObservable;// is the resopnse panel updater

    /**
     * Counstructor
     */
    public RequestListFrame() {
        responseObservable = new StatusObservable();
        mRequests = new ArrayList<Request>(100);
        mRequestPanels = new ArrayList<RequestPanel>();

        panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(new Color(40, 40, 40));
        panel.setPreferredSize(new Dimension(250, panel.getHeight()));
        panel.setOpaque(true);

        itemPanel = new JPanel();
        itemPanel.setPreferredSize(new Dimension(250, 530));
        itemPanel.setBackground(new Color(40, 40, 40));

        scrollPane = new JScrollPane(itemPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, 251, 530);

        panel.add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * @return the myRequests
     */
    public ArrayList<Request> getMyRequests() {
        return mRequests;
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Add Request to My Requests
     */
    public void addRequest(Request temp) {
        mRequests.add(temp);
        // System.out.println(temp.getName());
        RequestPanel tRequestPanel = new RequestPanel(temp.getName(), temp.getType());
        itemPanel.setPreferredSize(new Dimension(250, mRequests.size() * 30 + 530));// resize the item panel
        itemPanel.add(tRequestPanel.getPanel());
        mRequestPanels.add(tRequestPanel);
        itemPanel.validate();
        scrollPane.validate();
        panel.validate();

    }

    /**
     * RequestPanel is the panel that shows every requests name and type and link to
     * update other MainWindows' Panles
     */
    private class RequestPanel extends JPanel {
        private JPanel panel;
        private String t_name;// is the name of the request

        /**
         * 
         * @param r_name is the name of the Request
         * @param r_type is the Type of the Request
         */
        public RequestPanel(String r_name, String r_type) {

            t_name = r_name;

            panel = new JPanel(new BorderLayout(20, 20));
            panel.setBackground(new Color(90, 90, 90));
            panel.setPreferredSize(new Dimension(195, 20));
            MouseHandler mouseHandler = new MouseHandler();
            panel.addMouseListener(mouseHandler);

            JLabel name = new JLabel(r_name);// add name
            panel.add(name, BorderLayout.CENTER);
            name.setBackground(new Color(90, 90, 90));
            name.setForeground(Color.WHITE);
            name.setHorizontalAlignment(SwingConstants.LEFT);
            name.setOpaque(true);

            JLabel type = new JLabel(r_type);// add type
            panel.add(type, BorderLayout.WEST);
            type.setForeground(Color.GREEN);
            type.setBackground(new Color(90, 90, 90));
            type.setHorizontalAlignment(SwingConstants.CENTER);
            type.setOpaque(true);

            updateObservers(t_name);
        }

        /**
         * @return the name
         */
        public String getName() {
            return t_name;
        }

        /**
         * @return the panel
         */
        public JPanel getPanel() {
            return panel;
        }

        /**
         * MouseHandler
         */
        private class MouseHandler extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    updateObservers(t_name);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    delete(t_name);
                }
            }

        }

    }

    private void delete(String name) {
        Iterator<RequestPanel> it = mRequestPanels.iterator();
        while (it.hasNext()) {
            RequestPanel temp=it.next();
            if (temp.getName().equals(name)) {
                itemPanel.remove(temp.getPanel());
                itemPanel.revalidate();
                itemPanel.repaint();
                it.remove();
            }
        }
        Iterator<Request> it2=mRequests.iterator();
        while (it2.hasNext()) {
            Request temp=it2.next();
            if (temp.getName().equals(name)) {
                it2.remove();
            }
        }
        if (mRequests.size()==0) {
            updateObservers(null);
            return;
        }
        updateObservers(mRequestPanels.get(0).getName());
    }

    /**
     * Iterate throw requests and update observers with selected request
     */
    private void updateObservers(String name) {
        if (name == null) {
            responseObservable.notifyObservers(null);
        }
        for (Request temp : mRequests) {
            if (temp.getName().equals(name)) {
                responseObservable.notifyObservers(temp);
                return;
            }
        }
    }

    public void addObserver(Observer obs) {
        responseObservable.addObserver(obs);
    }

    private class StatusObservable extends Observable {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    }
}
