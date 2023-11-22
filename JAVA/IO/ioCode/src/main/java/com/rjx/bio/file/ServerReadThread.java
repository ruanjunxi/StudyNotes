package com.rjx.bio.file;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @Author junxi
 * @Date 2023/11/20   15:19
 * @Project ioCode
 * @description:
 */
public class ServerReadThread extends Thread{
    private Socket socket;
    public ServerReadThread(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run(){
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String suffix = dis.readUTF();
            System.out.println("服务端已经成功接收到了文件类型：" + suffix);
            //3.定义一个字节输出管道，负责把客户端发来的文件数据写出去
            OutputStream os = new FileOutputStream("D:\\rjx\\Doc\\JAVA\\IO\\ioCode\\src\\main\\resources\\serverFile\\"
                    +UUID.randomUUID().toString()+suffix);
            byte[] buffer = new byte[1024];
            int len;
            while((len=dis.read(buffer))>0){
                os.write(buffer);
            }
            os.close();
            System.out.println("服务端接收文件保存成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
