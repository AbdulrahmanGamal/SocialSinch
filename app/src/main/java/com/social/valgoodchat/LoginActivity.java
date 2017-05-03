package com.social.valgoodchat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;

import com.social.backendless.utils.LoggedUser;
import com.social.valgoodchat.app.SocialSinchApplication;
import com.social.valgoodchat.databinding.ActivityLoginBinding;
import com.social.valgoodchat.utils.Constants;
import com.social.valgoodchat.viewmodel.LoginViewViewModel;

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

        String userLogged = LoggedUser.getInstance().getUserIdLogged();
        boolean comesFromFault = getIntent().getBooleanExtra(Constants.FAULT_REFRESH_TOKEN, false);
        if (userLogged != null && !userLogged.isEmpty() && !comesFromFault) {
            loginViewViewModel.loadMainUserList();
            finish();
        }
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    		SocialSinchApplication.closeApplicationMessage(this);
	    }
	    return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
}
