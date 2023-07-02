package com.example.sfcm.src;

import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactFilter {

    //TODO Hyeon : For future extension
//    public class IncomingCallInfo{
//        private final String phoneNumber;
//        public IncomingCallInfo(String pn){
//            phoneNumber = pn;
//        }
//
//        public String getPhoneNumber(){
//            return this.phoneNumber;
//        }
//    }
    List<ContactPreference> contactList;

    public ContactFilter(ContactPreferenceDB database) {
        if (database != null) {
            contactList = database.mainDao().getAll();
        }
    }

    public Optional<ContactPreference> filter(String pn) {
        return null;
    }

    //TODO Hyeon : Temp
    public boolean contains(String pn) {
        boolean ret = false;

        for(ContactPreference i : contactList){
            ret |= i.phone.equals(pn);
        }

        return ret;
    }
}