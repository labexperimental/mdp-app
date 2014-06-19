//The MIT License (MIT)
//
//Copyright (c) 2014 LittleBoat
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.
//
//www.littleboat.com.br

package com.applause.mapapublico.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

	private static String DBPATH = "";
	private static String DBNAME = "mapadepublico.sqlite";
	private Context context;

	public DataHelper(Context context) {
		
		super(context, "mapadepublico.sqlite", null, 1);
		if(android.os.Build.VERSION.SDK_INT >= 4.2){
			DBPATH = context.getApplicationInfo().dataDir + "/databases/";         
	    } else {
	    	DBPATH = "/data/data/" + context.getPackageName() + "/databases/";
	    }
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private boolean checkDataBase() {

		SQLiteDatabase db = null;

		try {
			File dbFile = context.getDatabasePath(DBNAME);
			return dbFile.exists();
		} catch (SQLiteException e) {
			Log.e("SQL", "Banco não existe");
		}
		return db != null;
	}

	private void createDataBase() throws Exception {

		boolean exists = checkDataBase();

		if (!exists) {
			this.getReadableDatabase();

			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Não foi possível copiar o arquivo");
			}

		}
	}

	private void copyDatabase() throws IOException {

		String dbPath = DBPATH + DBNAME;

		// Abre o arquivo o destino para copiar o banco de dados
		OutputStream dbStream = new FileOutputStream(dbPath);

		// Abre Stream do nosso arquivo que esta no assets
		InputStream dbInputStream = context.getAssets().open(
				"mapadepublico.sqlite");

		byte[] buffer = new byte[1024];
		int length;
		while ((length = dbInputStream.read(buffer)) > 0) {
			dbStream.write(buffer, 0, length);
		}

		dbInputStream.close();

		dbStream.flush();
		dbStream.close();

	}

	public SQLiteDatabase getDatabase() {

		try {
			createDataBase();

			String path = DBPATH + DBNAME;

			return SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			Log.e("SQL", "Não Copiou");
			return getWritableDatabase();
		}

	}

}
