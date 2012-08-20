package no.bouvet.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private Logger logger = Logger.getLogger(Server.class.getName());
    private CharsetDecoder decoder;

    public Server() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            Charset charset = Charset.forName("ISO-8859-1");
            decoder = charset.newDecoder();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9999));
            Selector selector = Selector.open();
            while (!Thread.currentThread().isInterrupted()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                handleServerSocket(selector);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        } finally {
            assert serverSocketChannel != null;
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "What to do ??", e);
            }
        }
    }

    private void handleServerSocket(Selector selector) throws IOException {
        int select = selector.select();
        if (select != 0) {
            Set<SelectionKey> selectionKeys = new CopyOnWriteArraySet<>(selector.selectedKeys());
            ByteBuffer byteBuffer = ByteBuffer.allocate(255);
            byteBuffer.clear();
            for (SelectionKey selectionKey : selectionKeys) {
                selectionKeys.remove(selectionKey);
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                channel.read(byteBuffer);
                byteBuffer.flip();
                CharBuffer charBuffer = decoder.decode(byteBuffer);
                logger.log(Level.INFO, charBuffer.toString());
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
