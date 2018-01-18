package top.neusoftware.HTTPServer;

/**
 * Created by SuPhoebe on 2015/12/27.
 * 服务器线程处理类
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread {
    // 和本线程相关的Socket
    Socket socket = null;
    BufferedReader reader;
    PrintWriter writer;
    String workSpace;
    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        writer=new PrintWriter(socket.getOutputStream());
        workSpace="D:/";
    }

    //线程执行的操作，响应客户端的请求
    /*public void run(){
        InputStream is=null;
        InputStreamReader isr=null;
        BufferedReader reader=null;
        OutputStream os=null;
        PrintWriter pw=null;
        try {
            //获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String info=null;
            while((info=br.readLine())!=null){//循环读取客户端的信息
                System.out.println("我是服务器，客户端说："+info);
            }
            socket.shutdownInput();//关闭输入流
            //获取输出流，响应客户端的请求
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write("欢迎您！");
            pw.flush();//调用flush()方法将缓冲输出
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //关闭资源
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    public void run() {
    	DataOutputStream dos=null;
    	FileInputStream in = null;
    	try {
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String firstLineOfRequest = null;
    	try {
			firstLineOfRequest =reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String uri=firstLineOfRequest.split(" ")[1];
    	File file=new File(workSpace+uri);
    	if(file.exists()) {
	    	writer.println("HTTP/1.1 200 OK");
	    	if(uri.endsWith(".html")) {
	    		writer.println("Content-Type:text/html");
	    	}
	    	else if(uri.endsWith(".jpg")) {
	    		writer.println("Content-Type:image/jpg");
	    	}
	    	else {
	    		writer.println("Content-Type:application/octet-stream");
	    	}
	    	try {
				in=new FileInputStream(file);
				writer.println("Content-Length:"+in.available());
				writer.println();//空行结束头消息
				writer.flush();	
		    	byte[] b = new byte[1024];
	            int len = 0;      
            	len = in.read(b);
	            while(len!=-1)
	            {
					dos.write(b, 0, len);
					len =  in.read(b);
	            }
	            dos.flush();	
            }catch(Exception e) {
            	e.printStackTrace();
            }
    	}
    	else {
    		writer.println("HTTP/1.1 404 Not Found");
    		writer.println("Content-Type:text/plain");	
			writer.println("Content-Length:13");	
			writer.println();
			  //发送响应体
			writer.print("404 Not found");
		    writer.flush();
    	}
    }
}