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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactPreferenceAdapter.ViewHolder holder, int position)
    {
        final ContactPreference data = dataList.get(position);
        database = ContactPreferenceDB.getInstance(context);
        holder.textView.setText(data.name);
        holder.btEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ContactPreference contactPref = dataList.get(holder.getAdapterPosition());

                final int sID = contactPref.id;
                String sText = contactPref.name;

                //TODO
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_new_contact);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();

                EditText editText = dialog.findViewById(R.id.et_phone_number);
                Button btSave = dialog.findViewById(R.id.bt_save);

                editText.setText(sText);

                btSave.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        String uText = editText.getText().toString().trim();

                        ContactPreference prefToEdit = database.mainDao().getById(sID);

                        prefToEdit.name = uText;

                        database.mainDao().insert(prefToEdit);

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());

                        notifyDataSetChanged();
                    }
                });
            }
        });


        holder.btDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //notready
                if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) return;

                ContactPreference mainData = dataList.get(holder.getAdapterPosition());

                database.mainDao().delete(mainData);

                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView btEdit, btDelete;

        public ViewHolder(@NonNull View view)
        {
            super(view);
            textView = view.findViewById(R.id.tv_name);
            btEdit = view.findViewById(R.id.bt_edit);
            btDelete = view.findViewById(R.id.bt_delete);
        }
    }
}
