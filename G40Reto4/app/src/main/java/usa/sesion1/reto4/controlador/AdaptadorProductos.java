package usa.sesion1.reto4.controlador;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.modelo.Producto;

public class AdaptadorProductos extends BaseAdapter {

    Context context;
    ArrayList<Producto> productos;

    public AdaptadorProductos(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int posicion) {
        return productos.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return productos.get(posicion).getId();
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {

        View vista = view;

        LayoutInflater inflater = LayoutInflater.from(context);
        vista = inflater.inflate(R.layout.item_producto, null);

        ImageView imagen = (ImageView) vista.findViewById(R.id.imvImagen);
        TextView tvwNombre = (TextView) vista.findViewById(R.id.tvwNombre);
        TextView tvwPrecio = (TextView) vista.findViewById(R.id.tvwPrecio);

        tvwNombre.setText(productos.get(posicion).getNombre());
        tvwPrecio.setText("" +productos.get(posicion).getPrecio());
        imagen.setImageResource(productos.get(posicion).getImagen());

        return vista;
    }
}