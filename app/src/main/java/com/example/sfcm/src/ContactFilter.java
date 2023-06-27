package com.example.sfcm.src;

import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactFilter {
    public class IncomingCallInfo{

    }
    List<ContactPreference> contactList;

    public ContactFilter(ContactPreferenceDB database) {
        if (database != null) {
            contactList = database.mainDao().getAll();
        }
    }

    public Optional<ContactPreference> filter(IncomingCallInfo info) {
        return null;
    }
}