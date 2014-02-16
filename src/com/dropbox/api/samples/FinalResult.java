package com.dropbox.api.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dropbox.api.samples.chooser_start.R;

public class FinalResult extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.summary);
		String summary = getIntent().getExtras().getString("summary").toString();

		TextView summaryView = (TextView) findViewById(R.id.summaryText);
		summaryView.setText(summary);
	}

	public void onHomeClick(View view) {
		finish();
	}
}
