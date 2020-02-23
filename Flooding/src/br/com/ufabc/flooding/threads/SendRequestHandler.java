package br.com.ufabc.flooding.threads;

import br.com.ufabc.flooding.DAL.PeerDALMock;
import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.model.Request;
import br.com.ufabc.flooding.util.JSONParse;
import br.com.ufabc.flooding.util.PackageFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.List;

public class SendRequestHandler implements Runnable {

    private String destinyHost;
    private Request request;
    private Peer handlerPeer;

    SendRequestHandler(Request msg, Peer handler) {
        this.destinyHost = "localhost";
        this.handlerPeer = handler;
        this.request = msg;
    }

    @Override
    public void run() {
        try {
            String message = JSONParse.requestToJson(request);
            DatagramSocket socket = PackageFactory.createSocket();
            List<Integer>  vizinhosPeer = handlerPeer.getNeighborPeers();
            Peer destinyPeer;
            int destinyPort;
            
            for(Integer indice : vizinhosPeer) {
            	destinyPeer = new PeerDALMock().genereteNewPeer(indice);
            	destinyPort = destinyPeer.getPort();
            	System.out.println("Thread: " + Thread.currentThread().getName() + " " + 
                        handlerPeer.getPeerName() + ": encaminhando para " + destinyPeer.getPeerName());
            	socket.send(PackageFactory.createPackage(message, destinyHost, destinyPort));
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SendHandler " + handlerPeer.getPeerName() + "\n" + "Thread: " + 
                Thread.currentThread().getName() + "Erro no Handler");
            throw new RuntimeException("Send message Exception", e);
        }
    }

}
