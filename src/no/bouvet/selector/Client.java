package no.bouvet.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private Logger logger = Logger.getLogger(Client.class.getName());

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
            logger.log(Level.WARNING, e.getMessage(), e);
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "What to do???", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
