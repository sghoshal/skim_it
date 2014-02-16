package com.dropbox.api.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import android.os.AsyncTask;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class DocParser extends AsyncTask<Void, Long, Boolean>{

	private List<String> pdfUrls;
	private List<String> stringifiedFiles = new ArrayList<String>();
	
	public DocParser(List<String> pdfUrls) {
		this.pdfUrls = pdfUrls;
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
		System.out.println("----------RESULT 0:  " + stringifiedFiles.get(0));
		System.out.println("----------RESULT 111111:  " + stringifiedFiles.get(1));

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


	public void setStringifiedFiles(List<String> stringifiedFiles) {
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
