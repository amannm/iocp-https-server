/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.http.header;

import systems.cauldron.http.v1.http.util.constants.HttpContentCoding;
import systems.cauldron.http.v1.http.util.constants.HttpRequestMethod;

import javax.activation.MimeType;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Locale;

/**
 * @author Administrator
 */
public class AsciiHttpEntity {
    //Source data buffer
    //Cached interpreted data buffer[]

    //Locate resource
    private URI location;

    //Authorize request
    private HttpRequestMethod[] allow;

    //GET -> Load cached resource

    //Load original resource 
    private ByteBuffer data;
    private Locale locale;
    private MimeType type;
    private Date created;
    private Date modified;

    //Interpet resource
    private HttpContentCoding coding;
    private Date refreshed;
    private Date expired;
}
