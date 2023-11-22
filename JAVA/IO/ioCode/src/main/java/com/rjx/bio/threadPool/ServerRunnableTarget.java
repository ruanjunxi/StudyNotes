package com.rjx.bio.threadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   14:42
 * @Project ioCode
 * @description:
 */
public class ServerRunnableTarget implements Runnable{
    Socket socket;

    public ServerRunnableTarget(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        try {
            is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String msg;
            while((msg=br.readLine())!=null){
                System.out.println("服务端收到：" + msg +"来自端口："+socket.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
