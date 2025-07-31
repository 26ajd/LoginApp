package com.example.loginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * فئة مساعدة لإدارة قاعدة البيانات المحلية
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    
    // معلومات قاعدة البيانات
    private static final String DATABASE_NAME = "LoginApp.db";
    private static final int DATABASE_VERSION = 2;
    
    // جدول المستخدمين
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_LAST_LOGIN = "last_login";
    
    // استعلام إنشاء الجدول
    private static final String CREATE_TABLE_USERS = 
        "CREATE TABLE " + TABLE_USERS + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
        COLUMN_PASSWORD + " TEXT NOT NULL, " +
        COLUMN_EMAIL + " TEXT, " +
        COLUMN_FULL_NAME + " TEXT, " +
        COLUMN_PHONE + " TEXT, " +
        COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_LAST_LOGIN + " DATETIME" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // إنشاء جدول المستخدمين
        db.execSQL(CREATE_TABLE_USERS);
        
        // إدراج مستخدم افتراضي للاختبار
        insertDefaultUser(db);
        
        Log.d(TAG, "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // حذف الجدول القديم وإنشاء جديد
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        
        Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    /**
     * إدراج مستخدم افتراضي للاختبار
     */
    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "26ajd");
        values.put(COLUMN_PASSWORD, "26ajd");
        values.put(COLUMN_EMAIL, "busman.alazazi@gmail.com");
        values.put(COLUMN_FULL_NAME, "المدير العام");
        values.put(COLUMN_PHONE, "779713890");
        
        long result = db.insert(TABLE_USERS, null, values);
        if (result != -1) {
            Log.d(TAG, "Default user inserted successfully");
        } else {
            Log.e(TAG, "Failed to insert default user");
        }
    }

    /**
     * التحقق من صحة بيانات تسجيل الدخول
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @return true إذا كانت البيانات صحيحة، false إذا لم تكن كذلك
     */
    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        
        Cursor cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        
        if (isValid) {
            updateLastLogin(username);
        }
        
        return isValid;
    }

    /**
     * تحديث وقت آخر تسجيل دخول
     * @param username اسم المستخدم
     */
    private void updateLastLogin(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAST_LOGIN, System.currentTimeMillis());
        
        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        
        int rowsAffected = db.update(TABLE_USERS, values, whereClause, whereArgs);
        
        if (rowsAffected > 0) {
            Log.d(TAG, "Last login updated for user: " + username);
        }
        
        db.close();
    }

    /**
     * إضافة مستخدم جديد
     * @param username اسم المستخدم
     * @param password كلمة المرور
     * @param email البريد الإلكتروني
     * @param fullName الاسم الكامل
     * @param phone رقم الهاتف
     * @return true إذا تم الإدراج بنجاح، false إذا فشل
     */
    public boolean addUser(String username, String password, String email, String fullName, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_PHONE, phone);
        
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        
        if (result != -1) {
            Log.d(TAG, "User added successfully: " + username);
            return true;
        } else {
            Log.e(TAG, "Failed to add user: " + username);
            return false;
        }
    }

    /**
     * التحقق من وجود اسم المستخدم
     * @param username اسم المستخدم
     * @return true إذا كان موجوداً، false إذا لم يكن موجوداً
     */
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = db.query(
            TABLE_USERS,
            new String[]{COLUMN_ID},
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        
        return exists;
    }

    /**
     * الحصول على معلومات المستخدم
     * @param username اسم المستخدم
     * @return كائن User أو null إذا لم يوجد
     */
    public User getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = db.query(
            TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        );
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
            user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
            user.setLastLogin(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN)));
        }
        
        cursor.close();
        db.close();
        
        return user;
    }

    /**
     * تغيير كلمة المرور
     * @param username اسم المستخدم
     * @param newPassword كلمة المرور الجديدة
     * @return true إذا تم التحديث بنجاح، false إذا فشل
     */
    public boolean changePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        
        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        
        int rowsAffected = db.update(TABLE_USERS, values, whereClause, whereArgs);
        db.close();
        
        if (rowsAffected > 0) {
            Log.d(TAG, "Password changed successfully for user: " + username);
            return true;
        } else {
            Log.e(TAG, "Failed to change password for user: " + username);
            return false;
        }
    }
}

