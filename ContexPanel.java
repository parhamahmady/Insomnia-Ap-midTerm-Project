import java.util.*;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ContexPanel is a part of a request
 */
public class ContexPanel {
    private ArrayList<MainFieldPanel> fieldPanels;
    private JPanel panel;//panel that contains mainpanel
    private JPanel mainpanel;// is the  panel that contains fields
    private JButton makeField;// panel that contains fields
    private JScrollPane scrollPane;

    /**
     * Counstructor
     */
    public ContexPanel(String buttonName) {
        fieldPanels = new ArrayList<MainFieldPanel>();

        panel=new JPanel(new BorderLayout(0,0));

        mainpanel = new JPanel();
        mainpanel.setBackground(new Color(35, 35, 35));
        mainpanel.setPreferredSize(new Dimension(350,500));

        scrollPane=new JScrollPane(mainpanel);
        scrollPane.setBounds(0, 0, 420, 500);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        makeField = new JButton(buttonName);
        
        makeField.setVisible(false);
        mainpanel.add(makeField);
        scrollPane.revalidate();

        panel.add(scrollPane,BorderLayout.CENTER);
    }

    /**
     * @return the scrollPane
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    /**
     * Updates every field number after a delete
     */
    public void updateFieldNumber() {
        int i = 0;
        for (MainFieldPanel headerFieldPanel : fieldPanels) {
            headerFieldPanel.setNumber(i);
            i++;
        }
    }

    public void rebuild(Header input) {
    }

    public void updatePanel() {
    }

    /**
     * @return the mainpanel
     */
    public JPanel getMainpanel() {
        return mainpanel;
    }

    /**
     * @return the panel
     */
    public JPanel getpanel() {
        return panel;
    }

    /**
     * @return the makeField
     */
    public JButton getMakeField() {
        return makeField;
    }

    /**
     * @return the fieldPanels
     */
    public ArrayList<MainFieldPanel> getFieldPanels() {
        return fieldPanels;
    }

    public Header getMyHeader() {
        return null;
    }
    /**
     * @param fieldPanels the fieldPanels to set
     */
    public void setFieldPanels(ArrayList<MainFieldPanel> fieldPanels) {
        this.fieldPanels = fieldPanels;
    }

}