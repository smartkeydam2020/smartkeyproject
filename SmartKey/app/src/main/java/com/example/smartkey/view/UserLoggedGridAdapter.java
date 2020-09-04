package com.example.smartkey.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.smartkey.R;

import java.util.ArrayList;
import java.util.List;

public class UserLoggedGridAdapter extends RecyclerView.Adapter<UserLoggedGridAdapter.ItemHolder> {

    private LayoutInflater inflater;
    private List<String> keyList = new ArrayList<>();
    private Context miContexto;

    public UserLoggedGridAdapter(Context context) {
        inflater=LayoutInflater.from(context);
        miContexto = context;
        keyList.add("Cerraduras");
        keyList.add("Personas");
        keyList.add("Mi Perfil");
        keyList.add("lo que sea vergoso");
    }

    @NonNull
    @Override
    public UserLoggedGridAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.list_user_logged,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        if(keyList != null){
            final String key = keyList.get(position);
            holder.tvNombre.setText(key);

            final Context context = miContexto;

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context,"Evento lanzado con un onLongClick",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvNombre.setSelected(true);
        }
    }
}
