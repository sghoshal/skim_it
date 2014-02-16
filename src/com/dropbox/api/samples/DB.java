package com.dropbox.api.samples;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper{
	private static DB instance;

	public DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("exec super");
	}

	public static DB getInstance(Context context) {
		if(instance == null) {
			System.out.println("instance was null");

			instance = new DB(context);
		}
		System.out.println("Returing instance");
		return instance;
	}

	// Database Version
	private static final int DATABASE_VERSION = 1;
	private Cursor cursor;
	// Database Name
	private static final String DATABASE_NAME = "FileDB";
	// Contacts table name
	private static final String TABLE_FILE_FOLDER = "FileFolder";

	// Contacts Table Columns names
	private static final String FILE_NAME = "fileName";
	private static final String FOLDER_NAME = "folderName";
	private static final String LINK = "link";

	private static String CREATE_FILEFOLDER_TABLE = 
			"CREATE TABLE " + TABLE_FILE_FOLDER + 
			"(" + FILE_NAME + " TEXT, " + FOLDER_NAME + " TEXT, " + 
			LINK + " TEXT, " + 
			"CONSTRAINT pkey PRIMARY KEY " +
			"(" + FILE_NAME + ", " + FOLDER_NAME + "))";

	public SQLiteDatabase database;

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("Create Table Query: \n" + CREATE_FILEFOLDER_TABLE);
		db.execSQL(CREATE_FILEFOLDER_TABLE);
		System.out.println("Created Table");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE " + TABLE_FILE_FOLDER);
		onCreate(db);
	}
	
	/**
	 * Opens the Database Connection
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = instance.getWritableDatabase();
	}

	/**
	 * Closes the Database Connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		instance.close();
	}
	
	/**
	 * Adding a particular Person's Details for a Group in Database
	 * @param person
	 */
	public void addFile(String folder, FileDetails file) {

		System.out.println("ADDING FILE...TO FOLDER: " + folder);
		ContentValues values = new ContentValues();
		
		if(file != null) {
			values.put(FILE_NAME, file.getFileName()); 
			values.put(FOLDER_NAME, folder); 
			values.put(LINK, file.getLink());
		}
		else {
			values.put(FILE_NAME, "xxxx"); 
			values.put(FOLDER_NAME, "xxxx"); 
			values.put(LINK, "xxxx");
		}
		 
		System.out.println("VALUES: \n" + values.get(FOLDER_NAME) + values.get(FILE_NAME)
				+ values.get(LINK));
		
		// Inserting Row
		long newRowId;
		if (database != null)
			newRowId = database.insert(TABLE_FILE_FOLDER, null, values);
		else
			System.out.println("Database has not been opened.");
		System.out.println("INSERTED FILE DETAIL");
	}
	
	
	/**
	 * Return a List<String> of all the folders
	 * @return 	folderList	List<String> of all the folders
	 */
	public List<String> getAllFolders() {
		List<String> groupsList = new ArrayList<String>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s FROM %s GROUP BY %s",
						   FOLDER_NAME, TABLE_FILE_FOLDER, FOLDER_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				do {
					groupsList.add(cursor.getString(cursor.getColumnIndex(FOLDER_NAME)));
				} while (cursor.moveToNext());
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
			groupsList.add("Group Retrieval Exception Occured!");
		}
		System.out.println("ALL FOLDERS RETURNED: " + groupsList);
		return groupsList;
	}

	
	
	/**
	 * Return a List<PersonDetails> of all the members in a group as PersonDetails instances
	 * @return 	groupMembersList	List<PersonDetails> of all the members in a group
	 */
	public List<FileDetails> getFilesInFolder(String folder) {
		List<FileDetails> fileList = new ArrayList<FileDetails>();
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String query = String.format(
						   "SELECT %s, %s, %s FROM %s WHERE %s = '%s' ORDER BY %s",
						   FOLDER_NAME, FILE_NAME, LINK, TABLE_FILE_FOLDER,
						   FOLDER_NAME, folder, FILE_NAME);
					
			cursor = db.rawQuery(query, null);
			
			// If move to the first element is possible
			if (cursor.moveToFirst()) {
				String fileName, link;
				do {
					fileName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
					link = cursor.getString(cursor.getColumnIndex(LINK));
					fileList.add(new FileDetails(fileName, link));
				} while (cursor.moveToNext());
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	/**
	 * Returns true if rows were deleted, false if no rows were deleted
	 * or if there was an exception
	 * @param  
	 * @param phNum
	 * @return
	 */
	public boolean removeFileFromFolder(String folderName, String fileName) {
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String whereClause = String.format("%s = '%s' AND %s = '%s' ", 
								 FOLDER_NAME, folderName, FILE_NAME, fileName);
			return db.delete(TABLE_FILE_FOLDER, whereClause, null) > 0;
		}
		catch(Exception e) {
			System.out.println("******* COULD NOT DELETE FILE: " + fileName + 
					" FROM FOLDER: " + folderName + " FROM DB *******");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Delete entire Folder
	 * @param group
	 * @return
	 */
	public boolean removeEntireGroup (String folderName) {
		try {
			SQLiteDatabase db = instance.getReadableDatabase();
			String whereClause = String.format("%s = '%s' ", FOLDER_NAME, folderName);
			int ret = db.delete(TABLE_FILE_FOLDER, whereClause, null);
			System.out.println("RETURN VAL AFTER DELETION: " + ret);
			return (ret > 0);
		}
		catch(Exception e) {
			System.out.println("******* COULD NOT DELETE FOLDER: " + folderName +
					"AND ALL ITS FILES FROM DB *********");
			e.printStackTrace();
		}
		return false;
	}
	
}
