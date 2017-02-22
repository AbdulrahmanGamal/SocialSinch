package com.parse.sinch.social.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.parse.sinch.social.R;
import com.parse.sinch.social.TabActivity;
import com.parse.sinch.social.data.DataManager;
import com.parse.sinch.social.service.SinchService;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**
 * View model for the Login Activity
 */

public class LoginViewViewModel {
    private Context mContext;
    private String mEmail;
    private String mPassword;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mPasswordLayout;
    private Button mSignIn;
    private boolean mShowEmailError;
    private boolean mShowPasswordError;
    private Observable<CharSequence> mEmailChangeObservable;
    private Observable<CharSequence> mPasswordChangeObservable;

    private Pattern mEmailPattern = android.util.Patterns.EMAIL_ADDRESS;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private static final String TAG = "LoginViewViewModel";

    public LoginViewViewModel(Context context) {
        this.mContext = context;
        if (Backendless.UserService.loggedInUser() != null) {
            loadMainUserList();
            ((Activity)context).finish();
        }
    }

    @BindingAdapter("email")
    public static void setEmail(EditText emailEditText, LoginViewViewModel model) {
        model.setEmailChangeObservable(RxTextView.textChanges(emailEditText));
        model.setEmailLayout((TextInputLayout) emailEditText.getParent().getParent());
    }
    @BindingAdapter("password")
    public static void setPassword(EditText passwordEditText, LoginViewViewModel model) {
        model.setPasswordChangeObservable(RxTextView.textChanges(passwordEditText));
        model.setPasswordLayout((TextInputLayout) passwordEditText.getParent().getParent());
    }
    @BindingAdapter("signin")
    public static void setSignInButton(Button signInButton, LoginViewViewModel model) {
        Observable<Void> signInClickObservable = RxView.clicks(signInButton);
        model.setSignIn(signInButton);
        model.initSignInListener(signInClickObservable);
    }

    public View.OnClickListener onClickSignUp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }

    private void loadMainUserList() {
        mContext.startActivity(new Intent(mContext, TabActivity.class));
        mContext.startService(new Intent(mContext, SinchService.class));
    }

    private void initEmailSubscription(Observable<CharSequence> emailChangeObservable) {
        // Checks for validity of the email input field
        Subscription emailSubscription = emailChangeObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        showEmailError(false);
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        boolean isEmailValid = validateEmail(charSequence.toString());
                        if (!isEmailValid) {
                            showEmailError(true);
                        } else {
                            setEmail(charSequence.toString());
                            showEmailError(false);
                        }
                    }
                });

        mCompositeSubscription.add(emailSubscription);
    }

    private void initPasswordSubscription(Observable<CharSequence> passwordChangeObservable) {
        // Checks for validity of the password input field
        Subscription passwordSubscription = passwordChangeObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        showPasswordError(false);
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        boolean isPasswordValid = validatePassword(charSequence.toString());
                        if (!isPasswordValid) {
                            showPasswordError(true);
                        } else {
                            setPassword(charSequence.toString());
                            showPasswordError(false);
                        }
                    }
                });

        mCompositeSubscription.add(passwordSubscription);
    }

    private void initSignInSubscription(Observable<CharSequence> emailChangeObservable,
                                        Observable<CharSequence> passwordChangeObservable) {
        // Checks for validity of all input fields
        Subscription signInFieldsSubscription = Observable.
                combineLatest(emailChangeObservable,
                        passwordChangeObservable,
                        new Func2<CharSequence, CharSequence, Boolean>() {
                            @Override
                            public Boolean call(CharSequence email, CharSequence password) {
                                boolean isEmailValid = validateEmail(email.toString());
                                boolean isPasswordValid = validatePassword(password.toString());

                                return isEmailValid && isPasswordValid;
                            }
                        }).observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean validFields) {
                        if (validFields) {
                            enableSignIn();
                        } else {
                            disableSignIn();
                        }
                    }
                });

        mCompositeSubscription.add(signInFieldsSubscription);
    }

    private void initSignInListener(Observable<Void> signInClickObservable) {
        signInClickObservable
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        disableSignIn();
                    }
                })
                .flatMap(new Func1<Void, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(Void aVoid) {
                        return DataManager.getLoginObservable(mEmail, mPassword, true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        enableSignIn();
                        if (o instanceof BackendlessUser) {
                            Log.e(TAG, "Backendless user successfully retrieved: " + o);
                            loadMainUserList();
                        } else {
                            Log.e(TAG, "Error retrieving the user: " + o);
                        }
                    }
                });
    }

    /**
     * Validates the email pattern
     * @param email
     * @return
     */
    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email))
            return false;

        Matcher mEmailMatcher = mEmailPattern.matcher(email);
        return mEmailMatcher.matches();
    }

    /**
     * Validates password length
     * @param password
     * @return
     */
    private boolean validatePassword(String password) {
        return password.length() > 6;
    }

    private void enableSignIn(){
        mSignIn.setEnabled(true);
        mSignIn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ColorPrimary));
    }

    private void disableSignIn(){
        mSignIn.setEnabled(false);
        mSignIn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_gray));
    }

    public void showEmailError(boolean showEmailError){
        this.mShowEmailError = showEmailError;
        if (mShowEmailError) {
            getEmailLayout().setError(mContext.getString(R.string.invalid_email));
            enableError(getEmailLayout());
        } else {
            disableError(getEmailLayout());
        }
    }
    public boolean getShowEmailError() {
        return mShowEmailError;
    }
    private void showPasswordError(boolean showPasswordError){
        this.mShowPasswordError = showPasswordError;
        if (mShowPasswordError) {
            getPasswordLayout().setError(mContext.getString(R.string.invalid_password));
            enableError(getPasswordLayout());
        } else {
            disableError(getPasswordLayout());
        }
    }
    public boolean getShowPasswordError() {
        return mShowPasswordError;
    }

    private void setPasswordChangeObservable(Observable<CharSequence> passwordChangeObservable) {
        this.mPasswordChangeObservable = passwordChangeObservable;
    }

    private void setEmailChangeObservable(Observable<CharSequence> emailChangeObservable) {
        this.mEmailChangeObservable = emailChangeObservable;
    }

    private Observable<CharSequence> getEmailChangeObservable() {
        return mEmailChangeObservable;
    }

    private Observable<CharSequence> getPasswordChangeObservable() {
        return mPasswordChangeObservable;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public TextInputLayout getEmailLayout() {
        return mEmailLayout;
    }

    public void setEmailLayout(TextInputLayout emailLayout) {
        this.mEmailLayout = emailLayout;
    }

    public TextInputLayout getPasswordLayout() {
        return mPasswordLayout;
    }

    public void setPasswordLayout(TextInputLayout passwordLayout) {
        this.mPasswordLayout = passwordLayout;
    }

    public void setSignIn(Button signIn) {
        this.mSignIn = signIn;
    }

    public void hookObservables() {
     initEmailSubscription(getEmailChangeObservable());
     initPasswordSubscription(getPasswordChangeObservable());
     initSignInSubscription(getEmailChangeObservable(), getPasswordChangeObservable());
    }

        /**
     * Shows the error message under the Email EditText
     * @param textInputLayout
     */
    private void enableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2)
            textInputLayout.getChildAt(1).setVisibility(View.VISIBLE);
    }

    /**
     * Hides the email error message if is visible
     * @param textInputLayout
     */
    private void disableError(TextInputLayout textInputLayout) {
        if (textInputLayout.getChildCount() == 2) {
            textInputLayout.getChildAt(1).setVisibility(View.GONE);
            textInputLayout.setError(null);
        }
    }
}
