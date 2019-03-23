package com.example.cse410androidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class AddEntry extends AppCompatActivity {

    /*the code to upload a gallery image is referenced here from
    http://viralpatel.net/blogs/pick-image-from-galary-android-app/*/
    private static int RESULT_LOAD_IMAGE = 1;
    private static ArrayList<Entry> Entries = new ArrayList<Entry>();
    ArrayAdapter<Entry> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        EditText Note = (EditText) findViewById(R.id.editNote);

        adapter = new ArrayAdapter<Entry>(this, android.R.layout.simple_list_item_1,Entries);

        Button UploadPicture = (Button) findViewById(R.id.buttonUpload);
        UploadPicture.setOnClickListener(new View.OnClickListener() {

            /*INTENT TO TRIGGER THE IMAGE GALLERY PICKER*/
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Button SaveInfo = (Button) findViewById(R.id.buttonSave);
        SaveInfo.setOnClickListener(new View.OnClickListener() {

            /*INTENT TO TRIGGER THE IMAGE GALLERY PICKER*/
            @Override
            public void onClick(View arg0) {

                EditText titleText = (EditText) findViewById(R.id.editTitle);
                String title = titleText.getText().toString();

                EditText noteText = (EditText) findViewById(R.id.editNote);
                String note = noteText.getText().toString();

                Entry e = new Entry();
                e.setTitle(title);
                e.setNote(note);

                Entries.add(e);
                adapter.notifyDataSetChanged();
                finish();

            }
        });

        Button CancelInfo = (Button) findViewById(R.id.buttonCancel);
        CancelInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }
    protected void onPostExecute(){
        ListView lister = (ListView) findViewById(R.id.listViewer);
        lister.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathArray = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathArray, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathArray[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

}
