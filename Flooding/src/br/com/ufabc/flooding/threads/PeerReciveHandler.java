package br.com.ufabc.flooding.threads;

import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.model.Request;
import br.com.ufabc.flooding.util.JSONParse;
import br.com.ufabc.flooding.util.PackageFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

public class PeerReciveHandler implements Runnable {

    private DatagramSocket socket;
    private Peer peer;
    private DatagramPacket pacote;
    private boolean isAlive;

    public PeerReciveHandler(Peer peer) {
        this.peer = peer;
        this.isAlive = true;
        try {
            socket = PackageFactory.createSocket(peer.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("PeerReciveHandler: Erro ao criar o socket\n" + "\n" + e.getClass() + "\n"
                    + e.getMessage() + "\n");
            e.getStackTrace();
            throw new RuntimeException("Peer reciver", e);
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[64000];
            DatagramPacket pacote = PackageFactory.createPackage(buffer);

            System.out.println("PeerReciveHandler " + peer.getPeerName() + ": ouvindo em " + peer.getPort());
            socket.receive(pacote);

            System.out.println("PeerReciveHandler " + peer.getPeerName() + ": recebido -->\n" + new String(pacote.getData())
                    + "\nFrom: " + pacote.getAddress() + ":" + pacote.getPort() + "\nPacote size:"
                    + pacote.getLength());

            Thread trataMensagem = new Thread(() -> {
                String data = new String(pacote.getData());
                int port = pacote.getPort();
                try {
                    System.out.println(
                            "Console peer " + peer.getPeerName() + ": Recebimento de mensagem de " + port + "\n");
                    Request message = JSONParse.jsonToRequest(data.trim());
                    trataMensagem(message);

                } catch (Exception e) {
                    System.out.println("Peer recive message Exception:\n"
                            + e.getCause() + "\n" + e.getMessage() + "\n"
                            + e.getClass() + "\n");
                    e.getStackTrace();
                }

            });
            trataMensagem.run();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Peer recive Exception", e);
        }
    }

    public void killThread() {
        this.isAlive = false;
        socket.close();
    }

    private void trataMensagem(Request message) {
        String fileName = message.getFileName();
        UUID requestId = message.getRequestId();

        if (!peer.containsMessage(requestId)) {
            peer.addRequest(message);
            if (peer.contaisFile(fileName)) {
                enviaArquivo(message, fileName);
            } else {
                if (message.getTtl() > 0) {
                    encaminhaMensagem(message);
                } else {
                    System.out.println("Thread: " + Thread.currentThread().getName() + ". Console peer " + peer.getPeerName() + " : Recebendo pesquisa "
                            + message.getFileName() + ". Não tenho o arquivo, TTL=0 a mensagem não será encaminhada");
                }
            }
        } else {
            System.out.println("Thread: " + Thread.currentThread().getName() +
                    ". Console peer " + peer.getPeerName() + ": A consulta recebida já foi enviada anteriormente.");
        }
    }

    private void encaminhaMensagem(Request message) {
        System.out.println("Thread: " + Thread.currentThread().getName() + ". Console peer " + peer.getPeerName() + " : Recebendo pesquisa "
                + message.getFileName() + ". Não tenho o arquivo, encaminhando mensagem...");

        message.setTtl(message.getTtl() - 1);
        SendRequestHandler sender = new SendRequestHandler(message, peer);
        sender.run();
    }

    private void enviaArquivo(Request message, String fileName) {
        System.out.println("Thread: " + Thread.currentThread().getName() + ". Console peer " + peer.getPeerName() + " : Recebendo pesquisa "
                + fileName + ". Tenho o arquivo, encaminhando para o cliente");
        SendResponseHandler sender = new SendResponseHandler("localhost", message.getClientPort(), message.getFileName(), peer);
        sender.run();
        System.out.println(fileName);
        SendFileHandler handler = new SendFileHandler(peer, fileName, sender.getPort());
        handler.run();
    }
}
