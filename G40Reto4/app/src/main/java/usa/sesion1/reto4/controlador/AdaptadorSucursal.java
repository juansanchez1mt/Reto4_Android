package usa.sesion1.reto4.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import usa.sesion1.reto4.R;
import usa.sesion1.reto4.modelo.Sucursal;

public class AdaptadorSucursal extends
        RecyclerView.Adapter<AdaptadorSucursal.ViewHolderSucursal>
        implements View.OnClickListener{

    ArrayList<Sucursal> sucursales;
    private View.OnClickListener listener;

    public AdaptadorSucursal(ArrayList<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    @NonNull
    @Override
    public ViewHolderSucursal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sucursal, null, false);
        vista.setOnClickListener(this);
        return new ViewHolderSucursal(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSucursal holder, int position) {
        holder.imvImagenSuc.setImageResource(sucursales.get(position).getImagen());
        holder.tvwNombreSuc.setText(sucursales.get(position).getNombre());
        holder.tvwDireccionSuc.setText(sucursales.get(position).getDireccion());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return sucursales.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolderSucursal extends RecyclerView.ViewHolder {

        ImageView imvImagenSuc;
        TextView tvwNombreSuc;
        TextView tvwDireccionSuc;

        public ViewHolderSucursal(@NonNull View itemView) {
            super(itemView);

            imvImagenSuc = (ImageView) itemView.findViewById(R.id.imvImagenSuc);
            tvwNombreSuc = (TextView) itemView.findViewById(R.id.tvwNombreSuc);
            tvwDireccionSuc = (TextView) itemView.findViewById(R.id.tvwDireccionSuc);
        }
    }
}
