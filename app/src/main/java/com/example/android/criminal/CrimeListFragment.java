package com.example.android.criminal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CrimeAdaper mCrimeAdaper;
    private Crime mCrime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        new MenuInflater(getActivity()).inflate(R.menu.context_recycler_view_memu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(mCrime);
                mCrimeAdaper.deleteItem(mCrime);
                updateUI();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_recycler, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_crime_add_crime:
                Crime crime = new Crime();
                CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getUUID());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> list = new ArrayList<>();
        for (Crime crime: crimeLab.getCrimes()){
            if (crime.getTitle() == null){
                list.add(crime);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            crimeLab.deleteCrime(list.get(i));
        }
        List<Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdaper == null){
            mCrimeAdaper = new CrimeAdaper(crimes);
            mRecyclerView.setAdapter(mCrimeAdaper);
        }else {
            mCrimeAdaper.setCrimes(crimes);
            mCrimeAdaper.notifyDataSetChanged();
        }

    }

    private class CrimeHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolved;

        public CrimeHolder(View itemView){
            super((itemView));
            registerForContextMenu(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getUUID());
                    startActivity(intent);
                }
            });
            mSolved = (CheckBox)itemView.findViewById(R.id.crime_list_item_solved);
            mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCrime.setSolved(isChecked);
                    CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
                }
            });
            mTitleTextView = (TextView)itemView.findViewById(R.id.crime_list_item_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_list_item_date);
        }

        public void onBind(Crime crime){
            mCrime = crime;
            mSolved.setChecked(crime.getSolved());
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(DateFormat.format("E yy/MM/dd kk:mm", mCrime.getDate()).toString());
        }

        public Crime getCrime(){
            return mCrime;
        }
    }

    private class CrimeAdaper extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdaper(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_list_item, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.onBind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void deleteItem(Crime crime){
            mCrimes.remove(crime);
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

}
