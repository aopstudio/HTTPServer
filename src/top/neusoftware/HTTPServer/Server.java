package top.neusoftware.HTTPServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket ss;
	Socket s;
	
	public Server()  {
		try {
			ss=new ServerSocket(8888);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		while(true) {
			try {
				s = ss.accept();
				ServerThread st=new ServerThread(s);
				st.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
