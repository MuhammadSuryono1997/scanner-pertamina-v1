package topapp.id.dpac;

/**
 * Created by topapp.id on 10/02/18.
 */

public class Koneksi {
    public String getServer(){
        //String isi = "http://192.168.1.102/"; //over wifi
        //String isi = "http://10.0.2.2"; //over emulator
        String isi = "http://belajarbersama.online/";

        return isi;
    }

    public String getUrl()
    {
        String isi = getServer()+"assets/API/"; //over online web
        //String isi = getServer()+"API/"; //over online web
        return isi;
    }

    public String getImagesDir(){
        String isi = getServer()+"assets/images/";
       //String isi = getServer()+"images/";
        return isi;
    }

}
