package br.com.ufabc.flooding.nodes;

import br.com.ufabc.flooding.DAL.PeerDALMock;
import br.com.ufabc.flooding.model.Request;
import br.com.ufabc.flooding.util.JSONParse;
import br.com.ufabc.flooding.util.PackageFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class ClientApp {

	public static void main(String[] args) throws IOException {
		System.out.println(
				"------------------------------ CLIENT APPLICATION -------------------------------------------------------");

		System.out.println("Console Cliente: Write exit to close this aplication");

		buscaArquivo();
		System.out.println("Good bye!.....");
	}

	private static void buscaArquivo() throws IOException {
		Scanner clientIn = new Scanner(System.in);
		String fileName;
		int ttl;
		do {
			System.out.print("\nConsole Cliente: Video file name(.mp4):");
			fileName = clientIn.next();

			System.out.print("\nConsole Cliente: Set TTL for resource:");
			ttl = clientIn.nextInt();
			System.out.println();

			Request req = new Request(fileName, ttl);
			System.out.println("Console Cliente: Iniciando busca do arquivo " + fileName);
			sendRequest(req);
		} while (!fileName.equals("exit"));
	}

	private static void sendRequest(Request request) throws IOException {
		Random rd = new Random();
		int randomIndex = rd.nextInt(9) + 1;
		int randomPort = new PeerDALMock().genereteNewPeer(randomIndex).getPort();

		DatagramSocket socket = PackageFactory.createSocket();
		request.setClientPort(socket.getLocalPort());
		String message = JSONParse.requestToJson(request);
		DatagramPacket out = PackageFactory.createPackage(message, "localhost", randomPort);

		socket.send(out);

		socket.setSoTimeout(10000);

		try {
			byte[] receiverBuffer = new byte[64000];
            DatagramPacket pacote = PackageFactory.createPackage(receiverBuffer);
			socket.receive(pacote);
			trata(pacote);
		}catch(SocketTimeoutException e) {
			System.out.println("Console Cliente: sendRequest error nao foi possivel localizar o arquivo com o ttl informado");
			socket.close();
		}
	}
	
	private static void trata(final DatagramPacket pacote) {
		Thread trataMensage = new Thread(() -> {

			int port = pacote.getPort();
			String data = new String(pacote.getData());
			System.out.println("Console Cliente: mensagem recebida de "
					+ pacote.getAddress() + ":" + port);
			System.out.println("Console Cliente: " + data);

			Request message = JSONParse.jsonToRequest(data.trim());
			try {
				trataMensagem(message, pacote.getAddress());
			} catch (IOException e) {
				System.out.println("Console Cliente: erro no metodo trata " + e.getMessage());
				e.printStackTrace();
			}
		});
        trataMensage.run();
	}

	private static void trataMensagem(Request message, InetAddress address) throws IOException {
		int port = message.getClientPort();
		String file = message.getFileName();
		System.out.println("Console Cliente: iniciando conexao com " + address+ ":" + port);
		System.out.println("Iniciando transferencia!");
		int filesize=1022386;
		int currentTot;
        
        Socket socket = new Socket(address, port);


        byte [] bytearray  = new byte [filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while ((currentTot = is.read(bytearray)) != -1) {
        	bos.write(bytearray, 0, currentTot);
        }
        bos.flush();
        bos.close();
        socket.close();
        System.out.println("Console Cliente trataMensagem: Transferencia finalizada!");
		System.out.println("Console Cliente: O arquivo pode ser encontrado em " +
				System.getProperty("user.dir"));
  }
}
