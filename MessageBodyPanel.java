import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MessageBodyPanel is the body panel in response
 */
public class MessageBodyPanel {
    private JPanel panel;
    private JPanel northPanel;
    private Response response;
    private JRadioButton preview;
    private JRadioButton raw;
    private RawBodyPanel rawBodyPanel;
    private PreviewPanel previewPanel;

    /**
     * Counstructor
     */
    public MessageBodyPanel() {
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 45));
        panel.setOpaque(true);
        // North Panel
        northPanel = new JPanel(new BorderLayout(10, 10));

        preview = new JRadioButton("Preview");
        preview.addActionListener(new ButtonHandler());
        northPanel.add(preview, BorderLayout.WEST);

        raw = new JRadioButton("Raw");
        raw.addActionListener(new ButtonHandler());
        northPanel.add(raw, BorderLayout.EAST);
        raw.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(raw);
        bg.add(preview);

        rawBodyPanel = new RawBodyPanel();
        panel.add(rawBodyPanel.getPanel(), BorderLayout.CENTER);

        previewPanel = new PreviewPanel();

        panel.add(northPanel, BorderLayout.NORTH);
        panel.setVisible(false);

    }

    public void update(Response input) {
        response = input;

        rawBodyPanel.setRawText(response.getRawBody());
        rawBodyPanel.getPanel().repaint();

        if (response.getImageUrl() == null) {
            previewPanel.getPanel().setVisible(false);
        } else {
            previewPanel.setImageLabel(response.getImageUrl());
        }
        panel.setVisible(true);
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * ButtonHandler for Raw and Preview
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(raw)) {
                previewPanel.getPanel().setVisible(false);
                rawBodyPanel.getPanel().setVisible(true);
                panel.add(rawBodyPanel.getPanel(), BorderLayout.CENTER);
                panel.repaint();
            }
            if (e.getSource().equals(preview)) {
                previewPanel.getPanel().setVisible(true);
                rawBodyPanel.getPanel().setVisible(false);
                panel.add(previewPanel.getPanel(),BorderLayout.CENTER);
                panel.repaint();
            }

        }
    }

    /**
     * PreviewPanel is type of response body
     */
    private class PreviewPanel {
        private JPanel panel;
        private JLabel imageLabel;

        /**
         * Counstructor
         */
        public PreviewPanel() {
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(new Color(45, 45, 45));
            panel.setOpaque(true);
            imageLabel = new JLabel();

            panel.add(imageLabel, BorderLayout.CENTER);

        }

        /**
         * @param imageUrl the imageLabel to set
         */
        public void setImageLabel(String imageUrl) {
            imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(imageUrl)));
            panel.repaint();
        }

        /**
         * @return the panel
         */
        public JPanel getPanel() {
            return panel;
        }

        /**
         * @param panel the panel to set
         */
        public void setPanel(JPanel panel) {
            this.panel = panel;
        }

    }

    /**
     * RawBodyPanel is type of response
     */
    private class RawBodyPanel {
        private JPanel panel;
        private JTextArea rawText;

        public RawBodyPanel() {
            panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(new Color(45, 45, 45));
            panel.setOpaque(true);
            rawText = new JTextArea();
            rawText.setBackground(new Color(45,45,45));
            rawText.setForeground(Color.WHITE);
            rawText.setMaximumSize(new Dimension(100,100));
            rawText.setOpaque(true);
            panel.add(rawText, BorderLayout.CENTER);

        }

        /**
         * @return the panel
         */
        public JPanel getPanel() {
            return panel;
        }

        /**
         * @param rawText the rawText to set
         */
        public void setRawText(String text) {
            rawText.setText(text);
        }

        /**
         * @return the rawText
         */
        public JTextArea getRawText() {
            return rawText;
        }
    }

}