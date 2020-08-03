import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ResponsePanel
 */
public class ResponsePanel implements Observer {

    private JPanel panel;
    private JPanel northPanel;
    private JLabel status;
    private JLabel time;
    private JButton copy;
    private JTabbedPane tabbedPane;
    private MessageHeaderPanel headerPanel;
    private MessageBodyPanel bodyPanel;
    /**
     * Counstructor
     */
    public ResponsePanel() {
        panel = new JPanel(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(410, panel.getHeight()));
        panel.setBackground(new Color(45, 45, 45));
        panel.setOpaque(true);
        panel.setFocusable(false);
        // North Panel

        northPanel = new JPanel(new GridBagLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.setOpaque(true);

        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 25;

        status = new JLabel("Status");
        status.setBackground(Color.GREEN);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setOpaque(true);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 15;
        c.insets = new Insets(5, 5, 5, 5);

        northPanel.add(status, c);

        time = new JLabel("Time");
        time.setBackground(Color.gray);
        time.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 1;
        time.setOpaque(true);
        northPanel.add(time, c);

        copy = new JButton("Copy to ClipBoard");
        copy.setBackground(new Color(45, 45, 45));
        copy.setForeground(Color.WHITE);
        copy.addActionListener(new ButtonHandler());
        c.gridx = 2;
        c.ipadx = 20;
        c.ipady = 15;
        northPanel.add(copy, c);

        northPanel.setVisible(false);

        // SouthPanel

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setVisible(false);

        headerPanel=new MessageHeaderPanel();
        tabbedPane.add("Header",headerPanel.getPanel());

        bodyPanel=new MessageBodyPanel();
        tabbedPane.add("Body",bodyPanel.getPanel());

        tabbedPane.setSelectedComponent(bodyPanel.getPanel());

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        if (arg1 == null) {
            panel.setVisible(false);
            return;
        }
        panel.setVisible(true);
        Request temp = (Request) arg1;
        Response temp2 = temp.getMyResponse();
        updatePanel(temp2);
    }

    private void updatePanel(Response response) {
        // set status
        String temp = response.getStatusCode()+" " + response.getStatusMessage();
        status.setText(temp);
        northPanel.revalidate();
        northPanel.repaint();
        // set time
        time.setText(String.format("%ds", response.getTime()));
        northPanel.revalidate();
        northPanel.repaint();
        northPanel.setVisible(true);

        //set Header and Body Message
        headerPanel.reBuild(response);
        bodyPanel.update(response);
        tabbedPane.setVisible(true);
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * ButtonHandler for Copy Button
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(copy)) {
                System.out.println("Copied");
            }

        }

    }
}