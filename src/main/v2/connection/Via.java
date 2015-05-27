/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.connection;

/**
 *
 * @author admin
 */
public class Via {

    private final String protocolName;
    private final String protocolVersion;
    private final String receivedBy;
    private final String comment;

    public Via(String protocolName, String protocolVersion, String receivedBy, String comment) {
        this.protocolName = protocolName;
        this.protocolVersion = protocolVersion;
        this.receivedBy = receivedBy;
        this.comment = comment;
    }

    public Via(String protocolName, String protocolVersion, String receivedBy) {
        this.protocolName = protocolName;
        this.protocolVersion = protocolVersion;
        this.receivedBy = receivedBy;
        this.comment = null;
    }

    public static Via parse(String input) {
        String[] result = input.split(",");
        for (String s : result) {
            String[] asdf = s.split(" ");
            String comment = null;
            String pName = null;
            String pVers = null;
            String receiver = null;
            switch (asdf.length) {
                case 3:
                    comment = asdf[2];
                case 2:
                    String[] asdf2 = asdf[0].split("/", 2);
                    if (asdf2.length == 1) {
                        pName = null;
                        pVers = asdf2[0];
                    } else {
                        pName = asdf2[0];
                        pVers = asdf2[1];
                    }
                    receiver = asdf[1];
            }
            if (comment == null) {
                return new Via(pName, pVers, receiver);

            } else {
                return new Via(pName, pVers, receiver, comment);
            }
        }
        return null;
    }
}
