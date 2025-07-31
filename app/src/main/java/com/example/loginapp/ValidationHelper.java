package com.example.loginapp;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * فئة مساعدة للتحقق من صحة البيانات المدخلة
 */
public class ValidationHelper {

    /**
     * التحقق من صحة اسم المستخدم
     * @param username اسم المستخدم المراد التحقق منه
     * @return رسالة الخطأ أو null إذا كان صحيحاً
     */
    public static String validateUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return "يرجى إدخال اسم المستخدم";
        }
        
        if (username.length() < 3) {
            return "اسم المستخدم يجب أن يكون 3 أحرف على الأقل";
        }
        
        if (username.length() > 20) {
            return "اسم المستخدم يجب أن يكون أقل من 20 حرف";
        }
        
        // التحقق من أن اسم المستخدم يحتوي على أحرف وأرقام فقط
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "اسم المستخدم يجب أن يحتوي على أحرف وأرقام فقط";
        }
        
        return null; // لا توجد أخطاء
    }

    /**
     * التحقق من صحة كلمة المرور
     * @param password كلمة المرور المراد التحقق منها
     * @return رسالة الخطأ أو null إذا كانت صحيحة
     */
    public static String validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "يرجى إدخال كلمة المرور";
        }
        
        if (password.length() < 6) {
            return "كلمة المرور يجب أن تكون 6 أحرف على الأقل";
        }
        
        if (password.length() > 50) {
            return "كلمة المرور طويلة جداً";
        }
        
        // التحقق من وجود حرف كبير واحد على الأقل
        if (!password.matches(".*[A-Z].*")) {
            return "كلمة المرور يجب أن تحتوي على حرف كبير واحد على الأقل";
        }
        
        // التحقق من وجود حرف صغير واحد على الأقل
        if (!password.matches(".*[a-z].*")) {
            return "كلمة المرور يجب أن تحتوي على حرف صغير واحد على الأقل";
        }
        
        // التحقق من وجود رقم واحد على الأقل
        if (!password.matches(".*[0-9].*")) {
            return "كلمة المرور يجب أن تحتوي على رقم واحد على الأقل";
        }
        
        return null; // لا توجد أخطاء
    }

    /**
     * التحقق من صحة البريد الإلكتروني
     * @param email البريد الإلكتروني المراد التحقق منه
     * @return رسالة الخطأ أو null إذا كان صحيحاً
     */
    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "يرجى إدخال البريد الإلكتروني";
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "البريد الإلكتروني غير صحيح";
        }
        
        return null; // لا توجد أخطاء
    }

    /**
     * التحقق من صحة رقم الهاتف
     * @param phoneNumber رقم الهاتف المراد التحقق منه
     * @return رسالة الخطأ أو null إذا كان صحيحاً
     */
    public static String validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "يرجى إدخال رقم الهاتف";
        }
        
        // إزالة المسافات والرموز
        String cleanPhone = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        
        // التحقق من أن الرقم يحتوي على أرقام فقط
        if (!cleanPhone.matches("^[0-9+]+$")) {
            return "رقم الهاتف يجب أن يحتوي على أرقام فقط";
        }
        
        // التحقق من طول الرقم
        if (cleanPhone.length() < 10 || cleanPhone.length() > 15) {
            return "رقم الهاتف غير صحيح";
        }
        
        return null; // لا توجد أخطاء
    }

    /**
     * التحقق من تطابق كلمتي المرور
     * @param password كلمة المرور الأولى
     * @param confirmPassword كلمة المرور المؤكدة
     * @return رسالة الخطأ أو null إذا كانتا متطابقتين
     */
    public static String validatePasswordConfirmation(String password, String confirmPassword) {
        if (TextUtils.isEmpty(confirmPassword)) {
            return "يرجى تأكيد كلمة المرور";
        }
        
        if (!password.equals(confirmPassword)) {
            return "كلمتا المرور غير متطابقتين";
        }
        
        return null; // لا توجد أخطاء
    }

    /**
     * التحقق من صحة الاسم
     * @param name الاسم المراد التحقق منه
     * @return رسالة الخطأ أو null إذا كان صحيحاً
     */
    public static String validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "يرجى إدخال الاسم";
        }
        
        if (name.length() < 2) {
            return "الاسم يجب أن يكون حرفين على الأقل";
        }
        
        if (name.length() > 50) {
            return "الاسم طويل جداً";
        }
        
        // التحقق من أن الاسم يحتوي على أحرف فقط
        if (!name.matches("^[a-zA-Zأ-ي\\s]+$")) {
            return "الاسم يجب أن يحتوي على أحرف فقط";
        }
        
        return null; // لا توجد أخطاء
    }
}

