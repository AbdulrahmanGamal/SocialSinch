package com.parse.sinch.social;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.sinch.social.utils.Utils;

public class RegistroActivity extends Activity {

	private AQuery aq;
	private Uri pictureTakenUri;
	private Dialog progressDialog;
	private Context context;
	
	private static final String TAG = RegistroActivity.class.getName();
	
	private static final int SELECT_PICTURE_CODE = 0;
	private static final int TAKE_PICTURE_CODE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        
        aq = new AQuery(this);
        
        context = getApplicationContext();
        
        aq.id(R.id.imgProfile).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				selectProfilePicture();
				
			}
		});
        
        aq.id(R.id.btnSignUp).clicked(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 String name = aq.id(R.id.editUserName).getEditText().getText().toString();
				 String lastname = aq.id(R.id.editUserLastname).getEditText().getText().toString();
				 String username = aq.id(R.id.editUSer).getEditText().getText().toString();
				 String password = aq.id(R.id.editPassword).getEditText().getText().toString();
				 String number = aq.id(R.id.editUserPhone).getEditText().getText().toString();
				 

				 ParseUser user = new ParseUser();
				 user.setUsername(username);
				 user.setPassword(password);
				 user.put("NAME", name);
				 user.put("LASTNAME", lastname);
				 user.put("NUMBER", number);
				 
				 user.signUpInBackground(new SignUpCallback() {
					 public void done(com.parse.ParseException e) {
						 if (e == null) {
							 //start sinch service
							 //start next activity
							 startActivity(new Intent(RegistroActivity.this, ListaUsuariosActivity.class));
						 } else {
							 Toast.makeText(getApplicationContext(),
									 "Error al registrar:" + e.getMessage()
									 , Toast.LENGTH_LONG).show();
						 }
					 }
				 });
			}
		});
    }
    
    
    private void selectProfilePicture(){

    	CharSequence photoOptions[] = new CharSequence[] {getResources().getString(R.string.option_take_photo), getResources().getString(R.string.option_gallery)};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.select_photo_source));
			builder.setItems(photoOptions, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int option) {
					if(option == 0){

						// Creates a unique subdirectory of the designated app cache directory. Tries to use external
						// but if not mounted, falls back on internal storage.
						//String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + getResources().getString(R.string.photo_directory);

					    // Check if media is mounted or storage is built-in, if so, try and use external cache dir
					    // otherwise use internal cache dir

					    String dir = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
					                    !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
					                            context.getCacheDir().getPath();

						//Log.i(TAG, "Picture folder: " + dir);
						File newdir = new File(dir);
						//Log.i(TAG, "Picture Directory Exists: " + newdir.exists());
						if(!newdir.exists()){
							newdir.mkdirs();
						}

						File newfile = new File(dir + File.separator + getResources().getString(R.string.profile_picture_name));

						try {
							newfile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}       

						pictureTakenUri = Uri.fromFile(newfile);

						Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureTakenUri);

						startActivityForResult(cameraIntent, TAKE_PICTURE_CODE);

					}else{
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent,
								getResources().getString(R.string.pick_image)), SELECT_PICTURE_CODE);	    			        	
					}    			        
				}
			});
			
			builder.show();
    	
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "OnActivityResult in Activity: " + data);
		Log.i(TAG, "OnActivityResult requestCode: " + requestCode);
		Log.i(TAG, "OnActivityResult resultCode: " + resultCode);
		try{
			if(resultCode == Activity.RESULT_OK) {

				switch (requestCode) {
				
				case TAKE_PICTURE_CODE:
					processPicture(pictureTakenUri,false);					
					break;

				case SELECT_PICTURE_CODE:					 
					Uri picture = data.getData();
					processPicture(picture,true);
					break;
				default:
					break;
				}
			}else{
				if(requestCode == TAKE_PICTURE_CODE){

					File newfile = new File(pictureTakenUri.getPath());
					if(newfile.exists()){
						if(newfile.length() == 0){
							newfile.delete();
						}
					}
				}
			}

		}catch(Exception e){

			e.printStackTrace();

		}			
	}
	
	public void processPicture(Uri picture, boolean isFromGallery){
		
		try{
			    File f = null;
			    Bitmap bitmap = null;
			    String filepath = null;
			    
			    if(isFromGallery){	
		            // SDK >= 11 && SDK < 19
		            if (Build.VERSION.SDK_INT < 19)
		                filepath = Utils.getRealPathFromURI_API11to18(context, picture);
		            // SDK > 19 (Android 4.4)
		            else
		                filepath = Utils.getRealPathFromURI_API19(context, picture);
			    				    	
			    }else{
			    	filepath = picture.getPath();
			    }

			    bitmap = Utils.getResizedBitmap(this, filepath, 120);
			    f = new File(filepath);
				ExifInterface exif = new ExifInterface(f.getPath());
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

				int angle = 0;

				if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
					angle = 90;
				} 
				else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
					angle = 180;
				} 
				else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
					angle = 270;
				}
				else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
					angle = 270;
				}

				if(angle > 0){
					Matrix mat = new Matrix();
					mat.postRotate(angle);

					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
				}

				aq.id(R.id.imgProfile).image(bitmap, AQuery.RATIO_PRESERVE);
				aq.id(R.id.imgProfile).getView().invalidate();
				
				
		}catch(Exception e){

			e.printStackTrace();

		}
	}
}
