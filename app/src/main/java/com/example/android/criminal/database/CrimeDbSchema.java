package com.example.android.criminal.database;

import java.util.UUID;

/**
 * Created by Administrator on 2016/3/17.
 */
public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String Name = "Crimes";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECTNAME = "suspectName";
            public static final String SUSPECTID = "suspectId";
            public static final String SUSPECTPHONENUMBER = "suspectPhoneNumber";
        }
    }
}
