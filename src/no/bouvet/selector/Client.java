package no.bouvet.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * User: Reidar Sollid
 * Date: 8/19/12
 * Time: 1:15 PM
 */
public class Client {
    public Client() {
        SocketChannel socketChannel = null;
        try {
            String message = "A message from client " + System.currentTimeMillis();
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 9999));
            ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    System.out.println("What to do???");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
