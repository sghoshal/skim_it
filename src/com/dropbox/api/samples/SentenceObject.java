package com.dropbox.api.samples;


public class SentenceObject implements Comparable<SentenceObject> {
	String content;
	int indexNumber;
	String fileName;
	float score;
	
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public SentenceObject(String content, int indexNumber, String fileName) {
		super();
		this.content = content;
		this.indexNumber = indexNumber;
		this.fileName = fileName;
		this.score = 0;
	}
	
	public SentenceObject(String content, int indexNumber, String fileName,
			int score) {
		super();
		this.content = content;
		this.indexNumber = indexNumber;
		this.fileName = fileName;
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "SentenceObject [content=" + content + ", indexNumber="
				+ indexNumber + ", fileName=" + fileName + ", score=" + score
				+ "]";
	}
	public int compareTo(SentenceObject obj)
	{
		if(obj.score > score)
			return 1;
		else
			return -1;
			
	}
	
}
