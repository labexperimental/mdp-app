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

package com.applause.mapapublico.activities;

import com.applause.mapapublico.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

//Tela de sobre
public class SobreActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sobre);
		SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);  
		// verifica se o usuário esta logado, se não estiver esconde o botão de logout
		if(preferences.getInt("IDUser", 0) == 0){
			findViewById(R.id.btnLogout).setVisibility(View.INVISIBLE);
		}
		else{
			findViewById(R.id.btnLogout).setVisibility(View.VISIBLE);
		}
		findViewById(R.id.btnVoltar).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.btnLogout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences settings = SobreActivity.this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
				settings.edit().clear().commit();
				Intent hackbookIntent = new Intent(SobreActivity.this, HomeActivity.class);
				hackbookIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(hackbookIntent);
			}
		});
		
		//Botão de termos
		findViewById(R.id.btnTermosP).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (SobreActivity.this, PoliticaActivity.class);
				startActivity(intent);
			}
		});
	}
}
