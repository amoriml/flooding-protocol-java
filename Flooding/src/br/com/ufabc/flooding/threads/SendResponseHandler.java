package br.com.ufabc.flooding.threads;

import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.model.Request;
import br.com.ufabc.flooding.util.JSONParse;
import br.com.ufabc.flooding.util.PackageFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

public class SendResponseHandler implements Runnable {

	private String destinyHost;
    private int destinyPort;
    private Request request;
    private Peer handlerPeer;
    
	SendResponseHandler(String address, int clientPort, String fileName, Peer peer) {
		this.destinyHost = address;
		this.destinyPort = clientPort;
		this.request = new Request(fileName, 1);
		request.setClientPort(localPortFree());
		this.handlerPeer = peer;
	}

	private int localPortFree() {
		try(ServerSocket s = new ServerSocket(0)) {
			int port = s.getLocalPort();
			System.out.println("listening on port: " + s.getLocalPort());
			return port;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return localPortFree();
	}

	int getPort() {
		return request.getClientPort();
	}
	@Override
	public void run() {
		try {
            String message = JSONParse.requestToJson(request);
            
            DatagramSocket socket = PackageFactory.createSocket();
            socket.send(PackageFactory.createPackage(message, destinyHost, destinyPort));
            System.out.println(message + "\n\n");
            socket.close();
            System.out.println("SendHandler " + handlerPeer.getPeerName() + "\n" + "Thread: " + Thread.currentThread().getName()
                    + ": Resposta enviada");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SendHandler " + handlerPeer.getPeerName() + "\n" + "Thread: " + Thread.currentThread().getName()
                    + "Erro no Handler");
            throw new RuntimeException("Send message Exception", e);
        }
	}

}
