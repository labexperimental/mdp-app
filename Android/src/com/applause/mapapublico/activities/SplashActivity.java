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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.applause.mapapublico.R;

public class SplashActivity extends Activity {
//Tela de Splash
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				finish();

				SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);  
				//Verifica se o usuário fez login
				if(preferences.getInt("IDUser", 0) == 0){

					Intent hackbookIntent = new Intent().setClass(
							SplashActivity.this, HomeActivity.class);
					startActivity(hackbookIntent);
				}else{

					Intent hackbookIntent = new Intent().setClass(
							SplashActivity.this, CheckinActivity.class);
					startActivity(hackbookIntent);
				}
			}
		};
		final Timer timer = new Timer();
		//Após 2 segundos e meio (2500) a splash sai
		timer.schedule(task, 2500);
	}

}
