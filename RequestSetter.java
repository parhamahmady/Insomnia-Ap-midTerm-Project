import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * RequestMaker is a class to set a request options
 */
public class RequestSetter {
    private Request request;

    /**
     * Counstructor
     * 
     * @param request is the request we want to set it up
     */
    public RequestSetter(Request request) {
        this.request = request;
    }

    /**
     * set the request url
     * 
     * @param input
     * @throws Exception if the input url wasn't a valid Url;
     * 
     */
    public void setUrl(String input) {
        URL url;

        if (!isUrl(input)) {// check the validation

            throw new IllegalStateException();

        }

        if (input.startsWith("http") || input.startsWith("https")) {
            try {
                url = new URL(input);
                request.setUrl(url);

                return;
            } catch (Exception e) {

                throw new IllegalStateException();
            }
        }

        if (!input.startsWith("www.")) {
            String temp = "http://www." + input+"/";
            try {
                url = new URL(temp);

                request.setUrl(url);
                return;
            } catch (Exception e) {

                throw new IllegalStateException();

            }

        }
        try {
            url = new URL("http", input, "/");
            request.setUrl(url);
            return;
        } catch (Exception e) {

            throw new IllegalStateException();

        }

    }

    /**
     * Check the url validate
     * 
     * @return true if the url was valid
     */
    private boolean isUrl(String url) {

        if (!url.isBlank() && url.indexOf(".") != -1 && !url.startsWith(".")) {
            return true;
        }

        return false;
    }

    /**
     * Set request method(Type)
     * 
     * @param input is the name of the method
     */
    public void setMethod(String input) {
        request.setType(input);
    }

    /**
     * re build the Source by its input
     * 
     * @param header is the frame headers
     */
    public Header setHeaderFromGui(ArrayList<MainFieldPanel> fieldPanels) {
        Header tempHeader = new Header();
        for (MainFieldPanel mainFieldPanel : fieldPanels) {
            Field tempField = new Field(tempHeader.getHeaderFields().size());
            tempField.setHeader(mainFieldPanel.getHeader().getText());
            tempField.setValue(mainFieldPanel.getValue().getText());
            tempField.setActive(mainFieldPanel.getActive().isSelected());

            tempHeader.addCustomField(tempField);
        }
        return tempHeader;
    }

    public void setHeaderFromConsoul(String input) {
        input = input.substring(0, input.length());// remove citations

        readInfoFromConsoul(input, ":", ";", request.getMyheader());
        return;
    }

    public void setFormDataBodyFromConsoul(String input) {
        request.getMyBody().setType("FormData");
        input = input.substring(0, input.length());// remove citations
        readInfoFromConsoul(input, "=", "&", request.getMyBody().getFormData());

    }

    /**
     * Read from consoul convert the input to a valid json
     * 
     * @param input
     */
    public void setJsonBodyFromConsoul(String input) {
        request.getMyBody().setType("Json");
        Header tempHeader = new Header();
        input = input.substring(1, input.length() - 1);// erase the cotaion
        readInfoFromConsoul(input, ":", ",", tempHeader);
        input = "{";
        ArrayList<Field> tempFields = tempHeader.getHeaderFields();
        Iterator<Field> it = tempFields.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            input += "\"" + field.getHeader() + "\"" + ":" + "\"" + field.getValue() + "\"";
            if (it.hasNext()) {
                input += ",";
            }
        }
        input += "}";
        request.getMyBody().getJson().setText(input);
    }

    /**
     * Read the Consoul input cut it and set the Request's headers reqersively
     * 
     * @param input
     * @param seprater1 sign that seprate key from value
     * @param seprater2 sign that seprate diferent parts
     */
    private void readInfoFromConsoul(String input, String seprater1, String seprater2, Header myHeader) {
        String newText = input;
        if (!input.isBlank() && input.indexOf(seprater2) == -1) {

            String header = newText.substring(0, newText.indexOf(seprater1));
            String value = newText.substring(newText.indexOf(seprater1) + 1);
            myHeader.makeFeild(header, value);
            return;
        }

        newText = input.substring(0, input.indexOf(seprater2));

        String header = newText.substring(0, newText.indexOf(seprater1));
        String value = newText.substring(newText.indexOf(seprater1) + 1);
        myHeader.makeFeild(header, value);
        readInfoFromConsoul(input.substring(input.indexOf(seprater2) + 1), seprater1, seprater2, myHeader);

    }
}