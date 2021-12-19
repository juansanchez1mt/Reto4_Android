package usa.sesion1.reto4.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.controlador.AdaptadorSucursal;
import usa.sesion1.reto4.controlador.MyOpenHelper;
import usa.sesion1.reto4.modelo.Sucursal;

public class SucursalesActivity extends AppCompatActivity {


    private MapView myOpenMap;
    private MapController myMapController;

    ProgressDialog barraProgreso;
    RecyclerView rcvSucursales;
    ArrayList<Sucursal> sucursales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        rcvSucursales = (RecyclerView)findViewById(R.id.rcvSucursales);
        rcvSucursales.setLayoutManager(new LinearLayoutManager(this));

        barraProgreso = new ProgressDialog(this);
        barraProgreso.setMessage("Consultando en el servidor");
        barraProgreso.setTitle("Cargando Información");
        barraProgreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        new TareaAsincronica().execute();
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
                Intent intent7 = new Intent(SucursalesActivity.this, FavoritoActivity.class);
                startActivity(intent7);
                return true;

            case R.id.cat:
                Intent intent6 = new Intent(SucursalesActivity.this, CatalogoActivity.class);
                startActivity(intent6);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public class TareaAsincronica extends AsyncTask<Void, Void, Void> {

        //Sobre mi hilo principal, para gestionar el ProgressDialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barraProgreso.show();
        }

        //Sobre mi hilo secundario, para ejecutar mi tarea
        @Override
        protected Void doInBackground(Void... voids) {
            sucursales = new ArrayList<>();

            try {
                Thread.sleep(2000);
            }catch (Exception e){

            }

            MyOpenHelper dataBase = new MyOpenHelper(SucursalesActivity.this);
            SQLiteDatabase db = dataBase.getReadableDatabase();

            Cursor c = dataBase.leerSucursales(db);

            Sucursal sucursalTemp = null;

            while (c.moveToNext()){
                @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("id"));
                @SuppressLint("Range") String nombre = c.getString(c.getColumnIndex("nombre"));
                @SuppressLint("Range") String direccion = c.getString(c.getColumnIndex("direccion"));
                @SuppressLint("Range") double latitud = c.getDouble(c.getColumnIndex("latitud"));
                @SuppressLint("Range") double longitud = c.getDouble(c.getColumnIndex("longitud"));
                @SuppressLint("Range") int imagen = c.getInt(c.getColumnIndex("imagen"));

                sucursalTemp = new Sucursal(id, nombre, direccion, latitud, longitud, imagen);
                sucursales.add(sucursalTemp);

            }
            return null;
        }

        //Sobre mi hilo principal, para gestionar el ProgressDialog
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            barraProgreso.cancel();

            AdaptadorSucursal adaptador = new AdaptadorSucursal(sucursales);

            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Sucursal sucSeleccionada = sucursales.get(rcvSucursales.getChildAdapterPosition(view));
                    cargarMapa(sucSeleccionada);
                }
            });

            rcvSucursales.setAdapter(adaptador);

        }

        private void cargarMapa(Sucursal sucursal){
            GeoPoint sucursalSeleccionada = new GeoPoint(sucursal.getLatitud(), sucursal.getLongitud());
            Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

            myOpenMap = (MapView) findViewById(R.id.mapa);
            myOpenMap.setBuiltInZoomControls(true);
            myMapController = (MapController) myOpenMap.getController();
            myMapController.setCenter(sucursalSeleccionada);
            myMapController.setZoom(17);

            myOpenMap.setMultiTouchControls(true);

            final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), myOpenMap);
            myOpenMap.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca
            myLocationoverlay.enableMyLocation();
            myLocationoverlay.runOnFirstFix(new Runnable() {
                public void run() {
                    myMapController.animateTo(myLocationoverlay.getMyLocation());
                }
            });


            ArrayList<OverlayItem> puntos = new ArrayList<OverlayItem>();
            puntos.add(new OverlayItem(sucursal.getNombre(), sucursal.getDireccion(), sucursalSeleccionada));

            ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                    return false;
                }
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    return true;
                }
            };

            ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(), puntos, tap);
            capa.setFocusItemsOnTap(true);
            myOpenMap.getOverlays().add(capa);


        }
    }
}