package com.example.imc;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button envoyer = null;
	Button raz = null;

	EditText poids = null;
	EditText taille = null;

	RadioGroup group = null;

	TextView result = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Info", "Entered onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		envoyer = (Button) findViewById(R.id.calculerIMC);
		raz = (Button) findViewById(R.id.raz);

		poids = (EditText) findViewById(R.id.poids);
		taille = (EditText) findViewById(R.id.taille);

		group = (RadioGroup) findViewById(R.id.radioGroup);

		result = (TextView) findViewById(R.id.resultat);

		//Set listeners
		envoyer.setOnClickListener(envoyerListener);
		raz.setOnClickListener(razListener);
		poids.addTextChangedListener(textWatcher);
		taille.addTextChangedListener(textWatcher);

		// On crée un utilitaire de configuration pour cette animation
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.blabla);
		// On l'affecte au widget désiré, et on démarre l'animation
		envoyer.startAnimation(animation);
	}

	private TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			result.setText(getResources().getString(R.string.resultat));
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	// Uniquement pour le bouton "envoyer"
	private OnClickListener envoyerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String p = poids.getText().toString();
			String t = taille.getText().toString();
			if(p.matches("") || t.matches("")){
				Toast.makeText(MainActivity.this, getResources().getString(R.string.errone), Toast.LENGTH_SHORT).show();
				return;
			}

			Double pVal = Double.valueOf(p);
			Double tVal = Double.valueOf(t);
			if(group.getCheckedRadioButtonId() == R.id.radioCM) tVal /= 100;
			Double imc = pVal / Math.pow(tVal, 2);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			//Tag: Preferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String userName = prefs.getString("userName", "Anonymous");


			String r = userName + ", "+ getResources().getString(R.string.yourBmiIs)+ " "+ df.format(imc) +".";
			String z;
			if(imc < 18.5)	z = getResources().getString(R.string.souspoids);
			else if(imc >= 18.5 && imc < 25) z = getResources().getString(R.string.poids_normal);
			else if (imc >= 25 && imc < 35) z = getResources().getString(R.string.surpoids);
			else if (imc >= 35) z = getResources().getString(R.string.over_surpoids);
			else z = "";

			result.setText(r+"\n"+z);
		}
	};

	// Pour le bouton "RAZ"
	private OnClickListener razListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			poids.getText().clear();
			taille.getText().clear();
			result.setText(getResources().getString(R.string.resultat));
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch(item.getItemId()){
		case R.id.action_settings:
			Toast.makeText(this, R.string.clickedOnActionSettings, Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_about:
			Toast.makeText(this, R.string.clickedOnActionAbout, Toast.LENGTH_SHORT).show();
			break;
		}
		return false;

	}

	public boolean openSettingsActivity(MenuItem item){
		Intent intent = new Intent(this, SettingsActivity.class); // Create new Intent
		startActivity(intent);
		return false;
	}

}
