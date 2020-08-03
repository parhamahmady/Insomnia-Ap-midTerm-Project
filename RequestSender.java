import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;

/**
 * RequestSender will send request to the Host
 */
public class RequestSender {
    private HttpURLConnection connection;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private String responseFileName;
    private String uploadFileName;
    private Boolean saveResponse;
    private Boolean showBody;

    /**
     * Counstructor and it will send and get the information
     * 
     * @param request          will send
     * @param saveResponse     if true response will save as a text file
     * @param responseFileName name of the response file
     */
    public RequestSender(Request request, Boolean saveResponse, String responseFileName, Boolean showHeader,
            boolean showBody, Boolean reDirect, String uploadFileName) throws Exception{

        this.responseFileName = responseFileName;
        this.saveResponse = saveResponse;
        this.showBody = showBody;
        this.uploadFileName = uploadFileName;

        URL url = request.getUrl();

        // try {

            if (url.getProtocol().equals("https")) {// make the connection
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            connection.setRequestMethod(request.getType());// set Method

            addHeadersToConnection(connection, request.getMyheader());// send Headers
            addBodytoConnection(connection, request.getMyBody());// send Body

            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {// check the Redirction
                if (reDirect) {
                    URL tempUrl = new URL(connection.getHeaderField("Location"));
                    System.out.println("ReDirected to " + tempUrl);
                    connection.disconnect();
                    if (tempUrl.getProtocol().equals("https")) {// make the connection
                        connection = (HttpsURLConnection) tempUrl.openConnection();
                    } else {
                        connection = (HttpURLConnection) tempUrl.openConnection();
                    }
                    connection.setRequestMethod(request.getType());// set Method
                    addHeadersToConnection(connection, request.getMyheader());// send Headers
                    addBodytoConnection(connection, request.getMyBody());// send Body

                } else {
                    inputStream = new BufferedInputStream(connection.getInputStream());
                }
            }

            request.getMyResponse().setStatusCode(connection.getResponseCode());
            request.getMyResponse().setStatusMessage(connection.getResponseMessage());
            if ((connection.getResponseCode() / 100) == 2) {

                setResponse(request.getMyResponse(), connection);// set the response
                if (showHeader) {// show the HeadersFields
                    showResponseHeaders(request.getMyResponse());
                }
                if (showBody) {
                    
                    System.out.println(request.getMyResponse().getRawBody());
                }

            } else {
                System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());// show if it
                // couldn't
                // connect
            }

        // } catch (Exception e) {
        
        // } finally {
            connection.disconnect();
        // }
    }

    /**
     * Add headers to connection
     * 
     * @param connection
     * @param header
     */
    private void addHeadersToConnection(HttpURLConnection connection, Header header) {
        ArrayList<Field> fields = header.getHeaderFields();
        for (Field field : fields) {
            if (field.getActive()) {// check the activation
                connection.setRequestProperty(field.getHeader(), field.getValue());
            }
        }
    }

    private void addFormDataBodytoConnection(HttpURLConnection connection, Header formData) throws IOException {
        String boundary = "TheManiac";// make Boundry
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        outputStream = new BufferedOutputStream(connection.getOutputStream());// open the outStream

        if (!uploadFileName.isBlank()) {// if there was a file to upload
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            File uploadFile = new File(uploadFileName);
            outputStream.write(("Content-Disposition: form-data; filename=\"" + uploadFile.getName()
                    + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
            FileInputStream tempinput = new FileInputStream(uploadFile);
            byte[] bytes = tempinput.readAllBytes();
            outputStream.write(bytes);
            outputStream.write("\r\n".getBytes());
            tempinput.close();
        }

        ArrayList<Field> bodyFields = formData.getHeaderFields();
        for (Field field : bodyFields) {
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            if (field.getActive()) {// check the activation
                outputStream.write(
                        ("Content-Disposition: form-data; name=\"" + field.getHeader() + "\"\r\n\r\n").getBytes());
                outputStream.write((field.getValue() + "\r\n").getBytes());

            }
        }

        outputStream.write(("--" + boundary + "--\r\n").getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private void addJsonBodytoConnection(HttpURLConnection connection, String json) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");

        outputStream = new BufferedOutputStream(connection.getOutputStream());
        outputStream.write(json.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /**
     * add the body to the connection by its type
     * 
     * @param connection
     * @param body       that will add
     */
    private void addBodytoConnection(HttpURLConnection connection, Body body) throws IOException {
        if (connection.getRequestMethod().equals("POST") || connection.getRequestMethod().equals("PUT")) {

            connection.setDoOutput(true);

            if (body.getType().equals("FormData")) {// if the body was form Data
                addFormDataBodytoConnection(connection, body.getFormData());
                return;
            }
            if (body.getType().equals("Json")) {// if the body was Json
                addJsonBodytoConnection(connection, body.getJson().getText());
                return;
            }
        }
    }

    /**
     * Copy all response into request response part
     * 
     * @param response
     * @param connection
     * @throws IOException if the connection lost
     */
    private void setResponse(Response response, HttpURLConnection connection) throws IOException {
        response.setStatusCode(connection.getResponseCode());
        response.setStatusMessage(connection.getResponseMessage());
        // response.setTime(connection.getConnectTimeout());

        // if (connection.getDoInput()) {
            inputStream = new BufferedInputStream(connection.getInputStream());
            byte[] bytes=  inputStream.readAllBytes();

        if (saveResponse) {
            Saver saver = new Saver();
            saver.saveResponseBody(responseFileName,bytes, connection.getHeaderField("Content-Type"));
            inputStream.close();
        }

        
        response.setRawBody(new String(bytes));
        
        inputStream.close();

        // }

        ArrayList<Field> headerFields = new ArrayList<>();
        Map<String, List<String>> properties = connection.getHeaderFields();
        Set<String> prop = properties.keySet();
        for (String string : prop) {
            Field temp = new Field(headerFields.size());
            temp.setHeader(string);
            temp.setValue(connection.getHeaderField(string));
            headerFields.add(temp);
        }
        response.setHeaderFields(headerFields);
    }

    private void showResponseHeaders(Response response) {
        ArrayList<Field> rHeaders = response.getHeaderFields();
        for (Field field : rHeaders) {
            if (field.getHeader() == null) {
                System.out.println(field.getValue());
                continue;
            }
            System.out.println(field.getHeader() + " : " + field.getValue());
        }
    }
}