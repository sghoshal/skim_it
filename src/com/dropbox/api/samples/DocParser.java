package com.dropbox.api.samples;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class DocParser extends AsyncTask<Void, Long, Boolean>{

	private List<String> pdfUrls;
	private ArrayList<String> stringifiedFiles = new ArrayList<String>();
	private Context context;
	
	public DocParser(List<String> pdfUrls, Context context) {
		this.pdfUrls = pdfUrls;
		this.context = context;
	}


	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		PdfReader reader = null;
		StringBuilder sb = new StringBuilder("");

		for(int i=0; i<pdfUrls.size(); i++) {
			
			try {
				reader = new PdfReader(new URL(pdfUrls.get(i)));
				for(int j=0; j<reader.getNumberOfPages(); j++) {
					String page = PdfTextExtractor.getTextFromPage(reader, j+1);
					sb.append(page);
				}
				stringifiedFiles.add(sb.toString());
				sb = new StringBuilder("");
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		int i = 0;
		for(String str : stringifiedFiles) {
			
			System.out.println("----------RESULT: " + i + "--------\n" + str);
			i++;
			System.out.println("\n\n");
		}
		
		System.out.println("------ CALLING SUMMARIZER ------- ");
		System.out.println("\n\n");
		
		String summary = new String();
		int[] arrayIntegers = new int[stringifiedFiles.size()];
		for(int index = 0; i < stringifiedFiles.size(); index++) {
			arrayIntegers[index] = 1;
		}
		
		summary = SkipItMainClass.APIHelper(stringifiedFiles, 30);
		System.out.println("\n\n\n\n\n Summary: ");
		System.out.println(summary);
		
		Intent summaryIntent = new Intent(context, FinalResult.class);
		summaryIntent.putExtra("summary", summary);
		summaryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity( summaryIntent );
		
		return null;
	}


	public List<String> getPdfUrls() {
		return pdfUrls;
	}


	public void setPdfUrls(List<String> pdfUrls) {
		this.pdfUrls = pdfUrls;
	}


	public List<String> getStringifiedFiles() {
		return stringifiedFiles;
	}


	public void setStringifiedFiles(ArrayList<String> stringifiedFiles) {
		this.stringifiedFiles = stringifiedFiles;
	}

	/*
	public static List<String> stringifyPptData(List<String> pptUrls) throws MalformedURLException, IOException, URISyntaxException {
		FileInputStream fis = null;
		StringBuilder sb = new StringBuilder("");
		List<String> stringifiedFiles = new ArrayList<String>();
		PowerPointExtractor ppe = null;
		for(int i=0; i<pptUrls.size(); i++) {
			fis = new FileInputStream(new File(new URI(pptUrls.get(i))));
			ppe = new PowerPointExtractor(fis);
			sb.append(ppe.getText());
			stringifiedFiles.add(sb.toString());
			sb = new StringBuilder("");
		}
		ppe.close();
		return stringifiedFiles;
	}

	public static List<String> stringifyDocData(List<String> docUrls) throws MalformedURLException, IOException, URISyntaxException {
		POIFSFileSystem fs = null;
		HWPFDocument doc = null;
		WordExtractor we = null;
		String[] paragraphs = null;
		StringBuilder sb = new StringBuilder("");
		List<String> stringifiedFiles = new ArrayList<String>();
		for(int i=0; i<docUrls.size(); i++) {
			fs = new POIFSFileSystem(new FileInputStream(new File(new URI(docUrls.get(i)))));
			doc = new HWPFDocument(fs);
			we = new WordExtractor(doc);
			paragraphs = we.getParagraphText();
			for (int j=0; j<paragraphs.length; j++) {
				sb.append(paragraphs[j].toString());
			}
			stringifiedFiles.add(sb.toString());
		}
		we.close();
		return stringifiedFiles;
	}
	 */
}
