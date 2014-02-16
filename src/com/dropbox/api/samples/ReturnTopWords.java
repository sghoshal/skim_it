package com.dropbox.api.samples;

public class ReturnTopWords implements Comparable<ReturnTopWords> {
	public String word;
	public float value;
	@Override
	public String toString() {
		return "ReturnTopWords [word=" + word + ", value=" + value + "]";
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public ReturnTopWords(String word, float value) {
		super();
		this.word = word;
		this.value = value;
	}
	@Override
	public int compareTo(ReturnTopWords arg0) {
		if(arg0.value > value)
			return 1;
		else
			return -1;
	}
	
	
}
