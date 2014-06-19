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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.applause.mapapublico.R;
import com.applause.mapapublico.callback.CadastroCallback;
import com.applause.mapapublico.database.LocalDB;
import com.applause.mapapublico.model.LocalModel;
import com.applause.mapapublico.service.CadastroService;
import com.applause.mapapublico.util.GPSTracker;
import com.applause.mapapublico.util.Util;

//Tela de Cadastro

public class CadastroActivity extends Activity implements CadastroCallback {

	private Button btnSobre;
	private Button btnTermo;
	private Button btnPais;
	private Button btnCidade;
	private Button btnEstado;
	private Button btnBairro;
	private Button btnContinuar;

	private Button btnParticipar;
	private ImageButton btnMasc;
	private ImageButton btnFem;

	private EditText txtNome;
	private EditText txtEmail;
	private EditText txtIdade;

	private ArrayList<LocalModel> paises;
	private ArrayList<LocalModel> estados;
	private ArrayList<LocalModel> cidades;
	private ArrayList<LocalModel> bairros;

	private String lat;
	private String lng;

	private String idEstado;
	private String idIBGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);
		btnSobre = (Button) findViewById(R.id.btnSobre);
		btnTermo = (Button) findViewById(R.id.btnTermos);
		btnPais = (Button) findViewById(R.id.btnPais);
		btnCidade = (Button) findViewById(R.id.btnCidade);
		btnEstado = (Button) findViewById(R.id.btnEstado);
		btnBairro = (Button) findViewById(R.id.btnBairro);
		btnContinuar = (Button) findViewById(R.id.btnContinuar);

		btnParticipar = (Button) findViewById(R.id.btnParticipar);
		btnMasc = (ImageButton) findViewById(R.id.btnMasc);
		btnFem = (ImageButton) findViewById(R.id.btnFem);

		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtIdade = (EditText) findViewById(R.id.txtIdade);
		txtNome = (EditText) findViewById(R.id.txtNome);

		btnSobre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CadastroActivity.this,
						SobreActivity.class);
				startActivity(intent);
			}
		});
		btnTermo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CadastroActivity.this,
						PoliticaActivity.class);
				startActivity(intent);
			}
		});

		btnPais.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnPaisPressed();
			}
		});

		btnEstado.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnEstadoPressed();
			}
		});

		btnCidade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnCidadePressed();
			}
		});

		btnBairro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnBairroPressed();
			}
		});

		btnMasc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnMasc.setSelected(!btnMasc.isSelected());
				btnFem.setSelected(false);
			}
		});

		btnFem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnFem.setSelected(!btnFem.isSelected());
				btnMasc.setSelected(false);
			}
		});

		btnParticipar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnParticiparPressed();
			}
		});

		btnContinuar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent hackbookIntent = new Intent(CadastroActivity.this,
						CheckinActivity.class);
				startActivity(hackbookIntent);
			}
		});

		initPickerFields();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}

	//Setar valores default
	public void initPickerFields() {
		GPSTracker gps = new GPSTracker(this);
		if (gps.canGetLocation()) {
			lat = String.valueOf(gps.getLatitude());
			lng = String.valueOf(gps.getLongitude());
		} else {
			gps.showSettingsAlert();
		}

		idEstado = "35";
		btnCidade.setText("SÃO PAULO");
		btnEstado.setText("SP");
		btnPais.setText("BRASIL");
		btnBairro.setText("Selecione seu bairro");

		btnPais.setEnabled(true);
		btnCidade.setEnabled(true);
		btnEstado.setEnabled(true);
		btnBairro.setEnabled(true);
		btnParticipar.setEnabled(false);

	}

	private void btnParticiparPressed() {
		char sexo = 'M';
		if (btnFem.isSelected()) {
			sexo = 'F';
		}

		int idade = 0;
		if (!txtIdade.getText().toString().isEmpty()) {
			idade = Integer.parseInt(txtIdade.getText().toString());
		}
//Validar Email
		if (!txtEmail.getText().toString().isEmpty()) {
			boolean validate = Util.isValidEmail(txtEmail.getText().toString());
			if (!validate) {
				AlertDialog dialog = new AlertDialog(this) {
				};
				dialog.setTitle("Cadastro");
				dialog.setMessage("Formato de email inválido, por favor verifique seu email.");
				dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.show();
				return;
			}
		}

		CadastroService.efetuarCadastro(this, txtNome.getText().toString(),
				txtEmail.getText().toString(), sexo, idIBGE, lat, lng,
				String.valueOf(idade));

	}

	private void btnPaisPressed() {
//		btnParticipar.setEnabled(false);
		if (paises == null) {
			paises = new ArrayList<LocalModel>();
			LocalDB db = new LocalDB(CadastroActivity.this);
			paises = db.selectCountry();
		}
		String[] nomes = new String[paises.size()];
		for (int i = 0; i < paises.size(); i++) {
			nomes[i] = paises.get(i).nome;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CadastroActivity.this);
		builder.setTitle("Selecione um país");
		builder.setItems(nomes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String nome = paises.get(item).nome;
				btnPais.setText(nome);
				if (nome.equalsIgnoreCase("BRASIL")
						|| nome.equalsIgnoreCase(" BRASIL")) {
					btnEstado.setEnabled(true);
					btnParticipar.setEnabled(false);
				} else {
					btnEstado.setEnabled(false);
					btnParticipar.setEnabled(true);
				}

				idIBGE = paises.get(item).id;
				btnEstado.setText("--");
				btnCidade.setText("Selecione sua cidade");
				btnBairro.setText("Selecione seu bairro");

				btnCidade.setEnabled(false);
				btnBairro.setEnabled(false);

				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void btnBairroPressed() {
//		btnParticipar.setEnabled(false);
		if (bairros == null) {
			bairros = new ArrayList<LocalModel>();
			LocalDB db = new LocalDB(CadastroActivity.this);
			bairros = db.selectBairros();
		}
		String[] nomes = new String[bairros.size()];
		for (int i = 0; i < bairros.size(); i++) {
			nomes[i] = bairros.get(i).nome;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CadastroActivity.this);
		builder.setTitle("Selecione um bairro");

		builder.setItems(nomes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				btnBairro.setText(bairros.get(item).nome);
				idIBGE = bairros.get(item).id;
				btnParticipar.setEnabled(true);
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void btnEstadoPressed() {
//		btnParticipar.setEnabled(false);
		if (estados == null) {
			estados = new ArrayList<LocalModel>();
			LocalDB db = new LocalDB(CadastroActivity.this);
			estados = db.selectState();
		}
		String[] nomes = new String[estados.size()];
		for (int i = 0; i < estados.size(); i++) {
			nomes[i] = estados.get(i).nome;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CadastroActivity.this);
		builder.setTitle("Selecione um estado");

		builder.setItems(nomes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String nome = estados.get(item).nome;
				btnEstado.setText(nome);
				if (nome.equalsIgnoreCase("SP") || nome.equalsIgnoreCase(" SP")) {
					btnCidade.setEnabled(true);
					btnParticipar.setEnabled(false);
				} else {
					btnCidade.setEnabled(true);
					btnParticipar.setEnabled(false);
				}

				idEstado = estados.get(item).id;
				idIBGE = estados.get(item).id;
				btnCidade.setText("Selecione sua cidade");
				btnBairro.setText("Selecione seu bairro");

				btnBairro.setEnabled(false);

				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void btnCidadePressed() {
//		btnParticipar.setEnabled(false);
//		if (cidades == null) {
			cidades = new ArrayList<LocalModel>();
			LocalDB db = new LocalDB(CadastroActivity.this);
			cidades = db.selectCity(idEstado);
//		}
		String[] nomes = new String[cidades.size()];
		for (int i = 0; i < cidades.size(); i++) {
			nomes[i] = cidades.get(i).nome;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				CadastroActivity.this);
		builder.setTitle("Selecione uma cidade");

		builder.setItems(nomes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String nome = cidades.get(item).nome;
				btnCidade.setText(nome);
				if (nome.equalsIgnoreCase("SÃO PAULO")
						|| nome.equalsIgnoreCase(" SÃO PAULO")) {
					btnBairro.setEnabled(true);
					btnParticipar.setEnabled(false);
				} else {
					btnBairro.setEnabled(false);
					btnParticipar.setEnabled(true);
				}

				idIBGE = cidades.get(item).id;
				btnBairro.setText("Selecione seu bairro");

				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void successfullRegisterUser(int userID) {
		SharedPreferences preferences = getSharedPreferences("MyPreferences",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("IDUser", userID);
		editor.putString("Lat", lat);
		editor.putString("Lng", lng);
		editor.putString("Apelido", txtNome.getText().toString());
		editor.putString("IBGE", idIBGE);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM HH:mm");
		String data = simpleDateFormat.format(new Date());

		editor.putString("Data", data);

		editor.commit();

		Intent intent = new Intent(getApplicationContext(), CheckinActivity.class);
		ComponentName cn = intent.getComponent();
		Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
		startActivity(mainIntent);
		
//		Intent hackbookIntent = new Intent(CadastroActivity.this,
//				CheckinActivity.class);
//		hackbookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		hackbookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(hackbookIntent);
	}

	@Override
	public void failToRegisterUser(String reason) {
		AlertDialog dialog = new AlertDialog(this) {
		};
		dialog.setTitle("Cadastro");
		dialog.setMessage(reason);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}
}
