package com.example.dataproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class MyContentProvider extends ContentProvider {
    /**
     * URI解析
     * 1. URiMathcer：在ContentProvider创建时，制定好匹配规则，当调用了ContentProvider中的操作方法时，利用匹配类去匹配传的URI，
     * 根据不同的URI给出不同的处理
     * 2. URi自带的解析方法
     */
    /**
     * URI解析
     * 1. URiMathcer：ContentProviderが作成される際に、一致規則を指定し、ContentProviderの操作メソッドが呼び出されると、
     * 一致クラスを使用して渡されたURIを一致させ、異なるURIに対して異なる処理を提供します。
     * 2. URIが持つ組み込みの解析メソッド
     */

    private static final String TAG = "MyContentProvider";
    private SQLiteDatabase mDb;
    private UriMatcher mMatcher;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //1000
        int result = 0;
        int code = mMatcher.match(uri);
        if (code == 1000) {
            Log.i(TAG, "delete: 匹配到路径 helloworld 1000");
        } else if (code == 1001) {
            Log.i(TAG, "delete: 匹配到路径 helloworld/abc 1001");
        } else if (code == 1002) {
            Log.i(TAG, "delete: 匹配到路径 helloworld/任意数字的内容 1002");
        } else if (code == 1003) {
            Log.i(TAG, "delete: 匹配到路径 nihao/任意字符的内容 1003");
        } else {
            Log.i(TAG, "delete: 执行删除数据库的方法");
            result = mDb.delete("info_tb", selection, selectionArgs);
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        if (values.size() > 0) {
            Log.e(TAG, "insert: 调用了MyContentProvider中的insert方法");
            id = mDb.insert("info_tb", null, values);
        } else{
            String authority = uri.getAuthority();
            String path = uri.getPath();
            String query = uri.getQuery();
            String name = uri.getQueryParameter("name");
            String age = uri.getQueryParameter("age");
            String gender = uri.getQueryParameter("gender");
            Log.e(TAG, "insert: 主机名"+authority+"路径 "+path+"查询数据"+query );
            Log.e(TAG, "insert: name:"+name);
            Log.e(TAG, "insert: gender:"+gender);
            Log.e(TAG, "insert: age:"+age);
            values.put("name",name);
            values.put("age",age);
            values.put("gender",gender);
            id = mDb.insert("info_tb", null, values);
        }
        //IDをURLの後に追加する
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public boolean onCreate() {
        // ContentProviderを作成するとき、このメソッドを呼び出す

        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(), "stu.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String sql = "create table info_tb (_id integer primary key autoincrement," +
                        "name varchar(20)," +
                        "age integer," +
                        "gender varchar(2))";

                db.execSQL(sql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

        mDb = helper.getReadableDatabase();
        //para：match できない
        // com.imooc.myprovider/helloworld
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI("com.imooc.myprovider", "helloworld", 1000);
        mMatcher.addURI("com.imooc.myprovider", "helloworld/abc", 1001);
        mMatcher.addURI("com.imooc.myprovider", "helloworld/#", 1002);//数字
        mMatcher.addURI("com.imooc.myprovider", "nihao/*", 1003);//任意
        //trueをもどる
        return true;

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        /** para2:column
         * para3: condition
         * para4:condition value
         */


        Cursor c = mDb.query("info_tb", projection, selection, selectionArgs, null, null, sortOrder);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int result = mDb.update("info_tb", values, selection, selectionArgs);
        return result;
    }
}