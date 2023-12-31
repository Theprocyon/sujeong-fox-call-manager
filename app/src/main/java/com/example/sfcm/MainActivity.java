package com.example.sfcm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.sfcm.src.ContactFilter;
import com.example.sfcm.src.ContactPreferenceAdapter;
import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sfcm.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();


    private ActivityMainBinding binding;
    private List<ContactPreference> dataList = new ArrayList<>();
    private ContactPreferenceDB database;
    private ContactPreferenceAdapter adapter;

    //TODO thep : Add consturctor that parses the menifest xml..;
    private final PermissionManager mPermissionManager = new PermissionManager(this, Set.of(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS));

    private final ActivityResultLauncher<Intent> startForPickContact = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::onActivityResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = ContactPreferenceDB.getInstance(this);
        dataList = database.mainDao().getAll();

        setSupportActionBar(binding.toolbar);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactPreferenceAdapter(this, dataList);
        binding.recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(this::onButtonClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mPermissionManager.hasAllPermissions()){
            mPermissionManager.requestAllPermissions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO thep : Add settings
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //TODO thep : Add setting activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void launchActivityToPickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startForPickContact.launch(intent);
    }

    private void updateRecyclerView() {
        binding.recyclerView.post(() -> {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void openEditorDialog(ContactPreference preference) {


    }

    private void onButtonClick(View view) {
        if (view.getId() == binding.fab.getId()) {
            final PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            getMenuInflater().inflate(R.menu.menu_new_contact, popup.getMenu());

            popup.setOnMenuItemClickListener((menuItem) -> {
                if (menuItem.getItemId() == R.id.menu_new_contact_import) {
                    launchActivityToPickContact();
                    return true;
                }
                if (menuItem.getItemId() == R.id.menu_new_contact_custom) {
                    openEditorDialog(null);
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }

    private void onActivityResult(ActivityResult result){
        {
            if (result.getResultCode() == Activity.RESULT_OK) {

                CompletableFuture.runAsync(() -> {
                    String contactId;
                    String contactName;
                    String phoneNumber;
                    Intent data = result.getData();

                    if (data == null) {
                        Toast.makeText(this, "No Contact Data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Uri contactData = data.getData();

                    if (contactData == null) {
                        Toast.makeText(this, "No Contact Data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);

                    try {
                        // TODO thep : s
                        if (cursor.moveToFirst()) {
                            contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                            String hasPhoneNum = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            int idResult = Integer.parseInt(hasPhoneNum);

                            Log.d(TAG, "Selected " + contactName + ", " + " haspnum : " + hasPhoneNum);
                            if (idResult == 1) {
                                Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                        null,
                                        null
                                );

                                cursor2.moveToFirst();

                                phoneNumber = cursor2.getString(cursor2.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                cursor2.close();

                                ContactFilter filter = new ContactFilter(database);

                                if (!filter.contains(phoneNumber)) {

                                    ContactPreference contact = new ContactPreference();
                                    contact.name = contactName;
                                    contact.phone = phoneNumber;
                                    contact.enabled = true;
                                    contact.intervalTime = 300;
                                    contact.missingCallCount = 2;

                                    database.mainDao().insert(contact);
                                    dataList.clear();
                                    dataList.addAll(database.mainDao().getAll());
                                    updateRecyclerView();
                                }

                            } else {
                                Toast.makeText(this, "Selected contact doesn't have phone number.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        cursor.close();
                    }
                });
            }
        }
    }

}