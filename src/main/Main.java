/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {


    public static ExecutorService exe;
    public static AsynchronousChannelGroup group;

    static {
        exe = Executors.newFixedThreadPool(4);
    }

    public static void main(String[] args) {
        try {
            group = AsynchronousChannelGroup.withThreadPool(exe);
            exe.execute(new HttpServer(group, 8080, "<!doctype html><html><head><title>Hello World</title></head><body></body></html>"));
            exe.execute(new HttpsServer(group, 1234, "<!doctype html><html><head><title>Hello World</title></head><body></body></html>"));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }




}
