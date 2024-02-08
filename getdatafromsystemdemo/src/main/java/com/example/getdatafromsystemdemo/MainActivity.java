package com.example.getdatafromsystemdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.health.connect.changelog.ChangeLogTokenRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int PERMISSIONS_REQUEST_WRITE_CONTACTS=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSmsData();
            }
        });

        findViewById(R.id.btn_read_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    readContactData();
                }
            }
        });

        findViewById(R.id.btn_add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {
                    addContactData();
                }
            }
        });

    }

    private void addContactData() {
        ContentResolver resolver = getContentResolver();
        // 1. あるContentProviderに空のデータを挿入し、新しく生成されたIDを取得します。
        // 2. ちょうど生成されたIDを使用して、名前と電話番号を含むデータを別のContentProviderに挿入します。
        ContentValues values=new ContentValues();
        Uri uri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long id = ContentUris.parseId(uri);

        //名前を挿入する
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,"zyp");
        values.put(ContactsContract.Data.RAW_CONTACT_ID,id);//idを指定する
            //typeを指定する
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);

        //電話番号を挿入する
        values.clear();
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,"13833192360");
        values.put(ContactsContract.Data.RAW_CONTACT_ID,id);//idを指定する
            //typeを指定する
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        resolver.insert(ContactsContract.Data.CONTENT_URI, values);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContactData();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the contact", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                addContactData();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot add contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readContactData() {
// 連絡先に関して、名前とその他の情報（電話番号など）は異なるContentProviderの操作によって保存されます。
// ますます、名前とその他の情報は異なるテーブルに属していると考えます。
// 名前が存在するテーブルが主テーブルであり、その他の情報が従テーブルに存在します。
// 主テーブル内の主キーは、従テーブルで外部キーとして使用されます。

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (c.moveToNext()) {
//            ContactsContract.Contacts.DISPLAY_NAME  name
//            ContactsContract.Contacts._ID           key
            @SuppressLint("Range") String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            @SuppressLint("Range") String _id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            Log.e(TAG, "姓名是" + name + " _id is: " + _id);

//            ContactsContract.CommonDataKinds.Phone  phone info
            String selections = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
            Cursor c2 = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selections, new String[]{_id}, null);

            while (c2.moveToNext()) {
                @SuppressLint("Range") String number = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                name += " " + number;
            }
            Log.e(TAG, "readContactData: " + name);

//            String msg = "";
//            for (int i = 0; i < c.getColumnCount(); i++) {
//                msg += c.getString(i) + " ";
//            }
//            Log.i(TAG, msg);
        }
    }

    private void readSmsData() {
        Log.i(TAG, "readSmsData: ");
        //1. 内容処理者
        ContentResolver resolver = getContentResolver();
        //2. query メソッド
        /**
         * content://sms
         * content://sms/inbox 受信箱
         * content://sms/sent　発信箱
         * content://sms/draft　下書き箱
         */

        Uri uri = Uri.parse("content://sms/sent");
        Cursor cursor = resolver.query(uri, null, null, null, null);
        //3.  Cursorを解析する
        while (cursor.moveToNext()) {
            String msg = "";
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex("body"));

            msg = address + ":" + body;
//            for (int i = 0; i < cursor.getColumnCount(); i++) {
//                msg += cursor.getString(i) + "";
//            }
            Log.e(TAG, msg);
        }
    }
}