package br.com.ufabc.flooding.model;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FileAtt {
	public String nome;
	public Date dataCriacao;
	public Date ultimoAcesso;
	public long tamanhoArquivo;
	public Date ultimaModificacao;
	public Object fileKey;
	public String path;

	public FileAtt() {

	}

	public FileAtt(BasicFileAttributes atributes, String fileName, String fullPath) {
		dataCriacao = new Date(atributes.creationTime().toMillis());
		ultimoAcesso = new Date(atributes.lastAccessTime().toMillis());
		tamanhoArquivo = atributes.size();
		nome = fileName;
		ultimaModificacao = new Date(atributes.lastModifiedTime().toMillis());
		fileKey = atributes.fileKey();
		this.path = fullPath;
		
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FileAtt)) {
			return false;
		}
		return this.getFileKey().toString().equals(((FileAtt) obj).getFileKey().toString());
	}

	public Object getFileKey() {
		return fileKey;
	}
}
