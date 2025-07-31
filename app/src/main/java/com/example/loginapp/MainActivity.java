package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage;
    private MaterialButton btnLogout;
    private MaterialCardView cardProfile, cardSettings, cardAbout;
    
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_preferences";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // إعداد شريط الأدوات
        setupToolbar();
        
        // تهيئة المتغيرات
        initializeViews();
        initializeSharedPreferences();
        setupWelcomeMessage();
        setupClickListeners();
    }

    /**
     * إعداد شريط الأدوات
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("الصفحة الرئيسية");
        }
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
        btnLogout = findViewById(R.id.btn_logout);
        cardProfile = findViewById(R.id.card_profile);
        cardSettings = findViewById(R.id.card_settings);
        cardAbout = findViewById(R.id.card_about);
    }

    /**
     * تهيئة التفضيلات المشتركة
     */
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }

    /**
     * إعداد رسالة الترحيب
     */
    private void setupWelcomeMessage() {
        String username = sharedPreferences.getString(KEY_USERNAME, "المستخدم");
        tvWelcomeMessage.setText("مرحباً " + username + "!");
    }

    /**
     * إعداد مستمعي النقر
     */
    private void setupClickListeners() {
        // زر تسجيل الخروج
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // بطاقة الملف الشخصي
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "سيتم فتح الملف الشخصي", Toast.LENGTH_SHORT).show();
            }
        });

        // بطاقة الإعدادات
        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "سيتم فتح الإعدادات", Toast.LENGTH_SHORT).show();
            }
        });

        // بطاقة حول التطبيق
        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "حول التطبيق - الإصدار 1.0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * تسجيل الخروج
     */
    private void logout() {
        // مسح بيانات الاعتماد المحفوظة
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // عرض رسالة تأكيد
        Toast.makeText(this, "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show();
        
        // العودة إلى شاشة تسجيل الدخول
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * إنشاء قائمة الخيارات
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * التعامل مع اختيار عنصر من القائمة
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(this, "سيتم فتح الإعدادات", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * التعامل مع الضغط على زر الرجوع
     */
    @Override
    public void onBackPressed() {
        // إنهاء التطبيق عند الضغط على زر الرجوع
        finishAffinity();
    }
}

