package com.dropbox.api.samples;

import com.dropbox.api.samples.chooser_start.R;
import com.dropbox.api.samples.chooser_start.R.id;
import com.dropbox.api.samples.chooser_start.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateFolder extends Activity {

	private EditText folderName;
	private static int INTENT_VALUE = 7;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_folder);  

	}

	public void onCreateFolderOKClick(View view) {
		folderName = (EditText) findViewById(R.id.folderName);

		Intent addFilesIntent = new Intent(this, DisplayFiles.class);
		addFilesIntent.putExtra("folderName", folderName.getText().toString());
		startActivityForResult(addFilesIntent, INTENT_VALUE);

		setResult(Activity.RESULT_OK, addFilesIntent);
		finish();	
	}
}
