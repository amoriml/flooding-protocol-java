package br.com.ufabc.flooding.model;

import br.com.ufabc.flooding.DAL.PeerDALMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Peer {

	private List<FileAtt> filesStateList;
	private List<Request> requests;
	private String peerName;
	private int port;
	private List<Integer> neighborPeers;
	
	public Peer(String name, int port, List<Integer> neighborPeers) {
		super();
		this.peerName = name;
		this.port = port;
		this.neighborPeers = neighborPeers;
		this.requests = new ArrayList<>();
		this.filesStateList = new ArrayList<>();
	}

	public List<FileAtt> getStateOfNode() {
		return filesStateList;
	}

	public List<Request> getRequests() {
		return this.requests;
	}

	public void addRequest(Request e) {
		this.requests.add(e);
	}

	public void setStateOfNode(List<FileAtt> stateOfNode) {
		this.filesStateList = stateOfNode;
	}

	public boolean containsMessage(UUID requestId) {
		return this.getRequests()
				.stream().
				anyMatch((x) -> x.getRequestId().equals(requestId));
	}

	public boolean contaisFile(String fileName) {
		return this.getStateOfNode()
					.stream()
					.anyMatch((x) -> x.nome.equals(fileName));
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String nodeName) {
		this.peerName = nodeName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<Integer> getNeighborPeers() {
		return neighborPeers;
	}

	public void setNeighborPeers(List<Integer> neighborPeers) {
		this.neighborPeers = neighborPeers;
	}

	public Peer SortNeighbor() {
		Random rd = new Random();
		int randomPeer = this.getNeighborPeers().get(rd.nextInt(this.getNeighborPeers().size()));
		return new PeerDALMock().genereteNewPeer(randomPeer);
		
	}

}
