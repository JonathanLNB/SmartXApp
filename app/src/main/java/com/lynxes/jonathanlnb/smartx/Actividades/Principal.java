package com.lynxes.jonathanlnb.smartx.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
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
import com.lynxes.jonathanlnb.smartx.Fragmentos.Home;
import com.lynxes.jonathanlnb.smartx.Fragmentos.MenuLateral;
import com.lynxes.jonathanlnb.smartx.Herramientas.Tools;
import com.lynxes.jonathanlnb.smartx.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.apend.slider.ui.Slider;

public class Principal extends AppCompatActivity implements Home.OnFragmentInteractionListener, MenuLateral.OnFragmentInteractionListener {
    @BindView(R.id.vSmartX)
    TextView vSmartX;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GoogleApiClient mGoogleApiClient;
    private LoginManager loginManager;
    private Typeface TF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);
        TF = Typeface.createFromAsset(getAssets(), "GoogleSans-Regular.ttf");
        vSmartX.setTypeface(TF);
        mAuth = FirebaseAuth.getInstance();
        loginManager = LoginManager.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    logIn();
                }
            }
        };

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
        mostrarMenu();
    }

    public void logIn() {
        Intent i = new Intent(this, LogIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void mostrarMenu() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        Home h = new Home();
        fragmentTransaction.replace(R.id.lFragment, h).commit();
    }

    @OnClick(R.id.bMenu)
    public void abrirMenu() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        MenuLateral m = new MenuLateral();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.lMenu, m).commit();
    }

    public void cerrarSesion() {
        mAuth.signOut();
        loginManager.logOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess())
                    logIn();
                else
                    Tools.showMessage(Principal.this, getString(R.string.error_cerrar));
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
