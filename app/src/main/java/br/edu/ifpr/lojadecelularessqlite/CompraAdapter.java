package br.edu.ifpr.lojadecelularessqlite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.ViewHolder> {

    private List<Compra> lista;
    private Context context;

    public CompraAdapter(Context context, List<Compra> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_compra, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compra c = lista.get(position);

        holder.tvInfoAparelho.setText("ID Aparelho: " + c.getIdAparelho());
        holder.tvCliente.setText("Cliente: " + c.getCliente());
        holder.tvQtd.setText("Quantidade: " + c.getQuantidade());
        holder.tvData.setText("Data: " + c.getData());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, FormCompraActivity.class);
            i.putExtra("id", c.getId());
            i.putExtra("id_aparelho", c.getIdAparelho());
            i.putExtra("cliente", c.getCliente());
            i.putExtra("quantidade", c.getQuantidade());
            i.putExtra("data", c.getData());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvInfoAparelho, tvCliente, tvQtd, tvData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfoAparelho = itemView.findViewById(R.id.tvInfoAparelho);
            tvCliente = itemView.findViewById(R.id.tvCliente);
            tvQtd = itemView.findViewById(R.id.tvQtd);
            tvData = itemView.findViewById(R.id.tvData);
        }
    }
}