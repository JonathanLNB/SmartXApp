package com.lynxes.jonathanlnb.smartx.Herramientas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lynxes.jonathanlnb.smartx.R;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by user on 29/07/2017.
 */

public class Tools {
    private static ArrayMap<String, ProgressDialog> progressDialogs = new ArrayMap<>();
    private static OkHttpClient.Builder httpClient;
    private static OkHttpClient client;
    private static SecureRandom random = new SecureRandom();
    private static Snackbar snackbar;

    public static void showMessage(Activity activity, String string) {
        snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), string, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void iniciarHeaders(final Activity activity){
        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("user", activity.getString(R.string.user))
                        .header("pwd", activity.getString(R.string.pwd))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
    }

    public static OkHttpClient getHeaders() {
        client = httpClient.build();
        return client;
    }

    public static void fullScreen(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void showTMessage(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static String showProgress(Context context,
                                      int stringResourceId,
                                      boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(context.getString(stringResourceId));
        progressDialog.show();
        String progressName = getMyId();
        progressDialogs.put(progressName, progressDialog);
        return progressName;
    }

    public static ProgressDialog getProgressDialog(String progressId) {
        return progressDialogs.get(progressId);
    }

    public static String getMyId() {
        return new BigInteger(32, random).toString(32);
    }

    public static void dismissProgress(String progressDialogName) {
        if (progressDialogs.get(progressDialogName) != null) {
            progressDialogs.get(progressDialogName).dismiss();
            progressDialogs.remove(progressDialogName);
        }
    }

    public static void checkCamaraPermissions(Activity activity, int PERMISSION_REQUEST) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
    }

    public static void checkStoragePermissions(Activity activity, int PERMISSION_REQUEST) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
    }

}
