package no.bouvet.async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * User: Reidar Sollid
 * Date: 4/11/12
 * Time: 9:02 AM
 */
public class Server {
    private CharsetDecoder decoder;

    public Server() {
        Charset charset = Charset.forName("ISO-8859-1");
        decoder = charset.newDecoder();
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 20200);
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(address); // null binds a free port
            Future<AsynchronousSocketChannel> channelFuture = serverSocketChannel.accept();
            AsynchronousSocketChannel socketChannel = channelFuture.get(); // Blocking
            clientHandler(socketChannel);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void clientHandler(AsynchronousSocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        try {
            socketChannel.read(byteBuffer).get();
            System.out.println("Something is gotten");
            byteBuffer.flip();
            CharBuffer answer = decoder.decode(byteBuffer);
            System.out.println(answer.toString());
        } catch (InterruptedException | ExecutionException | CharacterCodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
