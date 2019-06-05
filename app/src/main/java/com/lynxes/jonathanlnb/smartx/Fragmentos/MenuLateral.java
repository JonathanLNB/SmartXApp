package com.lynxes.jonathanlnb.smartx.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lynxes.jonathanlnb.smartx.Actividades.Principal;
import com.lynxes.jonathanlnb.smartx.Actividades.SignIn;
import com.lynxes.jonathanlnb.smartx.Adaptadores.OpcionesAdapter;
import com.lynxes.jonathanlnb.smartx.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuLateral.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuLateral#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuLateral extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.lFondo)
    LinearLayout lFondo;
    @BindView(R.id.lMenuLateral)
    LinearLayout lMenuLateral;
    @BindView(R.id.rOpcionces)
    RecyclerView rOpciones;
    @BindView(R.id.bEditar)
    FloatingActionButton bEditar;
    @BindView(R.id.iFoto)
    CircleImageView iFoto;
    @BindView(R.id.vNombre)
    TextView vNombre;
    @BindView(R.id.vCorreo)
    TextView vCorreo;
    @BindView(R.id.vCerrarS)
    TextView vCerrarS;

    private Context context;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private OpcionesAdapter adapter;
    private Animation animationA;
    private Animation animationB;
    private Typeface TF;
    private Uri takenPhotoUri;
    private String nombreImagen;

    public MenuLateral() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuLateral.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuLateral newInstance(String param1, String param2) {
        MenuLateral fragment = new MenuLateral();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_lateral, container, false);
        context = getContext();
        ButterKnife.bind(this, v);
        TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");
        vNombre.setTypeface(TF);
        vCorreo.setTypeface(TF);
        vCerrarS.setTypeface(TF);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            vNombre.setText(user.getDisplayName());
            vCorreo.setText(user.getEmail());
            takenPhotoUri = user.getPhotoUrl();
            if (takenPhotoUri != null) {
                if (takenPhotoUri.toString().contains("s96-c/"))
                    nombreImagen = takenPhotoUri.toString().split("s96-c/")[0] + takenPhotoUri.toString().split("s96-c/")[1];
                else
                    nombreImagen = takenPhotoUri.toString() + "?height=500&width=500";
                Picasso.get().load(nombreImagen).placeholder(R.drawable.fotopersona).into(iFoto);

            }
        }

        animationA = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        animationB = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animationA.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lFondo.setVisibility(View.GONE);
                bEditar.hide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lFondo.startAnimation(animationB);
        rOpciones.setHasFixedSize(true);
        rOpciones.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<String> opciones = new ArrayList<String>();
        opciones.add("Home");
        opciones.add("Mis Pedidos");
        opciones.add("Wish List");
        opciones.add("Mis Direcciones");
        opciones.add("Formas de pago");
        opciones.add("Acerca De");
        adapter = new OpcionesAdapter(opciones, this);
        rOpciones.setAdapter(adapter);
        return v;
    }

    @OnClick(R.id.bCerrarS)
    public void cerrarSesion() {
        ((Principal) getActivity()).cerrarSesion();
    }

    @OnClick(R.id.bEditar)
    public void editar() {
        Intent i = new Intent(context, SignIn.class);
        startActivity(i);
    }

    @OnClick(R.id.lCerrar)
    public void cerrar() {
        bEditar.startAnimation(animationA);
        lFondo.startAnimation(animationA);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
