package br.com.ufabc.flooding.model;

import java.util.UUID;

public class Request {
	
	private UUID requestId;
	
	private int clientPort;
	
	private int ttl;
	
	private String fileName;

	public Request() {

	}
	
	public Request(String fileName, int ttl) {
		this.requestId = UUID.randomUUID();
		this.fileName = fileName;
		this.ttl = ttl;
	}

	public int getClientPort() {
		return this.clientPort;
	}

	public void setClientPort(int port) {
		this.clientPort = port;
	}

	public UUID getRequestId() {
		return this.requestId;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}