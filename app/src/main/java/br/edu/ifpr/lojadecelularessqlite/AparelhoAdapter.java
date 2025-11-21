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

public class AparelhoAdapter extends RecyclerView.Adapter<AparelhoAdapter.ViewHolder> {

    private List<Aparelho> lista;
    private Context context;

    public AparelhoAdapter(Context context, List<Aparelho> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_aparelho, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Aparelho a = lista.get(position);
        holder.tvModeloMarca.setText(a.getModelo() + " - " + a.getMarca());
        holder.tvPreco.setText("Preço: R$ " + a.getPreco());
        holder.tvEstoque.setText("Estoque: " + a.getEstoque());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, FormAparelhoActivity.class);
            i.putExtra("id", a.getId());
            i.putExtra("modelo", a.getModelo());
            i.putExtra("marca", a.getMarca());
            i.putExtra("preco", a.getPreco());
            i.putExtra("estoque", a.getEstoque());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvModeloMarca, tvPreco, tvEstoque;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModeloMarca = itemView.findViewById(R.id.tvModeloMarca);
            tvPreco = itemView.findViewById(R.id.tvPreco);
            tvEstoque = itemView.findViewById(R.id.tvEstoque);
        }
    }
}