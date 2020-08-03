import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * BodyPanel is the panel that manage request's body part
 */
public class BodyPanel {
    private JPanel panel;
    private JPanel northPanel;
    private JsonBodyPanel jsonBody;
    private ContexPanel formdataBody;
    private JRadioButton json;
    private JRadioButton form;
    private Request myRequest;// is the selected Request

    /**
     * Counstructor
     */
    public BodyPanel() {
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(35, 35, 35));

        jsonBody = new JsonBodyPanel();
        jsonBody.getPanel().setVisible(false);

        formdataBody = new FormDataBodyPanel();
        formdataBody.getpanel().setVisible(true);

        // Set the North Panel
        northPanel = new JPanel(new BorderLayout(10, 10));

        json = new JRadioButton("Json");
        json.addActionListener(new RadioButtonHandler());
        northPanel.add(json, BorderLayout.WEST);

        form = new JRadioButton("Form Data");
        form.addActionListener(new RadioButtonHandler());
        form.setSelected(true);
        panel.add(formdataBody.getpanel(), BorderLayout.CENTER);

        ButtonGroup bg = new ButtonGroup();
        bg.add(json);
        bg.add(form);
        northPanel.add(form, BorderLayout.EAST);

        panel.add(northPanel, BorderLayout.NORTH);
        northPanel.setVisible(false);
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    public JRadioButton getJson() {
        return json;
    }

    public JRadioButton getForm() {
        return form;
    }

    /**
     * 
     * @param myRequest is the selected request
     */
    public void update(Request input) {
        if (input == null) {
            panel.setVisible(false);
            return;
        }
        myRequest = input;

        northPanel.setVisible(true);

        if (myRequest.getMyBody().getType().equals("Json")) {
            /// set jason panel
            jsonBody.setText(myRequest.getMyBody().getJson().getText());

            json.setSelected(true);

            // make json visibale
            formdataBody.getpanel().setVisible(false);
            jsonBody.getPanel().setVisible(true);
            panel.add(jsonBody.getPanel(), BorderLayout.CENTER);

            return;
        }
        // update formData panel
        formdataBody.rebuild(myRequest.getMyBody().getFormData());
        // make formData visible
        form.setSelected(true);
        jsonBody.getPanel().setVisible(false);
        formdataBody.getpanel().setVisible(true);
        panel.add(formdataBody.getpanel(), BorderLayout.CENTER);
        return;
    }

    /**
     * RadioButtonHandler select the type of body
     */
    public class RadioButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (json.isSelected()) {
                myRequest.getMyBody().setType("Json");
                jsonBody.setText(myRequest.getMyBody().getJson().getText());
                formdataBody.getpanel().setVisible(false);
                jsonBody.getPanel().setVisible(true);
                panel.add(jsonBody.getPanel(), BorderLayout.CENTER);

            }
            if (form.isSelected()) {
                myRequest.getMyBody().setType("FormedData");
                formdataBody.rebuild(myRequest.getMyBody().getFormData());
                jsonBody.getPanel().setVisible(false);
                formdataBody.getpanel().setVisible(true);
                panel.add(formdataBody.getpanel(), BorderLayout.CENTER);

            }
        }

    }

    /**
     * JsonBody is a json type of Body
     */
    public class JsonBodyPanel {
        private JPanel panel;
        // private JTextField text;
        private JTextArea text;

        /**
         * Counstructor
         */
        public JsonBodyPanel() {
            panel = new JPanel(new BorderLayout(10, 10));

            // text = new JTextField();
            text=new JTextArea();
            
            panel.add(text, BorderLayout.CENTER);
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text.setText(text);
        }

        /**
         * @return the panel
         */
        public JPanel getPanel() {
            return panel;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text.getText();
        }

    }

    public ContexPanel getFormdataBody() {
        return formdataBody;
    }

    public JsonBodyPanel getJsonBody() {
        return jsonBody;
    }

    /**
     * FormDataBody is a type of Body
     */
    private class FormDataBodyPanel extends ContexPanel {
        private Header myFormBody;

        public FormDataBodyPanel() {
            super("Make Form");
            getMakeField().addActionListener(new ButtonHander());
            getMakeField().setVisible(false);
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
                    FieldPanel temp = new FieldPanel(getFieldPanels().size(), "Name", "Value");
                    myFormBody.addField();
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
         * 
         * set the new header from new selected Request
         * 
         * @param input is the request's header part
         */
        public void rebuild(Header input) {
            myFormBody = input;
            getMakeField().setVisible(true);
            updatePanel();
        }

        /**
         * this method repaint the panel with selected request info
         */
        public void updatePanel() {
            ArrayList<Field> fields = myFormBody.getHeaderFields();
            ArrayList<MainFieldPanel> tempFieldPanels = new ArrayList<MainFieldPanel>();// make a new list
            ArrayList<MainFieldPanel> fieldPanels = getFieldPanels();
            for (MainFieldPanel fieldPanel : fieldPanels) {
                getMainpanel().remove(fieldPanel.getPanel());
                getMainpanel().revalidate();
                getMainpanel().repaint();
            }
            for (Field field : fields) {
                FieldPanel temp = new FieldPanel(tempFieldPanels.size(), field.getHeader(), field.getValue());
                tempFieldPanels.add(temp);
                getMainpanel().add(temp.getPanel());
                getMainpanel().revalidate();
            }
            setFieldPanels(tempFieldPanels);// replace
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
                        myFormBody.removeField(getNumber());
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

}