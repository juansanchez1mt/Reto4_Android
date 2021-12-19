package usa.sesion1.reto4.casosdeuso;

import android.database.sqlite.SQLiteDatabase;

import usa.sesion1.reto4.controlador.MyOpenHelper;

public class ProductoCase {
    public static void agregarFavorito(int id, MyOpenHelper dataBase, SQLiteDatabase db){
        try{
            dataBase.seleccionarFavorito(id, db);
        }catch (Exception e){

        }finally {
            dataBase.close();
        }
    }
}
