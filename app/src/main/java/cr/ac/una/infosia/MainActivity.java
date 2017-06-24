package cr.ac.una.infosia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText input_decripcion;
    private ListView list_data;
   private ProgressBar circular_progress;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private Uri descargarFoto; //variable global

private List<Noticia> list_noticias = new ArrayList<>();

    private   Noticia selectedNoticia;

    private ListViewAdapter adapter = new ListViewAdapter(this,list_noticias);

    private Noticia noticia = new Noticia();

    //subir imagen
    private ImageButton imageButton;
    private StorageReference mStorage;
    private  static  final int GALLERY_INTENT=1;
    private ProgressDialog mProgressDialog;
    private int bandera = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog =  new ProgressDialog(this);
        //descargarFoto = "";
        //add el toolbar
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(" InfoSIA");
        setSupportActionBar(toolbar);

        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButton.setBackgroundResource(android.R.drawable.ic_menu_gallery); //nuevo
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                bandera = 1; //nuevo
            }
        });

//Control
        circular_progress = (ProgressBar)findViewById(R.id.circular_progress);
        input_decripcion = (EditText)findViewById(R.id.editText);

        list_data = (ListView)findViewById(R.id.list_data);
        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Noticia noticia = (Noticia) adapterView.getItemAtPosition(i);
                selectedNoticia = noticia;
                input_decripcion.setText(noticia.getDescripcion());
                //list_data.setAdapter(adapter);
            }
        });

        //Firebase
        initFirebase();
        addEventFirebaseListener();
        //list_data.setAdapter(adapter);

        /*Spinner spinner = (Spinner) findViewById(R.id.spinnerr);
        String[] valores = {"Ninguna","UNA","UCR","TEC","UNED","UTN"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){


// parte del progres
            mProgressDialog.setTitle("Subiendo Imagen");
            mProgressDialog.setMessage("Subiendo Imagen...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();


            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("fotos").child(uri.getLastPathSegment());
            System.out.println("StorageReference filepath");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println(" filepath.putFile(uri).addOnSuccessListener");
                    mProgressDialog.dismiss();


// este error da pero la vara esq no afecta en nada la vara corre como si nada
                    descargarFoto = taskSnapshot.getDownloadUrl();

                    Glide.with(MainActivity.this)
                            .load(descargarFoto)
                            .fitCenter()
                            .centerCrop()
                            .into(imageButton);



                    Toast.makeText(MainActivity.this, "se subio la foto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void addEventFirebaseListener() {
        //Progressing
        circular_progress.setVisibility(View.VISIBLE);
        list_data.setVisibility(View.INVISIBLE);

       // mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
        mDatabaseReference.child("Noticia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(list_noticias.size() > 0)
                    list_noticias.clear();//esta raro
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                   Noticia  noticia = postSnapshot.getValue(Noticia.class);
                    //Noticia  noticia = dataSnapshot.getValue(Noticia.class);


                    list_noticias.add(noticia);
                }
                ListViewAdapter adapter = new ListViewAdapter(MainActivity.this,list_noticias);
                list_data.setAdapter(adapter);

                circular_progress.setVisibility(View.INVISIBLE);
                list_data.setVisibility(View.VISIBLE);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String descripcionN = input_decripcion.getText().toString();
        if(item.getItemId() == R.id.menu_add)
        {


            if(TextUtils.isEmpty(descripcionN) || bandera == 0)
            {  Toast.makeText(MainActivity.this, "Favor completar los espacios.", Toast.LENGTH_LONG).show();


            }else {
                createUser();
            }       }
        else if(item.getItemId() == R.id.menu_save)
        {
            if(TextUtils.isEmpty(descripcionN))
            {  Toast.makeText(MainActivity.this, "Favor completar los espacios.", Toast.LENGTH_LONG).show();

            } else {
            Noticia  noticia = new Noticia(selectedNoticia.getUid(),input_decripcion.getText().toString());
            updateUser(noticia);
        }}
        else if(item.getItemId() == R.id.menu_remove){
            deleteUser(selectedNoticia);
        }else if (item.getItemId()== R.id.menu_salir){
             signOut();
            Intent LoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(LoginActivity);

        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    private void deleteUser(Noticia selectedNoticia) {
        mDatabaseReference.child("Noticia").child(selectedNoticia.getUid()).removeValue();
        clearEditText();

    }

    public void signOut() {
        mAuth.signOut();
    }

    private void updateUser(Noticia noticia) {
        if(bandera == 1){
            mDatabaseReference.child("Noticia").child(noticia.getUid()).child("descripcion").setValue(noticia.getDescripcion());
            mDatabaseReference.child("Noticia").child(noticia.getUid()).child("url").setValue(descargarFoto.toString());
        }else{
            mDatabaseReference.child("Noticia").child(noticia.getUid()).child("descripcion").setValue(noticia.getDescripcion());
        }
       // mDatabaseReference.child("Noticia").child(user.getUid()).child("email").setValue(user.getEmail());
        clearEditText();
    }

    private void createUser() {
        Noticia user = new Noticia(UUID.randomUUID().toString(),input_decripcion.getText().toString(), descargarFoto.toString());
        mDatabaseReference.child("Noticia").child(user.getUid()).setValue(user);
        clearEditText();

    }

    private void clearEditText() {
        input_decripcion.setText("");
       // input_email.setText("");
        //limpiar imagen@android:drawable/ic_menu_gallery
        imageButton.setImageBitmap(null);
        imageButton.setBackgroundResource(android.R.drawable.ic_menu_gallery);
        bandera = 0;

    }

}
