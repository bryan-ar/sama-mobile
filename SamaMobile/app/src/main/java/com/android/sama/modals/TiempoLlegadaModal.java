package com.android.sama.modals;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.sama.R;

public class TiempoLlegadaModal extends DialogFragment {

    private int _idPedido;

    public static TiempoLlegadaModal newInstance(int idPedido) {
        TiempoLlegadaModal fragment = new TiempoLlegadaModal();
        fragment._idPedido = idPedido;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_tiempo_llegada, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView ivCancel = view.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(view1 -> getDialog().dismiss());

        Button btnEntendido = view.findViewById(R.id.btnEntendido);
        btnEntendido.setOnClickListener(view1 -> getDialog().dismiss());

        super.onViewCreated(view, savedInstanceState);
    }
}
