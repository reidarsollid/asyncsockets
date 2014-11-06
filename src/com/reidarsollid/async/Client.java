package com.reidarsollid.async;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private Logger logger = Logger.getLogger(Client.class.getName());

    public Client() {
        try (AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open()) {
            InetAddress address = InetAddress.getByName("localhost");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 20200);
            socketChannel.connect(inetSocketAddress).get(); // Blocking, returning a Void wrapper class
            Future<Integer> write = socketChannel.write(ByteBuffer.wrap("Hello NIO2".getBytes("ISO-8859-1")));
            System.out.println(write.get());
        } catch (IOException | ExecutionException | InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
