import java.util.*;

/**
 * Header is the header part of the request
 */
public class Header {

    private ArrayList<Field> headerFields;

    public Header() {
        headerFields = new ArrayList<Field>();
    }

    /**
     * add field to header
     */
    public void addField() {
        headerFields.add(new Field(headerFields.size()));
    }

    /**
     * 
     * add a field with custom setting
     * 
     * @param field
     */
    public void addCustomField(Field field) {
        headerFields.add(field);
    }

    /**
     * Make a Field with specify values
     * 
     * @param header
     * @param value
     */
    public void makeFeild(String header, String value) {
        Field newField = new Field(headerFields.size());
        newField.setHeader(header);
        newField.setValue(value);
        headerFields.add(newField);
    }

    /**
     * remove a field from arraylist
     */
    public void removeField(int number) {
        headerFields.remove(number);
    }

    /**
     * Updates every field number after a delete
     */
    private void updateFieldNumber() {
        int i = 0;
        for (Field headerField : headerFields) {
            headerField.setNumber(i);
            i++;
        }
    }

    /**
     * @return the headerFields
     */
    public ArrayList<Field> getHeaderFields() {
        return headerFields;
    }

}