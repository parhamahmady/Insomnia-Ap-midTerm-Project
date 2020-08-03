import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
/**
 * Request
 */
public class Request {
    private String type;
    private String name;
    private Header myheader;
    private Body myBody;
    private Response myResponse;
    private URL url;

    /**
     * 
     * @param type is the type of the Request like Post and Get
     * @param name is the name of the Request
     */
    public Request(String type, String name) {
        this.name = name;
        this.type = type;
        myheader = new Header();
        myBody = new Body();
        myResponse = new Response();
    }

    /**
     * @param url the url to set
     */
    public void setUrl(URL url) {
        this.url = url;
    }
    public void setMyheader(Header myheader) {
        this.myheader = myheader;
    }
    /**
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @return the myResponse
     */
    public Response getMyResponse() {
        return myResponse;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the myheader
     */
    public Header getMyheader() {
        return myheader;
    }

    /**
     * @return the myBody
     */
    public Body getMyBody() {
        return myBody;
    }
}