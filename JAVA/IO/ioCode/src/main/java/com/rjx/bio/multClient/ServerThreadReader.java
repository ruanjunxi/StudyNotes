package com.rjx.bio.multClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   10:36
 * @Project ioCode
 * @description:
 */
public class ServerThreadReader extends Thread{
    private Socket socket;
    public ServerThreadReader(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        //3.从socket管道中得到一个字节输入流对象
        InputStream is = null;
        try {
            is = socket.getInputStream();
            //4.把字节输入流包装成一个缓存字符输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String msg;
            /*while ((msg = br.readLine()) != null){
            System.out.println("服务端接收到：" + msg);
            }*/
            while ((msg = br.readLine()) != null){
                System.out.println("服务端接收到："+  msg+" 来自客户端："+socket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
