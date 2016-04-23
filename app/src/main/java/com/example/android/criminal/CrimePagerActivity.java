package com.example.android.criminal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_UUID = "EXTRA_UUID";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        return intent;
    }

    public static Intent newIntentOfCrimeFragment(Context context, UUID uuid){
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        return intent;
    }

    public void sendResult(String title){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_pager);
        mViewPager = (ViewPager)findViewById(R.id.crime_pager_view_pager);
        mCrimes = CrimeLab.getCrimeLab(this).getCrimes();
        UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_UUID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getUUID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getUUID().equals(uuid)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
