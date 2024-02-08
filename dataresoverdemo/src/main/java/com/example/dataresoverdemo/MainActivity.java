package com.example.dataresoverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ContentResolver mResolver;
    private EditText nameEdt, ageEdt, idEdt;
    RadioGroup mRg;
    private String mGender;
    private ListView stuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ContentResolverのオブジェクトを取得する
        mResolver = getContentResolver();

        nameEdt = findViewById(R.id.name_edt);
        ageEdt = findViewById(R.id.age_edt);
        idEdt = findViewById(R.id.id_edt);
        stuList = findViewById(R.id.stu_list);

        mRg = findViewById(R.id.gender_rg);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    mGender = "男";
                } else {
                    mGender = "女";
                }
            }
        });
    }

    public void operate(View v) {
        String name = nameEdt.getText().toString();
        String age = ageEdt.getText().toString();
        String _id = idEdt.getText().toString();

        Uri uriStr = Uri.parse("content://com.imooc.myprovider");
        if (v.getId() == R.id.insert_btn) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("age", age);
            values.put("gender", mGender);

            Uri uri = mResolver.insert(uriStr, values);
            long id = ContentUris.parseId(uri);
            Toast.makeText(this, "添加成功，新学生的id是：" + id, Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.query_btn) {
            Cursor c = mResolver.query(uriStr, null, null, null, null);
            //CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER dataを更新する用
            //c---new String[]
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, c, new String[]{"_id",
                    "name", "age", "gender"}, new int[]{R.id.id_txt, R.id.name_txt, R.id.age_txt, R.id.gender_txt}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            stuList.setAdapter(adapter);

        } else if (v.getId() == R.id.delete_btn) {
            int result = mResolver.delete(uriStr, "_id=?", new String[]{_id});
            if (result > 0) {
                Toast.makeText(this, "删除成功，影响" + result + "行", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.update_btn) {
            //update info_tb set name=? age=? ... where _id=?

            ContentValues values2 = new ContentValues();
            values2.put("name", name);
            values2.put("age", age);
            values2.put("gender", mGender);
            int result = mResolver.update(uriStr, values2, "_id=?", new String[]{_id});
            if (result > 0) {
                Toast.makeText(this, "修改成功，影响" + result + "行", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.mather_btn) {
            mResolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld"), null, null);
            mResolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/abc"), null, null);
            mResolver.delete(Uri.parse("content://com.imooc.myprovider/helloworld/999"), null, null);
            mResolver.delete(Uri.parse("content://com.imooc.myprovider/nihao/abc"), null, null);
        } else if (v.getId() == R.id.uri_btn) {
            Uri uri = mResolver.insert(Uri.parse("content://com.imooc.myprovider/whatever?name=张三&age=23&gender=男"), new ContentValues());
            long id = ContentUris.parseId(uri);
            Toast.makeText(this, "添加成功2，也意味着解析成功，新学生的id是：" + id, Toast.LENGTH_SHORT).show();
        }


    }
}