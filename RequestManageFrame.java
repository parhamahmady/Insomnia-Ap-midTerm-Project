import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * RequestManageFrame is the maneger panel for a request
 */
public class RequestManageFrame implements Observer {
    private JPanel panel;// is the main panel
    private Request request;// is the request that is getting managed
    private JLabel name;// name of the request
    private JTextField url;// is the url for request
    private JComboBox type;// is the type of the request
    private JButton send;// is the send Button
    private JButton save;// is the save button
    private ContexPanel headerPanel;
    private BodyPanel bodyPanel;
    private RequestManageFrame own;
    private Controller controller;

    /**
     * Counstructor
     */
    public RequestManageFrame(Controller controller){
        this.controller = controller;

        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(35, 35, 35));
        // panel.setPreferredSize(new Dimension(300, panel.getHeight()));
        panel.setOpaque(true);

        // panel.setFocusable(false);

        //// North Panel

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        northPanel.setBackground(Color.WHITE);
        northPanel.setOpaque(true);
        northPanel.setFocusable(false);

        name = new JLabel();
        c.ipadx = 30;
        c.gridx = 1;
        c.gridy = 0;
        // c.insets = new Insets(5, 5, 5, 5);
        northPanel.add(name, c);

        url = new JTextField("Type The Url");
        url.setHorizontalAlignment(SwingConstants.LEFT);
        url.setVisible(false);
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 200;
        c.ipady = 5;
        northPanel.add(url, c);

        String[] types = { "GET", "POST", "PUT", "DELETE" };
        type = new JComboBox(types);
        type.setFocusable(false);
        type.setVisible(false);
        c.gridx = 0;
        c.ipadx = 0;
        c.ipady = 0;
        northPanel.add(type, c);

        send = new JButton("Send");
        send.addActionListener(new ButtonHandler());
        c.gridx = 3;
        send.setVisible(false);
        northPanel.add(send, c);

        save = new JButton("Save");
        save.addActionListener(new ButtonHandler());
        save.setVisible(false);
        c.gridx = 2;
        northPanel.add(save, c);

        /////// South Panel
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(new Color(35, 35, 35));

        headerPanel = new HeaderPanel();
        tabbedPane.addTab("Header", headerPanel.getpanel());

        bodyPanel = new BodyPanel();
        tabbedPane.add("Body", bodyPanel.getPanel());

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        own=this;
    }

    /**
     * update the request information
     */
    private void updateRequest() {
        name.setText(request.getName());
        url.setVisible(true);
        type.setVisible(true);
        send.setVisible(true);
        save.setVisible(true);
        type.setSelectedItem(request.getType());
        if (request.getUrl()!=null) {
            url.setText(request.getUrl().getHost());            
        }
        url.setVisible(true);
        headerPanel.rebuild(request.getMyheader());
        bodyPanel.update(request);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        // TODO Auto-generated method stub
        if (arg1 == null) {
            name.setText("");
            url.setVisible(false);
            type.setVisible(false);
            send.setVisible(false);
            save.setVisible(false);
            url.setVisible(false);
            headerPanel.rebuild(null);
            bodyPanel.update(null);
            panel.setVisible(false);
            return;
        }
        panel.setVisible(true);
        request = (Request) arg1;
        updateRequest();
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    public JComboBox getType() {
        return type;
    }

    public JTextField getUrl() {
        return url;
    }

    public BodyPanel getBodyPanel() {
        return bodyPanel;
    }

    public ContexPanel getHeaderPanel() {
        return headerPanel;
    }

    /**
     * ButtonHandler for send and save
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(save)) {
                // System.out.println("saved");
                controller.save(request,own);
            }
            if (e.getSource().equals(send)) {
                controller.send(request, own);
                
            }
        }

    }
}