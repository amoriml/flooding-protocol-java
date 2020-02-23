package br.com.ufabc.flooding.util;

import java.util.concurrent.ThreadFactory;

public class ExecuteThreadFactory implements ThreadFactory {
	private int counter = 0;
	private String prefix;

	public ExecuteThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	public Thread newThread(Runnable r) {
		return new Thread(r, prefix + "-" + counter++);
	}
}
