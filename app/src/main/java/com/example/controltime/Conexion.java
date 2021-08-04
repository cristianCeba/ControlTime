package com.example.controltime;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class Conexion {
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    boolean conecta=false  ;

public Conexion(){
    mDataBase = FirebaseDatabase.getInstance().getReference();
    mAuth = FirebaseAuth.getInstance();

}

public boolean ConexionBBDD(){
    conecta=false  ;
    mAuth.signInWithEmailAndPassword("Cristian_Ceballos1@hotmail.com", "holass")
            .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())   conecta=true;

                }
            });
    return conecta;
}

}
