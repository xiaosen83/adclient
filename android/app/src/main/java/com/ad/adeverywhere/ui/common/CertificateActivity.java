package com.ad.adeverywhere.ui.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.demo.DemoUserCert;
import com.ad.adeverywhere.utils.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CertificateActivity extends AppCompatActivity {

    public static final int CHOOSEN_CAMERAL = 0;
    public static final int CHOOSEN_GALLERY = 1;
    public static final int REQUEST_CAMERA = 0;
    public static final int SELECT_FILE = 1;

    private int userChoosenTask = -1;

    private ImageView idImage = null;
    private EditText txName = null;
    private EditText txIdentity = null;
    private TextView txCity = null;
    private Button btSubmit = null;

    private TextView txStatus = null;
    private TableRow rowStatus = null;
    private LinearLayout rowPhoto = null;

    private Boolean isCerted = false;
    private Bitmap idBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_activity_certificate);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        idImage = (ImageView)findViewById(R.id.iconTakePhoto);
        txName = (EditText)findViewById(R.id.txName);
        txIdentity = (EditText)findViewById(R.id.txIdentitiy);
        txCity = (TextView)findViewById(R.id.txCity);
        btSubmit = (Button)findViewById(R.id.btSubmit);

        txStatus = (TextView)findViewById(R.id.txStatus);
        rowStatus = (TableRow)findViewById(R.id.rowStatus);
        rowPhoto = (LinearLayout)findViewById(R.id.rowPhoto);

        initLayout();

    }

    private void initLayout(){
        DemoUserCert userCert = UserState.getInstance().getUserCert(this, true);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });
        if(userCert == null){
            //not certificated
            isCerted = false;
            txStatus.setText(R.string.cert_status_not);
            rowPhoto.setVisibility(View.VISIBLE);
            btSubmit.setText(R.string.cert_submit);
            idImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
            txName.setEnabled(true);
            txIdentity.setEnabled(true);
        }
        else{
            //certificated
            isCerted = true;
            //new FillCertedInfoTask().execute(userCert);
            btSubmit.setText(R.string.cert_unbind);
            txName.setText(userCert.getName()); txName.setEnabled(false);
            txCity.setText(userCert.getCity());
            txIdentity.setText(userCert.getIdentity()); txIdentity.setEnabled(false);
            txStatus.setText(R.string.cert_status_done);
            rowPhoto.setVisibility(View.GONE);
            //idImage.setImageBitmap(Util.getImage(userCert.getPhoto()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
      AlertDialog.Builder builder = new AlertDialog.Builder(CertificateActivity.this);
      builder.setTitle(R.string.photo_dlg_title);
      builder.setItems(R.array.photo_selector, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int item) {
            boolean result=Util.checkPermission(CertificateActivity.this);
             ListView lv = ((AlertDialog)dialog).getListView();
             String tx = lv.getItemAtPosition(item).toString();
             if (item == 0) {
                 userChoosenTask = CHOOSEN_CAMERAL;
                 cameraIntent();
             } else if (item == 1) {
                 userChoosenTask = CHOOSEN_GALLERY;
                 galleryIntent();
             } else{
                 dialog.dismiss();
             }
         }
      });
      builder.show();
    }

    private void cameraIntent()
    {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);//
      startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      switch (requestCode) {
         case Util.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               if(userChoosenTask == CHOOSEN_CAMERAL)
                  cameraIntent();
               else if(userChoosenTask == CHOOSEN_GALLERY)
                  galleryIntent();
            } else {
               //code for deny
            }
            break;
      }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == Activity.RESULT_OK) {
         if (requestCode == SELECT_FILE)
            onSelectFromGalleryResult(data);
         else if (requestCode == REQUEST_CAMERA)
            onCaptureImageResult(data);
      }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
      if (data != null) {
         try {
             idBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      idImage.setImageBitmap(idBitmap);
    }

    private void onCaptureImageResult(Intent data) {
      idBitmap = (Bitmap) data.getExtras().get("data");
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        idBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
      File destination = new File(Environment.getExternalStorageDirectory(),
            System.currentTimeMillis() + ".jpg");
      FileOutputStream fo;
      try {
         destination.createNewFile();
         fo = new FileOutputStream(destination);
         fo.write(bytes.toByteArray());
         fo.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      idImage.setImageBitmap(idBitmap);
    }

    private void onSubmitClicked(){
        if(!isCerted) {
            DemoUserCert userCert = new DemoUserCert();
            userCert.setName(txName.getText().toString());
            userCert.setUserId(UserState.getInstance().getUserId(this));
            userCert.setIdentity(txIdentity.getText().toString());
            userCert.setCity("上海");
            //userCert.setPhoto(Util.getBytes(idBitmap));
            if(userCert.getName().length() == 0 || userCert.getIdentity().length() == 0 || userCert.getCity().length() == 0)
            {
                showAlertDlg("Error", "Please fill all the filed!");
                return;
            }
            new SubmitCertTask().execute(userCert);
        }
        else{
            DemoDataSource ds = new DemoDataSource(CertificateActivity.this);
            ds.open();
            ds.unbindUserCert(UserState.getInstance().getUsername());
            ds.close();
            UserState.getInstance().setUserCert(null);
            isCerted = false;
            finish();
        }
    }

    private class SubmitCertTask extends AsyncTask<DemoUserCert, Void, Integer> {
        @Override
        protected Integer doInBackground(DemoUserCert... params) {
            DemoUserCert userCert = params[0];
            DemoDataSource ds = new DemoDataSource(CertificateActivity.this);
            ds.open();
            int num = (int)ds.addUserCert(userCert);
            ds.close();
            UserState.getInstance().setUserCert(userCert);
            return Integer.valueOf(num);
        }

        @Override
        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            if(num > 0)
            {
                isCerted = true;
                finish();
            }
            else{
                //error ...
                UserState.getInstance().setUserCert(null);
            }
        }
    }

    private void showAlertDlg(String title, String msg){
        //authenticated, show user info
        new AlertDialog.Builder(this).setTitle(title)
                .setMessage(msg)
                .setNegativeButton(getString(R.string.bt_cancel), null).show();
    }
}
