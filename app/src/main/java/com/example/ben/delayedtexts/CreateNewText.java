package com.example.ben.delayedtexts;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateNewText extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    SharedPreferences texts;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_text);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        texts= getPreferences(MODE_PRIVATE);
        editor= texts.edit();

        final ComponentName componentName = new ComponentName(this, SMSJobService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                String num= texts.getString("PhoneNum", "InvalidPhone");

                String message= texts.getString("Message", "InvalidMessage");
                String time= texts.getString("Time", "InvalidTime");

                if(num.equals("InvalidPhone")){
                    Snackbar.make(view, "Invalid Phone Number", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
                else if(message.equals("InvalidMessage")){
                    Snackbar.make(view, "Invalid Message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

                //TODO impliment the time feature and then enable this check
//                else if(time.equals("InvalidTime")){
//                    Snackbar.make(view, "Invalid Time set", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                }
                else{
                    //schedule job
                    JSONObject textInfo= new JSONObject();
                    String[] textInf= {num, message};
                    try{
                        textInfo.put("phoneNum", num);
                        textInfo.put("message", message);

                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }

                    //check these
                    PersistableBundle bundle = new PersistableBundle();
                    bundle.putStringArray("TextInfo", textInf);
                    //bundle.putString("TextInfo", textInfo.toString());
                    final JobInfo jobInfo = new JobInfo.Builder(12, componentName)
                            .setMinimumLatency(5000)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                            .setExtras(bundle)
                            .build();
                    JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
                    int resultCode = jobScheduler.schedule(jobInfo);
                    if (resultCode == JobScheduler.RESULT_SUCCESS) {
                        Log.d("Message", "Job scheduled!");
                    } else {
                        Log.d("Message", "Job not scheduled");
                    }

                }
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_new_text, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0:
                    RecipientSelector recipientSelector= new RecipientSelector();
                    return recipientSelector;
                case 1:
                    MessageScreen messageScreen = new MessageScreen();
                    return messageScreen;
                case 2:
                    ScheduleText scheduleText = new ScheduleText();
                    return scheduleText;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
