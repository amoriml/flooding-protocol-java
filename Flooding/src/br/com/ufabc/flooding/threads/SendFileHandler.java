package br.com.ufabc.flooding.threads;

import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.util.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SendFileHandler implements Runnable {

	private Peer handlerPeer;
	private String path;
	private int port;

	SendFileHandler(Peer handler, String filePath, int port) {
		handlerPeer = handler;
		this.path = filePath;
		this.port = port;
	}

	@Override
    public void run(){
    	try {
    		//create server socket
	        ServerSocket serverSocket = new ServerSocket(port);
	        Socket socket = serverSocket.accept();
	        System.out.println("Thread: " + Thread.currentThread().getName() + ". Accepted connection : " + socket);
	        
	        //create buffer to output scream
	        File transferFile = new File(Constants.PREFIX_FILE_NAME + handlerPeer.getPeerName() + File.separator + path);
	        byte [] bytearray  = new byte [(int)transferFile.length()];
            FileInputStream fin = new FileInputStream(transferFile);
            BufferedInputStream bin = new BufferedInputStream(fin);
            bin.read(bytearray, 0, bytearray.length);
            OutputStream os = socket.getOutputStream();

            System.out.println((int)transferFile.length() + " <- size");
            //flush of file
	        os.write(bytearray,0,bytearray.length);
	        os.flush();
	        socket.close();
	        bin.close();
	        serverSocket.close();
	        System.out.println("Thread: " + Thread.currentThread().getName() + ". File transfer complete\n" + path+"\n");
    	}
    	catch (IOException e){
    		System.out.println("Console " + this.handlerPeer.getPeerName() +
    				": Erro na escrita de arquivo no buffer..." + "\n" + e);
    	}
 
    }
}