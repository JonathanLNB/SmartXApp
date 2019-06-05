package com.lynxes.jonathanlnb.smartx.Actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lynxes.jonathanlnb.smartx.Herramientas.Tools;
import com.lynxes.jonathanlnb.smartx.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignIn extends AppCompatActivity {
    @BindView(R.id.iFoto)
    CircleImageView iFoto;
    @BindView(R.id.cCorreo)
    CardView cCorreo;
    @BindView(R.id.cContrasena)
    CardView cContrasena;
    @BindView(R.id.tNombre)
    TextInputLayout tNombre;
    @BindView(R.id.tApellido)
    TextInputLayout tApellido;
    @BindView(R.id.tNumTel)
    TextInputLayout tNumTel;
    @BindView(R.id.tCorreo)
    TextInputLayout tCorreo;
    @BindView(R.id.tContrasena)
    TextInputLayout tContrasena;
    @BindView(R.id.eNombre)
    EditText eNombre;
    @BindView(R.id.eApellido)
    EditText eApellido;
    @BindView(R.id.eNumTel)
    EditText eNumTel;
    @BindView(R.id.eCorreo)
    EditText eCorreo;
    @BindView(R.id.eContrasena)
    EditText eContrasena;
    @BindView(R.id.vInfo)
    TextView vInfo;
    @BindView(R.id.vSeleccionar)
    TextView vSeleccionar;

    private static final int REQUEST_PERMISSION = 200;
    private static final Integer CAMARA = 10, GALERIA = 11;
    private Uri takenPhotoUri;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage storage;
    private GoogleApiClient mGoogleApiClient;
    private LoginManager loginManager;
    private StorageReference storageRef;
    private Typeface TF;
    private SharedPreferences sharedPreferences;
    private Bitmap foto;
    private String nombreImagen, fbToken, idProgress, photoFileName = "temp.jpg";
    private boolean primera, subirImagen, imagenGoogle = false, sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Tools.checkStoragePermissions(this, REQUEST_PERMISSION);
        ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        TF = Typeface.createFromAsset(getAssets(), "GoogleSans-Regular.ttf");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences(getString(R.string.shareKey), MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified())
                        sesion = true;
                    else
                        sesion = false;
                    if (subirImagen)
                        subirFoto();
                    else
                        enviarInfo();
                }
            }
        };
        loginManager = LoginManager.getInstance();
        fbToken = sharedPreferences.getString(getString(R.string.shareKey), "");
        vInfo.setTypeface(TF);
        vSeleccionar.setTypeface(TF);
        eApellido.setTypeface(TF);
        eNombre.setTypeface(TF);
        eNumTel.setTypeface(TF);
        eCorreo.setTypeface(TF);
        eContrasena.setTypeface(TF);
        tApellido.setTypeface(TF);
        tNombre.setTypeface(TF);
        tNumTel.setTypeface(TF);
        tCorreo.setTypeface(TF);
        tContrasena.setTypeface(TF);
        revisarInfo();
    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), "tag");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d("tag", "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            return Uri.fromFile(file);
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMARA) {
                takenPhotoUri = getPhotoFileUri(photoFileName);
                foto = BitmapFactory.decodeFile(takenPhotoUri.getPath());
            } else {
                takenPhotoUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(takenPhotoUri, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                foto = BitmapFactory.decodeFile(imagePath, options);
            }
            iFoto.setImageBitmap(foto);
        } else {
            Tools.showMessage(this, getString(R.string.error_seleccion));
            subirImagen = false;
        }
    }

    public void revisarInfo() {
        primera = getIntent().getBooleanExtra("google", false);
        if (primera) {
            cCorreo.setVisibility(View.GONE);
            cContrasena.setVisibility(View.GONE);
            datosGoogle();
        }
    }

    private void datosGoogle() {
        String aux[];
        String nombre = user.getDisplayName();
        if (nombre != null) {
            aux = nombre.split(" ");
            if (aux.length == 4) {
                eNombre.setText(aux[0] + " " + aux[1]);
                eApellido.setText(aux[2] + " " + aux[3]);
            } else {
                if (aux.length == 3) {
                    eNombre.setText(aux[0]);
                    eApellido.setText(aux[1] + " " + aux[2]);
                } else {
                    eNombre.setText(nombre);
                }
            }
        }
        takenPhotoUri = user.getPhotoUrl();
        if (takenPhotoUri != null) {
            if (takenPhotoUri.toString().contains("s96-c/"))
                nombreImagen = takenPhotoUri.toString().split("s96-c/")[0] + takenPhotoUri.toString().split("s96-c/")[1];
            else
                nombreImagen = takenPhotoUri.toString() + "?height=500&width=500";
            Picasso.get().load(nombreImagen).into(iFoto);
            imagenGoogle = true;
        }
    }

    public void subirFoto() {
        if (!imagenGoogle) {
            if (takenPhotoUri != null) {
                storage = FirebaseStorage.getInstance();
                nombreImagen = (eNombre.getText().toString() + eApellido.getText().toString() + System.currentTimeMillis()).replace(" ", "");
                storageRef = storage.getReference("fotos_perfil/" + nombreImagen + ".jpg");
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        subir();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        if (exception.getMessage().contains("Object does not exist at location"))
                            subir();
                    }
                });

            } else
                Tools.showMessage(SignIn.this, getString(R.string.error_foto));
        } else {
            enviarInfo();
        }
    }

    public void subir() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();
        idProgress = Tools.showProgress(this, R.string.cargando, false);
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Tools.dismissProgress(idProgress);
                Tools.showMessage(SignIn.this, getString(R.string.error_foto_subir));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                takenPhotoUri = taskSnapshot.getUploadSessionUri();
                nombreImagen = takenPhotoUri.toString();
                enviarInfo();
                Tools.showMessage(SignIn.this, getString(R.string.exito_subir_imagen));
                Tools.dismissProgress(idProgress);
            }
        });
    }

    public void enviarInfo() {
        if (sesion) {
            SharedPreferences.Editor ed = sharedPreferences.edit();
            ed.putBoolean("authentication", true);
            ed.commit();
            Intent i;
            i = new Intent(SignIn.this, Principal.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignIn.this);
            builder.setTitle(R.string.verificar);
            builder.setIcon(R.drawable.movil);
            builder.setMessage(R.string.verificacorreo);
            builder.create();
            builder.show().setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mAuth.signOut();
                    loginManager.logOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (!status.isSuccess())
                                Tools.showMessage(SignIn.this, getString(R.string.error_cerrar));
                        }
                    });
                    finish();
                }
            });
        }
    }

    public boolean revisarCampos() {
        boolean pasa = true;
        if (TextUtils.isEmpty(eNombre.getText())) {
            eNombre.setError(getString(R.string.nombre));
            pasa = false;
        } else {
            eNombre.setError(null);
        }
        if (TextUtils.isEmpty(eApellido.getText())) {
            eApellido.setError(getString(R.string.apellido));
            pasa = false;
        } else {
            eApellido.setError(null);
        }
        if (TextUtils.isEmpty(eNumTel.getText())) {
            eNumTel.setError(getString(R.string.numTel));
            pasa = false;
        } else {
            eNumTel.setError(null);
        }
        if (!primera) {
            if (TextUtils.isEmpty(eCorreo.getText())) {
                eCorreo.setError(getString(R.string.correo));
                pasa = false;
            } else {
                eCorreo.setError(null);
            }
            if (TextUtils.isEmpty(eContrasena.getText())) {
                eContrasena.setError(getString(R.string.contrasena));
                pasa = false;
            } else {
                eContrasena.setError(null);
            }
        }
        return pasa;
    }

    @OnClick({R.id.iFoto, R.id.vSeleccionar})
    public void opcionesImagen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_camara);
        builder.setMessage(R.string.subtitulo_camara).setPositiveButton(R.string.galeria, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALERIA);
                subirImagen = true;
                imagenGoogle = false;
            }
        }).setNegativeButton(getResources().getString(R.string.camara), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intfoto = new Intent("android.media.action.IMAGE_CAPTURE");
                intfoto.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
                startActivityForResult(intfoto, CAMARA);
                subirImagen = true;
                imagenGoogle = false;
            }
        }).show();
    }

    @OnClick(R.id.bActualizar)
    public void registrarInfo() {
        if (revisarCampos()) {
            if (!primera) {
                mAuth.createUserWithEmailAndPassword(eCorreo.getText().toString(), eContrasena.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mAuth.addAuthStateListener(mAuthListener);
                                } else {
                                    Tools.showMessage(SignIn.this, getString(R.string.error_inicio));
                                }
                            }
                        });
            } else
                mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.checkCamaraPermissions(this, 200);
    }
}
