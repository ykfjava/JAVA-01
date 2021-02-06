package ykf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackendServer {
    // 后台服务端口
    public static final int BACKEND_PORT = 8088;
    // 运行标志位
    public static final AtomicBoolean RunningFlag = new AtomicBoolean(true);

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final ServerSocket serverSocket = new ServerSocket(BACKEND_PORT);
        while (RunningFlag.get()) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 循环退出之后关闭服务
        serverSocket.close();
        executorService.shutdown();
    }

    private static void service(Socket socket) {
        Set a = new HashSet();
        Set b = new LinkedHashSet();
        // TODO - 接收报文-解析Http头
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[8 * 1024];
            int len = inputStream.read(buffer);
            outputStream.write(buffer, 0, len);
            //
            byte[] input = outputStream.toByteArray();
            String inputContent = new String(input, "UTF-8");
            System.out.println("BackendServer收到请求:\n" + inputContent);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.flush();
            printWriter.close();
            //socket.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
