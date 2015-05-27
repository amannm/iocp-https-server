/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.http;

import v2.parse.Demarcation;

/**
 *
 * @author admin
 */
public class HttpHeader {

    private final Demarcation fieldName;
    private final Demarcation fieldValue;

    public HttpHeader(Demarcation fieldName, Demarcation fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public Demarcation getFieldName() {
        return fieldName;
    }

    public Demarcation getFieldValue() {
        return fieldValue;
    }
    
}
