package com.rjx.bio.threadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   14:50
 * @Project ioCode
 * @description:
 */
public class Server {
    public static void main(String[] args) {
        HandlerSocketServerPool pool = new HandlerSocketServerPool(3,10);
        try {
            ServerSocket ss = new ServerSocket(9999);

            while(true){
                Socket socket = ss.accept();
                Runnable target = new ServerRunnableTarget(socket);
                pool.execute(target);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
