package vaishnavism.eclass.ta.divyaprabandamfull;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    // ListView
    private ListView lv;
    private HashMap<String, String> item;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private String[] myStrings;
    private String searchFor;
    private String version;
    private int verCode;



    Uri gmmIntentUri;
    // Listview Adapter
    ArrayAdapter<String> adapter;

    // String Array content store
    String[] fillList;

    Spinner spinner_alwargal;
    ToggleButton toggle;
    ArrayAdapter<CharSequence> sp_adapter;
    int current_location = 0;
    int alwar_y = 1; //1 means alwar 0 means divyadesam
    String spSelectedValue;
    String foundNumber;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        try {
            android.content.pm.PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Welcome to Vaishnavism.eClass "+getString(R.string.app_name)+" Version: "+version, Snackbar.LENGTH_LONG);

        snackbar.show();
        // Prepare the alwargal spinner
        //
        spinner_alwargal = findViewById(R.id.Sp_Alwargal);
        sp_adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.alwargal, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_alwargal.setAdapter(sp_adapter);
        spinner_alwargal.setPopupBackgroundResource(R.color.alwarback);

        spinner_alwargal.getOnItemSelectedListener();
        //
        // Prepare to get toggle
        //
        toggle = findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    //

                    sp_adapter = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.divyadesam, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner_alwargal.setPopupBackgroundResource(R.color.ddback);
                    spinner_alwargal.setAdapter(sp_adapter);
                    alwar_y = 0; // means divyadesam

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "திவ்யதேசம் தேர்ந்தெடுத்தீர்கள் ", Snackbar.LENGTH_LONG);

                    snackbar.show();


                } else {
                    // The toggle is disabled
                    // Create an ArrayAdapter using the string array and a default spinner layout

                    sp_adapter = ArrayAdapter.createFromResource(getBaseContext(),
                            R.array.alwargal, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    spinner_alwargal.setPopupBackgroundResource(R.color.alwarback);
                    spinner_alwargal.setAdapter(sp_adapter);
                    alwar_y = 1; // means alwar
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "அழ்வாரை தேர்ந்தெடுத்தீர்கள் ", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    //Toast.makeText(MainActivity.this, "Alwar is selected", Toast.LENGTH_LONG).show();
                }
            }
        });


        // spinner population is complete

        // To prepare and populate ListView with Prabandam
        fillList = getResources().getStringArray(R.array.prabandam);


        //Create Collection to search
        //final List<String> findMe = new ArrayList<String>(Arrays.asList(fillList));

        // Adding items to listview
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fillList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fillList);
        lv = findViewById(R.id.lv_dp);
        lv.setAdapter(adapter);


        // Manage Thedal activity
        /*
        EditText thedu;
        thedu = findViewById(R.id.myFilter);


        thedu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //adapter.getFilter().filter(editable.toString());

            }
        });
        */// Manage thedal activity ends here

        //
        //Handle the values and
        //filter the listview based
        //on the selected value
        //
        spinner_alwargal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                spSelectedValue = item.toString();
                String[] splitItems = spSelectedValue.split("\\.");

                foundNumber = splitItems[0];

                if (foundNumber.length() > 2) {
                    //fileter base on divyadesam

                    //Toast.makeText(MainActivity.this, "DD" + foundNumber + ".", Toast.LENGTH_LONG).show();
                    adapter.getFilter().filter("DD" + foundNumber);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, spSelectedValue + " தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

                    snackbar.show();

                } else {
                    //fileter base on alwars
                    //Toast.makeText(MainActivity.this, "A" + foundNumber + ".", Toast.LENGTH_LONG).show();
                    if (foundNumber == "")
                        adapter.getFilter().filter("");
                    else
                        adapter.getFilter().filter("A" + foundNumber + ".");
                    //Snackbar snackbar = Snackbar
                    //        .make(coordinatorLayout, spSelectedValue + " தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

                    // snackbar.show();
                }

                // Toast.makeText(MainActivity.this, "Selected item is "+spSelectedValue, Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity.this, "Split value is "+foundNumber, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //
        //spinner validation complete
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "vaishnavism.eclass@gmail.com | fb.com/vaishnavism.eclass "+getString(R.string.app_name)+version, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    //Performing action onItemSelected and onNothing selected
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Log.d("VC", "Selected Item is " + parent.getItemAtPosition(pos));
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search_item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("வடிகட்டி");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //goto first
        if (id == R.id.action_increase_first) {
            current_location = 1;
            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "முதல் பாசுரம்", Snackbar.LENGTH_SHORT);

            snackbar.show();
            //Toast.makeText(this, "Top Row", Toast.LENGTH_LONG).show();
            return true;
        }

        // goto last
        if (id == R.id.action_increase_last) {
            current_location = 0;
            current_location = adapter.getCount() - 1;
            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "Bottom Row", Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "கடைசி பாசுரம் ", Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by +10
        if (id == R.id.action_increase_ten) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() + 10;
            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by +20
        if (id == R.id.action_increase_twenty) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() + 20;
            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by +50
        if (id == R.id.action_increase_fifty) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() + 50;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by +100
        if (id == R.id.action_increase_hundred) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() + 100;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }


        //move by -10
        if (id == R.id.action_decrease_ten) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() - 10;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);
            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by -20
        if (id == R.id.action_decrease_twenty) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() - 20;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);

            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by -50
        if (id == R.id.action_decrease_fifty) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() - 50;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);

            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        //move by -100
        if (id == R.id.action_decrease_hundred) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() - 100;

            lv.smoothScrollToPosition(current_location);
            lv.setSelection(current_location);

            //Toast.makeText(this, "At " + current_location, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "வரி  " + current_location, Snackbar.LENGTH_SHORT);

            snackbar.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tirupallandu) {
            // Handle the camera action
            lv.smoothScrollToPosition(1);
            lv.setSelection(1);
            //} else if (id == R.id.nav_triplicane) {
            //  adapter.getFilter().filter("34.");
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருப்பல்லாண்டு தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_periyalwartirumozhi) {
            lv.smoothScrollToPosition(16);
            lv.setSelection(16);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பெரியாழ்வார் திருமொழி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_tiruppavai) {
            lv.smoothScrollToPosition(477);
            lv.setSelection(477);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருப்பாவை தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_nachiyartirumozhi) {
            lv.smoothScrollToPosition(509);
            lv.setSelection(509);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "நாச்சியார் திருமொழி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_perumaltirumozhi) {
            lv.smoothScrollToPosition(655);
            lv.setSelection(655);


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பெருமாள் திருமொழி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tiruchandavirutham) {
            lv.smoothScrollToPosition(762);
            lv.setSelection(762);


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருச்சந்த விருத்தம் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tirumaalai) {
            lv.smoothScrollToPosition(884);
            lv.setSelection(884);


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருமாலை தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tirupalliyezhuchi) {
            lv.smoothScrollToPosition(930);
            lv.setSelection(930);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருப்பள்ளி எழுச்சி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_amalanadhipiran) {
            lv.smoothScrollToPosition(942);
            lv.setSelection(942);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "அமலனாதிபிரான் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_kanninumsiruthambu) {
            lv.smoothScrollToPosition(954);
            lv.setSelection(954);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "கண்ணிநுண்சிறுத்தாம்பு தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
//mudal ayiram is over
        } else if (id == R.id.nav_periyatirumozhi) {
            lv.smoothScrollToPosition(966);
            lv.setSelection(966);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பெரிய திருமொழி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tirukurunthandagam) {
            lv.smoothScrollToPosition(2055);
            lv.setSelection(2055);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருக்குறுந்தாண்டகம் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tirunedunthandagam) {
            lv.smoothScrollToPosition(2075);
            lv.setSelection(2075);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருநெடுந்தாண்டகம் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
//irandam ayiram is over
        } else if (id == R.id.nav_mudaltiruvandhadhi) {
            lv.smoothScrollToPosition(2105);
            lv.setSelection(2105);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "முதல் திருவந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_irandamtiruvandhadhi) {
            lv.smoothScrollToPosition(2206);
            lv.setSelection(2206);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "இரண்டாம் திருவந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_mundramtiruvandhadhi) {
            lv.smoothScrollToPosition(2307);
            lv.setSelection(2307);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "மூன்றாம் திருவந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_nanmugantiruvandhadhi) {
            lv.smoothScrollToPosition(2408);
            lv.setSelection(2408);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "நான்முகன் திருவந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tiruvirutham) {
            lv.smoothScrollToPosition(2505);
            lv.setSelection(2505);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருவிருத்தம் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tiruvasirium) {
            lv.smoothScrollToPosition(2606);
            lv.setSelection(2606);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருவாசிரியம் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_periyatiruvandhadhi) {
            lv.smoothScrollToPosition(2614);
            lv.setSelection(2614);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பெரிய திருவந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tiruyezhukutrarikkai) {
            lv.smoothScrollToPosition(2702);
            lv.setSelection(2702);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருஎழுகூற்றிருக்கை தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_siriyatirumadal) {
            lv.smoothScrollToPosition(2705);
            lv.setSelection(2705);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "சிறிய திருமடல் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_periyatirumadal) {
            lv.smoothScrollToPosition(2783);
            lv.setSelection(2783);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பெரிய திருமடல் தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_iramanusanootrandhadhi) {
            lv.smoothScrollToPosition(4041);
            lv.setSelection(4041);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "இராமானுச நூற்றந்தாதி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_tiruvoimozhi) {
            lv.smoothScrollToPosition(2933);
            lv.setSelection(2933);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "திருவாய்மொழி தேர்ந்தெடுத்தீர்கள்", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_refresh) {
            adapter.getFilter().filter("");
        /*} else if (id == R.id.nav_manage) {
            adapter.getFilter().filter("");*/

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Refreshed Selection", Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.action_notify) {
            // adapter.getFilter().filter("");
        /*} else if (id == R.id.nav_manage) {
            adapter.getFilter().filter("");*/
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
            //Snackbar snackbar = Snackbar
            //        .make(coordinatorLayout, "நாள் தோறும் நாலாயிரம் தற்போது உபயோகத்தில் இல்லை ", Snackbar.LENGTH_SHORT);

            //snackbar.show();


        } else if (id == R.id.action_bhagyam) {
            adapter.getFilter().filter("");

            int listSize = adapter.getCount();
            Random rand = new Random();
            int showMe = rand.nextInt(listSize - 1);
            //String showText = fillList[showMe].replace("\n","\r");

            lv.smoothScrollToPosition(showMe);
            lv.setSelection(showMe);

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "அடியேன் பாக்கியம் " + showMe, Snackbar.LENGTH_SHORT);

            snackbar.show();

        } else if (id == R.id.nav_share) {
            //Toast.makeText(getBaseContext(), "You are " + lv.getFirstVisiblePosition(),
            //       Toast.LENGTH_LONG).show();
            shareText();


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "பாசுரம் பகிரப்பட்டது", Snackbar.LENGTH_SHORT);

            snackbar.show();
        } else if (id == R.id.nav_bookmarks) {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "தற்போது உபயோகத்தில் இல்லை ", Snackbar.LENGTH_SHORT);

            snackbar.show();
        }


        //Map management
        else if (id == R.id.action_map) {
            current_location = 0;
            current_location = lv.getFirstVisiblePosition() - 100;

            Log.d("VC", "spSelectedValue is : " + spSelectedValue);
            if (spSelectedValue != "") {
                Log.d("VC", "foundNumber is : " + foundNumber);
                switch (foundNumber) {
                    //
                    // Divyadesangal
                    //

                    case "001":
                        //gmmIntentUri = Uri.parse("geo:10.862391,78.6880503?q="+Uri.encode("VM6Q+XX Tiruchirappalli, Tamil Nadu"));
                        gmmIntentUri = Uri.parse("geo:10.862391,78.6880503?q=" + Uri.encode("Sri Ranganathar Swamy Temple - Srirangam"));
                        break;
                    case "002":
                        //gmmIntentUri = Uri.parse("geo:10.8268405,78.6733927?q="+Uri.encode("RMGF+Q9 Tiruchirappalli, Tamil Nadu"));
                        //gmmIntentUri = Uri.parse("geo:10.8268405,78.6733927?q="+Uri.encode("RMGF+Q9"));
                        gmmIntentUri = Uri.parse("geo:10.8268405,78.6733927?q=" + Uri.encode("Uraiyoor Kamalavalli Nachiyar Temple, Uraiyoor"));
                        break;
                    case "003":
                        //gmmIntentUri = Uri.parse("geo:10.8156531,79.1387186?q="+Uri.encode("R48Q+6H Thanjavur, Tamil Nadu"));
                        gmmIntentUri = Uri.parse("geo:10.8156531,79.1387186?q=" + Uri.encode("Thanjai Manikundra Perumal Koil"));
                        break;
                    case "004":
                        //gmmIntentUri = Uri.parse("geo:10.8678007,78.882062?q="+Uri.encode("VV9J+3V Jangamarajapuram, Tamil Nadu"));
                        gmmIntentUri = Uri.parse("geo:10.8678007,78.882062?q=" + Uri.encode("Arulmigu Vadivazhagiya Nambi Perumal Temple"));
                        break;
                    case "005":
                        //gmmIntentUri = Uri.parse("geo:10.8761911,78.7031901?q="+Uri.encode("VPG3+V8 Tiruchirappalli, Tamil Nadu"));
                        gmmIntentUri = Uri.parse("geo:10.8761911,78.7031901?q=" + Uri.encode("Sri Uthamar Kovil"));
                        break;
                    case "006":
                        //gmmIntentUri = Uri.parse("geo:10.9584559,78.6610257");
                        //gmmIntentUri = Uri.parse("geo:10.9584559,78.6610257?q="+Uri.encode("XM49+93"));
                        gmmIntentUri = Uri.parse("geo:10.9584559,78.6610257?q=" + Uri.encode("Sri Pundarikakshan Perumal Temple"));
                        break;
                    case "007":
                        //gmmIntentUri = Uri.parse("geo:10.9713847,79.3016856");
                        //gmmIntentUri = Uri.parse("geo:10.9713847,79.3016856?q="+Uri.encode("X8C3+H7"));
                        gmmIntentUri = Uri.parse("geo:10.9713847,79.3016856?q=" + Uri.encode("Valvilraman Temple, pullaboothangudi"));
                        break;
                    case "008":
                        //gmmIntentUri = Uri.parse("geo:10.8392752,78.8868842");
                        //gmmIntentUri = Uri.parse("geo:10.8392752,78.8868842?q="+Uri.encode("RVQQ+PJ"));
                        gmmIntentUri = Uri.parse("geo:10.8392752,78.8868842?q=" + Uri.encode("Appakudathan Temple, Thanjavur"));
                        break;
                    case "009":
                        //gmmIntentUri = Uri.parse("geo:10.9762644,79.3125074");
                        //gmmIntentUri = Uri.parse("geo:10.9762644,79.3125074?q="+Uri.encode("X8G7+H9");
                        gmmIntentUri = Uri.parse("geo:10.9762644,79.3125074?q=" + Uri.encode("Sri Aandualkkumayan Perumal Temple"));
                        break;
                    case "010":
                        //gmmIntentUri = Uri.parse("geo:11.046532,79.5772793");
                        //gmmIntentUri = Uri.parse("geo:11.046532,79.5772793?q="+Uri.encode("2HWH+HQ"));
                        gmmIntentUri = Uri.parse("geo:11.046532,79.5772793?q=" + Uri.encode("Sri Devadirajan Temple, Therazendur"));

                        break;
                    case "011":
                        //gmmIntentUri = Uri.parse("geo:10.9912416,79.6686181");
                        //gmmIntentUri = Uri.parse("geo:10.9912416,79.6686181?q="+Uri.encode("XMR9+FQ"));
                        gmmIntentUri = Uri.parse("geo:10.9912416,79.6686181?q=" + Uri.encode("Sirupuliyur Permual Temple,Sirupuliyur"));
                        break;
                    case "012":
                        //gmmIntentUri = Uri.parse("geo:10.877866,79.4460331");
                        //gmmIntentUri = Uri.parse("geo:10.877866,79.4460331?q="+Uri.encode("VFH3+GQ"));
                        gmmIntentUri = Uri.parse("geo:10.877866,79.4460331?q=" + Uri.encode("Saranatha Perumal Temple , Thirucherai"));
                        break;
                    case "013":
                        //gmmIntentUri = Uri.parse("geo:11.1332446,79.7820281");
                        //gmmIntentUri = Uri.parse("geo:11.1332446,79.7820281?q="+Uri.encode("4QPV+X7"));
                        gmmIntentUri = Uri.parse("geo:11.1332446,79.7820281?q=" + Uri.encode("Thalachangadu naanmadhiya Perumal Temple"));
                        break;
                    case "014":
                        //gmmIntentUri = Uri.parse("geo:10.9594697,79.3728103");
                        //gmmIntentUri = Uri.parse("geo:10.9594697,79.3728103?q="+Uri.encode("X95F+QX"));
                        gmmIntentUri = Uri.parse("geo:10.9594697,79.3728103?q=" + Uri.encode("Arulmigu Sarangapani Swamy Temple"));
                        break;
                    case "015":
                        //gmmIntentUri = Uri.parse("geo:10.8591207,79.1080521");
                        //gmmIntentUri = Uri.parse("geo:10.8591207,79.1080521?q="+Uri.encode("V465+3H"));
                        gmmIntentUri = Uri.parse("geo:10.8591207,79.1080521?q=" + Uri.encode("Sri Hara Saabha Vimocchana Perumal Temple, Kandiyur"));
                        break;
                    case "016":
                        //gmmIntentUri = Uri.parse("geo:10.9614854,79.4298828");
                        //gmmIntentUri = Uri.parse("geo:10.9614854,79.4298828?q="+Uri.encode("XC6J+HR"));
                        gmmIntentUri = Uri.parse("geo:10.9614854,79.4298828?q=" + Uri.encode("Arulmigu Oppiliappan Temple, Thirunageswaram, Tamil Nadu"));
                        break;
                    case "017":
                        //gmmIntentUri = Uri.parse("geo:10.8674457,79.6947384");
                        //gmmIntentUri = Uri.parse("geo:10.8674457,79.6947384?q="+Uri.encode("VP94+H6"));
                        gmmIntentUri = Uri.parse("geo:10.8674457,79.6947384?q=" + Uri.encode("Sri Sowriraja Perumal, Thirukkannapuram, Tamil Nadu"));
                        break;
                    case "018":
                        //gmmIntentUri = Uri.parse("geo:11.2050422,79.7746665");
                        //gmmIntentUri = Uri.parse("geo:11.2050422,79.7746665?q="+Uri.encode("6Q3F+5Q"));
                        gmmIntentUri = Uri.parse("geo:11.2050422,79.7746665?q=" + Uri.encode("DD34b - Sri Lakshmi Narasimhar Temple"));
                        break;
                    case "019":
                        //gmmIntentUri = Uri.parse("geo:10.7597338,79.8341482");
                        //gmmIntentUri = Uri.parse("geo:10.7597338,79.8341482?q="+Uri.encode("QR6R+74"));
                        gmmIntentUri = Uri.parse("geo:10.7597338,79.8341482?q=" + Uri.encode("Soundararaja Perumal Temple, Nagapattinam, Tamil Nadu"));
                        break;
                    case "020":
                        //gmmIntentUri = Uri.parse("geo:10.9193101,79.4357588");
                        //gmmIntentUri = Uri.parse("geo:10.9193101,79.4357588?q="+Uri.encode("WC8W+86"));
                        gmmIntentUri = Uri.parse("geo:10.9193101,79.4357588?q=" + Uri.encode("Kal Garudan temple, Tirunarayur Perumal Temple, Nachiyar Koil"));
                        break;
                    case "021":
                        //gmmIntentUri = Uri.parse("geo:10.9220726,79.3678109");
                        //gmmIntentUri = Uri.parse("geo:10.9220726,79.3678109?q="+Uri.encode("W9CC+RV"));
                        gmmIntentUri = Uri.parse("geo:10.9220726,79.3678109?q=" + Uri.encode("Nathankoil, Nandipura Vinnagaram, Tamil Nadu"));
                        break;
                    case "022":
                        //gmmIntentUri = Uri.parse("geo:10.8216827,78.88057");
                        //gmmIntentUri = Uri.parse("geo:10.8216827,78.88057?q="+Uri.encode("4J5W+WF"));
                        gmmIntentUri = Uri.parse("geo:10.8216827,78.88057?q=" + Uri.encode("Arulmigu Parimala Ranganathar Temple"));
                        break;
                    case "023":
                        //gmmIntentUri = Uri.parse("geo:11.3991275,79.6921652");
                        //gmmIntentUri = Uri.parse("geo:11.3991275,79.6921652?q="+Uri.encode("9MXV+M8"));
                        gmmIntentUri = Uri.parse("geo:11.3991275,79.6921652?q=" + Uri.encode("Thiruchitrakoodam Sri Govindaraja Perumal Temple, Chidambaram, Tamil Nadu"));
                        break;
                    case "024":
                        //gmmIntentUri = Uri.parse("geo:11.2399313,79.7276986");
                        //gmmIntentUri = Uri.parse("geo:11.2399313,79.7276986?q="+Uri.encode("6PRJ+9M"));
                        gmmIntentUri = Uri.parse("geo:11.2399313,79.7276986?q=" + Uri.encode("Thadalan Temple Divyadesam, Sirkazhi, Tamil Nadu"));
                        break;
                    case "025":
                        //gmmIntentUri = Uri.parse("geo:10.925379,79.203757");
                        //gmmIntentUri = Uri.parse("geo:10.925379,79.203757?q="+Uri.encode("W6G3+3C"));
                        gmmIntentUri = Uri.parse("geo:10.925379,79.203757?q=" + Uri.encode("Sri Jagatharakshaga Perumal, thirukoodalur"));
                        break;
                    case "026":
                        //gmmIntentUri = Uri.parse("geo:10.757222,79.7611013");
                        //gmmIntentUri = Uri.parse("geo:10.757222,79.7611013?q="+Uri.encode("QQ47+J7"));
                        gmmIntentUri = Uri.parse("geo:10.757222,79.7611013?q=" + Uri.encode("Sri Syamalameni Perumal Koil, Thirukkanangudi, Tamil Nadu"));
                        break;
                    case "027":
                        //gmmIntentUri = Uri.parse("geo:10.799689,79.5836252");
                        //gmmIntentUri = Uri.parse("geo:10.799689,79.5836252?q="+Uri.encode("QHXP+RM"));
                        gmmIntentUri = Uri.parse("geo:10.799689,79.5836252?q=" + Uri.encode("Sri Bhaktavatsala Perumal Temple, Thirukkannamangai, Tamil Nadu"));
                        break;
                    case "028":
                        //gmmIntentUri = Uri.parse("geo:10.9473538,79.24971");
                        //gmmIntentUri = Uri.parse("geo:10.9473538,79.24971?q="+Uri.encode("W7W4+QJ"));
                        gmmIntentUri = Uri.parse("geo:10.9473538,79.24971?q=" + Uri.encode("Sri Gajendra Varadha Perumal Temple Divyadesam, Kabisthalam, Tamil Nadu"));
                        break;
                    case "029":
                        //gmmIntentUri = Uri.parse("geo:11.0568568,79.4355271");
                        //gmmIntentUri = Uri.parse("geo:11.0568568,79.4355271?q="+Uri.encode("3C4V+PF"));
                        gmmIntentUri = Uri.parse("geo:11.0568568,79.4355271?q=" + Uri.encode("Thiruvelliyangudi Kola Villi Ramar - Sukhra Sthalam"));
                        break;
                    case "030":
                        //gmmIntentUri = Uri.parse("geo:11.173971,79.7746833");
                        //gmmIntentUri = Uri.parse("geo:11.173971,79.7746833?q="+Uri.encode("5QFG+HP"));
                        gmmIntentUri = Uri.parse("geo:11.173971,79.7746833?q=" + Uri.encode("Thirumanimadakoil, Nangur, Tamil Nadu"));
                        break;
                    case "031":
                        //gmmIntentUri = Uri.parse("geo:11.179804,79.7760783");
                        //gmmIntentUri = Uri.parse("geo:11.179804,79.7760783?q="+Uri.encode("5QHH+W8"));
                        gmmIntentUri = Uri.parse("geo:11.179804,79.7760783?q=" + Uri.encode("Vaikundanathar Temple, North Agraharam, Nangur, Tamil Nadu"));
                        break;
                    case "032":
                        //gmmIntentUri = Uri.parse("geo:11.1749062,79.7767851");
                        //gmmIntentUri = Uri.parse("geo:11.1749062,79.7767851?q="+Uri.encode("5QFH+XH"));
                        gmmIntentUri = Uri.parse("geo:11.175148, 79.779500?q=" + Uri.encode("Thiru Arimeya Vinnagaram, Nangur, Tamil Nadu"));
                        break;
                    case "033":
                        //gmmIntentUri = Uri.parse("geo:11.196842,79.7733153");
                        //gmmIntentUri = Uri.parse("geo:11.196842,79.7733153?q="+Uri.encode("5QWG+P6"));
                        gmmIntentUri = Uri.parse("geo:11.196842,79.7733153?q=" + Uri.encode("Deiva Nayaka Perumal Temple, Keezhachalai, Tamil Nadu"));
                        break;
                    case "034":
                        //gmmIntentUri = Uri.parse("geo:11.178813, 79.776688");
                        //gmmIntentUri = Uri.parse("geo:11.178813, 79.776688?q="+Uri.encode("5QHG+GM"));
                        gmmIntentUri = Uri.parse("geo:11.178813, 79.776688?q=" + Uri.encode("Van Purushotaman Temple Divyadesam, Nangur, Tamil Nadu"));
                        break;
                    case "035":
                        //gmmIntentUri = Uri.parse("geo:11.1787597,79.7745287");
                        //gmmIntentUri = Uri.parse("geo:11.1787597,79.7745287?q="+Uri.encode("5QHH+9V"));
                        gmmIntentUri = Uri.parse("geo:11.1787597,79.7745287?q=" + Uri.encode("Sempon Arangar Temple, Nangur, Tamil Nadu"));
                        break;
                    case "036":
                        //gmmIntentUri = Uri.parse("geo:11.176563, 79.777437");
                        //gmmIntentUri = Uri.parse("geo:11.176563, 79.777437?q="+Uri.encode("5QGG+JX"));
                        gmmIntentUri = Uri.parse("geo:11.176563, 79.777437?q=" + Uri.encode("Pallikonda Perumal Temple, Nangur, Tamil Nadu"));
                        break;
                    case "037":
                        //gmmIntentUri = Uri.parse("geo:11.1797679,79.7860465");
                        //gmmIntentUri = Uri.parse("geo:11.1797679,79.7860465?q="+Uri.encode("5QHQ+W7"));
                        gmmIntentUri = Uri.parse("geo:11.1797679,79.7860465?q=" + Uri.encode("Thirumanikkoodam, Nangur, Tamil Nadu"));
                        break;
                    case "038":
                        //gmmIntentUri = Uri.parse("geo:11.1769277,79.7802844");
                        //gmmIntentUri = Uri.parse("geo:11.1769277,79.7802844?q="+Uri.encode("5QHH+2P"));
                        gmmIntentUri = Uri.parse("geo:11.1769277,79.7802844?q=" + Uri.encode("Sri Gopala Krishna Perumal Temple (Thirukkavalampaadi), Nadu Street, Nangur, Tamil Nadu"));
                        break;
                    case "039":
                        //gmmIntentUri = Uri.parse("geo:11.190232,79.7577838");
                        //gmmIntentUri = Uri.parse("geo:11.190232,79.7577838?q="+Uri.encode("5QR7+2W"));
                        gmmIntentUri = Uri.parse("geo:11.190232,79.7577838?q=" + Uri.encode("Annan Perumal Temple, Annankoil, Tamil Nadu"));
                        break;
                    case "040":
                        //gmmIntentUri = Uri.parse("geo:11.1694419,79.7853381");
                        //gmmIntentUri = Uri.parse("geo:11.1694419,79.7853381?q="+Uri.encode("5Q9W+XW"));
                        gmmIntentUri = Uri.parse("geo:11.1694419,79.7853381?q=" + Uri.encode("Parthanpalli Perumal Temple, Parthanpalli, Tamil Nadu"));
                        break;
                    case "041":
                        //gmmIntentUri = Uri.parse("geo:10.0744153,78.1987761");
                        //gmmIntentUri = Uri.parse("geo:10.0744153,78.1987761?q="+Uri.encode("MFR5+5F"));
                        gmmIntentUri = Uri.parse("geo:10.0744153,78.1987761?q=" + Uri.encode("Alagar Kovil, Manamadurai, Tamil Nadu"));
                        break;
                    case "042":
                        //gmmIntentUri = Uri.parse("geo:10.0610884,78.5586324");
                        //gmmIntentUri = Uri.parse("geo:10.0610884,78.5586324?q="+Uri.encode("3H66+C8"));
                        gmmIntentUri = Uri.parse("geo:10.0610884,78.5586324?q=" + Uri.encode("Thirukkoshtiyur Sowmiya Narayana Perumal Temple, Tirukostiyur, Tamil Nadu"));
                        break;
                    case "043":
                        //gmmIntentUri = Uri.parse("geo:10.2466933,78.7512678");
                        //gmmIntentUri = Uri.parse("geo:10.2466933,78.7512678?q="+Uri.encode("6QW2+QP"));
                        gmmIntentUri = Uri.parse("geo:10.2466933,78.7512678?q=" + Uri.encode("Sri Sathyagiri Natha Perumal Temple, Thirumayam, Tamil Nadu"));
                        break;
                    case "044":
                        //gmmIntentUri = Uri.parse("geo:9.2828381,78.8228917");
                        //gmmIntentUri = Uri.parse("geo:9.2828381,78.8228917?q="+Uri.encode("7RMF+5X"));
                        gmmIntentUri = Uri.parse("geo:9.2828381,78.8228917?q=" + Uri.encode("Thiruppullani Sri Kalyana Jagannatha Perumal Temple, Thiruppullani, Tamil Nadu"));
                        break;
                    case "045":
                        //gmmIntentUri = Uri.parse("geo:9.479969,77.7914443");
                        //gmmIntentUri = Uri.parse("geo:9.479969,77.7914443?q="+Uri.encode("FRJ6+PJ"));
                        gmmIntentUri = Uri.parse("geo:9.479969,77.7914443?q=" + Uri.encode("DD100 Thiruthangalappan Perumal Kovil, Divya Desam, Thiruthangal, Sivakasi, Tamil Nadu"));
                        break;
                    case "046":
                        //gmmIntentUri = Uri.parse("geo:9.9499908,78.2043904");
                        //gmmIntentUri = Uri.parse("geo:9.9499908,78.2043904?q="+Uri.encode("X624+9V"));
                        gmmIntentUri = Uri.parse("geo:9.9499908,78.2043904?q=" + Uri.encode("Thirumohoor Kalameghaperumal Temple, Thirumohur, Tamil Nadu"));
                        break;
                    case "047":
                        //gmmIntentUri = Uri.parse("geo:9.914401,78.1119179");
                        //gmmIntentUri = Uri.parse("geo:9.914401,78.1119179?q="+Uri.encode("W477+QJ"));
                        gmmIntentUri = Uri.parse("geo:9.914401,78.1119179?q=" + Uri.encode("Koodal Alagar Temple, Madurai, Tamil Nadu"));
                        break;
                    case "048":
                        //gmmIntentUri = Uri.parse("geo:9.5082963,77.6297757");
                        //gmmIntentUri = Uri.parse("geo:9.5082963,77.6297757?q="+Uri.encode("GJ5J+88"));
                        gmmIntentUri = Uri.parse("geo:9.5082963,77.6297757?q=" + Uri.encode("Srivilliputtur Andal Temple, Srivilliputhur, Tamil Nadu"));
                        break;
                    case "049":
                        //gmmIntentUri = Uri.parse("geo:8.6072417,77.9358017");
                        //gmmIntentUri = Uri.parse("geo:8.6072417,77.9358017?q="+Uri.encode("JW4Q+V5"));
                        gmmIntentUri = Uri.parse("geo:8.6072417,77.9358017?q=" + Uri.encode("Alwarthirunagari Temple, Tuticorin, Tamil Nadu"));
                        break;
                    case "050":
                        //gmmIntentUri = Uri.parse("geo:8.6103255,77.9706641");
                        gmmIntentUri = Uri.parse("geo:8.6103255,77.9706641?q=" + Uri.encode("Thirutholaivillimangalam Irettai Thirupathi Sri Srinivasa Perumal Temple"));
                        break;
                    case "051":
                        //gmmIntentUri = Uri.parse("geo:8.4918852,77.6557895");
                        gmmIntentUri = Uri.parse("geo:8.4918852,77.6557895?q=" + Uri.encode("Arulmigu Vanamamalai Perumal Temple, State Highway 89, Kamaraj Nagar, Nanguneri, Tamil Nadu"));
                        break;
                    case "052":
                        //gmmIntentUri = Uri.parse("geo:8.6397652,77.930612");
                        gmmIntentUri = Uri.parse("geo:8.6397652,77.930612?q=" + Uri.encode("ThiruPulingudi Perumal Temple, Thiruppuliangudi, Tamil Nadu"));
                        break;
                    case "053":
                        //gmmIntentUri = Uri.parse("geo:8.6038155,77.9847067");
                        gmmIntentUri = Uri.parse("geo:8.6038155,77.9847067?q=" + Uri.encode("ThenThirupperai Thiru Makara Nedunkulai Kaather Perumal Temple, Then Thirupperai, Tamil Nadu"));
                        break;
                    case "054":
                        //gmmIntentUri = Uri.parse("geo:8.6310022,77.904246");
                        gmmIntentUri = Uri.parse("geo:8.6310022,77.904246?q=" + Uri.encode("Sri Vaikuntanathan Perumal Temple, Srivaikuntam, Tamil Nadu"));
                        break;
                    case "055":
                        //gmmIntentUri = Uri.parse("geo:8.6370669,77.9219663");
                        gmmIntentUri = Uri.parse("geo:8.6370669,77.9219663?q=" + Uri.encode("ThiruVaragunamangai Perumal Temple, Tamil Nadu"));
                        break;
                    case "056":
                        //gmmIntentUri = Uri.parse("geo:8.6418073,77.9917502");
                        gmmIntentUri = Uri.parse("geo:8.6415287,77.9929338?q=" + Uri.encode("Navathirupathi 6, Major District Road 435, Perunkulam, Tamil Nadu"));
                        break;
                    case "057":
                        //gmmIntentUri = Uri.parse("geo:8.4354726,77.5639157");
                        gmmIntentUri = Uri.parse("geo:8.4354726,77.5639157?q=" + Uri.encode("Sri Vaishnava Nambi Temple, Thirukkurungudi, Tamil Nadu"));
                        break;
                    case "058":
                        //gmmIntentUri = Uri.parse("geo:8.5967365,77.9554361");
                        gmmIntentUri = Uri.parse("geo:8.5967365,77.9554361?q=" + Uri.encode("Vaithamanidhi Perumal Temple, Thirukkolur, Tamil Nadu"));
                        break;
                    case "059":
                        //gmmIntentUri = Uri.parse("geo:8.4827779,76.9414019");
                        gmmIntentUri = Uri.parse("geo:8.4827779,76.9414019?q=" + Uri.encode("Sree Padmanabhaswamy Temple, West Nada, East Fort, Pazhavangadi, Thiruvananthapuram, Kerala"));
                        break;
                    case "060":
                        //gmmIntentUri = Uri.parse("geo:8.2046569,77.4450946");
                        gmmIntentUri = Uri.parse("geo:8.2046569,77.4450946?q=" + Uri.encode("Thiruvazhimarban Temple,Thiruppathisaram, Thiruppathisaram, Tamil Nadu"));
                        break;
                    case "061":
                        //gmmIntentUri = Uri.parse("geo:10.0354513,76.3272997");
                        gmmIntentUri = Uri.parse("geo:10.0354513,76.3272997?q=" + Uri.encode("Thrikkakkara Vamana Moorthy Temple, Kochi, Kerala"));
                        break;
                    case "062":
                        //gmmIntentUri = Uri.parse("geo:10.1878006,76.3248854");
                        gmmIntentUri = Uri.parse("geo:10.1878006,76.3248854?q=" + Uri.encode("Thirumoozhikkulam Sree Lakshmana Perumal Temple, Govt UP School, Kurumaseri, Kerala"));
                        break;
                    case "063":
                        //gmmIntentUri = Uri.parse("geo:9.3023464,76.5781402");
                        gmmIntentUri = Uri.parse("geo:9.3023464,76.5781402?q=" + Uri.encode("Thripuliyoor Mahavishnu Temple, Chengannur Kollakadavu Road, Puliyoor, Kerala"));
                        break;
                    case "064":
                        //gmmIntentUri = Uri.parse("geo:9.3265441,76.6020495");
                        gmmIntentUri = Uri.parse("geo:9.3265441,76.6020495?q=" + Uri.encode("Thrichittattu Mahavishnu Temple, Chengannur, Kerala"));
                        break;
                    case "065":
                        //gmmIntentUri = Uri.parse("geo:10.8638482,75.9810183");
                        gmmIntentUri = Uri.parse("geo:10.8638482,75.9810183?q=" + Uri.encode("Thirunavaya Nava Mukunda Temple, Thirunavaya, Kerala"));
                        break;
                    case "066":
                        //gmmIntentUri = Uri.parse("geo:9.3737242,76.5601926");
                        gmmIntentUri = Uri.parse("geo:9.374084, 76.562403?q=" + Uri.encode("Sree Vallabha Temple, Thiruvalla, Kerala"));
                        break;
                    case "067":
                        //gmmIntentUri = Uri.parse("geo:9.3445467,76.571145");
                        gmmIntentUri = Uri.parse("geo:9.3445467,76.571145?q=" + Uri.encode("Thiruvanvandoor Mahavishnu Temple, Temple Road, Thiruvanvandoor, Kerala"));
                        break;
                    case "068":
                        //gmmIntentUri = Uri.parse("geo:8.3308999,77.2633241");
                        gmmIntentUri = Uri.parse("geo:8.3308999,77.2633241?q=" + Uri.encode("Arulmigu Adikesava Perumal Temple, Thiruvattaru, Tamil Nadu"));
                        break;
                    case "069":
                        //gmmIntentUri = Uri.parse("geo:9.3453992,76.5746961");
                        gmmIntentUri = Uri.parse("geo:9.3453992,76.5746961?q=" + Uri.encode("Tirumittacode Uyyavantha Perumal temple, Thirumittacode, Kerala"));
                        break;
                    case "070":
                        //gmmIntentUri = Uri.parse("geo:9.4383021,76.5615105");
                        gmmIntentUri = Uri.parse("geo:9.4383021,76.5615105?q=" + Uri.encode("Thrikodithanam Mahavishnu Kshetram, Changanassery, Kerala"));
                        break;
                    case "071":
                        //gmmIntentUri = Uri.parse("geo:9.3280563,76.6856423");
                        gmmIntentUri = Uri.parse("geo:9.3280563,76.6856423?q=" + Uri.encode("Thiru Aranmula Parthasarathy Temple, Mavelikkara - Chengannur - Kozhenchery Road, Mallapuzhassery, Kerala"));
                        break;
                    case "072":
                        //gmmIntentUri = Uri.parse("geo:11.745002,79.7070921");
                        gmmIntentUri = Uri.parse("geo:11.745002,79.7070921?q=" + Uri.encode("Thiruvahindrapuram Temple, Thiruvandipuram, Tamil Nadu"));
                        break;
                    case "073":
                        //gmmIntentUri = Uri.parse("geo:11.9662904,79.2030874");
                        gmmIntentUri = Uri.parse("geo:11.9662904,79.2030874?q=" + Uri.encode("Thirukovalur Thiru Vikraman Temple, Thirukoilure, Tamil Nadu"));
                        break;
                    case "074":
                        //gmmIntentUri = Uri.parse("geo:12.8191263,79.7216852");
                        gmmIntentUri = Uri.parse("geo:12.8191263,79.7216852?q=" + Uri.encode("Sri Varadharaja Perumal Temple, Kanchipuram, Tamil Nadu"));
                        break;
                    case "075":
                        //gmmIntentUri = Uri.parse("geo:12.8226828,79.7086126");
                        gmmIntentUri = Uri.parse("geo:12.8226828,79.7086126?q=" + Uri.encode("Sri Ashtabuja Perumal Temple, Chinna Kanchipuram, Ennaikaran, Kanchipuram, Tamil Nadu"));
                        break;
                    case "076":
                        //gmmIntentUri = Uri.parse("geo:12.8244544,79.7043318");
                        gmmIntentUri = Uri.parse("geo:12.8244544,79.7043318?q=" + Uri.encode("Vilakoli Perumal Temple, Vilakadi Koil Street, Ennaikaran, Kanchipuram, Tamil Nadu"));
                        break;
                    case "077":
                        //gmmIntentUri = Uri.parse("geo:12.8221867,79.7042937");
                        gmmIntentUri = Uri.parse("geo:12.8221867,79.7042937?q=" + Uri.encode("Sri Tiruvelukkai Sri Azhagiya SingaPerumal Temple, Singaperumal Sannidhi Street, Ennaikaran, Kanchipuram, Tamil Nadu"));
                        break;
                    case "078":
                        //gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627");
                        gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627?q=" + Uri.encode("Pandava Thoodha Perumal Temple, Pandavaperumal Koil Street, Periya, Kanchipuram, Tamil Nadu"));
                        break;
                    case "079":
                        //gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627");
                        gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627?q=" + Uri.encode("Pandava Thoodha Perumal Temple, Pandavaperumal Koil Street, Periya, Kanchipuram, Tamil Nadu"));
                        break;
                    case "080":
                        //gmmIntentUri = Uri.parse("geo:12.8472899,79.6973725");
                        gmmIntentUri = Uri.parse("geo:12.8472899,79.6973725?q=" + Uri.encode("Thiru Ekambaranathar Temple, Kanchipuram, Tamil Nadu"));
                        break;
                    case "081":
                        //gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627");
                        gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627?q=" + Uri.encode("Thiru Ooraga Perumal Temple, Murugan Koil Road, Devi Nagar, Kundrathur, Sikkarayapuram, Tamil Nadu"));
                        break;
                    case "082":
                        //gmmIntentUri = Uri.parse("geo:12.8240753,79.7102791");
                        gmmIntentUri = Uri.parse("geo:12.8240753,79.7102791?q=" + Uri.encode("Yadhothakari Kovil, Ennaikaran, Kanchipuram, Tamil Nadu"));
                        break;
                    case "083":
                        //gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627");
                        gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627?q=" + Uri.encode("Pandava Thoodha Perumal Temple, Pandavaperumal Koil Street, Periya, Kanchipuram, Tamil Nadu"));
                        break;
                    case "084":
                        //gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627");
                        gmmIntentUri = Uri.parse("geo:12.8427151,79.6947627?q=" + Uri.encode("Pandava Thoodha Perumal Temple, Pandavaperumal Koil Street, Periya, Kanchipuram, Tamil Nadu"));
                        break;
                    case "085":
                        //gmmIntentUri = Uri.parse("geo:12.8406997,79.7010309");
                        gmmIntentUri = Uri.parse("geo:12.8406997,79.7010309?q=" + Uri.encode("Sri kamakshi amman temple, Periya, Kanchipuram, Tamil Nadu"));
                        break;
                    case "086":
                        //gmmIntentUri = Uri.parse("geo:12.8436685,79.7054579");
                        gmmIntentUri = Uri.parse("geo:12.8436685,79.7054579?q=" + Uri.encode("Pavalavannaperumal temple, Thiruveethipallam, Kanchipuram, Tamil Nadu"));
                        break;
                    case "087":
                        //gmmIntentUri = Uri.parse("geo:12.8339781,79.7028987");
                        gmmIntentUri = Uri.parse("geo:12.8339781,79.7028987?q=" + Uri.encode("Vaikundanathar Temple, North Agraharam, Nangur, Tamil Nadu"));
                        break;
                    case "088":
                        //gmmIntentUri = Uri.parse("geo:12.8726207,79.616526");
                        gmmIntentUri = Uri.parse("geo:12.8726207,79.616526?q=" + Uri.encode("Sri Vijayaraghava Perumal Thirukovil, Thiruppukuzhi, Tamil Nadu"));
                        break;
                    case "089":
                        //gmmIntentUri = Uri.parse("geo:13.1122929,80.0247869");
                        gmmIntentUri = Uri.parse("geo:13.1122929,80.0247869?q=" + Uri.encode("Sri Ennai Petra Thayaar Samedha Bhaktavatsala Perumal Temple, Thiruninravur, Tamil Nadu"));
                        break;
                    case "090":
                        //gmmIntentUri = Uri.parse("geo:13.1432373,79.905265");
                        gmmIntentUri = Uri.parse("geo:13.1432373,79.905265?q=" + Uri.encode("Sri Vaidhya Veeraraghava Swamy Temple, Tiruvallur, Tamil Nadu"));
                        break;
                    case "091":
                        //gmmIntentUri = Uri.parse("geo:12.9637629,80.1124334");
                        gmmIntentUri = Uri.parse("geo:12.9637629,80.1124334?q=" + Uri.encode("Tiruneermalai - Divya Desam 61, Tiruneermalai, Chennai, Tamil Nadu"));
                        break;
                    case "092":
                        //gmmIntentUri = Uri.parse("geo:12.7559186,80.2229847");
                        gmmIntentUri = Uri.parse("geo:12.7559186,80.2229847?q=" + Uri.encode("Nithya Kalyana Perumal Temple"));
                        break;
                    case "093":
                        //gmmIntentUri = Uri.parse("geo:12.6223248,80.1561225");
                        gmmIntentUri = Uri.parse("geo:12.6223248,80.1561225?q=" + Uri.encode("Sthala Sayana Perumal Temple, South Mada Street, Mahabalipuram, Tamil Nadu"));
                        break;
                    case "094":
                        //gmmIntentUri = Uri.parse("geo:13.0537413,80.2749809");
                        gmmIntentUri = Uri.parse("geo:13.0537413,80.2749809?q=" + Uri.encode("Arulmighu Parthasarathyswamy Temple, Narayana Krishnaraja Puram, Triplicane, Chennai, Tamil Nadu"));
                        break;
                    case "095":
                        //gmmIntentUri = Uri.parse("geo:13.1011936,79.4070825");
                        gmmIntentUri = Uri.parse("geo:13.1011936,79.4070825?q=" + Uri.encode("Sholinghur Narasimha Temple, Sholinghur, Tamil Nadu"));
                        break;
                    case "096":
                        //gmmIntentUri = Uri.parse("geo:13.6830435,79.3463009");
                        gmmIntentUri = Uri.parse("geo:13.6830435,79.3463009?q=" + Uri.encode("Sri Venkateswara Swamy Vaari Temple, S Mada St, Tirumala, Tirupati, Andhra Pradesh"));
                        break;
                    case "097":
                        //gmmIntentUri = Uri.parse("geo:15.1155895,78.6706802");
                        gmmIntentUri = Uri.parse("geo:15.1155895,78.6706802?q=" + Uri.encode("Diguva Ahobila Lakshmi Narasimha Swamy Temple, Ahobilam, Andhra Pradesh"));
                        break;
                    case "098":
                        //gmmIntentUri = Uri.parse("geo:26.7148123,82.8205362");
                        gmmIntentUri = Uri.parse("geo:26.7148123,82.8205362?q=" + Uri.encode("Ram Janmabhoomi-Babri Masjid, Sai Nagar, Ayodhya, Uttar Pradesh"));
                        break;
                    case "099":
                        //gmmIntentUri = Uri.parse("geo:27.4379317,80.5482634");
                        gmmIntentUri = Uri.parse("geo:27.4379317,80.5482634?q=" + Uri.encode("Chakra Teerth, Misrikh-cum-Neemsar, Uttar Pradesh"));
                        break;
                    case "100":
                        //gmmIntentUri = Uri.parse("geo:28.8166048,83.8692402");
                        gmmIntentUri = Uri.parse("geo:28.8166048,83.8692402?q=" + Uri.encode("Muktinath Temple, Ranipauwa, Nepal"));
                        break;
                    case "101":
                        //gmmIntentUri = Uri.parse("geo:30.7417338,79.4852149");
                        gmmIntentUri = Uri.parse("geo:30.7417338,79.4852149?q=" + Uri.encode("Badrinath Temple, Badrinath, Uttarakhand"));
                        break;
                    case "102":
                        //gmmIntentUri = Uri.parse("geo:30.1456004,78.5957502");
                        gmmIntentUri = Uri.parse("geo:30.1456004,78.5957502?q=" + Uri.encode("Shri Neelamega Perumal Temple, NH 58, Devaprayag, Uttarakhand"));
                        break;
                    case "103":
                        //gmmIntentUri = Uri.parse("geo:30.5551441,79.5624624");
                        gmmIntentUri = Uri.parse("geo:30.5551441,79.5624624?q=" + Uri.encode("Narsingh Mandir, Joshimath, Uttarakhand"));
                        break;
                    case "104":
                        //gmmIntentUri = Uri.parse("geo:22.2376336,68.9652169");
                        gmmIntentUri = Uri.parse("geo:22.2376336,68.9652169?q=" + Uri.encode("Dwarkadhish Temple, Dwarka, Gujarat"));
                        break;
                    case "105":
                        //gmmIntentUri = Uri.parse("geo:27.4990838,77.649163");
                        gmmIntentUri = Uri.parse("geo:27.4990838,77.649163?q=" + Uri.encode("Shri Krishna Janmasthan Temple, Mathura, Uttar Pradesh"));
                        break;
                    case "106":
                        //gmmIntentUri = Uri.parse("geo:26.7207197,82.3464552");
                        gmmIntentUri = Uri.parse("geo:26.7207197,82.3464552?q=" + Uri.encode("Gokul Dham, Gokul, Mahaban Bangar, Uttar Pradesh"));
                        break;
                    case "107":
                        //gmmIntentUri = Uri.parse("geo:13.6816393,79.3421028");
                        gmmIntentUri = Uri.parse("geo:13.6830435,79.3463009?q=" + Uri.encode("Sri Venkateswara Swamy Vaari Temple, S Mada St, Tirumala, Tirupati, Andhra Pradesh"));
                        break;
                    case "108":
                        //gmmIntentUri = Uri.parse("geo:13.6816393,79.3421028");
                        gmmIntentUri = Uri.parse("geo:13.6830435,79.3463009?q=" + Uri.encode("Sri Venkateswara Swamy Vaari Temple, S Mada St, Tirumala, Tirupati, Andhra Pradesh"));
                        break;


                    //
                    // Azhwaargal Birth place
                    case "1":
                        //gmmIntentUri = Uri.parse("geo:12.8240753,79.7102791");
                        //gmmIntentUri = Uri.parse("geo:12.8240753,79.7102791?q="+Uri.encode("RPF6+JX"));
                        gmmIntentUri = Uri.parse("geo:12.8240753,79.7102791?q=" + Uri.encode("Sonnavannam Seitha Perumal Temple, Ennaikaran, Kanchipuram, Tamil Nadu"));
                        break;
                    case "2":
                        //gmmIntentUri = Uri.parse("geo:12.617809, 80.193282");
                        gmmIntentUri = Uri.parse("geo:12.617809, 80.193282?q=" + Uri.encode("Sthala Sayana Perumal Temple, South Mada Street, Mahabalipuram, Tamil Nadu"));
                        break;
                    case "3":
                        //gmmIntentUri = Uri.parse("geo:13.038053, 80.271866");
                        gmmIntentUri = Uri.parse("geo:13.038053, 80.271866?q=" + Uri.encode("Sri Adikesava Perumal Peyalvar Temple, Alamelu Manga Puram, Sankarapuram, Mylapore, Chennai, Tamil Nadu"));
                        break;
                    case "4":
                        //gmmIntentUri = Uri.parse("geo:13.052653, 80.061390");
                        gmmIntentUri = Uri.parse("geo:13.052653, 80.061390?q=" + Uri.encode("Thirumazhisai Alwar Temple, Thirumazhisai, Tamil Nadu"));
                        break;
                    case "5":
                        //gmmIntentUri = Uri.parse("geo:8.607613, 77.937969");
                        gmmIntentUri = Uri.parse("geo:8.607613, 77.937969?q=" + Uri.encode("Alwarthirunagari Temple, Tuticorin, Tamil Nadu"));
                        break;
                    case "6":
                        //gmmIntentUri = Uri.parse("geo:8.596800, 77.957620");
                        gmmIntentUri = Uri.parse("geo:8.596800, 77.957620?q=" + Uri.encode("Thirukkolur Perumal Kovil, Thirukkolur, Tamil Nadu"));
                        break;
                    case "7":
                        //gmmIntentUri = Uri.parse("geo:10.215975, 76.207158");
                        gmmIntentUri = Uri.parse("geo:10.215975, 76.207158?q=" + Uri.encode("Thiruvanchikulam Mahadeva Temple, Sringapuram, Thrissur, Kerala"));
                        break;
                    case "8":
                        //gmmIntentUri = Uri.parse("geo:9.5082963,77.6305964");
                        gmmIntentUri = Uri.parse("geo:9.5082963,77.6305964?q=" + Uri.encode("Srivilliputtur Andal Temple, Srivilliputhur, Tamil Nadu"));
                        break;
                    case "9":
                        //gmmIntentUri = Uri.parse("geo:9.5082963,77.6305964");
                        gmmIntentUri = Uri.parse("geo:9.5082963,77.6305964?q=" + Uri.encode("Srivilliputtur Andal Temple, Srivilliputhur, Tamil Nadu"));
                        break;
                    case "10":
                        //gmmIntentUri = Uri.parse("geo:10.8293156,78.6701353");
                        gmmIntentUri = Uri.parse("geo:10.8293156,78.6701353?q=" + Uri.encode("AZHWAR KOIL, Thirumandangudi, Tamil Nadu"));
                        break;
                    case "11":
                        //gmmIntentUri = Uri.parse("geo:10.8293156,78.6701353");
                        gmmIntentUri = Uri.parse("geo:10.8293156,78.6701353?q=" + Uri.encode("Uraiyoor, Tiruchirappalli, Tamil Nadu"));
                        break;
                    case "12":
                        //gmmIntentUri = Uri.parse("geo:11.2255556,79.7972557");
                        gmmIntentUri = Uri.parse("geo:11.2255556,79.7972557?q=" + Uri.encode("Sri Ugra Narasimmar Temple Thirukuraiyaloor"));
                        break;


                    default:
                        //Uri.encode("M8MW+8X Tirupati, Andhra Pradesh");
                        //gmmIntentUri = Uri.parse( "geo:13.6816393,79.3421028?q="+Uri.encode("M8MW+8X Tirupati, Andhra Pradesh"));
                        gmmIntentUri = Uri.parse("geo:13.6830435,79.3463009?q=" + Uri.encode("Sri Venkateswara Swamy Vaari Temple, S Mada St, Tirumala, Tirupati, Andhra Pradesh"));
                        break;
                }
                //gmmIntentUri = Uri.parse("geo:10.862391,78.6880503");

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Preparing to launch Map", Snackbar.LENGTH_SHORT);

                snackbar.show();

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                //mapIntent.addCategory("Divyadesam");

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    snackbar = Snackbar
                            .make(coordinatorLayout, "Starting Map", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    startActivity(mapIntent);

                    snackbar = Snackbar
                            .make(coordinatorLayout, "Map launched.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else
                    snackbar = Snackbar
                            .make(coordinatorLayout, "Unable to launch Map.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }


            return true;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareText() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //lv.getFirstVisiblePosition();

        //String shareString = fillList[(int) adapter.getItemId(lv.getFirstVisiblePosition())];//String.valueOf(adapter.getItemId(current_location));
        String shareString = fillList[(int) adapter.getItemId(lv.getFirstVisiblePosition())];
        String footerString = "Shared from திவ்யபிரபந்தம் Android App, Download from https://goo.gl/LcEZQ2 or https://goo.gl/k1iSq8";
        share.putExtra(Intent.EXTRA_SUBJECT, "Message from Vaishnavism.eClass@gmail.com");
        share.putExtra(Intent.EXTRA_TEXT, shareString + "\n\n" + footerString);

        startActivity(Intent.createChooser(share, "Share திவ்யபிரபந்தம்"));
    }

}