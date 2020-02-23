package br.com.ufabc.flooding.nodes;

import br.com.ufabc.flooding.DAL.PeerDALMock;
import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.threads.FileSystemHandler;
import br.com.ufabc.flooding.threads.PeerReciveHandler;
import br.com.ufabc.flooding.util.ExecuteThreadFactory;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeerApp {

	public static void main(String[] args) {
		int peerNumber;
		if(args != null && args.length > 0 && args[0] != null){
			peerNumber = Integer.parseInt(args[0]);
			System.out.println("Console Peer: New peer initialization: number "+ peerNumber);
		} else {
			System.out.println("Console Peer: Write the name of peer: ");
			Scanner teclado = new Scanner(System.in);
			peerNumber = teclado.nextInt();
		}
		Peer peer = new PeerDALMock().genereteNewPeer(peerNumber);

		System.out.println("Console Peer: Starting " + peer.getPeerName() + " at localhost::" + peer.getPort() + "...");
		

		ScheduledExecutorService executorFileSystem = Executors
				.newSingleThreadScheduledExecutor(new ExecuteThreadFactory("fileSystem"));
		

		ScheduledExecutorService executorListen = Executors
				.newSingleThreadScheduledExecutor(new ExecuteThreadFactory("peerRecive"));

		executorFileSystem.scheduleAtFixedRate(new FileSystemHandler(peer), 0, 3, TimeUnit.SECONDS);
		executorListen.scheduleAtFixedRate(new PeerReciveHandler(peer), 0, 1, TimeUnit.SECONDS);
	}
}
