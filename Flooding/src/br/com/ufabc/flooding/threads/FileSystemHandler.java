package br.com.ufabc.flooding.threads;

import br.com.ufabc.flooding.model.FileAtt;
import br.com.ufabc.flooding.model.Peer;
import br.com.ufabc.flooding.util.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHandler implements Runnable {

	private String folderPath;
	private Peer handlerPeer;

	public FileSystemHandler(Peer handlerPeer) {
		this.folderPath = Constants.PREFIX_FILE_NAME
				+ handlerPeer.getPeerName();
		this.handlerPeer = handlerPeer;
	}

	@Override
	public void run() {
		try {
			File folder = new File(folderPath);
			File[] filesList = folder.listFiles();
			List<FileAtt> stateOfNode = new ArrayList<>();
			if (filesList != null) {
				for (File f : filesList) {
					BasicFileAttributes atributes = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
					FileAtt fileAtt = new FileAtt(atributes, f.getName(), f.getAbsolutePath());
					stateOfNode.add(fileAtt);
					System.out.println("FileSystemHandler " + handlerPeer.getPeerName() + ": thread "
							+ Thread.currentThread().getName() + ":arquivo: " + f.getName() + " atualizado");
				}
				this.handlerPeer.setStateOfNode(stateOfNode);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("FileSystemHandler" + handlerPeer.getPeerName() + ": thread: "
					+ Thread.currentThread().getName() + " Exception: " + e.getClass() + "\n");
            e.getStackTrace();
			throw new RuntimeException("File reader Exception", e);
		}
	}

	public String getFolderPath() {
		return this.folderPath;
	}
}
