package com.lynxes.jonathanlnb.smartx.Actividades;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lynxes.jonathanlnb.smartx.Herramientas.Tools;
import com.lynxes.jonathanlnb.smartx.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogIn extends AppCompatActivity {
    @BindView(R.id.iFacebook)
    ImageView iFacebook;
    @BindView(R.id.iGoogle)
    ImageView iGoogle;
    @BindView(R.id.bIniciar)
    Button bIniciar;
    @BindView(R.id.bRegistrar)
    Button bRegistrar;
    @BindView(R.id.tUser)
    TextInputLayout tUser;
    @BindView(R.id.tContrasena)
    TextInputLayout tContrasena;
    @BindView(R.id.eUser)
    EditText eUser;
    @BindView(R.id.eContrasena)
    EditText eContrasena;
    @BindView(R.id.vIniciarC)
    TextView vIniciarC;
    @BindView(R.id.vIniciarO)
    TextView vIniciarO;
    @BindView(R.id.vOlvido)
    TextView vOlvido;

    private static final int RC_SIGN_IN_GOOGLE = 1;
    private static final int RC_SIGN_IN_FACEBOOK = 64206;
    private Typeface TF;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private LoginManager loginManager;
    private Dialog dialog;
    private String id;
    private boolean google = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.lynxes.jonathanlnb.smartx",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        TF = Typeface.createFromAsset(getAssets(), "GoogleSans-Regular.ttf");
        bIniciar.setTypeface(TF);
        bRegistrar.setTypeface(TF);
        tUser.setTypeface(TF);
        tContrasena.setTypeface(TF);
        eUser.setTypeface(TF);
        eContrasena.setTypeface(TF);
        vIniciarC.setTypeface(TF);
        vIniciarO.setTypeface(TF);
        vOlvido.setTypeface(TF);
        loginManager = LoginManager.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Revisare si esta en la base de datos
                    Intent mainMenu;
                    mainMenu = new Intent(LogIn.this, SignIn.class);
                    mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainMenu.putExtra("google", google);
                    startActivity(mainMenu);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        };
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
            }

            @Override
            public void onError(FacebookException exception) {
                Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
            }
        });
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
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        id = Tools.showProgress(LogIn.this, R.string.iniciandoS, false);
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Tools.dismissProgress(id);
                        finish();
                        if (!task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                            builder.setTitle("Error");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage(task.getException().getMessage());
                            builder.create();
                            builder.show();
                            Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
                        }
                    }
                });
    }

    public boolean validar() {
        boolean pasa = true;
        if (eUser.getText().length() > 0)
            eUser.setError(null);
        else {
            eUser.setError(getString(R.string.error_campo));
            pasa = false;
        }
        if (eContrasena.getText().length() > 0)
            eContrasena.setError(null);
        else {
            eContrasena.setError(getString(R.string.error_campo));
            pasa = false;
        }
        return pasa;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else
            Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                            builder.setTitle("Error");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage(task.getException().getMessage());
                            builder.create();
                            builder.show();
                            Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
                        }
                    }
                });
    }


    @OnClick(R.id.bRegistrar)
    public void registrar() {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @OnClick(R.id.bIniciar)
    public void iniciaCorreo() {
        google = false;
        if (validar()) {
            id = Tools.showProgress(LogIn.this, R.string.iniciandoS, false);
            mAuth.signInWithEmailAndPassword(eUser.getText().toString(), eContrasena.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Tools.dismissProgress(id);
                            if (!task.isSuccessful()) {
                                Tools.showMessage(LogIn.this, getString(R.string.error_inicio));
                                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                                builder.setTitle("Error");
                                builder.setIcon(R.mipmap.ic_launcher);
                                builder.setMessage(task.getException().getMessage());
                                builder.create();
                                builder.show();
                            }
                        }
                    });
        }
    }

    @OnClick(R.id.iGoogle)
    public void iniciarGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
        google = true;
    }

    @OnClick(R.id.iFacebook)
    public void iniciarFacebook() {
        loginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        google = true;
    }

    @OnClick(R.id.vOlvido)
    public void recuperarPassword() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.content_contrasena);
        dialog.setCancelable(false);
        final EditText eCorreo = (EditText) dialog.findViewById(R.id.eCorreo);
        final Button bEnviar = (Button) dialog.findViewById(R.id.bEnviar);
        final Button bCancelar = (Button) dialog.findViewById(R.id.bCancelar);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == bEnviar)
                    FirebaseAuth.getInstance().sendPasswordResetEmail(eCorreo.getText().toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                            builder.setTitle("Error");
                            builder.setIcon(R.mipmap.ic_launcher);
                            builder.setMessage(e.getMessage());
                            builder.create();
                            builder.show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                                builder.setTitle("Éxito");
                                builder.setIcon(R.mipmap.ic_launcher);
                                builder.setMessage("Se envió un correo a: " + eCorreo.getText().toString());
                                builder.create();
                                builder.show();
                            }
                        }
                    });
                dialog.dismiss();
            }
        };
        bEnviar.setOnClickListener(clickListener);
        bCancelar.setOnClickListener(clickListener);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else
        if (requestCode == RC_SIGN_IN_FACEBOOK)
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
