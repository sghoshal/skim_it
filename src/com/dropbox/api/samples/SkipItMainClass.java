package com.dropbox.api.samples;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class SkipItMainClass {
	public static String APICall(List<String> inputFromDhyanam, int numberOfLinesFromDhyanam) throws InterruptedException
	{
/*		
		 * Prepare input from the previous part
		 
		ArrayList<String> inputFromDhyanam = new ArrayList<String>();		

		StringBuffer information = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader("resources/nirmal.txt"));
			while((line = br.readLine())!= null)
			{
				information.append(line+"\n");
			}
			inputFromDhyanam.add(new String(information));
			
			br = new BufferedReader(new FileReader("resources/war.txt"));
			while((line = br.readLine())!=null)
			{
				information.append(line+"\n");				
			}
			inputFromDhyanam.add(new String(information));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Input file has been put into an arraylist of strings");
		
		
		int numberOfLinesFromDhyanam = 20;
*/		
		/*
		 * Prepare the IDF hashMap
		 */
		String idfPaths = "resources/idf.txt";
		HashMap<String, Float> idfCounts = getIdf(idfPaths);
		System.out.println("We have the idf back");
		
		/*
		 * Prepare the TFIDF hashMap from the list of various files the user has selected
		 */
		HashMap<String, Float> tfIdfCounts = getTfIdf(idfCounts, inputFromDhyanam);
		System.out.println("We have the tfidf scores back");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		 * Prepare a list of sentenceObjects (content, fileInfo, chronological information) which will be used later. A score of zero is being associated with them. 
		 */
		ArrayList<SentenceObject> allInformation = new ArrayList<SentenceObject>();
		int lineIdentifier = 0;
		for(int i=0;i<inputFromDhyanam.size();i++)
		{
			String[] allLines = inputFromDhyanam.get(i).split("\n");
			for(String eachLine : allLines)
			{
				allInformation.add(new SentenceObject(eachLine,++lineIdentifier,new String(i+"")));
			}
		}
		
		/*
		 * Assigning scores to the sentence based on the TF-IDF scores of the words in it. 
		 */

		float sentScore = 0.0f;

		System.out.println("Assigning sentence scores");
		try
		{
			Thread.sleep(1000);
		}
		catch(Exception e)
		{
			
		}
		for(SentenceObject currentSentence : allInformation)
		{
			sentScore = 0.0f;
			String[] words = currentSentence.content.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" +");
			for(String word : words)
			{
				if(word.compareTo("")==0)
					continue;
				
				if(tfIdfCounts.get(word) == null)
					System.out.println(word);
				else
					sentScore += tfIdfCounts.get(word);
			}
			sentScore = sentScore/words.length;
			currentSentence.setScore(sentScore);
		}
		System.out.println("Sentence scores done");
		System.out.println(allInformation.size());
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		/*
		 * We have all the sentences based on the scores we have
		 */
		System.out.println("Trying to sort");
		Collections.sort(allInformation);
		System.out.println("SORTING DONE");
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		/*
		 * We are just allowed to show a limited number of sentences based on the time the user has at his/her disposal
		 */
		ArrayList<SentenceObject> importantSentences = new ArrayList<SentenceObject>();
		for(int i=0;i<numberOfLinesFromDhyanam && i<allInformation.size();i++)
		{
			importantSentences.add(allInformation.get(i));
		}
//		System.out.println(importantSentences);
//		System.out.println("Reduced number of sentences");
//		System.out.println(importantSentences.size());
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		Collections.sort(importantSentences, new IndexComparator());
		// System.out.println(importantSentences);

		StringBuilder finalResult = new StringBuilder();

		for(SentenceObject sentence : importantSentences)
		{
			//System.out.println(sentence.content);
			finalResult.append(sentence.content + "\n");
		}
		return finalResult.toString();
	}
	
	private static HashMap<String, Float> getTfIdf(
			HashMap<String, Float> idfCounts,
			List<String> fileInformations) {
		
		int idfFiles = 0;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("resources/idf.txt"));
			idfFiles = Integer.parseInt(br.readLine());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		StringBuffer combinedText = new StringBuffer();

		for(String currentString : fileInformations)
		{	
			currentString = currentString.replaceAll("[^a-zA-Z ]", "").toLowerCase();
			combinedText.append(currentString + "\n");
		}
		
		String[] allWords = new String(combinedText).split(" +");
		
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
		
		for(String currentWord : allWords)
		{
			if(wordCount.get(currentWord) == null)
				wordCount.put(currentWord, 1);
			else
				wordCount.put(currentWord, wordCount.get(currentWord)+1);
		}
		
		HashMap<String, Float> tfIdfMap = new HashMap<String, Float>();
		Float tempWord;
		for(String word : wordCount.keySet())
		{
			if((tempWord = idfCounts.get(word)) == null)
			{
				tfIdfMap.put(word, (wordCount.get(word))/(float)Math.log(idfFiles));
			}
			else
			{
				tfIdfMap.put(word, tempWord * wordCount.get(word));
			}
		}
		
		return tfIdfMap;
	}

	private static HashMap<String, Float> getIdf(String idfPath) {
		
		HashMap<String, Float> idfMap = new HashMap<String, Float>();
		
		String newLine = null;
		String word = null;
		float score = 0.0f;
		try {
			BufferedReader br = new BufferedReader(new FileReader(idfPath));
			br.readLine();
			while((newLine=br.readLine())!=null)
			{
				word = newLine.split("\t")[0];
				score = Float.parseFloat(newLine.split("\t")[1]);
				idfMap.put(word, score);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idfMap;
	
	}

	private static void doPreProcessing(
			ArrayList<SentenceObject> fileInformations) {

		for(SentenceObject currentSentence : fileInformations)
		{
	//		Stemmer.helper("resources/111.txt");
		}
		
		
	}

	private static ArrayList<SentenceObject> getContentFromFile(int mode, String string) {

		ArrayList<SentenceObject> fileInformations = new ArrayList<SentenceObject>();
		String newLine = null;

		if(mode == 1)
		{
			/*
			 * It just deals with a single file. 
			 */
			try {
				int i=0;
				String fileName = string.substring(string.lastIndexOf("/")+1);
				BufferedReader br = new BufferedReader(new FileReader(string));
				while((newLine = br.readLine()) != null)
				{
					if(newLine.trim().compareTo("")!=0)
					{	
						fileInformations.add(new SentenceObject(newLine.trim(),++i,fileName));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(mode == 2)
		{
			/*
			 * It is a list of files, read each file and then add it's sentenceObject objects
			 */

			File folder = new File(string);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					System.out.println("File " + listOfFiles[i].getName());
				} else if (listOfFiles[i].isDirectory()) {
					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
		}
		else
		{
			System.out.println("Illegal mode");
		}
		return fileInformations;
	}
}
