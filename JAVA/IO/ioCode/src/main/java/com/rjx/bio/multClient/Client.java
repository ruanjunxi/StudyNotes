package com.rjx.bio.multClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author junxi
 * @Date 2023/11/20   10:16
 * @Project ioCode
 * @description:
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);
        //2.从Socket对象中获取一个字节输出流
        OutputStream os = socket.getOutputStream();
        //3.把字节输出流包装成一个打印流
        PrintStream ps = new PrintStream(os);
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.print("请说：");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }
    }
}
