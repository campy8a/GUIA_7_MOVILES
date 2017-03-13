package com.dhamova.fvr;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.jar.*;

public class RegistroActivity extends AppCompatActivity {

    Button  btnImages;
    ImageView imagen;
    public final static int REQUEST_PHOTO =123;
    public final static int REQUEST_PERMISSION =111;
    private static final String TAG = "Photo";

    Uri imagetoUploadUri1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnImages = (Button) findViewById(R.id.btn_select_img);
        btnImages.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
               seleccionarImagen();
            }
        });

        imagen = (ImageView) findViewById((R.id.foto));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imagetoUploadUri1 = data.getData();
            }
            if (imagetoUploadUri1 != null) {
                Uri selectedImage = imagetoUploadUri1;
                getContentResolver().notifyChange(selectedImage, null);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                    Log.d(TAG, "No hay permiso");

                    Bitmap reducedSizeBitmap = getBitmap(getRealPathFromURI(imagetoUploadUri1));
                    if (reducedSizeBitmap != null) {
                        Log.d(TAG, "llega bien");
                        imagen.setImageBitmap(reducedSizeBitmap);
                    }
                }
                else {
                    Toast.makeText(this, "ERROR CAPTURANDO IMAGEN DE URI", Toast.LENGTH_LONG).show();
                }


                }

            }
        }





    private void seleccionarImagen()
    {
        PackageManager packageManager = getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)==false)
        {
            Toast.makeText(this,"Este dispositivo no tiene camara",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent pickIntent2 = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent2.setType("image/*");
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(),"/drawable/user.png");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        String pickTitle = "Seleccionar o tomar una foto";
        Intent chooserIntent = Intent.createChooser(pickIntent2,pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
        imagetoUploadUri1 = Uri.fromFile(f);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(chooserIntent,REQUEST_PHOTO);
        }
    }


    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result ="";
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try{
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);

            }catch(Exception e)
            {
                Log.d("data", contentURI.toString());
                return contentURI.getPath();
            }
            cursor.close();
        }
        return result;
    }
}
