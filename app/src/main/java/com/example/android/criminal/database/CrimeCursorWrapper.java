package com.example.android.criminal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.android.criminal.Crime;

import java.util.Date;
import java.util.UUID;


public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int solved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
        String suspectId = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECTID));
        String suspectName = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECTNAME));
        String suspectPhoneNumber = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECTPHONENUMBER));


        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(solved != 0);
        crime.setSuspectId(suspectId);
        crime.setSuspectName(suspectName);
        crime.setSuspectPhoneNumber(suspectPhoneNumber);

        return crime;
    }
}
