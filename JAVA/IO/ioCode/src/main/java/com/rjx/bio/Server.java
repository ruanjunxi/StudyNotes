package com.rjx.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   10:17
 * @Project ioCode
 * @description:
 */
public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("===服务端启动===");
            //1.定义一个ServerSocket对象进行服务端的端口注册
            ServerSocket ss = new ServerSocket(9999);
            //2. 监听客户端的Socket连接请求
            Socket socket = ss.accept();
            //3.从socket管道中得到一个字节输入流对象
            InputStream is = socket.getInputStream();
            //4.把字节输入流包装成一个缓存字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String msg;
            /*while ((msg = br.readLine()) != null){
            System.out.println("服务端接收到：" + msg);
            }*/
            while ((msg = br.readLine()) != null){
                System.out.println("服务端接收到：" + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
