package com.parse.sinch.social;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.sinch.social.service.SinchService;

public class LoginActivity extends Activity {

	private AQuery aq;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        aq = new AQuery(this);
        
        aq.id(R.id.btnLogin).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 
				 String username = aq.id(R.id.editUSer).getEditText().getText().toString();
				 String password = aq.id(R.id.editPassword).getEditText().getText().toString();
				 ParseUser.logInInBackground(username, password, new LogInCallback() {
					 	public void done(ParseUser user, com.parse.ParseException e) {
					 		if (user != null) {
					 			//start sinch service
					 			//start next activity
								startActivity(new Intent(LoginActivity.this, ListaUsuariosActivity.class));
								startService(new Intent(LoginActivity.this, SinchService.class));
					 		} else {
					 			Toast.makeText(getApplicationContext(),
					 					"Error al Iniciar Sesion: " + e.getMessage() ,
					 					Toast.LENGTH_LONG).show();
					 		}
					 	}
				 });
				 
			}
		});
        
        aq.id(R.id.btnRegister).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
			}
		});
        
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
			startActivity(new Intent(LoginActivity.this, ListaUsuariosActivity.class));
			startService(new Intent(LoginActivity.this, SinchService.class));
        }
    }

    @Override
    public void onDestroy() {
    	stopService(new Intent(this, SinchService.class));
    	super.onDestroy();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    		closeApplicationMessage();		   		        		        
	    }
	    return false;
	}
	
	private void closeApplicationMessage(){
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(R.string.salir)
		.setMessage(R.string.salir_msj)
		.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish(); 
			}

		})
		.setNegativeButton(R.string.cancelar, null)
		.show();
	}
}
