package com.pg.fontdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MainActivity";
    Button txtBtnUploadImage, btnShowMarker;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 111;
    private static final int CAMER_PERMISSION = 222;
    private final int CAMERA_REQUEST_PROFILE_PIC = 1;
    private final int SELECT_PICTURE_PROFILE_PIC = 2;
    private String binaryImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBtnUploadImage = findViewById(R.id.txtBtnUploadImage);
        btnShowMarker = findViewById(R.id.btnShowMarker);

        txtBtnUploadImage.setOnClickListener(this);
        btnShowMarker.setOnClickListener(this);
        //AIzaSyCvLBNmFTSKzfat1uT3VIaAk42IpD2vRt0
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtBtnUploadImage:
                uploadImageDialog();
                break;
            case R.id.btnShowMarker:
                Intent i = new Intent(this, MyLocationActivity.class);
                startActivity(i);
                break;
        }
    }


    /**
     * will open upload image dialog
     */
    private void uploadImageDialog() {
        AlertDialog.Builder gallaryDilog = new AlertDialog.Builder(this);
        gallaryDilog.setTitle("Upload Pictures Option ");
        gallaryDilog.setMessage("How do you want to set your picture?");
        gallaryDilog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        readExternalStoragePermission();
                    }
                });
        gallaryDilog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        openCamerPersmission();

                    }
                });
        gallaryDilog.show();
    }   //end of uploadImageDialog


    private void readExternalStoragePermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        Log.d(TAG, "Already have read external storage permission :: " + checkIfAlreadyHaveReadStoragePermission());
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (!checkIfAlreadyHaveReadStoragePermission()) {
                requestForReadStoragePermission();
            } else {
                openGallery();
            }
        } else {
            openGallery();
        }
    }


    /**
     * will open gallery
     */
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_PROFILE_PIC);
    }   //end of openGallery

    /**
     * check SMS read permission
     *
     * @return
     */
    private boolean checkIfAlreadyHaveReadStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * will give camera permission
     */
    private void openCamerPersmission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            //read sms permission
            if (!checkIfAlreadyHaveCameraPermission()) {
                requestForCameraPermission();
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    /***
     * request read external storage permission
     */
    private void requestForReadStoragePermission() {
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_READ_EXTERNAL_STORAGE);
    }

    /**
     * check SMS read permission
     *
     * @return
     */
    private boolean checkIfAlreadyHaveCameraPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestForCameraPermission() {
        // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMER_PERMISSION);
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMER_PERMISSION);
    }

    /**
     * will open camera
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROFILE_PIC);
    }   //end of openCamera


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "request code :: " + requestCode);
        switch (requestCode) {
            case CAMER_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                Log.d(TAG, "request code :: " + CAMER_PERMISSION);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "request code :: " + CAMER_PERMISSION);
                    openCamera();

                } else {

                }
                break;


            case REQUEST_READ_EXTERNAL_STORAGE:
                Log.d(TAG, "REQUEST_READ_EXTERNAL_STORAGE:: " + REQUEST_READ_EXTERNAL_STORAGE);
                Log.d(TAG, "REQUEST_READ_EXTERNAL_STORAGE:: " + grantResults.length + " " + grantResults[0]);
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {


                }
                break;

        }
    }

    //handle gallery request
    private void uploadPicFromGallery(Intent data) {
        Log.d(TAG, "uploadPicFromGallery :: ");
        Uri uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //  ByteArrayOutputStream out = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  //used to compress uploaded image
            binaryImage = encodeToBase64(bitmap);
            Log.d(TAG, "Binray image :: " + binaryImage);
            setImageInUserImage(binaryImage);

        } catch (IOException e) {
            Toast.makeText(this, "Error in uploading your image please upload again ...!", 1);
            e.printStackTrace();
        }

    }//end of uploadPicFromGallery()

    /**
     * will set binary image in imageView
     *
     * @param binaryImage
     */
    private void setImageInUserImage(String binaryImage) {
        byte[] decodedBytes = Base64.decode(binaryImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        //setttttt bipmap here

    }   //end of setImageInUserImage


    public String encodeToBase64(Bitmap image) {
        String imageEncoded = null;
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.PNG, 10, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "error while converting to code");
        }
        return imageEncoded;
    }//end of encodeToBase64()


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult :: data :: " + data);
            if (data != null) {
                if (requestCode == CAMERA_REQUEST_PROFILE_PIC) {//profile from camera
                    browseImage(data);
                } else if (requestCode == SELECT_PICTURE_PROFILE_PIC) {//profile from gallery
                    uploadPicFromGallery(data);
                }
            }
        }//end of resultCode if


    }//end of onActivityResult()


    //camera request handle
    private void browseImage(Intent data) {
        if (data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String profilePicBinaryData = encodeToBase64(bitmap);
            Log.d(TAG, "profilePicBinaryData :: " + profilePicBinaryData);
            binaryImage = profilePicBinaryData;
            setImageInUserImage(profilePicBinaryData);
        }
    }//end of browseImage()

}

