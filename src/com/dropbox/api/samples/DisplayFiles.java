package com.dropbox.api.samples;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.dropbox.api.samples.chooser_start.R;
import com.dropbox.chooser.android.DbxChooser;


public class DisplayFiles extends Activity {

	static final String APP_KEY = "gffy44ifl4tl003"; /* This is for you to fill in! */;
	static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed

	private Button mChooserButton;
	private DbxChooser mChooser;

	private List<FileDetails> fileInputList;
	private Map<String, String> mapLinkName;
	private DbxChooser.ResultType resultType;
	private ListView listViewLinks;
	private StableArrayAdapter adapter;

	private String folder;
	private DB dbInstance;

	private CharSequence[] fileDialogOptions = {"Generate summary", 
	"Delete"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);

		folder = getIntent().getStringExtra("folderName");
		listViewLinks = (ListView) findViewById(R.id.lvLinks);
		dbInstance = DB.getInstance(DisplayFiles.this);

		displayAllFiles();

		mChooser = new DbxChooser(APP_KEY);
		fileInputList = dbInstance.getFilesInFolder(folder);

		mapLinkName = new HashMap<String, String>();
		mChooserButton = (Button) findViewById(R.id.chooser_button);

		mChooserButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// DbxChooser.ResultType resultType;
				resultType = DbxChooser.ResultType.DIRECT_LINK;

				mChooser.forResultType(resultType)
				.launch(DisplayFiles.this, DBX_CHOOSER_REQUEST);
			}
		});

		setLongClickListener(listViewLinks);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DBX_CHOOSER_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				DbxChooser.Result result = new DbxChooser.Result(data);
				Log.d("main", "Link to selected file: " + result.getLink());

				if(mapLinkName.containsKey(result.getLink().toString()) == false) {

					FileDetails newFile = new FileDetails(result.getName().toString(), 
							result.getLink().toString());
					fileInputList.add(newFile);
					mapLinkName.put(result.getLink().toString(), result.getName().toString());
					// Adding the picked file to the DB
					DB db = DB.getInstance(DisplayFiles.this);
					db.addFile(folder, newFile);
				}

				adapter = new StableArrayAdapter(DisplayFiles.this,
						android.R.layout.simple_list_item_1, fileInputList);
				listViewLinks.setAdapter(adapter);	

				mChooser.forResultType(resultType)
				.launch(DisplayFiles.this, DBX_CHOOSER_REQUEST);

			} else {
				// Failed or was cancelled by the user.
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void displayAllFiles() {

		fileInputList = dbInstance.getFilesInFolder(folder);
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, fileInputList);
		listViewLinks.setAdapter(adapter);	
	}

	public void onReturnToFoldersClick(View view) {
		finish();
	}

	private void showLink(int id, Uri uri) {
		TextView v = (TextView) findViewById(id);
		if (uri == null) {
			v.setText("", TextView.BufferType.NORMAL);
			return;
		}
		v.setText(uri.toString(), TextView.BufferType.NORMAL);
		v.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public void onGenerateClick (View view) {
		System.out.println("Contents: " + fileInputList);
		for(String key : mapLinkName.keySet()) {
			System.out.println("KEY: " + key + " Value: " + mapLinkName.get(key));
		}
	} 

	public void generateFileSummary(String folder, FileDetails file) {
		System.out.println("PRODUCING SUMMARY FOR FILE " + 
				file.getFileName() + " = LINK: " + file.getLink() + 
				" IN FOLDER: " + folder);
	}

	public void setLongClickListener(final ListView listViewGroups) {
		listViewGroups.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> par, View v,
					int pos, long id) {

				final int position = pos;
				final AdapterView<?> parent = par;

				AlertDialog dialog;

				final FileDetails fileSelected = (FileDetails) parent.getItemAtPosition(position);
				System.out.println("FILE SELECTED: " + fileSelected.getFileName());

				AlertDialog.Builder builder = new AlertDialog.Builder(DisplayFiles.this);				
				builder.setTitle("Options for " + fileSelected.getFileName())
				.setItems(fileDialogOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						System.out.println("Option clicked: " + fileDialogOptions[which]);

						switch(which) {
						// Get the Files in folder to generate folder summary
						case 0:
							generateFileSummary(folder, fileSelected);
							break;

							// Delete the files in folder	
						case 1:
							if(fileInputList.contains(fileSelected)) {

								fileInputList.remove(fileSelected);
								mapLinkName.remove(fileSelected.getLink());
								DB.getInstance(DisplayFiles.this).
									removeFileFromFolder(folder, fileSelected.getFileName());
							
								displayAllFiles();
							}
							break;
						}
					}
				});
				dialog = builder.create();
				dialog.show();
				return true;				
			}
		});
	}


	/**
	 * 
	 * @author soum
	 *
	 */
	private class StableArrayAdapter extends ArrayAdapter<FileDetails> {

		HashMap<FileDetails, Integer> mIdMap = new HashMap<FileDetails, Integer>();

		private List<String> list_items;
		private List<String> orig_items;
		private List<String> before_search;

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<FileDetails> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			FileDetails item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
