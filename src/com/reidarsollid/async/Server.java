package com.reidarsollid.async;

import javax.print.attribute.standard.Severity;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private Logger logger = Logger.getLogger(Severity.class.getName());
    private CharsetDecoder decoder;

    public Server() {
        Charset charset = Charset.forName("ISO-8859-1");
        decoder = charset.newDecoder();
        InetSocketAddress address = new InetSocketAddress("localhost", 20200);
        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(address)) {
            Future<AsynchronousSocketChannel> channelFuture = serverSocketChannel.accept();
            AsynchronousSocketChannel socketChannel = channelFuture.get(); // Blocking
            clientHandler(socketChannel);
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private void clientHandler(AsynchronousSocketChannel socketChannel) throws ExecutionException, InterruptedException, CharacterCodingException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        socketChannel.read(byteBuffer).get();
        byteBuffer.flip();
        CharBuffer answer = decoder.decode(byteBuffer);
        logger.log(Level.INFO, answer.toString());
    }

    public static void main(String[] args) {
        new Server();
    }
}
