import java.net.*;
import java.io.*;
import java.util.*;

public class MultichatServer
{
	HashMap clients;

	MultichatServer(){
		clients = new HashMap();    //hashmap = key�� value�� ������.
		Collections.synchronizedMap(clients); //synchronized
	}

	public void open(){
		ServerSocket serverSocket = null;
		Socket socket = null;

		try
		{
			serverSocket=new ServerSocket(7777);         //�������� = 7777
			System.out.println("������ ���Ƚ��ϴ�.");

			while(true){
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+"]���� �����ϼ̽��ϴ�.");

				S_input thread = new S_input(socket);
				thread.start();
			}
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

//	void Msend(){


	void send(String msg){
		Iterator it = clients.keySet().iterator();
		
		while(it.hasNext()){
			try
			{
				DataOutputStream out = (DataOutputStream)clients.get(it.next());
				out.writeUTF(msg);
			}
			catch (IOException e)
			{
			}
		}
	}
	
	
	public static void main(String[] args){
		new MultichatServer().open();
	}

	class S_input extends Thread
	{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		S_input(Socket socket)
		{
			this.socket = socket;
			try
			{
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			}
			catch (IOException e)
			{
			}
		}

		public void run(){
			String name="";
			
			try
			{
				name = in.readUTF();
				send(name+"���� �����ϼ̽��ϴ�.");

//				Msend();

				clients.put(name, out);
				System.out.println("���� �����ڼ��� "+clients.size()+"�Դϴ�");
				
				while(in!=null){
					send(in.readUTF());
				}
			}
			catch (IOException e)
			{

			} finally{
				System.out.println(name+"���� �����ϼ̽��ϴ�.");
				send(name+"���� �����ϼ̽��ϴ�.");
				clients.remove(name);
			}
		}
	}
}