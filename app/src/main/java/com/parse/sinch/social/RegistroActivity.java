package com.parse.sinch.social;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.makeramen.RoundedDrawable;
import com.makeramen.RoundedImageView;
import com.parse.sinch.social.utils.Utils;

public class RegistroActivity extends Activity {

	private AQuery aq;
	private Uri pictureTakenUri;
	private ProgressDialog progressDialog;
	private Context context;
    private Bitmap profiePictureResized;
    //private ParseFile profilePictureFile;
	
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

                if (profiePictureResized != null) {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    profiePictureResized.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();

//                    profilePictureFile = new ParseFile(context.getResources().getString(R.string.profile_picture_name), bitmapdata);
//
//                    progressDialog = new ProgressDialog(RegistroActivity.this);
//                    progressDialog.setTitle(getResources().getString(R.string.loading));
//                    progressDialog.setMessage(getResources().getString(R.string.loading_msj));
//                    progressDialog.show();
//
//                    profilePictureFile.saveInBackground(new SaveCallback() {
//
//                        public void done(ParseException e) {
//                            if (e != null) {
//                                progressDialog.dismiss();
//                                new AlertDialog.Builder(RegistroActivity.this)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .setTitle(R.string.error_title)
//                                        .setMessage(R.string.error_save_picture_msj + " " + e.getMessage())
//                                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        })
//                                        .show();
//
//                            } else {
//
//                                savePersonalInformation();
//                            }
//                        }
//                    });
//
                }else{
                    savePersonalInformation();
                }
            }

		});
    }
    
    private void savePersonalInformation(){

        //Toast.makeText(((OutsideNavigationActivity)ctx),
        //	"Picture Sucessfully Saved ", Toast.LENGTH_LONG).show();
        // other fields can be set just like with ParseObject
        String name = aq.id(R.id.editUserName).getEditText().getText().toString();
        String lastname = aq.id(R.id.editUserLastname).getEditText().getText().toString();
        final String username = aq.id(R.id.editUSer).getEditText().getText().toString();
        final String password = aq.id(R.id.editPassword).getEditText().getText().toString();
        String number = aq.id(R.id.editUserPhone).getEditText().getText().toString();

//        ParseUser user = new ParseUser();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.put("NAME", name);
//        user.put("LASTNAME", lastname);
//        user.put("NUMBER", number);
//
//        if(profilePictureFile != null) {
//            user.put("PICTURE", profilePictureFile);
//        }
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                progressDialog.dismiss();
//                if (e == null) {
//                    new AlertDialog.Builder(RegistroActivity.this)
//                            .setIcon(android.R.drawable.ic_dialog_info)
//                            .setTitle(R.string.success)
//                            .setMessage(R.string.success_registered)
//                            .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    progressDialog = new ProgressDialog(RegistroActivity.this);
//                                    progressDialog.setTitle(getResources().getString(R.string.loading));
//                                    progressDialog.setMessage(getResources().getString(R.string.loading_msj));
//                                    progressDialog.show();
//
//                                    ParseUser.logInInBackground(username, password, new LogInCallback() {
//                                        public void done(ParseUser user, ParseException e) {
//                                            progressDialog.dismiss();
//                                            if (user != null) {
//                                                startActivity(new Intent(RegistroActivity.this, TabActivity.class));
//                                            } else {
//                                                new AlertDialog.Builder(RegistroActivity.this)
//                                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                                        .setTitle(R.string.error_title)
//                                                        .setMessage(R.string.error_login_msj + " " + e.getMessage())
//                                                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                            }
//                                                        })
//                                                        .show();
//                                            }
//                                        }
//                                    });
//                                }
//
//                            })
//                            .show();
//
//                } else {
//                    progressDialog.dismiss();
//                    new AlertDialog.Builder(RegistroActivity.this)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setTitle(R.string.error_title)
//                            .setMessage(R.string.error_sign_up_msj + " " + e.getMessage())
//                            .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            })
//                            .show();
//                }
//            }
//        });

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

                profiePictureResized = Utils.getResizedBitmap(this, filepath, 100);
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

                    profiePictureResized = Bitmap.createBitmap(profiePictureResized, 0, 0, profiePictureResized.getWidth(), profiePictureResized.getHeight(), mat, true);
				}

				aq.id(R.id.imgProfile).image(profiePictureResized);
				aq.id(R.id.imgProfile).getView().invalidate();
				
				
		}catch(Exception e){

			e.printStackTrace();

		}
	}
}
