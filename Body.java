import java.util.*;

/**
 * Body is the body part of the request
 */
public class Body {
    private String type;// type of body FormData or Json
    private Header formData;
    private Json json;

    /**
     * Counstructor
     */
    public Body() {
        formData = new Header();
        json = new Json();
        type = "FormData";
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the formData
     */
    public Header getFormData() {
        return formData;
    }

    public void setFormData(Header formData) {
        this.formData = formData;
    }

    /**
     * @return the json
     */
    public Json getJson() {
        return json;
    }

    /**
     * Json is a type of a body
     */
    public class Json {
        private String text;

        /**
         * Counstructor
         */
        public Json() {

        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text the text to set
         */
        public void setText(String text) {
            this.text = text;
        }

    }
}