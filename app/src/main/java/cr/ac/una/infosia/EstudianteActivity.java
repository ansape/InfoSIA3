package cr.ac.una.infosia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EstudianteActivity extends AppCompatActivity {

    private ListView list_student;
    private List<Noticia> list_noticias = new ArrayList<>();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        list_student = (ListView)findViewById(R.id.list_student);
        Toolbar toolbar2= (Toolbar)findViewById(R.id.toolbar2);
        toolbar2.setTitle("        Noticias");
        setSupportActionBar(toolbar2);

        initFirebase();
        addEventFirebaseListener();
    }

    private void addEventFirebaseListener() {

        mDatabaseReference.child("Noticia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(list_noticias.size() > 0)
                    list_noticias.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Noticia  noticia = postSnapshot.getValue(Noticia.class);

                    list_noticias.add(noticia);
                }
                ListViewAdapter adapter = new ListViewAdapter(EstudianteActivity.this,list_noticias);
                list_student.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference  = mFirebaseDatabase.getReference();
    }
}
