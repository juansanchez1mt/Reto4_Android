package usa.sesion1.reto4.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.casosdeuso.ProductoCase;
import usa.sesion1.reto4.controlador.AdaptadorProductos;
import usa.sesion1.reto4.controlador.MyOpenHelper;
import usa.sesion1.reto4.modelo.Producto;

public class CatalogoActivity extends AppCompatActivity {

    ArrayList<Producto> misProductos;
    ListView lvwProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        lvwProductos = (ListView) findViewById(R.id.lvwProductos);

        ProgressDialog progressDialog = new ProgressDialog(CatalogoActivity.this);
        progressDialog.setMessage("Iniciando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        new Consulta(CatalogoActivity.this, progressDialog).execute();

        //ArrayList<Producto> misProductos = consultarProductos(this);


        lvwProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                Producto p = (Producto) adapterView.getItemAtPosition(posicion);
                lanzarDialogo(p);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudeopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.fav:
                Intent intent = new Intent(CatalogoActivity.this, FavoritoActivity.class);
                startActivity(intent);
                return true;
            case R.id.suc:
                Intent intent2 = new Intent(CatalogoActivity.this, SucursalesActivity.class);
                startActivity(intent2);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }

    }

    public void consultarProductos (Context context){
        misProductos = new ArrayList<>();

        MyOpenHelper dataBase = new MyOpenHelper(CatalogoActivity.this);
        SQLiteDatabase db = dataBase.getReadableDatabase();

        Cursor c = dataBase.leerProductos(db);

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
                consultarProductos(CatalogoActivity.this);
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
            lvwProductos.setAdapter(adapter);
        }
    }

    private void lanzarDialogo(Producto p){
        MyOpenHelper dataBase = new MyOpenHelper(CatalogoActivity.this);
        SQLiteDatabase db = dataBase.getWritableDatabase();

        DialogoDeConfirmacion ddc = new DialogoDeConfirmacion();
        ddc.setParametros(p, dataBase, db);
        ddc.show(getSupportFragmentManager(), "DialogoDeConfirmacion");
    }


    public static class DialogoDeConfirmacion extends DialogFragment {

        Producto producto;
        MyOpenHelper dataBase;
        SQLiteDatabase db;

        public void setParametros(Producto producto,  MyOpenHelper dataBase, SQLiteDatabase db){
            this.producto = producto;
            this.dataBase = dataBase;
            this.db = db;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Está seguro de agregar este producto a favoritos?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //UPDATE
                            ProductoCase.agregarFavorito(producto.getId(), dataBase, db);
                            Toast.makeText(getActivity(), "El producto " + producto.getNombre() + " ha sido agregado a favoritos ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.e("TAG_INFO", "Producto NO agregado a favooritos ");
                            Toast.makeText(getActivity(), "Producto NO agregado a favoritos ", Toast.LENGTH_LONG).show();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}