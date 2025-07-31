package com.example.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    // متغيرات عناصر الواجهة
    private TextInputEditText etUsername, etPassword;
    private TextInputLayout tilUsername, tilPassword;
    private MaterialButton btnLogin;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword, tvSignUp;
    
    // متغيرات للتفضيلات المشتركة
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_preferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";
    
    // بيانات تسجيل الدخول الافتراضية (للاختبار)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // تهيئة المتغيرات
        initializeViews();
        initializeSharedPreferences();
        loadSavedCredentials();
        setupClickListeners();
    }

    /**
     * تهيئة عناصر الواجهة
     */
    private void initializeViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        btnLogin = findViewById(R.id.btn_login);
        cbRememberMe = findViewById(R.id.cb_remember_me);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
    }

    /**
     * تهيئة التفضيلات المشتركة
     */
    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }

    /**
     * تحميل بيانات الاعتماد المحفوظة
     */
    private void loadSavedCredentials() {
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (rememberMe) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            
            etUsername.setText(savedUsername);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }
    }

    /**
     * إعداد مستمعي النقر
     */
    private void setupClickListeners() {
        // مستمع النقر لزر تسجيل الدخول
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // مستمع النقر لرابط نسيت كلمة المرور
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPassword();
            }
        });

        // مستمع النقر لرابط إنشاء حساب جديد
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });
    }

    /**
     * محاولة تسجيل الدخول
     */
    private void attemptLogin() {
        // إعادة تعيين رسائل الخطأ
        tilUsername.setError(null);
        tilPassword.setError(null);

        // الحصول على القيم المدخلة
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // التحقق من صحة كلمة المرور
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("يرجى إدخال كلمة المرور");
            focusView = etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            tilPassword.setError("كلمة المرور قصيرة جداً");
            focusView = etPassword;
            cancel = true;
        }

        // التحقق من صحة اسم المستخدم
        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("يرجى إدخال اسم المستخدم");
            focusView = etUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            tilUsername.setError("اسم المستخدم غير صحيح");
            focusView = etUsername;
            cancel = true;
        }

        if (cancel) {
            // هناك خطأ؛ لا تحاول تسجيل الدخول وركز على الحقل الأول الذي يحتوي على خطأ
            focusView.requestFocus();
        } else {
            // تنفيذ تسجيل الدخول الفعلي
            performLogin(username, password);
        }
    }

    /**
     * التحقق من صحة اسم المستخدم
     */
    private boolean isUsernameValid(String username) {
        return username.length() >= 3;
    }

    /**
     * التحقق من صحة كلمة المرور
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    /**
     * تنفيذ عملية تسجيل الدخول
     */
    private void performLogin(String username, String password) {
        // التحقق من بيانات الاعتماد (مثال بسيط)
        if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
            // تسجيل الدخول ناجح
            
            // حفظ بيانات الاعتماد إذا كان المستخدم يريد تذكرها
            if (cbRememberMe.isChecked()) {
                saveCredentials(username, password);
            } else {
                clearSavedCredentials();
            }
            
            // عرض رسالة نجاح
            Toast.makeText(this, "تم تسجيل الدخول بنجاح!", Toast.LENGTH_SHORT).show();
            
            // الانتقال إلى النشاط الرئيسي
            navigateToMainActivity();
            
        } else {
            // تسجيل الدخول فاشل
            Toast.makeText(this, "اسم المستخدم أو كلمة المرور غير صحيحة", Toast.LENGTH_LONG).show();
            
            // مسح كلمة المرور
            etPassword.setText("");
            etPassword.requestFocus();
        }
    }

    /**
     * حفظ بيانات الاعتماد
     */
    private void saveCredentials(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, true);
        editor.apply();
    }

    /**
     * مسح بيانات الاعتماد المحفوظة
     */
    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false);
        editor.apply();
    }

    /**
     * التعامل مع نسيت كلمة المرور
     */
    private void handleForgotPassword() {
        Toast.makeText(this, "سيتم إرسال رابط إعادة تعيين كلمة المرور إلى بريدك الإلكتروني", Toast.LENGTH_LONG).show();
        
        // هنا يمكنك إضافة منطق إعادة تعيين كلمة المرور
        // مثل فتح نشاط جديد أو إرسال طلب إلى الخادم
    }

    /**
     * التعامل مع إنشاء حساب جديد
     */
    private void handleSignUp() {
        Toast.makeText(this, "سيتم فتح صفحة إنشاء حساب جديد", Toast.LENGTH_SHORT).show();
        
        // هنا يمكنك إضافة منطق إنشاء حساب جديد
        // مثل فتح نشاط التسجيل
        // Intent intent = new Intent(this, SignUpActivity.class);
        // startActivity(intent);
    }

    /**
     * الانتقال إلى النشاط الرئيسي
     */
    private void navigateToMainActivity() {
        // إنشاء Intent للانتقال إلى النشاط الرئيسي
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

