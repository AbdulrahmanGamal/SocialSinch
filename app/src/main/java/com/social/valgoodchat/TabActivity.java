package com.social.valgoodchat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.social.valgoodchat.app.SocialSinchApplication;
import com.social.valgoodchat.databinding.ActivityOptionsTabBinding;
import com.social.valgoodchat.viewmodel.TabOptionsViewModel;

public class TabActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TabOptionsViewModel tabOptionsViewModel =
                new TabOptionsViewModel(getSupportFragmentManager());
        ActivityOptionsTabBinding activityOptionsTabBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_options_tab);
        activityOptionsTabBinding.setViewModel(tabOptionsViewModel);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        tabOptionsViewModel.setupWithViewPager();
                    }
                });
        // Create a tab listener that is called when the user changes tabs.
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // When the tab is selected, switch to the
//                // corresponding page in the ViewPager.
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // hide the given tab
//            }
//
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // probably ignore this event
//            }
//        };

        // Add Chat and Users tabs
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setIcon(getResources().getDrawable(R.drawable.tab_chat))
//                            .setTabListener(tabListener));
//        actionBar.addTab(
//                actionBar.newTab()
//                        .setIcon(getResources().getDrawable(R.drawable.tab_friend))
//                        .setTabListener(tabListener));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_llamar_sms, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_llamar:
                //mostrarDialogo(R.string.llamar);
                return true;
            case R.id.action_sms:
                //mostrarDialogo(R.string.sms);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SocialSinchApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SocialSinchApplication.activityPaused();
    }

//    private void notifyRealTimePresence(EventStatus status) {
//        String eventContent = status.toString();
//        if (status.equals(EventStatus.OFFLINE)) {
//            Date currentTime = Calendar.getInstance(Locale.getDefault()).getTime();
//            eventContent = DateUtils.convertDateToString(currentTime);
//        }
//
//        EventMessage eventMessage = new EventMessage(LoggedUser.getInstance().getUserIdLogged(),
//                Constants.PUBLISH_ALL,
//                eventContent,
//                status);
//        RxIncomingEventBus.getInstance().sendEvent(eventMessage);
//    }
//    public void mostrarDialogo(int resId){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        // Get the layout inflater
//        LayoutInflater inflater = getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.llamar_sms_dialogo, null))
//                // Add action buttons
//                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(TabActivity.this, CallingActivity.class));
//                    }
//                })
//                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//
//        builder.setTitle(resId);
//        builder.create().show();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
        }
        return false;
    }
}