package com.dropbox.api.samples;

import java.util.Comparator;

public class IndexComparator implements Comparator<SentenceObject>{
    @Override
	public int compare(SentenceObject o1, SentenceObject o2)
	{
    	 return (o1.indexNumber>o2.indexNumber ? 1 : (o1.indexNumber==o2.indexNumber ? 0 : -1));
	}
}