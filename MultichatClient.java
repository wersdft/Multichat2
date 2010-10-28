import java.net.*;
import java.io.*;
import java.util.*;

public class MultichatClient
{
	public static void main(String[] args){
		try
		{
			Scanner scanner = new Scanner(System.in);
			System.out.print("����� ID�Է� : ");
			String name = scanner.nextLine();
			System.out.print("������ Ip�Է� : ");
			String serverIp = scanner.nextLine();
			Socket socket=new Socket(serverIp,7777);

			System.out.println("����Ϸ�");

			Thread sender = new Thread(new ClientSender(socket, name));
			Thread receiver = new Thread(new ClientReceiver(socket));

			sender.start();
			receiver.start();
		}
		catch (Exception e)
		{
		}
	}

	static class ClientSender extends Thread
	{
		Socket socket;
		DataOutputStream out;                         // �Է� �������� 
		String name;

		ClientSender(Socket socket,String name){
			this.socket=socket;
			try
			{
				out = new DataOutputStream(socket.getOutputStream());
				this.name=name;
			}
			catch(Exception e){}
		}



		public void run(){
			Scanner scanner = new Scanner(System.in);
			try
			{	
				if (out!=null)
				{
					out.writeUTF(name);
				}

				while(out !=null){
					out.writeUTF("["+name+"]"+scanner.nextLine());
				}

				
			}
			catch (IOException e)
			{
			}
		}
	}

	static class ClientReceiver extends Thread               // ��� Ŭ����
	{
		Socket socket;
		DataInputStream in;

		ClientReceiver(Socket socket){
			this.socket = socket;
			try
			{
				in = new DataInputStream(socket.getInputStream());
			}
			catch (IOException e)
			{
			}
		}

		public void run(){
			try
			{
				while(true){
				System.out.println('\n'+in.readUTF()+'\n');
				}
			}
			catch (IOException e)
			{
			}
		}
	}
}
