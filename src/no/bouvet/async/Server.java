package no.bouvet.async;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * User: Reidar Sollid
 * Date: 4/11/12
 * Time: 9:02 AM
 */
public class Server {
    public Server() {
        try {
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(null); // null binds a free port
            Future<AsynchronousSocketChannel> channelFuture = serverSocketChannel.accept();
            AsynchronousSocketChannel socketChannel = channelFuture.get(); // Blocking
            // //channelFuture.get(10, TimeUnit.SECONDS);

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
