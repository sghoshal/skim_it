package com.dropbox.api.samples;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dropbox.api.samples.chooser_start.R;


public class MainActivity extends Activity {

	private static final int STATIC_INTEGER_VALUE = 10;
	private static final int FOLDER_ALERT_DIALOG = 1;
	private CharSequence[] folderDialogOptions = {"Generate folder summary", 
			"Delete folder"};

	private ListView listViewFolders;
	private StableArrayAdapter adapter;
	private List<String> folderList;
	private DB dbInstance;

	private boolean isCalledOnce = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        boolean isEnabled = Settings.System.getInt(this.getApplicationContext().getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		setContentView(R.layout.activity_main);

		listViewFolders = (ListView) findViewById(R.id.lvFolders);
		isCalledOnce = true;

		dbInstance = DB.getInstance(MainActivity.this);
		dbInstance.open();
		
		displayAllFolders();

		setClickListener(listViewFolders);
		setLongClickListener(listViewFolders);
	}

	public void onCreateFolderClick(View view) {
		Intent i = new Intent(this, CreateFolder.class);    
		startActivityForResult(i, STATIC_INTEGER_VALUE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
		case (STATIC_INTEGER_VALUE) : { 
			if (resultCode == Activity.RESULT_OK) { 

				String folderReceived = data.getExtras().getString("folderName");
				System.out.println("FOLDER: " + folderReceived);
			} 
			break; 
		} 
		}
	}

	@Override
	protected void onResume() {
		if(isCalledOnce) {
			super.onResume();
			displayAllFolders();
		}
	}
	
	public void displayAllFolders() {
		folderList = dbInstance.getAllFolders();
		adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, folderList);
		listViewFolders.setAdapter(adapter);	
	}


	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog dialog = null;

		switch(id) {
		case FOLDER_ALERT_DIALOG:

			break;
		}
		return dialog;
	}
	
	public void setClickListener(final ListView listViewFolders) {
		listViewFolders.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {

				final String selectedFolder = (String) parent.getItemAtPosition(position);
				System.out.println("FOLDER SELECTED: " + selectedFolder);

				Intent toDisplayFiles = new Intent(MainActivity.this, 
						DisplayFiles.class);
				toDisplayFiles.putExtra("folderName", selectedFolder);
				startActivity(toDisplayFiles);
			}
		});
	}

	public void onFolderSummaryClick(View view) {
		
	}
	
	public void setLongClickListener(final ListView listViewFolders) {
		listViewFolders.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {

				AlertDialog dialog;

				final String folderSelected = (String) parent.getItemAtPosition(position);
				System.out.println("FOLDER SELECTED: " + folderSelected);

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);				
				builder.setTitle("Options for " + folderSelected)
				.setItems(folderDialogOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("Option clicked: " + folderDialogOptions[which]);

						
						switch(which) {
						// Get the Files in folder to generate folder summary
						case 0:
							List<FileDetails> filesInFolder = 
								DB.getInstance(MainActivity.this).getFilesInFolder(folderSelected);
							
							generateFolderSummary(folderSelected, filesInFolder);
							break;
							
						// Delete the files in folder	
						case 1:
							DB.getInstance(MainActivity.this).removeEntireGroup(folderSelected);
							displayAllFolders();
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
	
	public void generateFolderSummary(String folderName, List<FileDetails> files) {
		System.out.println("PRODUCING FOLDER SUMMARY FOR FOLDER " + 
					folderName + "...");
		List<String> fileLinksInFolder = new ArrayList<String>();
		for (FileDetails f : files) {
			fileLinksInFolder.add(f.getLink());
		}
		System.out.println("Files: " + fileLinksInFolder);
		
		
		DocParser docParser = new DocParser(fileLinksInFolder, getApplicationContext());
		docParser.execute();
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		private List<String> list_items;
		private List<String> orig_items;
		private List<String> before_search;

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
	
}
