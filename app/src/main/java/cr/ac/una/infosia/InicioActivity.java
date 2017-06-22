package cr.ac.una.infosia;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InicioActivity extends AppCompatActivity {
    private Button admbtn;
    private Button estbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        admbtn =(Button)findViewById(R.id.btnAdmin);
        estbtn =(Button)findViewById(R.id.btnEst);


        admbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent LoginActivity = new Intent(getApplicationContext(), cr.ac.una.infosia.LoginActivity.class);
                startActivity(LoginActivity);
            }
        });



        estbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), cr.ac.una.infosia.EstudianteActivity.class);
                startActivity(intent);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

finishAffinity();
    }
}
