package com.parse.sinch.social;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.sinch.social.adapter.UserAdapter;
import com.parse.sinch.social.entities.UserInfo;

public class ListaUsuariosActivity extends Activity {

	private AQuery aq;
	private ProgressDialog progressDialog;
	private BroadcastReceiver receiver;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        
        getActionBar().setTitle(R.string.usuarios);
    
        aq = new AQuery(this);
        
        UserInfo user;
        final List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        
        user = new UserInfo();
        user.setName("Margot Becerra");
        userInfoList.add(user);
        
        user = new UserInfo();
        user.setName("Karen Precoz");
        user.setStatus(true);
        userInfoList.add(user);
        
        
        user = new UserInfo();
        user.setName("Julio Kalimba");
        userInfoList.add(user);
        
        user = new UserInfo();
        user.setName("Pedro Aguilar");
        user.setStatus(true);
        userInfoList.add(user);
                
        UserAdapter adapter = new UserAdapter(this, userInfoList);                
        aq.id(R.id.listUsers).adapter(adapter);       
        
        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
        	public void done(List<ParseUser> userList, com.parse.ParseException e) {
        		if (e == null) {
        			UserInfo user = null;
        			for (int i=0; i<userList.size(); i++) {
        				user = new UserInfo();
        		        user.setName(userList.get(i).getUsername());
        		        userInfoList.add(user);
        			}
        			UserAdapter adapter = new UserAdapter(ListaUsuariosActivity.this, userInfoList);                
        	        aq.id(R.id.listUsers).adapter(adapter);  
        	        aq.id(R.id.listUsers).itemClicked(new AdapterView.OnItemClickListener() {
        	        	@Override
        	        	public void onItemClick(AdapterView<?> a, View v, int i, long l) {
        	        		openConversation(userInfoList, i);
        	        	}
						
        	        });
        		} else {
        			Toast.makeText(getApplicationContext(),
        					"Error Cargando lista de Usuarios: " + e.getMessage(),
        					Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        
        //broadcast receiver to listen for the broadcast
        //from MessageService
        receiver = new BroadcastReceiver() {
        	@Override
        	public void onReceive(Context context, Intent intent) {
        		Boolean success = intent.getBooleanExtra("success", false);
        		progressDialog.dismiss();
        		//show a toast message if the Sinch
        		//service failed to start
        		if (!success) {
        			Toast.makeText(getApplicationContext(), "Servicio de Mensajeria Fallo en Iniciar", Toast.LENGTH_LONG).show();
        		}
        	}
        };
        
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("com.parse.sinch.social.ListaUseriosActivity"));
        
    }
    
    public void openConversation(List<UserInfo> users, int pos) {
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.whereEqualTo("username", users.get(pos).getUsername());
    	query.findInBackground(new FindCallback<ParseUser>() {
    		public void done(List<ParseUser> user, ParseException e) {
    			if (e == null) {
    				 Intent intent = new Intent(ListaUsuariosActivity.this, MessagesActivity.class);
    				 intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
    				 startActivity(intent);
    			} else {
    				Toast.makeText(getApplicationContext(),
    						"Error encontrando usuario: " + e.getMessage(),
    						Toast.LENGTH_SHORT).show();
    			}
    		}
    	});
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
                mostrarDialogo(R.string.llamar);
                return true;
            case R.id.action_sms:
            	mostrarDialogo(R.string.sms);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void mostrarDialogo(int resId){
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.llamar_sms_dialogo, null))
        // Add action buttons
               .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       startActivity(new Intent(ListaUsuariosActivity.this, CallingActivity.class));
                   }
               })
               .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });      
        
        builder.setTitle(resId);
        builder.create().show();

        
    	
    }
    
}
