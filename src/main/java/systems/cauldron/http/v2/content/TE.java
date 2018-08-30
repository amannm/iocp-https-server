/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v2.content;

import java.util.List;

/**
 * @author admin
 */
public class TE {
    private boolean trailers;
    private List<QualityAdapter<Encoding>> encodings;
}
