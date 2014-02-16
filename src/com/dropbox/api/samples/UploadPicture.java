package com.dropbox.api.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.exception.DropboxException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UploadPicture extends AsyncTask<Void, Long, Boolean>{
	
	private DropboxAPI<?> mApi;
    private String mPath;
    private File mFile;
    private Entry mRequest;
    
	public UploadPicture(Context context, DropboxAPI<?> api, String dropboxPath,
            File file)
	{
		mApi = api;
		mPath = dropboxPath;
		mFile = file;
		
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
		//FileInputStream fis;
		
			InputStream fis = new FileInputStream(mFile);
			mRequest = mApi.putFile(mPath, fis, mFile.length(),
					null,null);
			Log.d("DbExampleLog","file rev:!" + mRequest.rev);
			 /*if (mRequest != null) {
	                mRequest.upload();
	                return true;
	          }*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}
