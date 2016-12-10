package ru.ryuzmukhametov.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends ListActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "MainActivity";

    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        setupAlarm();
    }


    @Override
    protected void onResume() {
        super.onResume();
        actualizeAdapter();
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        ListAdapter.Item item = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("bitmapPath", item.mFullPath);
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        //finish();
    }

    private void actualizeAdapter() {
        ListAdapter.Item[] adapterData = getAdapterData();
        this.mAdapter = new ListAdapter(this, adapterData);
        this.setListAdapter(mAdapter);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageUtils.saveImage(imageBitmap, imageDir());
            actualizeAdapter();
        }
    }

    private ListAdapter.Item[] getAdapterData() {
        File directory = imageDir();
        File[] files = directory.listFiles();

        ListAdapter.Item res[] = new ListAdapter.Item[files.length];
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String name = file.getName();
                res[i] = new ListAdapter.Item(name, file.getAbsolutePath());
            }
        }

        return res;
    }

    private File imageDir() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        return myDir;
    }

    public void setupAlarm()
    {
        Long time = new GregorianCalendar().getTimeInMillis()+ 60*1000;

        Intent intentAlarm = new Intent(this, AlarmNotificationReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(this.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

    }



}
