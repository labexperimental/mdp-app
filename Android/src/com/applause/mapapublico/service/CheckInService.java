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

package com.applause.mapapublico.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.applause.mapapublico.callback.CheckinCallback;

public class CheckInService {

	public static final String URL = "http://virada.applausemobile.com/";
	//Método para fazer login
	public static void makeCheckIn (final String lat, final String lng, final String idIbge, final int idUser, final CheckinCallback callback){
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				Http http = new Http();
				String retorno;
				try {
					retorno = http.doGet(URL + "checkin.php?id=" + idUser
							+ "&lat=" + lat + "&lon=" + lng
							+ "&origem=" + idIbge);
					JSONObject json = new JSONObject(retorno);
					return json;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				//Callback de webservice
				if(result == null || !result.optString("status").equalsIgnoreCase("ok")){
					callback.failToCheckIn("Falha ao efetuar o checkIn, por favor tente de novo.");
				}else{
					callback.successfullCheckIn();
				}
			}
		};
		task.execute();
	}

}
