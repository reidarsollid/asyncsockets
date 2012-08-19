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

/**
 * User: Reidar Sollid
 * Date: 8/19/12
 * Time: 1:15 PM
 */
public class Server {
    private CharsetDecoder decoder;

    public Server() {
        try {
            Charset charset = Charset.forName("ISO-8859-1");
            decoder = charset.newDecoder();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9999));
            Selector selector = Selector.open();
            while (!Thread.currentThread().isInterrupted()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                handleServerSocket(selector);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerSocket(Selector selector) throws IOException {
        int select = selector.select();
        if (select == 1) {
            Set<SelectionKey> selectionKeys = new CopyOnWriteArraySet<>(selector.selectedKeys());
            ByteBuffer byteBuffer = ByteBuffer.allocate(255);
            byteBuffer.clear();
            for (SelectionKey selectionKey : selectionKeys) {
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                channel.read(byteBuffer);
                byteBuffer.flip();
                CharBuffer charBuffer = decoder.decode(byteBuffer);
                System.out.println(charBuffer.toString());
                selectionKeys.remove(selectionKey);
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
