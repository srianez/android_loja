package br.com.silas.breja.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.silas.breja.R;
import br.com.silas.breja.model.Item;

public class RecyclerTesteAdapter extends RecyclerView.Adapter<RecyclerTesteAdapter.RecyclerTesteViewHolder> {
 
    public static ClickRecyclerView_Interface clickRecyclerViewInterface;
    Context mctx;
    private List<Item> mList;
 
    public RecyclerTesteAdapter(Context ctx, List<Item> list, ClickRecyclerView_Interface clickRecyclerViewInterface) {
        this.mctx = ctx;
        this.mList = list;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }

    @Override
    public RecyclerTesteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemlist_itemdalista, viewGroup, false);
        return new RecyclerTesteViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(RecyclerTesteViewHolder viewHolder, int i) {
        Item item = mList.get(i);

        viewHolder.setIsRecyclable(false);

        viewHolder.viewNome.setText(viewHolder.viewNome.getText().toString()+": "+item.getNome());
        viewHolder.viewTipo.setText(viewHolder.viewTipo.getText().toString()+": "+item.getTipo());
        viewHolder.viewDescricao.setText(viewHolder.viewDescricao.getText().toString()+": "+item.getDescricao());
 
    }
 
    @Override
    public int getItemCount() {
        return mList.size();
    }
 
 
    protected class RecyclerTesteViewHolder extends RecyclerView.ViewHolder {
 
        protected TextView viewNome, viewTipo, viewDescricao;
 
        public RecyclerTesteViewHolder(final View itemView) {
            super(itemView);
 
            viewNome = (TextView) itemView.findViewById(R.id.textview_nome);
            viewTipo = (TextView) itemView.findViewById(R.id.textview_tipo);
            viewDescricao = (TextView) itemView.findViewById(R.id.textview_descricao);
            //btnRemoverUserListaUsuario = (FloatingActionButton) itemView.findViewById(R.id.btnRemoverUserListaUsuario);

            //Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 
                    clickRecyclerViewInterface.onCustomClick(mList.get(getLayoutPosition()));
 
                }
            });
        }
    }
}