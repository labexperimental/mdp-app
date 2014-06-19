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

import com.applause.mapapublico.callback.CadastroCallback;

public class CadastroService {

	public static final String URL = "http://virada.applausemobile.com/";

	//Método para efetuar Cadastro
	public static void efetuarCadastro(final CadastroCallback callback,
			final String nickname, final String email, final char sexo,
			final String codigoIbge, final String lat, final String lng,
			final String idade) {
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				Http http = new Http();
				String retorno;
				try {
					retorno = http.doGet(URL + "registro.php?email=" + email
							+ "&nome=" + nickname + "&sexo=" + sexo
							+ "&codigo=" + codigoIbge + "&lat=" + lat + "&lon="
							+ lng + "&idade=" + idade);
					JSONObject json = new JSONObject(retorno);
					return json;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				//Manda para a callback o resultado
				if(result == null || !result.optString("status").equalsIgnoreCase("ok")){
					callback.failToRegisterUser("Falha ao efetuar o cadastro, por favor tente de novo.");
				}else{
					callback.successfullRegisterUser(result.optInt("id"));
				}
			}

		};
		task.execute();
	}
}
