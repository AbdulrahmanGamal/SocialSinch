package com.parse.sinch.social;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import com.parse.sinch.social.databinding.ActivityLoginBinding;
import com.parse.sinch.social.service.SinchService;
import com.parse.sinch.social.viewmodel.LoginViewViewModel;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        final LoginViewViewModel loginViewViewModel = new LoginViewViewModel(this);
        binding.setViewModel(loginViewViewModel);
        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        loginViewViewModel.hookObservables();
                    }
                });
//		RxView.clicks(mSignUp)
//				.subscribe(new Action1<Void>() {
//					@Override
//					public void call(Void aVoid) {
//						startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
//					}
//				});

//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser != null) {
//			startActivity(new Intent(LoginActivity.this, TabActivity.class));
//			//startService(new Intent(LoginActivity.this, SinchService.class));
//            finish();
//        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
