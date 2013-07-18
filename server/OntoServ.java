/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontoserv.server;

import ontoserv.container.ListenerContainer;

/**
 *
 * @author admin
 */
public class OntoServ {
    public static void main(String[] args) {
        ListenerContainer container = new ListenerContainer();
        container.start();
    }
}
