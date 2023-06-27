package com.example.sfcm.src;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sfcm.R;
import com.example.sfcm.databinding.CardviewContactBinding;
import com.example.sfcm.databinding.DialogNewContactBinding;
import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import java.util.List;

public class ContactPreferenceAdapter extends RecyclerView.Adapter<ContactPreferenceAdapter.ViewHolder>
{
    private List<ContactPreference> dataList;
    private Activity context;
    private ContactPreferenceDB database;

    public ContactPreferenceAdapter(Activity context, List<ContactPreference> dataList)
    {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactPreferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        CardviewContactBinding b = CardviewContactBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactPreferenceAdapter.ViewHolder holder, int position)
    {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardviewContactBinding itemBinding;

        public ViewHolder(@NonNull CardviewContactBinding binding)
        {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bindItem(ContactPreference item, int position) {

            final ContactPreference data = item;

            database = ContactPreferenceDB.getInstance(context);
            itemBinding.tvName.setText(data.name);
            itemBinding.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactPreference contactPref = dataList.get(position);

                    final int sID = contactPref.id;
                    String sText = contactPref.name;

                    final Dialog dialog = new Dialog(context);
                    DialogNewContactBinding binding = DialogNewContactBinding.inflate(context.getLayoutInflater());
                    dialog.setContentView(binding.getRoot());

                    int width = WindowManager.LayoutParams.MATCH_PARENT;
                    int height = WindowManager.LayoutParams.WRAP_CONTENT;

                    dialog.getWindow().setLayout(width, height);

                    EditText editText = binding.etPhoneNumber;
                    Button btSave = binding.btSave;

                    editText.setText(sText);

                    dialog.show();

                    btSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            String uText = editText.getText().toString().trim();

                            ContactPreference prefToEdit = contactPref;

                            prefToEdit.name = uText;

                            database.mainDao().insert(prefToEdit);

                            dataList.clear();
                            dataList.addAll(database.mainDao().getAll());

                            notifyDataSetChanged();
                        }
                    });
                }
            });


            itemBinding.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //notready
                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                    final int pos = getAdapterPosition();

                    ContactPreference mainData = dataList.get(pos);

                    database.mainDao().delete(mainData);

                    dataList.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, dataList.size());
                }
            });
        }
    }
}
