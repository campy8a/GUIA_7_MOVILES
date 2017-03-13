package com.dhamova.fvr;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK)
        {
            if(data!=null)
            {
                imagetoUploadUri1 = data.getData();
            }
            if(imagetoUploadUri1!=null)
            {
                Uri selectedImage = imagetoUploadUri1;
                getContentResolver().notifyChange(selectedImage,null);

                if( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
                    Log.d(TAG,"No hay permiso");
                }
               // Bitmap reducedSizeBitmap = getBitmap(getRealPathFromUri(imagetoUploadUri1));


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
        Intent pickIntent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

}
