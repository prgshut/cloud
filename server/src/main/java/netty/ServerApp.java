package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerApp {
    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
        ServerBootstrap boot=new ServerBootstrap();
        boot.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new OutByteBufHandel(), new ReadCommand(), new PutHandel(), new GetHandel()
                        );
                    }

                });
            System.out.println("start server");
        ChannelFuture f = boot.bind(8189).sync(); // запуск прослушивания порта 8189 для подключения клиентов
        f.channel().closeFuture().sync(); // ожидание завершения работы сервера
    } finally {
        workGroup.shutdownGracefully(); // закрытие пула
        bossGroup.shutdownGracefully(); // закрытие пула
    }


    }

    public static void main(String[] args) throws InterruptedException {
        new ServerApp().run();
    }
}
