package cr.ac.una.infosia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by juaco_000 on 5/15/2017.
 */

public class ListViewAdapter extends BaseAdapter {
    Activity activity;
    List<Noticia> lstNoticia;
    LayoutInflater inflater;
    Uri uri;
    Context context;


    public  ListViewAdapter(Activity activity, List<Noticia>lstNoticia) {


        this.activity = activity;
        this.lstNoticia = lstNoticia;
        Context context;


    }


    @Override
    public int getCount() {return lstNoticia.size();}

    @Override
    public Object getItem(int i) {
        return lstNoticia.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View View, ViewGroup viewGroup) {//View view

        inflater= (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item,null);
        TextView txtNoticias = (TextView)itemView.findViewById(R.id.textView_2);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);
      //  Spinner spinner = (Spinner)itemView.findViewById(R.id.spinnerr);
            //txtNoticias.setText(noti.getDescripcion());
        txtNoticias.setText(lstNoticia.get(i).getDescripcion());
        //imageView.setImageBitmap(getBitmapFromURL(lstNoticia.get(i).getUrl())); //ultima prueba
        /*mostrar imagen*/
        //Bitmap bitmap = getImageFromURL(lstNoticia.get(i).getUrl());
        //System.out.println(lstNoticia.get(i).getUrl());
        //imageView.setImageBitmap(bitmap);
        //uri = Uri.parse(lstNoticia.get(i).getUrl());
        //imageView.setImageURI(uri);

        /*Bitmap obtener_imagen = get_imagen(lstNoticia.get(i).getUrl());
        imageView.setImageBitmap(obtener_imagen);*/



        /*libreria de quincho*/

        Glide.with(this.activity)
                .load(lstNoticia.get(i).getUrl())
                .fitCenter()
                .centerCrop()
                .into(imageView);

        return itemView;

    }

    public Bitmap get_imagen(String url) {
        Bitmap bm = null;
        try {
            URL _url = new URL(url);
            URLConnection con = _url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
    }

    public Bitmap getImageFromURL(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
            System.out.println("Lo hizo bien "+url);
            return myBitmap;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("No esta haciendo");
            return null;
        }
    }

    @Nullable
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
