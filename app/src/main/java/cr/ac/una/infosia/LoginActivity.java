package cr.ac.una.infosia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by juaco_000 on 5/29/2017.
 */

public class LoginActivity extends AppCompatActivity {


    private EditText mEmailFiel;
    private EditText mPasswordFiel;
    private  Button mLoginBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       //login menu
        Toolbar toolbar1= (Toolbar)findViewById(R.id.toolbar1);
        toolbar1.setTitle("InfoSIA");
        setSupportActionBar(toolbar1);
        //login menu

        mAuth = FirebaseAuth.getInstance();
        mEmailFiel=( EditText)findViewById(R.id.editEmail);
        mPasswordFiel=(EditText)findViewById(R.id.editContraseña);
        mLoginBtn =(Button)findViewById(R.id.btnlogin);

        /*
        *  Este listener se encarga del resultado del login
        *
        *   NOTA! solo se ejecuta en taskSuccesfull -> usurio loguea o desloguea.
        *
        * */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // login

                    /**
                     *
                     * Aqui va la logica para pasarlo a la vista de usuarios logueados
                     *
                     *
                     */

                    Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(MainActivity);


                    Toast.makeText(LoginActivity.this, " Usuario logueado", Toast.LENGTH_LONG).show();

                } else {
                    // log out

                    Toast.makeText(LoginActivity.this, "Usuario deslogueado.", Toast.LENGTH_LONG).show();
                }
            }
        };//fin AuthStateListener()


        /*
        *  Boton de logueo que activa la consulta de login a firebase
        *
        * */
        mLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                startSignIn();
            }
        });


      //mLoginOutBtn.setOnLongClickListener();

    }//fin OnCreate()

    @Override
    protected void onStart() {
        super.onStart();
     mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void signOut() {
        mAuth.signOut();
    }


    /**
     *
     *   Método de logueo  -> Solo si da success activa el listener de arriba (login/logout),
     *
     *
     *   en caso de error se ejecuta el else siempre.
     *
     *
     * */
    private void startSignIn(){
    String email = mEmailFiel.getText().toString();
    String password = mPasswordFiel.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)) {

            Toast.makeText(LoginActivity.this, "Favor completar los espacios.", Toast.LENGTH_LONG).show();

        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //Si todo sale bien se ejecuta el listener de arriba.

                            /**
                             *
                             * Este es el if de error fallo(conexion/ fallo de auht)
                             *
                             */
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, " hay Problemas con el inicio.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         if (item.getItemId()== R.id.menu_logatras){
            signOut();
            Intent InicioActivity = new Intent(getApplicationContext(), InicioActivity.class);
            startActivity(InicioActivity);

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
       Intent InicioActivity1 = new Intent(getApplicationContext(), InicioActivity.class);
        startActivity(InicioActivity1);

    }
}
