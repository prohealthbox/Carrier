package comcmput301f16t01.github.carrier;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // THIS LISTENS TO THE TABS BEING MOVED.
        // WE CAN UPDATE LIST VIEWS, FAB, ETC WITH THIS
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                changeFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        };
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        // Maybe necessary, maybe not, need to do research.
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        // We start on the rider tab, so we hide the driver fab
        FloatingActionButton driver_fab = (FloatingActionButton) findViewById(R.id.fab_driver);
        driver_fab.hide();

    }

    /**
     * Shows the correct FAB depending on what tab position we are at.
     *
     * @param position the current screen tab we are in (i.e. 0=Rider, 1=Driver)
     */
    private void changeFab(int position) {
        FloatingActionButton rider_fab = (FloatingActionButton) findViewById(R.id.fab_rider);
        FloatingActionButton driver_fab = (FloatingActionButton) findViewById(R.id.fab_driver);
        switch (position) {
            case 0:
                driver_fab.hide();
                rider_fab.show();
                break;

            case 1:
                rider_fab.hide();
                driver_fab.show();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_viewProfile) {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", UserController.getLoggedInUser());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        if (id == R.id.action_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }


        if (id == R.id.action_logOut) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When back is pressed or the "Log Out" menu option is selected:
     * Pop up a AlertDialog to confirm and open a new LoginActivity, while closing the current
     * RiderMainActivity.
     */
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Are you sure?");
        adb.setMessage("Log out and return to the login screen?");
        adb.setCancelable(true);
        final Activity activity = MainActivity.this;
        adb.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginMemory lm = new LoginMemory( activity );
                lm.saveUsername( "" ); // remove the username from memory
                activity.finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                UserController uc = new UserController();
                uc.logOutUser();
            }
        });
        adb.setNegativeButton("Cancel", null);
        adb.show();
    }

    public void startMakeRequestActivity(View view) {
        // This will start the make request activity for a rider when they press the rider FAB
        //Toast.makeText(this, "RIDER FAB", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, MakeRequestActivity.class);
        startActivity(intent);
    }

    public void startSearchActivity(View view) {
        // This will start the Search activity for a driver when they want to search requests
        // after they press the driver FAB
        //Toast.makeText(this, "DRIVER FAB", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            // TODO (after) allow the ability to toggle between what requests are shown (?)

            ListView requestListView = (ListView) rootView.findViewById( R.id.listView_homeRequestList );
            if( getArguments().getInt(ARG_SECTION_NUMBER) == 1 ) {
                fillRiderRequests( requestListView );
            } else {
                fillDriverRequests( requestListView );
            }

            return rootView;
        }

        /**
         * Sets up the ListView for the driver.
         * @param requestListView
         */
        private void fillDriverRequests(ListView requestListView) {
            RequestController rc = new RequestController();
            User loggedInUser = UserController.getLoggedInUser();
            RequestList rl = RequestController.getInstance();
            if (rc.getOfferedRequests(loggedInUser).size() == 0){
                User testUser = new User("TestUser");
                Request testRequest1 = new Request(testUser, new Location(), new Location());
                Request testRequest2 = new Request(testUser, new Location(), new Location(),
                        "I gotta go home please.");
                testRequest1.setFare(100);
                rl.add(testRequest1);
                rl.add(testRequest2);
                rc.addDriver(testRequest1, loggedInUser);
                rc.addDriver(testRequest2, loggedInUser);
            }
            ArrayList<Request> requestList = rc.getOfferedRequests(loggedInUser);
            DriverRequestAdapter requestArrayAdapter = new DriverRequestAdapter(this.getContext(),
                    R.layout.driverrequestlist_item, requestList);
            requestListView.setAdapter(requestArrayAdapter);
            final Context ctx = this.getContext();

            /**
             * When we click a request we want to be able to see it in another activity
             * Use bundles to send the position of the request in a list
             */

        }

        /**
         * Sets up the ListView for the rider.
         * @param requestListView
         */

        private void fillRiderRequests(ListView requestListView) {
            RequestController rc = new RequestController();
            User loggedInUser = UserController.getLoggedInUser();
            // Mike's old line, Kieter rewrote with Mike, you can probably delete it
            //final ArrayList<Request> requestList = rc.getRequests( loggedInUser );
            final ArrayList<Request> requestList = rc.getInstance();


            if (requestList.size() == 0) {
                
                // Create sample requests because this is probably not set up yet.
                // TODO: remove these tests
                Request requestOne = new Request(loggedInUser, new Location(), new Location(), "test request 1!");
                ElasticUserController.FindUserTask fut = new ElasticUserController.FindUserTask();
                fut.execute("sarah");
                User sarah = null;
                try {
                    sarah = fut.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                requestOne.setChosenDriver(sarah);

                Request requestTwo = new Request(loggedInUser, new Location(), new Location(), "test request 2!");
                requestTwo.setChosenDriver(sarah);

                requestOne.setStatus(Request.COMPLETE);
                requestTwo.setStatus(Request.PAID);
                requestList.add(requestOne);
                requestList.add(requestTwo);
            }

            // Mike's old line, Kieter rewrote it with Mike, you can probably delete it
//            RequestAdapter requestArrayAdapter = new RequestAdapter(this.getContext(),
//                    R.layout.requestlist_item, requestList );
            RequestAdapter requestArrayAdapter = new RequestAdapter(this.getContext(),
                    R.layout.requestlist_item, rc.getRequests(UserController.getLoggedInUser()) );

            requestListView.setAdapter( requestArrayAdapter );

            final Context ctx = this.getContext();
            /*requestListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println( "hi" );
                    return true;
                }
            });*/


            /**
             * When we click a request we want to be able to see it in another activity
             * Use bundles to send the position of the request in a list
             */
            requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), RiderRequestActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    //bundle.putString("activity", "MainActivity");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "RIDER ACTIVITY";
                case 1:
                    return "DRIVER ACTIVITY";
            }
            return null;
        }
    }
}
