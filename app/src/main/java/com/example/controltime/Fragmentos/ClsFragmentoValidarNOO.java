/*

package com.example.controltime.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.controltime.R;
import com.example.controltime.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;


*/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClsFragmentoValidarNOO#newInstance} factory method to
 * create an instance of this fragment.
 *//*


public class ClsFragmentoValidarNOO extends Fragment {
    View vista;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClsFragmentoValidarNOO() {
        // Required empty public constructor
    }


*/
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClsFragmentoValidar.
     *//*


    // TODO: Rename and change types and number of parameters
    public static ClsFragmentoValidarNOO newInstance(String param1, String param2) {
        ClsFragmentoValidarNOO fragment = new ClsFragmentoValidarNOO();
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
          vista = inflater.inflate(R.layout.fragment_cls_fragmento_validar, container, false);

       SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getFragmentManager());
        ViewPager viewPager = vista.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = vista.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return vista;




    }

}*/
