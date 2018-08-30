/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.server;

import systems.cauldron.http.v1.container.ListenerContainer;

/**
 * @author admin
 */
public class OntoServ {
    public static void main(String[] args) {
        ListenerContainer container = new ListenerContainer();
        container.start();
    }
}
