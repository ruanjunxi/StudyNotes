package com.rjx.bio.file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   15:03
 * @Project ioCode
 * @description:
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        while(true){
            Socket socket = ss.accept();
            new ServerReadThread(socket).start();
        }
    }

}
