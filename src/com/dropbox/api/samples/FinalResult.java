package com.dropbox.api.samples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dropbox.api.samples.chooser_start.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

public class FinalResult extends Activity {

	private UploadRequest mRequest;
	private String summary;

	final static private String APP_KEY = "2fa7zsubsw6gukm";
	final static private String APP_SECRET = "jhujg8uaatgkwe4";

	final static private AccessType ACCESS_TYPE = AccessType.AUTO;

	// In the class declaration section:
	private DropboxAPI<AndroidAuthSession> mDBApi;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.summary);
		summary = getIntent().getExtras().getString("summary").toString();

		TextView summaryView = (TextView) findViewById(R.id.summaryText);
		
		summaryView.setText(summary);
		
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);
	}

	public void onHomeClick(View view) {
		finish();
	}

	public void onUploadClick(View view) {
		mDBApi.getSession().startOAuth2Authentication(FinalResult.this);
	}

	protected void onResume() {
		super.onResume();

		if (mDBApi.getSession().authenticationSuccessful()) {
			try {
				// Required to complete auth, sets the access token on the session
				mDBApi.getSession().finishAuthentication();

				String accessToken = mDBApi.getSession().getOAuth2AccessToken();
				System.out.println("test");
				File file = new File(getApplicationContext().getFilesDir(), "sample1.txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(summary);
				bw.close();
				String path = "sampleStarted/sample1.txt";
				System.out.println(getApplicationContext().getFilesDir());
				//File pdffile = new File(getApplicationContext().getFilesDir(), "sample1.pdf");

				UploadPicture up = new UploadPicture(getApplicationContext(), mDBApi, path, file);
				up.execute();


				System.out.println("End of File Upload Function");

			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}	
}
