package com.example.inventoryui.Activities.Shared;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Annotations.ChechBoxAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.DataAccess.BaseData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Models.Shared.PagerVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.example.inventoryui.Models.User;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMainActivity< Item extends BaseModel,
        IndexVM extends BaseIndexVM,
        Filter extends BaseFilterVM,
        ItemData extends BaseData >
        extends AppCompatActivity {

    final String TAG="MyActivity_baseMain";

    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager;
    NestedScrollView nestedScrollView;
    LinearLayout filterLayout;
    Map<String,CheckBox> filterCheckBoxes;
    Map<SearchableSpinner, Pair<String,List>> filterSpinners;
    int filtersChecked = 0;
    boolean filtersSet = false;
    boolean isLoadingMore = false;

    Class filterClass;
    protected IndexVM model;
    protected ItemData itemData;
    protected FloatingActionButton addFab;
    protected User loggedUser;
    protected ArrayList<Item> items;
    protected RecyclerView.Adapter adapter;

    protected abstract ItemData getItemData();
    protected abstract Filter getNewFilter();
    protected abstract IndexVM getNewIndexVM();
    protected abstract RecyclerView.Adapter getAdapter();
    protected abstract void checkAddFabForLoggedUser();
    protected abstract void checkIntentAndGetItems();
    protected abstract void checkItemsFromIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("This is toolbar.");
        setSupportActionBar(toolbar);
        loggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();
        checkAddFabForLoggedUser();

        itemData = getItemData();

        recyclerView=findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        items = new ArrayList<>();
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        nestedScrollView= findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if (scrollY > oldScrollY)
                    {
                        if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()))
                        {
                            if(model.getPager().getPagesCount() > model.getPager().getPage()+1) {
                                model.getPager().setPage(model.getPager().getPage() + 1);
                                isLoadingMore = true;
                                Log.i(TAG, "getting items from nested scroll on scroll ");
                                getItems();
                            }
                        }
                    }
                }
            }
        });

        checkIntentAndGetItems();

        itemData.getIndexVM().observe(this, new Observer<IndexVM>() {
            @Override
            public void onChanged(IndexVM indexVM) {
                model=indexVM;
                if(!filtersSet) {
                    filterClass = model.getFilter().getClass();
                    setFilters();
                    setFilterListners();
                    filtersSet=true;
                }
                if(!isLoadingMore){
                    items.clear();
                    items.addAll(model.getItems());
                    adapter.notifyDataSetChanged();
                    nestedScrollView.scrollTo(0,0);
                }else {
                    isLoadingMore = false;
                    items.addAll(model.getItems());
                    recyclerView.post(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } );

        /***************************************************/
        //recyclerView.setPadding(0, getToolBarHeight(), 0, 0);
    }

    private void setFilters(){
        filterCheckBoxes = new HashMap<>();
        filterSpinners = new HashMap<>();

        for (Field f :filterClass.getDeclaredFields()) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) { continue;}
                else if(annotation instanceof ChechBoxAnnotation)
                {
                    ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                    CheckBox chckbox = new CheckBox(getApplicationContext());
                    String name = chckboxAnnotation.title();
                    chckbox.setText(name);
                    chckbox.setPadding(0,0,50,0);
                    filterCheckBoxes.put(chckboxAnnotation.target(), chckbox);
                    if(chckboxAnnotation.target().equals("all"))chckbox.setChecked(true);

                }else if(annotation instanceof DropDownAnnotation){
                    DropDownAnnotation dropDownAnnotation = (DropDownAnnotation)annotation;
                    SearchableSpinner searchableSpinner = new SearchableSpinner(this);
                    f.setAccessible(true);
                    List<Object> list = new ArrayList<>();

                    try {

                        list = (ArrayList)f.get(model.getFilter()) ;
                        if(list == null )continue;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    list.add(0,new SelectItem(dropDownAnnotation.title()));
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
                    searchableSpinner.setAdapter(adapter);
                    searchableSpinner.setTitle(dropDownAnnotation.title());
                    filterSpinners.put(searchableSpinner,new Pair( dropDownAnnotation.target() , list));
                }
            }
        }

        addToFilterLayout();
    }

    private void addToFilterLayout(){
        filterLayout = findViewById(R.id.filter_linear_layout);
        for(CheckBox checkBox : filterCheckBoxes.values()){

            filterLayout.addView(checkBox);
        }

        for(SearchableSpinner spinner : filterSpinners.keySet()){
            filterLayout.addView(spinner);
        }
    }

    private void setFilterListners(){

        setCheCkBoxListners();
        setSpinnerListners();

    }

    private void setSpinnerListners() {

        for(final SearchableSpinner spinner : filterSpinners.keySet()){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!(filtersChecked==0 && position ==0)){
                        Pair pair = filterSpinners.get(spinner);
                        String fieldTarget = (String) pair.first;
                        for (Field f : filterClass.getDeclaredFields()) {
                            f.setAccessible(true);
                            if (fieldTarget.equals(f.getName())) {
                                try {
                                    if (position > 0) {
                                        filtersChecked++;
                                        SelectItem selectItem = (SelectItem) ((ArrayList)pair.second).get(position);
                                        String value = selectItem.getValue();
                                        if (f.getType().isAssignableFrom(Long.class)) {
                                            f.set(model.getFilter(), Long.parseLong(value));
                                        } else if (f.getType().isAssignableFrom(String.class)) {
                                            f.set(model.getFilter(), value);
                                        }
                                    } else {
                                        if (filtersChecked > 0) filtersChecked--;
                                        f.set(model.getFilter(), null);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        getItems();
                        checkfilters();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void checkfilters(){
        if (filtersChecked > 0) {
            model.getFilter().setAll(null);
            filterCheckBoxes.get("all").setChecked(false);
        } else {
            newAllFilter();
        }
    }

    private void newAllFilter(){
        model.setFilter(getNewFilter());
        model.getFilter().setAll(true);
        for (CheckBox b : filterCheckBoxes.values()) {
            b.setChecked(false);
        }
        for(SearchableSpinner spinner : filterSpinners.keySet()){
            spinner.setSelection(0);
        }
        filterCheckBoxes.get("all").setChecked(true);
    }

    private void checkfilterBoxes(String fieldName , CheckBox v) {

        if ( fieldName.equals("all") && filtersChecked >0 && v.isChecked()) {
            filtersChecked = 0;
        } else {
            if (v.isChecked()) {
                filtersChecked++;
            } else {
                if(filtersChecked!=0)filtersChecked--;
            }
        }
    }

    private void setCheCkBoxListners() {

       for( final Map.Entry<String, CheckBox> entry : filterCheckBoxes.entrySet()){
           entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fieldName = entry.getKey();
                    checkfilterBoxes(fieldName, (CheckBox) v);
                    checkfilters();
                    for (Field f : filterClass.getDeclaredFields()) {
                        f.setAccessible(true);
                        if(f.getName().equals(fieldName)){
                            try {
                                f.set(model.getFilter(), ((CheckBox) v).isChecked() ? true : null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    getItems();
                }
            });
        }
    }

    public int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        Log.i(TAG,"tool bar height = "+toolBarHeight);
        return (int) (toolBarHeight*2);
    }

    protected void getItems() {

        Log.i(TAG,"************      getting products       *************");
        if(this.model==null) model = getNewIndexVM();
        checkItemsFromIntent();

        if(model.getPager()==null || !isLoadingMore){
            model.setPager(new PagerVM());
            model.getPager().setPage(0);
            model.getPager().setItemsPerPage(10);
        }
        itemData.getAll( model);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthenticationManager.activityResumed();
        AuthenticationManager.setActiveActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthenticationManager.activityPaused();
        AuthenticationManager.setActiveActivity(null);
    }


}
