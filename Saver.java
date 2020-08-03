import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Saver is the class that manage every thing we want to save
 */
public class Saver {
    private FileOutputStream writer;
    private Formatter textWriter;

    /**
     * save the Start options
     * 
     * @param exitState    is the types of exits (terminate or Hide)
     * @param fDirectState is the state of follow director
     */
    public void atStart(int exitState, boolean fDirectState) {
        try {
            cleanUp("./Files/atStart.bin");
            writer = new FileOutputStream("./Files/atStart.bin");
            writer.write(exitState);
            if (fDirectState) {
                writer.write(1);
            } else {
                writer.write(0);
            }
        } catch (Exception e) {
            System.err.println("Can't save");
        } finally {
            try {
                writer.close();

            } catch (Exception e) {
                System.err.println("Can't close file");
            }
        }
    }

    /**
     * Save a request information in a text file
     * 
     * @param request will save
     */
    public void saveRequest(Request request, String list) {
        File tempFile = new File("./Files/Requests/" + list);
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        String filename = "./Files/Requests/" + list + "/" + request.getName() + ".txt";

        try {
            cleanUp(filename);

            textWriter = new Formatter(filename);

            textWriter.format("%s\n", request.getName());// write the name of request

            textWriter.format("%s\n", request.getUrl()); // write Url

            textWriter.format("-M\n%s\n", request.getType());// write type

            if (request.getMyheader().getHeaderFields().size() != 0) {// write headers
                textWriter.format("-H\n\"");
                ArrayList<Field> headerFields = request.getMyheader().getHeaderFields();
                Iterator<Field> it = headerFields.iterator();
                while (it.hasNext()) {
                    Field temp = it.next();
                    textWriter.format("%s:%s", temp.getHeader(), temp.getValue());
                    if (it.hasNext()) {
                        textWriter.format(";");
                    }
                }
                textWriter.format("\"\n");
            }

            if (request.getMyBody().getFormData().getHeaderFields().size() != 0) {// write FromData Body
                textWriter.format("-d\n\"");
                ArrayList<Field> formDataFields = request.getMyBody().getFormData().getHeaderFields();
                Iterator<Field> it = formDataFields.iterator();
                while (it.hasNext()) {
                    Field temp = it.next();
                    textWriter.format("%s=%s", temp.getHeader(), temp.getValue());
                    if (it.hasNext()) {
                        textWriter.format("&");
                    }
                }
                textWriter.format("\"\n");
            }
            if (!request.getMyBody().getJson().getText().isBlank()) {//write JsonBody
                
            }
        } catch (Exception e) {
            System.err.println("Can't Save the request");
        } finally {
            textWriter.close();
        }
    }

    public void saveResponseBody(String filename, byte[] bytes, String type) {
        // set the filename
        if (filename.indexOf(".") == -1) {// if the format of file wasn't init

            if (type != null) {// if the header didn't have contenct-Type
                Scanner tempScanner = new Scanner(type.substring(type.indexOf("/") + 1));
                filename = "./Files/Bodies/" + filename + "." + tempScanner.next();
            } else {
                filename = "./Files/Bodies/" + filename;
            }

            if (filename.indexOf(";") != -1) {
                filename = filename.substring(0, filename.length() - 1);
            }
        } else {
            filename = "./Files/Bodies/" + filename;
        }
        try {
            
            // Write to file
            if (type != null && type.indexOf("text") != -1) {
                cleanUp(filename);
                textWriter = new Formatter(filename);
                textWriter.format("%s", new String(bytes));
                textWriter.close();
                
                return;
            } else {

                cleanUp(filename);
                writer = new FileOutputStream(filename);
                writer.write(bytes);
                writer.close();
                
                return;
            }

        } catch (Exception e) {
            System.out.println("Can't Save");
        }

    }

    /**
     * delete the existing file
     * 
     * @param uri
     */
    private void cleanUp(String address) {
        File selected = new File(address);
        if (selected.exists()) {
            selected.delete();
        }
    }
}