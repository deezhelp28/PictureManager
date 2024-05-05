package com.cavemann.picturemanager.picture;

import java.io.File;

public class Picture {

	private File file;
	private long size;
	private String hash;
	
	public Picture(File file, int size, String hash) {
		super();
		this.file = file;
		this.size = size;
		this.hash = hash;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}