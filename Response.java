
import java.util.*;

/**
 * Response is the respons of request
 */
public class Response {
    private String statusMessage;
    private int time;
    private int statusCode;
    private ArrayList<Field> headerFields;
    private String rawBody;
    private String imageUrl;//address of saved image

    /**
     * Counstructor
     */
    public Response() {
        headerFields = new ArrayList<Field>();
        
    }

    /**
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param rawBody the rawBody to set
     */
    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }

    /**
     * @return the rawBody
     */
    public String getRawBody() {
        return rawBody;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @return the headerFields
     */
    public ArrayList<Field> getHeaderFields() {
        return headerFields;
    }

    /**
     * @param headerFields the headerFields to set
     */
    public void setHeaderFields(ArrayList<Field> headerFields) {
        this.headerFields = headerFields;
    }

    /**
     * add field to Header
     * 
     * @param input is the field that wants to add
     */
    public void addHeaderField(Field input) {
        headerFields.add(input);
    }
}