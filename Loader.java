import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Loader to load every thing from files
 */
public class Loader {
    private FileInputStream loader;
    private Scanner textReader;

    /**
     * Load MainFrame Settings
     * 
     * @param mainWindow is the mainFrame
     */
    public void atStart(MainWindow mainWindow) {
        try {
            loader = new FileInputStream("./Files/atStart.bin");
            mainWindow.getMainFrame().setDefaultCloseOperation(loader.read());// set exit type
            int temp = loader.read();
            if (temp == 1) {
                mainWindow.getMenuBar().setfRedirect(true);
            } else {
                mainWindow.getMenuBar().setfRedirect(false);
            }
        } catch (Exception e) {
            System.err.println("Can't Load file");
        } finally {
            try {
                loader.close();
            } catch (Exception e) {
                System.err.println("Cant close");
            }
        }
        try {

            Path path = Paths.get("./Files/Requests");
            addLoadedRequestToRList(mainWindow.getRequestListFrame(), loadSavedRequets(""));
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for (Path path2 : directoryStream) {//load requests inside lists
                if (!path2.toFile().isDirectory()) {//if it wasn't directory will continue
                    continue;
                }
                String temp=path2.toString().substring(path2.toString().indexOf("./Files/Requests")+"./Files/Requests".length()+1);
                addLoadedRequestToRList(mainWindow.getRequestListFrame(), loadSavedRequets(temp));
            }
        } catch (Exception e) {
            System.out.println("cant load saved requests");
        }
    }

    /**
     * Load all save requests in Request folder using a new CommandParser
     * 
     * @return arraylist of all saved Requests
     */
    public ArrayList<Request> loadSavedRequets(String list) {
        ArrayList<Request> loadRequests = new ArrayList<>();
        FileReader fileReader;
        Path path = Paths.get("./Files/Requests/" + list);
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for (Path path2 : directoryStream) {

                if (path2.toFile().isDirectory()) {
                    continue;
                }
                fileReader = new FileReader(path2.toFile());
                textReader = new Scanner(fileReader);
                String command = "";
                // textReader.nextLine();//skip from the name
                while (textReader.hasNext()) {
                    command = command + textReader.next() + " ";
                }

                textReader.close();
                fileReader.close();

                CommandParser parser = new CommandParser();
                parser.getCommand(command, false);
                loadRequests.add(parser.getTempRequest());
            }
            return loadRequests;

        } catch (Exception e) {
            System.err.println("Can't Load Saved Requests");
        }

        return loadRequests;

    }

    /**
     * will add all requests in addlist to the frame
     * 
     * @param frame
     * @param addList
     */
    private void addLoadedRequestToRList(RequestListFrame frame, ArrayList<Request> addList) {
        for (Request request : addList) {
            frame.addRequest(request);
        }
    }
}