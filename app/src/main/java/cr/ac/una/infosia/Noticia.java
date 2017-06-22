package cr.ac.una.infosia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by juaco_000 on 5/14/2017.
 */

public class Noticia {
    private String uid, universidad, descripcion, data, url;
    private Bitmap foto;


    public Noticia(String uid, String universidad, String descripcion, Bitmap foto, String data) {
        this.uid = uid;   // Primary key and key
        this.universidad = universidad;
        this.descripcion = descripcion;
        this.foto = foto;
        this.data = data;
    }

    public Noticia(String uid, String universidad, String descripcion, Bitmap foto, String data, String url) {
        this.uid = uid;   // Primary key and key
        this.universidad = universidad;
        this.descripcion = descripcion;
        this.foto = foto;
        this.data = data;
        this.url = url;
    }

    public Noticia() {
    }

    public Noticia(String uid, String descripcion) {
        this.uid = uid;
        this.descripcion = descripcion;
    }

    public Noticia(String uid, String descripcion, String url) {
        this.uid = uid;
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }
/*Para almacenar la imagen se utiliza el atributo data que recuperará la imagen en base64 del webservice,
posteriormente se decodificaran esos datos y se convertirán en un Bitmap que se almacenará en el atributo photo.
 Ese atributo photo será el usado para aplicarlo al ImageView. Además de los atributos, hemos incluido los métodos geters y setter,
  el único que tiene algo especial es el método setData
que además de asignar el valor a su variable, la decodificará, creará el Bitmap y lo asignará al atributo photo.*/
    public void setData(String data) {
        this.data = data;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            this.foto = BitmapFactory.decodeByteArray(byteData, 0,
                    byteData.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}