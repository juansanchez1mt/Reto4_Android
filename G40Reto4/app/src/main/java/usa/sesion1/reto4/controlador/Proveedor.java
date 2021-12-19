package usa.sesion1.reto4.controlador;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.modelo.Producto;
import usa.sesion1.reto4.modelo.Sucursal;

public class Proveedor {

    public static ArrayList<Producto> cargarProductos = getProductos();

    public static ArrayList<Producto> getProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Pelota de Navidad", 650000, R.drawable.pelota));
        productos.add(new Producto("Guirnalda", 300000, R.drawable.girnalda));
        productos.add(new Producto("Galletas", 200000, R.drawable.galletas));
        productos.add(new Producto("Luz", 100000, R.drawable.luz));
        productos.add(new Producto("Papa Noel", 900000, R.drawable.papa));



        return productos;
    }

    public static ArrayList<Sucursal> getSucursales() {
        ArrayList<Sucursal> sucursales = new ArrayList<>();
        sucursales.add(
                new Sucursal("Sucursal Cosmocentro ", "Calle 5 No. 50 – 103. Cali, Colombia", 3.4514587679609106, -76.53142561275456,
                        R.drawable.tienda1));

        sucursales.add(
                new Sucursal("Sucursal Unicentro ", " Carrera 100 N° 5-169. Cali, Colombia", 3.3792567305514294, -76.53875877978035,
                        R.drawable.tienda2));

        sucursales.add(
                new Sucursal("Sucursal Chipichape ", "Cl. 38 Nte. #6N – 45. Cali, Colombia", 3.4769050145870044, -76.52826540486801,
                        R.drawable.tienda3));



        return sucursales;
    }
}
