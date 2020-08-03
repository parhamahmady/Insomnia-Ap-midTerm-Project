import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * CommandParser is the class to get an prosses the commands
 */
public class CommandParser {
    private Request tempRequest;
    private RequestSender sender;
    private ArrayList<Request> savedRequests;
    private Loader loader;
    private Scanner fScanner; // is the main scanner for commands
    private String commands;// contains all command parts

    // foo.com -M Post --headers "man:salam;to:bye;maha:koja;they:fuckoff" -d
    // "firstname=hadi&lastname=tabatabaee"

    /**
     * Counstructor
     */
    public CommandParser() {
        loader = new Loader();
        fScanner = new Scanner(System.in);
    }

    /**
     * Get the commands from consoul or other method and make a request as its input
     * information and send if the Send was true
     * 
     * @param inputCommands the command string
     * @param send          to check if should send
     */
    public void getCommand(String inputCommands, Boolean send) {

        if (inputCommands == null) {
            System.out.printf(">Insomnia  ");
        }

        if (inputCommands == null) {

            commands = fScanner.nextLine();

        } else {
            commands = inputCommands;

        }

        Scanner tokenizer = new Scanner(commands);

        if (inputCommands == null) {// Set the Temt Request class
            String rand = "sdufnsdfsdlfjsldfjsld21239jkase1eklsadjkasek132easd123";

            tempRequest = new Request("GET",
                    rand.substring((new Random().nextInt(52) * new Random().nextInt(52) % 52 + 1)));
            send = true;
        } else {
            if (!send) {
                tempRequest = new Request("GET", tokenizer.next());
            } else {
                String rand = "sdufnsdfsdlfjsldfjsld";
                tempRequest = new Request("GET", rand.substring(new Random().nextInt(15), 20));
            }
            inputCommands = null;
        }

        RequestSetter requestSetter = new RequestSetter(tempRequest);
        String saveFileName = "";// for request Body
        Boolean save = false;// will true if we wanted to save request
        String requestSavelistName = "";
        String uploadFileAdress = "";
        boolean saveResponse = false;
        boolean showHeader = false;
        boolean reDirect = false;
        boolean showBody = true;
        String temp = "";

        while (tokenizer.hasNext() && !commands.isEmpty()) {
            try {
                temp = tokenizer.next();
                switch (temp) {

                    case "-M":
                    case "--method":
                        String method = tokenizer.next();
                        method=method.toUpperCase();
                        if (!checkTheMethod(method)) {
                            throw new IllegalArgumentException();
                        }
                        requestSetter.setMethod(method);
                        break;

                    case "-H":
                    case "--headers":
                        requestSetter.setHeaderFromConsoul(tokenizer.next());
                        break;

                    case "-d":
                    case "--data":
                        requestSetter.setFormDataBodyFromConsoul(tokenizer.next());
                        break;
                    case "--upload":
                        uploadFileAdress = tokenizer.next();

                        break;
                    case "--json":
                    case "-j":
                        requestSetter.setJsonBodyFromConsoul(tokenizer.next());

                        break;
                    case "-i":
                        showHeader = true;
                        break;
                    case "-f":
                        reDirect = true;
                        break;
                    case "--help":
                    case "-h":
                        help();
                        send = false;
                        break;

                    case "-O":
                    case "--output":
                        saveResponse = true;
                        saveFileName = NameMaker(temp, tokenizer);
                        if (saveFileName.isEmpty()) {
                            saveFileName = "output_[" + java.time.LocalDate.now() + "]";
                        }
                        break;

                    case "-nb":
                        showBody = false;
                        break;

                    case "-S":
                    case "--save":
                        save = true;
                        requestSavelistName = NameMaker(temp, tokenizer);
                        break;

                    case "list":
                        String temp2 = NameMaker(temp, tokenizer);
                        if (!temp2.isBlank()) {
                            savedRequests = loader.loadSavedRequets(temp2);
                        } else {
                            savedRequests = loader.loadSavedRequets("");
                        }
                        send = false;
                        int i = 1;
                        for (Request request : savedRequests) {
                            System.out.printf("%d : ", i);
                            showRequestInfo(request);
                            i++;
                        }
                        break;
                    case "fire":
                        send = false;
                        String temp3 = NameMaker(temp, tokenizer);
                        if (!temp3.isEmpty()) {
                            savedRequests = loader.loadSavedRequets(temp3);
                        } else {
                            savedRequests = loader.loadSavedRequets("");
                        }
                        while (tokenizer.hasNext()) {
                            int number = tokenizer.nextInt() - 1;
                            tempRequest = savedRequests.get(number);
                            sender = new RequestSender(tempRequest, saveResponse, saveFileName, showHeader, showHeader,
                                    reDirect, uploadFileAdress);

                        }

                        break;
                    default:// for url
                        // send = true;
                        requestSetter.setUrl(temp);
                        break;
                }
            } catch (Exception e) {
                send = false;
                save = false;
                System.out.println("Your Information is not Valid");
            }

        }
        if (save) {
            Saver saver = new Saver();
            saver.saveRequest(tempRequest, requestSavelistName);
        }
        if (send && !commands.isEmpty()) {
           try {
            sender = new RequestSender(tempRequest, saveResponse, saveFileName, showHeader, showBody, reDirect,
                    uploadFileAdress);   
           } catch (Exception e) {
            System.err.println("Can't connect");
          
           }
            
        }
    }
    /**
     * 
     * @param method
     * @return true if the method was valid
     */
    private Boolean checkTheMethod(String method) {
        HashSet<String> methodSet = new HashSet<>();
        methodSet.add("PUT");
        methodSet.add("DELETE");
        methodSet.add("GET");
        methodSet.add("POST");
        if (methodSet.contains(method)) {
            return true;
        }
        return false;
    }

    /**
     * Print the temp Request Information
     * 
     * @param tempRequest
     */
    private void showRequestInfo(Request tempRequest) {
        System.out.printf("url : %s ", tempRequest.getUrl().toString());// print Url
        System.out.printf(" | ");
        System.out.printf("Method : %s ", tempRequest.getType());// print method
        System.out.printf(" | ");

        // print Headers
        if (tempRequest.getMyheader().getHeaderFields().size() != 0) {
            System.out.printf("Headers : ");
            ArrayList<Field> fields = tempRequest.getMyheader().getHeaderFields();
            for (Field field : fields) {
                System.out.printf(",%s : %s,", field.getHeader(), field.getValue());
            }
            System.out.printf(" | ");
        }
        // Print FormData Body
        if (tempRequest.getMyBody().getFormData().getHeaderFields().size() != 0) {
            System.out.printf("FormData Body : ");
            ArrayList<Field> formbodies = tempRequest.getMyBody().getFormData().getHeaderFields();
            for (Field field : formbodies) {
                System.out.printf(",%s = %s,", field.getHeader(), field.getValue());
            }
            System.out.printf(" | ");
        }
        System.out.println();// for enter button
    }

    /**
     * Check if the user typed the name of the saving file or not and return the
     * name
     * 
     * @param key         is the --output or -o
     * @param mainScanner is the tokenizer to move its cursor forward
     * @return the name of the saving file if exist
     */
    private String NameMaker(String key, Scanner mainScanner) {
        String name;
        Scanner tempScanner;
        String temp = commands.substring(commands.indexOf(key) + key.length());
        tempScanner = new Scanner(temp);

        if (!temp.isEmpty() && ((name = tempScanner.next()).indexOf("-") == -1)) {
            return name = mainScanner.next();
        }
        return "";
    }

    /**
     * Print a Help for user
     */
    private void help() {
        System.out.println("Welcome To Insomnia (Consoul Edition)");
        System.out.println("-M , --method + type = set the request Method");
        System.out.println("-H , --headers + type = set the request Headers");
        System.out.println("-d + type = set the request FormDataBody");
        System.out.println("--upload + absolout path , to add a file to formData Body");
        System.out.println("-f , set FollowRedirect true");
        System.out.println("-S , --save + listname(optional),will Save the request");
        System.out.println("-i ,show response Headers");
        System.out.println("-nb ,hide response Body");
        System.out.println("-O , --output  + filename(optional),will save response Output");
        System.out.println("-list + listname(optional) , show all list name or requests list ");
        System.out.println("-fire + listname(optional) + index , reSend the request");
    }

    /**
     * @return the tempRequest
     */
    public Request getTempRequest() {
        return tempRequest;
    }
}