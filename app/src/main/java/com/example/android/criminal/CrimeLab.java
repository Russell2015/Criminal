package com.example.android.criminal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.android.criminal.database.CrimeBaseHelper;
import com.example.android.criminal.database.CrimeCursorWrapper;
import com.example.android.criminal.database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/14.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    private ContentValues getContentValues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getUUID().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.getSolved() ? 1 : 0);
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SUSPECTID, crime.getSuspectId());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SUSPECTNAME, crime.getSuspectName());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SUSPECTPHONENUMBER, crime.getSuspectPhoneNumber());
        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mSQLiteDatabase.query(CrimeDbSchema.CrimeTable.Name,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public static CrimeLab getCrimeLab(Context context) {
        if (sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public void addCrime(Crime crime){
        mSQLiteDatabase.insert(CrimeDbSchema.CrimeTable.Name, null, getContentValues(crime));
    }

    public void updateCrime(Crime crime){
        mSQLiteDatabase.update(CrimeDbSchema.CrimeTable.Name, getContentValues(crime),
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{crime.getUUID().toString()});
    }

    public void deleteCrime(Crime crime){
        mSQLiteDatabase.delete(CrimeDbSchema.CrimeTable.Name,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{crime.getUUID().toString()});
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try{
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    crimes.add(cursor.getCrime());
                    cursor.moveToNext();
                }
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID uuid){
        Crime crime;
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuid.toString()});
        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFileDir(Crime crime){
        File getExternalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (getExternalFilesDir == null){
            return null;
        }
        return new File(getExternalFilesDir, crime.getPhotoFileName());
    }


}
