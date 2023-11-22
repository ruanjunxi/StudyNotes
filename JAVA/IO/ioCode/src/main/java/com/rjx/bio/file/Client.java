package com.rjx.bio.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Author junxi
 * @Date 2023/11/20   15:03
 * @Project ioCode
 * @description:
 */
public class Client {
    public static void main(String[] args) {
        try(InputStream is = new
                FileInputStream("D:\\rjx\\Doc\\JAVA\\IO\\ioCode\\src\\main\\resources\\ClientFile\\1.png");){
            //1.请求与服务端的Socket连接
            Socket socket = new Socket("127.0.0.1", 8888);
            //2.把字节输出流包装成一个数据输出流(DataOutputStream可以做分段数据发送)
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            //3.先发送上传文件的后缀给服务器
            dos.writeUTF(".png");
            //4.把文件数据发送给服务端进行接收
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0){
                dos.write(buffer,0,len);
            }
            dos.flush();
            socket.shutdownOutput();//通知服务端，我客户端这边的数据已经发送完毕了
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
