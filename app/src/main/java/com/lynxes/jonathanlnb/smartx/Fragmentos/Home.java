package com.lynxes.jonathanlnb.smartx.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lynxes.jonathanlnb.smartx.API.Modelos.Productos;
import com.lynxes.jonathanlnb.smartx.API.SmartX;
import com.lynxes.jonathanlnb.smartx.Adaptadores.ProductoAdapter;
import com.lynxes.jonathanlnb.smartx.Herramientas.Tools;
import com.lynxes.jonathanlnb.smartx.R;
import com.lynxes.jonathanlnb.smartx.TDA.Producto;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.sCarrusel)
    Slider sCarrusel;
    @BindView(R.id.rProductos1)
    RecyclerView rProductos1;
    @BindView(R.id.rProductos2)
    RecyclerView rProductos2;

    private Gson gson;
    private Retrofit retrofit;
    private SmartX restClient;
    private Context context;
    private ProductoAdapter adapter1, adapter2;
    private ArrayList<Producto> p1, p2;
    private String progress;
    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        Tools.iniciarHeaders(getActivity());
        gson = new GsonBuilder()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(Tools.getHeaders())
                .build();
        restClient = retrofit.create(SmartX.class);
        ButterKnife.bind(this, v);
        List<Slide> slideList = new ArrayList<>();
        slideList.add(new Slide(0, "https://images-na.ssl-images-amazon.com/images/G/33/Wireless/MOTO_G6_1500X400.jpg",0));
        slideList.add(new Slide(1,"https://images-na.ssl-images-amazon.com/images/G/33/MX-hq/2017/img/Wireless_Products/TecnologiaParaVestir_1500x400.jpg" , 0));
        slideList.add(new Slide(2,"https://assets.liverpool.com.mx/assets/web/images/targeted_promotions/es/Bpromos01d_261118cel_new.jpg" , 0));
        sCarrusel.addSlides(slideList);
        rProductos1.setLayoutManager(new LinearLayoutManager(context));
        rProductos2.setLayoutManager(new LinearLayoutManager(context));
        loadProducto();
        return v;
    }

    public void loadProducto(){
        Call<Productos> call = restClient.allProducts();
        progress = Tools.showProgress(getActivity(), R.string.cargando, false);
        call.enqueue(new Callback<Productos>() {
            @Override
            public void onResponse(Call<Productos> call, Response<Productos> response) {
                boolean columna = false;
                if(response.isSuccessful()){
                    p1 = new ArrayList<>();
                    p2 = new ArrayList<>();
                    for(Producto p: response.body().getProductos()){
                        if(columna)
                            p1.add(p);
                        else
                            p2.add(p);
                        columna = !columna;
                    }
                    adapter1 = new ProductoAdapter(p1, getActivity());
                    adapter2 = new ProductoAdapter(p2, getActivity());
                    rProductos1.setAdapter(adapter1);
                    rProductos2.setAdapter(adapter2);
                }
                else
                    Tools.showMessage(getActivity(), getString(R.string.error_servicio));
                Tools.dismissProgress(progress);
            }

            @Override
            public void onFailure(Call<Productos> call, Throwable t) {
                Tools.showMessage(getActivity(), getString(R.string.error_servicio));
                Tools.dismissProgress(progress);
            }
        });
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
