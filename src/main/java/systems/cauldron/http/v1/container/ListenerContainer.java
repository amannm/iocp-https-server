/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.container;

import systems.cauldron.http.v1.http.state.HttpRequestParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//import ontoserv.connection.HTTPChannel;

/**
 * @author admin
 */
public class ListenerContainer {

    private static final Logger logger = Logger.getLogger("Listener Container");
    private int numThreads = 4;
    private List<SocketListener> listenerList = new ArrayList<>();
    private AsynchronousChannelGroup group;

    public ListenerContainer() {
//        try {
//            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
//
//            // Step 2: Define a class to be loaded.
//
//            String classNameToBeLoaded = "net.viralpatel.itext.pdf.DemoClass";
//
//
//            // Step 3: Load the class
//
//            Class myClass = myClassLoader.loadClass(classNameToBeLoaded);
//
//
//            // Step 4: create a new instance of that class
//
//            Object whatInstance = myClass.newInstance();
//
//            String methodParameter = "a quick brown fox";
//
//            // Step 5: get the method, with proper parameter signature.
//            // The second parameter is the parameter type.
//            // There can be multiple parameters for the method we are trying to call,
//            // hence the use of array.
//
//            Method myMethod = myClass.getMethod("demoMethod", new Class[]{String.class});
//
//
//            // Step 6:
//            // Calling the real method. Passing methodParameter as
//            // parameter. You can pass multiple parameters based on
//            // the signature of the method you are calling. Hence
//            // there is an array.
//
//            String returnValue = (String) myMethod.invoke(whatInstance, new Object[]{methodParameter});
//
//            System.out.println("The value returned from the method is:" + returnValue);
//        } catch (SecurityException | IllegalArgumentException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            logger.severe(e.getMessage());
//        }
        //
        // create a class loader loading from "foo.jar"
        //
//       URL url = new URL("file:foo.jar");
//       URLClassLoader loader = new URLClassLoader (new URL[] {url});
//       Class cl = Class.forName ("Foo", true, loader);
//       Runnable foo = (Runnable) cl.newInstance();
//       foo.run();
//       loader.close ();
//
//       // foo.jar gets updated somehow
//
//       loader = new URLClassLoader (new URL[] {url});
//       cl = Class.forName ("Foo", true, loader);
//       foo = (Runnable) cl.newInstance();
//       // run the new implementation of Foo
//       foo.run();

        try {
            group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(numThreads));
            SocketListener listener2 = new SocketListener("HTTP Listener", new InetSocketAddress(8080), group);
            listener2.registerListener(new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
                @Override
                public void completed(final AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                    attachment.accept(attachment, this);
                    HttpRequestParser parser = new HttpRequestParser(result, ByteBuffer.allocateDirect(4096));
                }

                @Override
                public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            listenerList.add(listener2);
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    public void start() {
        for (SocketListener listener : listenerList) {
            try {
                listener.start();
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }
        }
        try {
            logger.info("Waiting for thread termination...");
            group.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.severe(ex.getMessage());
        } finally {
            logger.info("Threads terminated. Shutting down group");
            group.shutdown();
        }

    }
}
