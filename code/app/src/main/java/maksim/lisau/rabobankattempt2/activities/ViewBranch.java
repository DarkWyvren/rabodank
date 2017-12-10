package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import maksim.lisau.rabobankattempt2.database.Invoice;
import maksim.lisau.rabobankattempt2.graphs.GraphType;
import maksim.lisau.rabobankattempt2.graphs.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import maksim.lisau.rabobankattempt2.HistoryStorage;
import maksim.lisau.rabobankattempt2.Item;
import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;
import maksim.lisau.rabobankattempt2.database.Supplier;
import maksim.lisau.rabobankattempt2.graphs.GraphStream;
import maksim.lisau.rabobankattempt2.graphs.Grapher;
import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

import static maksim.lisau.rabobankattempt2.database.DatabaseHandler.invoiceHashMap;

public class ViewBranch extends AppCompatActivity {

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
    private static Branch viewBranch;
    //View mode=1, Edit mode=0.
    private int viewMode;
    private Bundle temp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));*/
        Bundle bundle= getIntent().getExtras();
        //Get viewed branch
        viewBranch= DatabaseHandler.branchHashMap.get(bundle.getString("Branch key"));

        //Init graphics
        setContentView(R.layout.activity_view_branch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        HistoryStorage.addRecent(new Item(0,viewBranch.name));
        viewMode=bundle.getInt("View type");
        //Setting up graphs




        //FrameLayout frame = new FrameLayout(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_branch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/ Up button, so long
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
        public static String supplier;
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
            View rootView = inflater.inflate(R.layout.fragment_view_branch, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView branchName=(TextView) rootView.findViewById(R.id.branchNameAddress);
            branchName.setText("Branch Name: "+viewBranch.name+"\nBranch Address: "+viewBranch.address.getFormattedAddressFull());

            // give adapter to ListView UI element to render
            ArrayList <String> listItems=new ArrayList<>();
            listItems.addAll(DatabaseHandler.getTopSuppliers(viewBranch.name));
            System.out.println("listItems size: "+listItems.size());
            ListView suppliersList=(ListView) rootView.findViewById(R.id.topSuppliers);
            ArrayAdapter ad = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1, listItems);
            suppliersList.setAdapter(ad);

            Branch sup = viewBranch;
            GraphStream graphStream = new GraphStream("transactions",new Node[]{});
            String ext[] ={"Transaction magnitude","$","year"};
            ArrayList<String> externals = new ArrayList(Arrays.asList(ext));
            ArrayList<Invoice> invoices = new ArrayList<>();
            System.out.println("supplier:"+sup.returnName()+" supplier invoice:"+sup.transactionIDs);
            System.out.println("extbefore:"+externals);
            for(long id:sup.transactionIDs){
                invoices.add(invoiceHashMap.get(id));

            }

            invoices.sort(new Comparator<Invoice>() {
                @Override
                public int compare(Invoice invoice, Invoice t1) {
                    return (int)(invoice.getDate().getTime()-t1.getDate().getTime());
                }
            });

            int index = 0;
            int yearupto=0;
            int casham = 0;
            for(Invoice i:invoices){
                Calendar cal = Calendar.getInstance();
                cal.setTime(i.getDate());
                int year = cal.get(Calendar.YEAR);

                if(year>yearupto){
                    if(yearupto!=0) {
                        graphStream.insert(new Node(casham, index));
                        externals.add(yearupto+"");
                        index++;
                        casham=0;
                    }
                    yearupto=year;
                }
                casham+=i.getCashAmount();


            }
            graphStream.insert(new Node(casham, index));
            externals.add(yearupto+"");

            int total=0;
            Long[] invoces=new Long[viewBranch.transactionIDs.size()];
            viewBranch.transactionIDs.toArray(invoces);
            for (int i=0; i<viewBranch.transactionIDs.size(); i++) {
                total+=invoiceHashMap.get(invoces[i]).getCashAmount();
            }
            System.out.println("total trancsaction volume:"+total);


            System.out.println("extafter:"+externals);
            System.out.println("nodesafter:"+graphStream.stream);
            //
            RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.fragment);
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            DisplayMetrics m = getResources().getDisplayMetrics();
            PFragment p = new PFragment(new Grapher(m.density* 160f,m.widthPixels,m.heightPixels/4,
                                                    new GraphStream[]{graphStream},GraphType.BAR,externals));
            fragmentTransaction.add(R.id.fragment, p);
            fragmentTransaction.commit();



            suppliersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView textView1=(TextView) view;
                    supplier=textView1.getText().toString();
                    Intent intent=new Intent(getActivity(), ViewSupplier.class);
                    Bundle b=new Bundle();
                    b.putString("Supplier key", supplier);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    textView.setText("Overview");
                    //Adding top suppliers to list view
                    //frame.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    textView.setText("Recent");
                    break;
                case 2:
                    textView.setText("Trends");
                    //frame.setVisibility(View.VISIBLE);




                    break;

            }
            return rootView;
        }
    }
    public static class GrapherFragment extends PFragment {

        private Grapher g;
        public GrapherFragment() {

            super(new Grapher(100,800,400));
        }

        private static final String ARG_SECTION_NUMBER = "section_number";
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_grapher, container, false);


            return rootView;
        }
        public static GrapherFragment newInstance(int sectionNumber) {
            GrapherFragment fragment = new GrapherFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            if (g != null) {
                g.onRequestPermissionsResult(
                        requestCode, permissions, grantResults);
            }else{
                System.out.println("W");
            }
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
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
    public void switchMode(View view) {
        viewMode=viewMode==0?1:0;
        temp.clear();
        temp.putInt("View type",viewMode);
        onCreate(temp);
    }
    public void gotoHome(View view) {
        Intent intent=new Intent(ViewBranch.this,Home.class);
        startActivity(intent);
    }
}
