import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * FieldPanel is HeaderFeild gui
 */
public class MainFieldPanel {
    private JPanel panel;
    private JTextField header;
    private JTextField value;
    private JCheckBox active;
    private JButton delete;
    private int number;

    /**
     * Counstructor
     * 
     * @param number is the index of the field in the arraylist of all panelfields
     */
    public MainFieldPanel(int number, String headertext, String valueText) {

        this.number = number;

        panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(375, 30));
        panel.setBackground(new Color(35, 35, 35));
        GridBagConstraints c = new GridBagConstraints();

        header = new JTextField(headertext);
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(Color.WHITE);
        c.weightx = 0.9;
        c.ipadx = 100;
        c.gridx = 0;
        panel.add(header, c);

        value = new JTextField(valueText);
        value.setBackground(new Color(35, 35, 35));
        value.setForeground(Color.WHITE);
        c.gridx = 2;
        panel.add(value, c);

        active = new JCheckBox();
        active.setSelected(true);
        active.setBackground(new Color(35, 35, 35));
        c.gridx = 3;
        c.ipadx = 0;
        panel.add(active, c);

        delete = new JButton("\u2672");// the trash sign
        delete.setBackground(new Color(35, 35, 35));
        delete.setForeground(Color.WHITE);
        c.gridx = 4;
        c.ipadx = 0;
        panel.add(delete, c);
    }

    

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the active
     */
    public JCheckBox getActive() {
        return active;
    }

    /**
     * @return the delete
     */
    public JButton getDelete() {
        return delete;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @return the header
     */
    public JTextField getHeader() {
        return header;
    }

    /**
     * @return the value
     */
    public JTextField getValue() {
        return value;
    }

    /**
     * @return the active
     */
    public Boolean isActive() {
        return active.isSelected();
    }
    
    
}