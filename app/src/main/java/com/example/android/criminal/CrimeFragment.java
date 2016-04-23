package com.example.android.criminal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;



public class CrimeFragment extends Fragment {
    private static final String ARGS_UUID = "UUID";
    private static final String DIALOG_TAG = "Dialog tag";
    private static final String TIME_TAG = "Time tag";
    private static final String PHOTOVIEW = "Photo view";
    private static final int REQUEST_CODE = 1;
    private static final int TIME_REQUEST_CODE = 2;
    private static final int CONTACT_REQUEST_CODE = 3;
    private static final int REQUEST_TAKE_PHOTO = 4;
    private Crime mCrime;
    private EditText mEditText;
    private Button mDateButton;
    private CheckBox mCheckBox;
    private Button mTimeButton;
    private Button mSuspectButton;
    private Button mSendReportButton;
    private Button mDailSuspectPhoneNumberButton;
    private File mPhotoFile;
    private ImageView mImageView;
    private ImageButton mImageButton;

    public static CrimeFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_UUID, uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID)getArguments().getSerializable(ARGS_UUID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.getCrimeLab(getActivity()).getPhotoFileDir(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_crime_fragment_delete_crime:
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onStop() {
        super.onStop();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onResume() {
        super.onResume();
        CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_details, container, false);
        mEditText = (EditText)view.findViewById(R.id.crime_details_title);
        mEditText.setText(mCrime.getTitle());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)view.findViewById(R.id.crime_details_date_button);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
                dialog.show(fragmentManager, DIALOG_TAG);
            }
        });

        mTimeButton = (Button)view.findViewById(R.id.crime_details_time_button);
        mTimeButton.setText(DateFormat.format("kk:mm", mCrime.getDate()).toString());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                timePickerFragment.setTargetFragment(CrimeFragment.this, TIME_REQUEST_CODE);
                timePickerFragment.show(fragmentManager, TIME_TAG);
            }
        });

        mCheckBox = (CheckBox)view.findViewById(R.id.crime_details_checkBox);
        mCheckBox.setChecked(mCrime.getSolved());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mSuspectButton = (Button)view.findViewById(R.id.crime_details_suspect);
        final Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, CONTACT_REQUEST_CODE);
            }
        });
        if (mCrime.getSuspectName() != null){
            mSuspectButton.setText("嫌疑人：" + mCrime.getSuspectName());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(intent, 0) == null){
            mSuspectButton.setEnabled(false);
        }

        mDailSuspectPhoneNumberButton = (Button)view.findViewById(R.id.crime_details_dail);
        mDailSuspectPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + mCrime.getSuspectPhoneNumber()));
                startActivity(intent);
            }
        });
        if (mCrime.getSuspectName() != null){
            mDailSuspectPhoneNumberButton.setText("拨打嫌疑人" + mCrime.getSuspectName() + "的号码");
        }

        mSendReportButton = (Button)view.findViewById(R.id.crime_details_send_report);
        mSendReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_report));
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mPhotoFile));
                sendIntent.setType("image/*");
                sendIntent = Intent.createChooser(sendIntent, getString(R.string.send_report));
                startActivity(sendIntent);
            }
        });

        mImageView = (ImageView)view.findViewById(R.id.crime_details_picture_view);
        updatePhotoView();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewFragment photoViewFragment = PhotoViewFragment.newInstance(mPhotoFile);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if (mPhotoFile == null || !mPhotoFile.exists()){
                    photoViewFragment.setMenuVisibility(false);
                }else {
                    photoViewFragment.show(fragmentManager, PHOTOVIEW);
                }
            }
        });

        mImageButton = (ImageButton)view.findViewById(R.id.crime_details_image_capture_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri pictureStoreUri = Uri.fromFile(mPhotoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureStoreUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            mCrime.setDate(date);
            updateDate();
        }else if (requestCode == TIME_REQUEST_CODE){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.RETURN_TIMER_DATE);
            mCrime.setDate(date);
            mTimeButton.setText(DateFormat.format("kk:mm", mCrime.getDate()).toString());
        }else if(requestCode == CONTACT_REQUEST_CODE && data != null){
            String suspectId;
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
            Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try{
                if (cursor.getCount() == 0){
                    return;
                }
                cursor.moveToFirst();
                String suspectName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                suspectId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                mCrime.setSuspectName(suspectName);
                mCrime.setSuspectId(suspectId);
                mSuspectButton.setText("嫌疑人：" + suspectName);
                mDailSuspectPhoneNumberButton.setText("拨打嫌疑人" + suspectName + "的号码");
            }finally {
                cursor.close();
            }

            Uri commonDataKindPhoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            queryFields = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
            String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ";
            String[] whereArgs = new String[] {suspectId};

            cursor = getActivity().getContentResolver().query(commonDataKindPhoneUri,
                    queryFields, whereClause, whereArgs, null);
            try{
                if (cursor.getCount() == 0){
                    return;
                }
                cursor.moveToFirst();
                String suspectPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                mCrime.setSuspectPhoneNumber(suspectPhoneNumber);
            }finally {
                cursor.close();
            }

        }else if (requestCode == REQUEST_TAKE_PHOTO){
            updatePhotoView();
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("E yy/MM/dd", mCrime.getDate()).toString());
    }

    private String getCrimeReport(){
        String solvedString = null;
        if (mCrime.getSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateString = DateFormat.format("yy/MM/dd，EEE", mCrime.getDate()).toString();

        String suspectString = mCrime.getSuspectName();
        if (mCrime.getSuspectName() != null){
            suspectString = getString(R.string.crime_report_suspect, suspectString);
        }else{
            suspectString = getString(R.string.crime_report_no_suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspectString);

    }

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mImageView.setImageBitmap(null);
        }else {
            Log.i("CrimeFragment", mPhotoFile.getPath());
            Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }

    }
}
