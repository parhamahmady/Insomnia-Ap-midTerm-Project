import java.util.*;


/**
 * HeaderField is the field in headerTab has a panel that set Header and Value
 */
public class Field {
    private String header;
    private String value;
    private Boolean active;
    private int number;

    /**
     * 
     * @param number is the index of the field in arraylist
     */
    public Field(int number) {
        header = "header";
        value = "value";
        active = true;
        this.number = number;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
}