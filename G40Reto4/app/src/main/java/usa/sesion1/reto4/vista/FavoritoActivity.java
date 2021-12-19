package usa.sesion1.reto4.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.controlador.AdaptadorProductos;
import usa.sesion1.reto4.controlador.MyOpenHelper;
import usa.sesion1.reto4.modelo.Producto;

public class FavoritoActivity extends AppCompatActivity {

    ArrayList<Producto> misProductos;
    ListView lvwFavoritos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        lvwFavoritos = (ListView) findViewById(R.id.lvwProductosFavoritos);

        ProgressDialog progressDialog = new ProgressDialog(FavoritoActivity.this);
        progressDialog.setMessage("Cargando Información...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        new Consulta(FavoritoActivity.this, progressDialog).execute();

        //ArrayList<Producto> misProductos = consultarProductosFavoritos(this);

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudeopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cat:
                Intent intent3 = new Intent(FavoritoActivity.this, CatalogoActivity.class);
                startActivity(intent3);
                return true;
            case R.id.suc:
                Intent intent4 = new Intent(FavoritoActivity.this, SucursalesActivity.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public ArrayList<Producto> consultarProductosFavoritos(Context context){
        misProductos = new ArrayList<>();

        MyOpenHelper dataBase = new MyOpenHelper(FavoritoActivity.this);
        SQLiteDatabase db = dataBase.getReadableDatabase();

        Cursor c = dataBase.leerProductosFavoritos(db);

        while (c.moveToNext()){
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("id"));
            @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex("nombre"));
            @SuppressLint("Range") int precio = c.getInt(c.getColumnIndex("precio"));
            @SuppressLint("Range") int imagen = c.getInt(c.getColumnIndex("imagen"));
            @SuppressLint("Range") int favorito = c.getInt(c.getColumnIndex("favorito"));

            if(favorito == 1){
                misProductos.add(new Producto(id, nombre, precio, imagen, true));
            }else{
                misProductos.add(new Producto(id, nombre, precio, imagen, false));
            }
        }
        return misProductos;

    }

    public class Consulta extends AsyncTask<Void, Void, Void> {

        Context context;
        ProgressDialog progressDialog;

        public Consulta(Context context, ProgressDialog progressDialog) {
            this.context = context;
            this.progressDialog = progressDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
                consultarProductosFavoritos(FavoritoActivity.this);
            }catch (Exception e){
                Log.e("TAG_ERR", "" + e.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            AdaptadorProductos adapter = new AdaptadorProductos(getApplicationContext(), misProductos);
            lvwFavoritos.setAdapter(adapter);
        }
    }
}