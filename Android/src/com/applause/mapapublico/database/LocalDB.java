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

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.applause.mapapublico.model.LocalModel;

public class LocalDB {
	private Context context;
	private SQLiteDatabase db;
	
	//Construtor
	public LocalDB(Context context) {
		this.context = context;
		DataHelper openHelper = new DataHelper(this.context);
		this.db = openHelper.getDatabase();
	}
	
	//Obtem Paises
	public ArrayList<LocalModel> selectCountry(){
		ArrayList<LocalModel> list = new ArrayList<LocalModel>();
		Cursor cursor = db.rawQuery(
				"select id,nome2 from paises order by nome",
				null);
		if (cursor.moveToFirst()) {
			do {
				LocalModel canal = new LocalModel();
				canal.id = cursor.getString(0);
				canal.nome = cursor.getString(1);
				list.add(canal);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//Obtem Estado
	public ArrayList<LocalModel> selectState(){
		ArrayList<LocalModel> list = new ArrayList<LocalModel>();
		Cursor cursor = db.rawQuery(
				"select * from estados order by nome",
				null);
		if (cursor.moveToFirst()) {
			do {
				LocalModel canal = new LocalModel();
				canal.id = cursor.getString(0);
				canal.nome = cursor.getString(1);
				list.add(canal);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//Obtem Cidade
	public ArrayList<LocalModel> selectCity(String id){
		ArrayList<LocalModel> list = new ArrayList<LocalModel>();
		Cursor cursor = db.rawQuery(
				"select id,nome2 from municipios where estado_id="+id+" order by nome",
				null);
		if (cursor.moveToFirst()) {
			do {
				LocalModel canal = new LocalModel();
				canal.id = cursor.getString(0);
				canal.nome = cursor.getString(1);
				list.add(canal);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	//Obtem Bairros
	public ArrayList<LocalModel> selectBairros(){
		ArrayList<LocalModel> list = new ArrayList<LocalModel>();
		Cursor cursor = db.rawQuery(
				"select id,nome2 from distritos order by nome",
				null);
		if (cursor.moveToFirst()) {
			do {
				LocalModel canal = new LocalModel();
				canal.id = cursor.getString(0);
				canal.nome = cursor.getString(1);
				list.add(canal);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
}
