import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MessageHeaderPanel is the massage header in response part
 */
public class MessageHeaderPanel {
    private JPanel panel;
    private JPanel itemPanel;
    private ArrayList<MainFieldPanel> fieldPanels;
    private JScrollPane scrollPane;
    private Response response;// is the selected request's respons

    /**
     * Counstructor
     */
    public MessageHeaderPanel() {
        fieldPanels = new ArrayList<MainFieldPanel>();

        panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(new Color(45, 45, 45));
        panel.setOpaque(true);
        panel.setPreferredSize(new Dimension(400, panel.getHeight()));

        itemPanel = new JPanel();
        itemPanel.setPreferredSize(new Dimension(400, 600));
        itemPanel.setBackground(new Color(45, 45, 45));

        scrollPane = new JScrollPane(itemPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 0, 410, 700);

        panel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * get the selected request's Response update the panel
     * 
     * @param input
     */
    public void reBuild(Response input) {
        response = input;
        updatePanel();
    }

    /**
     * Update the panel with the selected Request's response
     */
    private void updatePanel() {
        ArrayList<Field> newFieldPanels = response.getHeaderFields();
        ArrayList<MainFieldPanel> tMainFieldPanels = new ArrayList<MainFieldPanel>();

        // Remove the past panels

        for (MainFieldPanel fieldPanel : fieldPanels) {

            itemPanel.remove(fieldPanel.getPanel());
            itemPanel.revalidate();
            itemPanel.repaint();
        }

        // Make new FieldPanels
        MainFieldPanel panel = new MainFieldPanel(0, "Header", "Value");
        panel.getHeader().setFocusable(false);
        panel.getValue().setFocusable(false);
        panel.getDelete().setVisible(false);
        panel.getActive().setVisible(false);
        tMainFieldPanels.add(panel);

        itemPanel.setPreferredSize(new Dimension(400,600));// resize the panel
        itemPanel.add(panel.getPanel());
        itemPanel.validate();
        scrollPane.validate();

        for (Field newfield : newFieldPanels) {
            MainFieldPanel tempPanel = new MainFieldPanel(newFieldPanels.size(), newfield.getHeader(),
                    newfield.getValue());
            tempPanel.getHeader().setFocusable(false);
            tempPanel.getValue().setFocusable(false);
            tempPanel.getDelete().setVisible(false);
            tempPanel.getActive().setVisible(false);

            tMainFieldPanels.add(tempPanel);

            itemPanel.setPreferredSize(new Dimension(400, tMainFieldPanels.size() * 30 + 600));// resize the panel
            itemPanel.add(tempPanel.getPanel());
            itemPanel.validate();
            scrollPane.validate();
        }
        fieldPanels = tMainFieldPanels;// replace
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }
}