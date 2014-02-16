package com.dropbox.api.samples;

public class FileDetails {
	
	private String fileName;
	private String link;
	
	public FileDetails(String fileName, String link) {
		this.fileName = fileName;
		this.link = link;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return fileName;
	}
	
	@Override
	public boolean equals(Object o) {
		FileDetails obj = (FileDetails) o;	
		return (fileName.equals(obj.getFileName()) && link.equals(obj.getLink()));
	}
}
