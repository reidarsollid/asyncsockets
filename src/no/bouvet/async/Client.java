package no.bouvet.async;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

/**
 * User: Reidar Sollid
 * Date: 4/11/12
 * Time: 9:31 AM
 */
public class Client {
    public Client() {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
            InetAddress address = InetAddress.getByName("localhost");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(address, 202000);
            socketChannel.connect(inetSocketAddress).get(); // Blocking

        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
