package com.parse.sinch.social.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.parse.sinch.social.MessagesActivity;
import com.parse.sinch.social.R;
import com.parse.sinch.social.entities.UserInfo;

public class UserAdapter extends BaseAdapter {

	private Context context;
	private AQuery aq;
	private List<UserInfo> userInfo;
	
	public UserAdapter(Context context, List<UserInfo> userInfo){
		this.context = context;
		this.userInfo = userInfo;        
	}
	
	@Override
	public int getCount() {		
		return userInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.user_lista_detalles, null);
		}

		aq = new AQuery(convertView);
		
		
		aq.id(R.id.txtNombre).text(userInfo.get(position).getName());
		
		if(userInfo.get(position).isStatus()){
			aq.id(R.id.imgStatus).image(context.getResources().getDrawable(R.drawable.connected));
		}
		
		aq.id(R.id.userElement).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	               context.startActivity(new Intent(context, MessagesActivity.class));
				
			}
		});
		
		return convertView;
	}

	
}
