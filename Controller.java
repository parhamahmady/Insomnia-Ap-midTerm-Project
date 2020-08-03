import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.event.*;

/**
 * 
 * Controller is the class that controls Model and View
 */
public class Controller {
    private MainWindow mainWindow;
    private RequestSender sender;
    private RequestSetter setter;
    private Boolean followRedDirect;
    private ResponsePanel responsePanel;

    /**
     * 
     * @param mainWindow the model
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        mainWindow.show();

        responsePanel = mainWindow.getResponsePanel();
    }

    /**
     * Set the request and save it as a file
     * 
     * @param request will save
     * @param fram    is the ManagePanel
     */
    public void save(Request request, RequestManageFrame fram) {
        try {
            setRequest(request, fram, (String) fram.getType().getSelectedItem(), fram.getUrl().getText());
            SaveListChosser saveListChosser = new SaveListChosser(request);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "The entered Information is Not Valid", "!!!!",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void send(Request request, RequestManageFrame fram) {
        try {
            setRequest(request, fram, (String) fram.getType().getSelectedItem(), fram.getUrl().getText());
            BackGroundSender backGroundSender = new BackGroundSender(request, responsePanel, followRedDirect);
            backGroundSender.execute();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The entered Information is Not Valid", "!!!!",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void setRequest(Request request, RequestManageFrame frame, String method, String url)
            throws IllegalArgumentException {
        setter = new RequestSetter(request);
        setter.setMethod(method);
        setter.setUrl(url);
        request.setMyheader(setter.setHeaderFromGui(frame.getHeaderPanel().getFieldPanels()));// for Headers
        if (frame.getBodyPanel().getJson().isSelected()) {// for type of Body
            request.getMyBody().setType("Json");
        } else {
            request.getMyBody().setType("FormData");
        }
        request.getMyBody()
                .setFormData(setter.setHeaderFromGui(frame.getBodyPanel().getFormdataBody().getFieldPanels()));// for

        // FormData
        request.getMyBody().getJson().setText(frame.getBodyPanel().getJsonBody().getText());
    }

    /**
     * 
     * @return help text for help to user
     */
    public String help() {
        String helpText = "\tThe Insomnia \nSend will send the request \nSave will save the request\nCopy to Clipboard will save responceBody\nto remove a request right click \non its name at list";
        return helpText;
    }

    /**
     * Set the sender for fallow redirect
     */
    public void setFollowRedDirect(Boolean followRedDirect) {
        this.followRedDirect = followRedDirect;
    }

    /**
     * SaveListChosser is the frame that we choose the list name and save it
     */
    private class SaveListChosser extends MouseAdapter implements ActionListener {
        private JFrame frame;
        private JComboBox options;
        private JButton save;
        private JButton cancle;
        private JTextField newName;
        private Request request;
        private JPanel panel;

        /**
         * Counstructor
         */
        public SaveListChosser(Request request) {
            this.request = request;

            frame = new JFrame("Which List?");
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setMinimumSize(new Dimension(200, 200));

            panel = new JPanel(new BorderLayout(5, 5));
            frame.setContentPane(panel);

            options = new JComboBox(loadListDirectiries());

            panel.add(options, BorderLayout.NORTH);

            newName = new JTextField("List name");
            newName.addMouseListener(this);
            panel.add(newName, BorderLayout.SOUTH);

            save = new JButton("Save");
            save.addActionListener(this);
            panel.add(save, BorderLayout.WEST);

            cancle = new JButton("Cancle");
            cancle.addActionListener(this);
            panel.add(cancle, BorderLayout.EAST);

            frame.setVisible(true);
        }

        /**
         * 
         * @return all ListDirectories
         */
        private String[] loadListDirectiries() {
            ArrayList<String> names = new ArrayList<>();
            try {
                Path path = Paths.get("./Files/Requests");
                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
                for (Path path2 : directoryStream) {
                    if (path2.toFile().isDirectory()) {
                        String temp = path2.toString().substring(
                                path2.toString().indexOf("./Files/Requests") + "./Files/Requests".length() + 1);
                        names.add(temp);
                    }
                }
                names.add(".");
            } catch (Exception e) {
                System.out.println("cant load");
            }
            String[] tempnames = new String[names.size()];
            int i = 0;
            for (String string : names) {
                tempnames[i] = string;
                i++;
            }
            return tempnames;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(save)) {
                frame.setVisible(false);
                save();

            }
            if (e.getSource().equals(cancle)) {
                frame.setVisible(false);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            newName.setText((String) options.getSelectedItem());

            panel.validate();

        }

        /**
         * Will save the request
         */
        private void save() {

            Saver save = new Saver();
            String list = newName.getText();
            if (list.equals(".")) {
                list = "";
            }
            save.saveRequest(request, list);

        }

    }

}