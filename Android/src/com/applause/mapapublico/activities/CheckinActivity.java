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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applause.mapapublico.R;
import com.applause.mapapublico.callback.CheckinCallback;
import com.applause.mapapublico.service.CheckInService;
import com.applause.mapapublico.util.GPSTracker;
//Tela de CheckIn
public class CheckinActivity extends Activity implements CheckinCallback {

	private boolean isVisualizar;

	private int idUser;
	
	private Button btnCheckIn;
	private Button btnParticipar;
	private ImageButton btnSobreC;

	private ProgressBar pvProgress;

	private TextView lblNome;
	private TextView lblLocal;

	private WebView webView;
	private CountDownTimer cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		SharedPreferences preferences = getSharedPreferences("MyPreferences",
				Context.MODE_PRIVATE);
		//Verifica se o usuário esta visualizando ou participando do app
		if (preferences.getInt("IDUser", 0) == 0) {
			isVisualizar = true;
		} else {
			isVisualizar = false;
			idUser = preferences.getInt("IDUser", 0);
		}

		btnCheckIn = (Button) findViewById(R.id.btnCheckin);
		btnSobreC = (ImageButton) findViewById (R.id.btnSobreC);
		btnParticipar = (Button) findViewById(R.id.btnParticiparC);

		pvProgress = (ProgressBar) findViewById(R.id.pvProgress);

		lblNome = (TextView) findViewById(R.id.lblNome);
		lblLocal = (TextView) findViewById(R.id.lblLocal);

		webView = (WebView) findViewById(R.id.webView1);

		initiateView();
	}

	private void initiateView() {
		if (isVisualizar) {
			btnCheckIn.setVisibility(View.GONE);
			btnParticipar.setVisibility(View.VISIBLE);
			pvProgress.setVisibility(View.GONE);
		} else {
			SharedPreferences preferences = getSharedPreferences(
					"MyPreferences", Context.MODE_PRIVATE);

			btnCheckIn.setVisibility(View.VISIBLE);
			btnParticipar.setVisibility(View.GONE);

			lblNome.setText(preferences.getString("Apelido", ""));
			lblLocal.setText(preferences.getString("Lat", "") + ", "
					+ preferences.getString("Lng", ""));

			
		}
		//Webview Para dar zoom na imagem
		webView.getSettings().setBuiltInZoomControls(true);
		if(android.os.Build.VERSION.SDK_INT >= 11){
			webView.getSettings().setDisplayZoomControls(false);
		}
		String data = "<html><body style=\"background:#000000\"><img src=\"http://d39wqyw5lyhv7u.cloudfront.net/img/app.jpg\" width=\"100%\"/></body><html>";
		webView.loadDataWithBaseURL(
				"http://d39wqyw5lyhv7u.cloudfront.net/img/app.jpg", data,
				"text/html", "utf-8", "");
		webView.setBackgroundColor(0x00000000);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		btnParticipar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnSobreC.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (CheckinActivity.this, SobreActivity.class);
				startActivity(intent);
			}
		});
		
		btnCheckIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GPSTracker gps = new GPSTracker(CheckinActivity.this);
				if (gps.canGetLocation()) {
					SharedPreferences preferences = getSharedPreferences(
							"MyPreferences", Context.MODE_PRIVATE);
					String lat = String.valueOf(gps.getLatitude());
					String lng = String.valueOf(gps.getLongitude());
					String idIbge = preferences.getString("IBGE", "");
					
					CheckInService.makeCheckIn(lat, lng, idIbge, idUser, CheckinActivity.this);
					
					btnCheckIn.setEnabled(false);
				}else{
					gps.showSettingsAlert();
				}
			}
		});
	}
//Callback de sucesso de Check In
	@Override
	public void successfullCheckIn() {
		SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);  
		SharedPreferences.Editor editor = preferences.edit();

		GPSTracker gps = new GPSTracker(CheckinActivity.this);
		if (gps.canGetLocation()) {
			String lat = String.valueOf(gps.getLatitude());
			String lng = String.valueOf(gps.getLongitude());
			editor.putString("Lat", lat);
			editor.putString("Lng", lng);
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM HH:mm");
		String data = simpleDateFormat.format(new Date());

		editor.putString("Data", data);
		editor.putString("checkIn", data);
		
		editor.commit();
		lblNome.setText(preferences.getString("Apelido", ""));
		lblLocal.setText(preferences.getString("Lat", "") + ", "
				+ preferences.getString("Lng", ""));

		AlertDialog dialog = new AlertDialog(this) {
		};
		dialog.setTitle("Check-In");
		dialog.setMessage("DADOS ENVIADOS COM SUCESSO!\n\n5 MINUTOS é o tempo para o sistema se atualizar. Aguarde, na hora do próximo checkin seu percurso aparecerá no mapa.");
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
		
		webView.getSettings().setBuiltInZoomControls(true);
		if(android.os.Build.VERSION.SDK_INT >= 11){
			webView.getSettings().setDisplayZoomControls(false);
		}
		String d = "<html><body style=\"background:#000000\"><img src=\"http://d39wqyw5lyhv7u.cloudfront.net/img/app.jpg\" width=\"100%\"/></body><html>";
		webView.loadDataWithBaseURL(
				"http://d39wqyw5lyhv7u.cloudfront.net/img/app.jpg", d,
				"text/html", "utf-8", "");
		webView.setBackgroundColor(0x00000000);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		
		pvProgress.setProgress(0);
		pvProgress.setMax(300000);

		//Timer de 5 minutos para ver tempo restante para próximo check in 
	    int oneMin= 5 * 60 * 1000;
		cd = new CountDownTimer(oneMin, 1000) { 

	        public void onTick(long millisUntilFinished) {

	            int total = (int) millisUntilFinished;
	           
	            pvProgress.setProgress(total);
	        }
	        
	        

	        public void onFinish() {
            	btnCheckIn.setEnabled(true);
            	pvProgress.setProgress(300000);
	        }
	    }.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Quando tela acorda verificar quanto já andou do count down
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM HH:mm");
		SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);  
		try {
			Date now = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
			Date timer = simpleDateFormat.parse(preferences.getString("checkIn", ""));
			if(cd == null && !preferences.getString("checkIn", "").equalsIgnoreCase("")){
				long diff = now.getTime() - timer.getTime();
			    final int diffMinutes = (int) (diff / (60 * 1000));
			    if(diffMinutes < 5){
	            	btnCheckIn.setEnabled(false);
			    	pvProgress.setProgress(0);
				    int oneMin= (5 - diffMinutes) * 60 * 1000;
					pvProgress.setMax(5 * 60 * 1000);

					cd = new CountDownTimer(oneMin, 1000) { 

				        public void onTick(long millisUntilFinished) {

				            int total = (int) millisUntilFinished;
				           
				            pvProgress.setProgress(total);
				        }
				        
				        

				        public void onFinish() {
			            	btnCheckIn.setEnabled(true);
			            	pvProgress.setProgress(0);
				        }
				    }.start();
			    }
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void failToCheckIn(String reason) {
		AlertDialog dialog = new AlertDialog(this) {
		};
		dialog.setTitle("CheckIn");
		dialog.setMessage(reason);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
		
	}
}
