import javax.swing.border.EmptyBorder;

// import Header.HeaderField;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Header is the Header tab in SouthMenu
 */
public class HeaderPanel extends ContexPanel {
    private Header myHeader;// is the Header that we manage it

    public HeaderPanel() {

        super("Make Header");
        getMakeField().addActionListener(new ButtonHander());
    }

    /**
     * ButtonHander for add field
     */
    private class ButtonHander implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            JButton tempButton = getMakeField();

            if (e.getSource().equals(tempButton)) {
                FieldPanel temp = new FieldPanel(getFieldPanels().size(), "Header", "Value");
                myHeader.addField();
                // headerFieldPanels.add(temp);
                getFieldPanels().add(temp);

                getMainpanel().setPreferredSize(new Dimension(360, getFieldPanels().size() * 30 + 500));
                getMainpanel().add(temp.getPanel());
                getScrollPane().revalidate();
                getMainpanel().revalidate();
            }
        }

    }

    /**
     * this method repaint the panel with selected request info
     */
    public void updatePanel() {
        ArrayList<Field> headerFields = myHeader.getHeaderFields();
        ArrayList<MainFieldPanel> tempFieldPanels = new ArrayList<MainFieldPanel>();// make a new list
        ArrayList<MainFieldPanel> headerFieldPanels = getFieldPanels();
        for (MainFieldPanel headerFieldPanel : headerFieldPanels) {
            getMainpanel().remove(headerFieldPanel.getPanel());
            getMainpanel().revalidate();
            getMainpanel().repaint();
        }
        for (Field headerField : headerFields) {
            FieldPanel temp = new FieldPanel(tempFieldPanels.size(), headerField.getHeader(), headerField.getValue());
            tempFieldPanels.add(temp);
            getMainpanel().add(temp.getPanel());
            getMainpanel().revalidate();
        }
        setFieldPanels(tempFieldPanels);// replace
    }

    public Header getMyHeader() {
        return myHeader;
    }

    /**
     * 
     * set the new header from new selected Request
     * 
     * @param input is the request's header part
     */
    public void rebuild(Header input) {
        if (input == null) {
            getMakeField().setVisible(false);
            return;
        }
        myHeader = input;
        getMakeField().setVisible(true);
        updatePanel();
    }

    /**
     * FieldPanel is HeaderFeild gui
     */
    private class FieldPanel extends MainFieldPanel {

        /**
         * Counstructor
         * 
         * @param number is the index of the field in the arraylist of all panelfields
         */
        public FieldPanel(int number, String headertext, String valueText) {

            super(number, headertext, valueText);
            getDelete().addActionListener(new DeleteButtonHandler());

        }

        /**
         * DeleteButtonHandler
         * 
         */
        public class DeleteButtonHandler implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JButton tempdelete = getDelete();
                if (e.getSource().equals(tempdelete)) {
                    myHeader.removeField(getNumber());
                    // getMainpanel().remove(headerFieldPanels.get(getNumber()).getPanel());
                    getMainpanel().remove(getFieldPanels().get(getNumber()).getPanel());
                    // headerFieldPanels.remove(getNumber());
                    getFieldPanels().remove(getNumber());
                    getMainpanel().revalidate();
                    getMainpanel().repaint();
                    updateFieldNumber();// update the index numbers

                }

            }

        }

    }
}