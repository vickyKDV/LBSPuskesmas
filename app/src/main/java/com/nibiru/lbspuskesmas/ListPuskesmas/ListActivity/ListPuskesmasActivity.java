package com.nibiru.lbspuskesmas.ListPuskesmas.ListActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity.MapsActivity;
import com.nibiru.lbspuskesmas.R;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.app.SearchManager;
import android.widget.EditText;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;


public class ListPuskesmasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    private Toolbar toolbar;

    private RvPuskesmasAdapter mAdapter;

    private ArrayList<PuskesmasModel> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // ButterKnife.bind(this);
        findViews();
        initToolbar(getResources().getString(R.string.app_name));
        setAdapter();


    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);


        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing edittext color
        EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Nama puskesmas");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }


                return null;


            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<PuskesmasModel> filterList = new ArrayList<PuskesmasModel>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getNama_puskesmas().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });
        return true;
    }

    private void setAdapter() {
        modelList.add(new PuskesmasModel("Puskesmas seroja", new LatLng(-6.2504, 106.98909), "seroja"));
        modelList.add(new PuskesmasModel("Puskesmas kaliabang tengah", new LatLng(-6.17567, 107.00027), "kaliabangtengah"));
        modelList.add(new PuskesmasModel("Puskesmas teluk pucung", new LatLng(-6.21864, 107.03038),"telukpucung"));
        modelList.add(new PuskesmasModel("Puskesmas marga mulya", new LatLng(-6.23005, 107.00484),"margamulya"));
        modelList.add(new PuskesmasModel("Puskesmas pejuang", new LatLng(-6.19864, 106.9933),"pejuang"));
        modelList.add(new PuskesmasModel("Puskesmas kota baru", new LatLng(-6.2144, 106.96816),"kotabaru"));
        modelList.add(new PuskesmasModel("Puskesmas bintara", new LatLng(-6.23923, 106.94691),"bintara"));
        modelList.add(new PuskesmasModel("Puskesmas bintara jaya", new LatLng(-6.23923, 106.94691),"bintarajaya"));
        modelList.add(new PuskesmasModel("Puskesmas kranji", new LatLng(-6.22616, 106.97078),"kranji"));
        modelList.add(new PuskesmasModel("Puskesmas rawa tembaga", new LatLng(-6.23363, 106.98059),"rawatembaga"));
        modelList.add(new PuskesmasModel("Puskesmas perumnas II", new LatLng(-6.24582, 106.98914),"perumnas"));
        modelList.add(new PuskesmasModel("Puskesmas marga jaya", new LatLng(-6.2374, 106.99427),"margajaya"));
        modelList.add(new PuskesmasModel("Puskesmas pekayon jaya", new LatLng(-6.26689, 106.97468),"pekayonjaya"));
        modelList.add(new PuskesmasModel("Puskesmas jaka mulya", new LatLng(-6.28064, 106.96878),"jakamulya"));
        modelList.add(new PuskesmasModel("Puskesmas bojong rawa lumbu", new LatLng(-6.2776, 106.99889),"bojongrawalumbu"));
        modelList.add(new PuskesmasModel("Puskesmas bojong menteng", new LatLng(-6.30027, 106.99573),"bojongmenteng"));
        modelList.add(new PuskesmasModel("Puskesmas pengasinan", new LatLng(-6.277839, 107.010086),"pengasinan"));
        modelList.add(new PuskesmasModel("Puskesmas karang kitri", new LatLng(-6.25777, 107.01111),"karangkitri"));
        modelList.add(new PuskesmasModel("Puskesmas aren jaya", new LatLng(-6.24929, 107.0299),"arenjaya"));
        modelList.add(new PuskesmasModel("Puskesmas duren jaya", new LatLng(-6.23582, 107.02264),"durenjaya"));
        modelList.add(new PuskesmasModel("Puskesmas pondok gede", new LatLng(-6.28282, 106.91344),"pondokgede"));
        modelList.add(new PuskesmasModel("Puskesmas jati rahayu", new LatLng(-6.29588, 106.92616),"jatirahayu"));
        modelList.add(new PuskesmasModel("Puskesmas jati warna", new LatLng(-6.305523, 106.932362),"jatiwarna"));
        modelList.add(new PuskesmasModel("Puskesmas jati Makmur", new LatLng(-6.2752, 106.92534),"jatimakmur"));
        modelList.add(new PuskesmasModel("Puskesmas jati bening", new LatLng(-6.2775223, 106.953222),"jatibening"));
        modelList.add(new PuskesmasModel("Puskesmas jati sampurna", new LatLng(-6.362401, 106.928108),"jatisampurna"));
        modelList.add(new PuskesmasModel("Puskesmas jati asih", new LatLng(-6.293063, 106.964716),"jatiasih"));
        modelList.add(new PuskesmasModel("Puskesmas jati luhur", new LatLng(-6.319799, 106.949739),"jatiluhur"));
        modelList.add(new PuskesmasModel("Puskesmas jakasetia", new LatLng(-6.276015, 106.978195),"jakasetia"));
        mAdapter = new RvPuskesmasAdapter(ListPuskesmasActivity.this, modelList);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new RvPuskesmasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, PuskesmasModel model) {
                //handle item click events here
                MapsActivity.nama_tempat = model.getNama_puskesmas();
                MapsActivity.aimage = model.getAimage();
                MapsActivity.lokasi_puskesmas = new LatLng(model.getLatLng().latitude,model.getLatLng().longitude);
                startActivity(new Intent(ListPuskesmasActivity.this, MapsActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }

        return false;
    }
}
