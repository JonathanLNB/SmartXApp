package com.lynxes.jonathanlnb.smartx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lynxes.jonathanlnb.smartx.Actividades.LogIn;
import com.lynxes.jonathanlnb.smartx.Actividades.Principal;
import com.lynxes.jonathanlnb.smartx.Herramientas.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {
    @BindView(R.id.iLogo)
    ImageView iLogo;
    @BindView(R.id.iFondo)
    ImageView iFondo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences sharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    private LoginManager loginManager;
    private Animation animationA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Tools.iniciarHeaders(this);
        sharedPreferences = getSharedPreferences(getString(R.string.shareKey), MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        loginManager = LoginManager.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT)
                                .show();

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Intent mainMenu;
                if (user != null)
                    if (sharedPreferences.getBoolean("authentication", false))
                        mainMenu = new Intent(SplashScreen.this, Principal.class);
                    else {
                        mAuth.signOut();
                        loginManager.logOut();
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {

                            }
                        });
                        mainMenu = new Intent(SplashScreen.this, LogIn.class);
                    }
                else
                    mainMenu = new Intent(SplashScreen.this, LogIn.class);
                mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainMenu);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
        iniciarAnimacion();
    }

    private void iniciarAnimacion() {
        animationA = AnimationUtils.loadAnimation(this, R.anim.animation_splash);
        animationA.setAnimationListener(this);
        iLogo.startAnimation(animationA);
        iFondo.startAnimation(animationA);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.fullScreen(getWindow());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
