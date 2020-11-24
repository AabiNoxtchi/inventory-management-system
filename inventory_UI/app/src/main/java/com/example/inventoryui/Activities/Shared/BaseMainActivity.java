package com.example.inventoryui.Activities.Shared;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.DataAccess.BaseData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Models.Shared.FilterType;
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

    final String TAG = "MyActivity_baseMain";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NestedScrollView nestedScrollView;
    LinearLayout filterLayout;

    Map<String, CheckBox> filterCheckBoxes;
    Map<SearchableSpinner, Pair<String, List>> filterSpinners;
    Map<String, RadioGroup>  filterRadioGroups;
    List<RadioButton> checkedButtons;

    int windowHeight=0;
    int windowWidth=0;
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
   //  private BaseFilterVM filter;

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
        setSupportActionBar(toolbar);
        loggedUser = ((AuthenticationManager) this.getApplication()).getLoggedUser();
        checkAddFabForLoggedUser();

        itemData = getItemData();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        items = new ArrayList<>();
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if (scrollY > oldScrollY) {
                        if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                            if (model.getPager().getPagesCount() > model.getPager().getPage() + 1) {
                                model.getPager().setPage(model.getPager().getPage() + 1);
                                isLoadingMore = true;
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
                model = indexVM;
                if (!filtersSet) {
                    filterClass =  model.getFilter().getClass();
                    setFirstFilters();
                    setFilterListners();
                    filtersSet = true;
                }
                if (!isLoadingMore) {
                    items.clear();
                    items.addAll(model.getItems());
                    adapter.notifyDataSetChanged();
                    nestedScrollView.scrollTo(0, 0);
                } else {
                    isLoadingMore = false;
                    items.addAll(model.getItems());
                    recyclerView.post(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void setFirstFilters(){

        filterCheckBoxes = new HashMap<>();
        filterSpinners = new HashMap<>();
        filterRadioGroups = new HashMap<>();
        filterLayout = findViewById(R.id.filter_linear_layout);
        setFilters(filterLayout, filterCheckBoxes,filterSpinners, FilterType.First,filterRadioGroups);
    }

    private void setFilters( LinearLayout filterLayout, Map<String, CheckBox> filterCheckBoxes,
                 Map<SearchableSpinner, Pair<String, List>> filterSpinners ,FilterType filterType,
                             @Nullable Map<String, RadioGroup>  dialogFilterRadioGroups) {

        for (Field f : filterClass.getDeclaredFields()) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) {
                    continue;
                } else if (annotation instanceof ChechBoxAnnotation) {
                    ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                    String name = chckboxAnnotation.title();
                    Boolean obj = (Boolean) getValue(f, model.getFilter());

                    if( name.equals("all") || filterType.equals(FilterType.First)) {
                        CheckBox chckbox = giveMeCheckBox(name,obj);
                        filterCheckBoxes.put(chckboxAnnotation.target(), chckbox);
                        if (filtersChecked == 0 && name.equals("all")) chckbox.setChecked(true);
                    }else{
                        List<String> names = new ArrayList<>();
                        names.add(name);
                        names.add("not "+name);
                        RadioGroup rg = giveMeRadioGroup(names,filterType);
                        if (obj != null && obj == true) {
                            ((RadioButton)rg.getChildAt(0)).setChecked(true);
                        }else if(obj != null && obj == false){
                            ((RadioButton)rg.getChildAt(1)).setChecked(true);
                        }

                        dialogFilterRadioGroups.put(chckboxAnnotation.target(), rg);
                    }
                }else if(annotation instanceof EnumAnnotation) {
                    EnumAnnotation enumAnnotation = (EnumAnnotation) annotation;
                    String target = enumAnnotation.target();
                    List<Object> objList = (List) getValue(f, model.getFilter());
                    List<String> names = new ArrayList<>();
                    for(Object obj : objList) {
                        String name = ((SelectItem) obj).getName();
                        names.add(name);
                    }
                    RadioGroup rg = giveMeRadioGroup(names,filterType);
                    dialogFilterRadioGroups.put(enumAnnotation.target(), rg);

                    if(filtersChecked >0){
                        for (Field ff : filterClass.getDeclaredFields()){
                            if(ff.getName().equals(target) &&
                                    getValue(ff,model.getFilter()) != null){

                                String selectedText = (getValue(ff,model.getFilter())).toString();
                                int count = rg.getChildCount();
                                for(int i=0;i<count;i++){
                                    RadioButton btn = (RadioButton)rg.getChildAt(i);
                                    if(btn.getText().toString().equals(selectedText)){
                                       btn.setChecked(true);
                                       break;
                                    }
                                }
                            }
                        }
                    }
                }else if (annotation instanceof DropDownAnnotation) {
                    DropDownAnnotation dropDownAnnotation = (DropDownAnnotation) annotation;
                    SearchableSpinner searchableSpinner = new SearchableSpinner(this);
                    List<Object> list = (List)getValue(f,model.getFilter());
                    if (list == null) continue;
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
                    searchableSpinner.setAdapter(adapter);
                    searchableSpinner.setTitle(dropDownAnnotation.title());
                    filterSpinners.put(searchableSpinner, new Pair(dropDownAnnotation.target(), list));

                    if(filtersChecked >0){
                        for (Field ff : filterClass.getDeclaredFields()){
                            if(ff.getName().equals(dropDownAnnotation.target()) &&
                                    getValue(ff,model.getFilter()) != null){
                                searchableSpinner.setSelection(
                                        list.indexOf(
                                                 new SelectItem((String)getValue(ff, model.getFilter()) )));

                            }
                        }
                    }
                }
            }
        }

        for (CheckBox checkBox : filterCheckBoxes.values()) {
            filterLayout.addView(checkBox);
            addLine(filterLayout);
        }

        if(dialogFilterRadioGroups != null) {
            for (RadioGroup rg : dialogFilterRadioGroups.values()) {
                filterLayout.addView(rg);
                addLine(filterLayout);
            }
        }

        for (SearchableSpinner spinner : filterSpinners.keySet()) {
            filterLayout.addView(spinner);
            addLine(filterLayout);
        }
    }

    private RadioGroup giveMeRadioGroup(List<String> names ,FilterType filterType) {
        RadioGroup rg = new RadioGroup(this);

        for(String name : names){
            RadioButton rb1 =  new RadioButton(this);
            rb1.setText(name);
            rg.addView(rb1);
        }



        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams( // width,height
                        (int) RelativeLayout.LayoutParams.MATCH_PARENT,(int) RelativeLayout.LayoutParams.WRAP_CONTENT );

        if(filterType.equals(FilterType.First)){
            rg.setOrientation(RadioGroup.HORIZONTAL);
            params.topMargin = 30;
            params.leftMargin=50;
            params.rightMargin=50;
        }
        else {
            rg.setOrientation(RadioGroup.VERTICAL);
            params.topMargin = 40;
            params.bottomMargin = 40;
        }

        rg.setLayoutParams(params);
        return rg;
    }

    private CheckBox giveMeCheckBox(String name,Boolean obj) {
        CheckBox chckbox = new CheckBox(getApplicationContext());
        chckbox.setText(name);
        chckbox.setPadding(0, 0, 50, 0);

        if (obj != null && obj == true) {
            chckbox.setChecked(true);
        }
        return chckbox;
    }

    private void addLine(LinearLayout filterLayout) {

        RelativeLayout.LayoutParams lineparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        View line = new View(this);
        line.setLayoutParams(lineparams);
        line.setBackgroundColor(getResources().getColor(android.R.color.black));
        filterLayout.addView(line);
    }

    private void setFilterListners() {

        setCheCkBoxListners();
        setSpinnerListners();
        setRadioGroupListners();

    }

   // /********** for enum ****/
    private void setRadioGroupListners() {

        if(checkedButtons == null)
            checkedButtons = new ArrayList<>();

        for(final Map.Entry<String,RadioGroup> entry : filterRadioGroups.entrySet()) {
            final RadioGroup rg = entry.getValue();
            int count = rg.getChildCount();

            for(int i =0;i<count;i++){
                RadioButton b = (RadioButton)rg.getChildAt(i);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RadioButton radioButton = (RadioButton)v;
                        if(checkedButtons.contains(radioButton)) {
                            rg.clearCheck();
                            checkedButtons.remove(radioButton);
                        }
                        if(!(filtersChecked==0 && !radioButton.isChecked())) {
                            String fieldName = entry.getKey();
                            String buttonName = radioButton.getText().toString();
                            for (Field f : filterClass.getDeclaredFields()) {
                                if (f.getName().equals(fieldName)) {
                                    if (radioButton.isChecked()) {
                                        setValue(f,buttonName);
                                        filtersChecked++;
                                        checkedButtons.add(radioButton);
                                    }else{

                                        setValue(f,null);
                                        filtersChecked--;
                                    }
                                    break;
                                }
                            }
                            getItems();
                            checkfilters();
                        }
                    }
                });
            }
        }
    }



    private void setCheCkBoxListners() {

        for (final Map.Entry<String, CheckBox> entry : filterCheckBoxes.entrySet()) {
            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fieldName = entry.getKey();
                    checkfilterBoxes(fieldName, (CheckBox) v);
                    checkfilters();
                    if(filtersChecked>0)
                    for (Field f : filterClass.getDeclaredFields()) {
                        if (f.getName().equals(fieldName)) {
                            setValue(f, ((CheckBox) v).isChecked() ? true : null);
                            break;
                        }
                    }
                    getItems();

                }
            });
        }
    }


    private void setSpinnerListners() {

        for (final SearchableSpinner spinner : filterSpinners.keySet()) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!(filtersChecked == 0 && position == 0)) {
                        Pair pair = filterSpinners.get(spinner);
                        String fieldTarget = (String) pair.first;
                        for (Field f : filterClass.getDeclaredFields()) {
                            if (fieldTarget.equals(f.getName())) {
                                    if (position > 0) {
                                        filtersChecked++;
                                        SelectItem selectItem = (SelectItem) ((ArrayList) pair.second).get(position);
                                        String value = selectItem.getValue();
                                        setValue(f,value);
                                    } else {
                                        if (filtersChecked > 0) filtersChecked--;
                                        setValue(f,null);
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

    private void checkfilterBoxes(String fieldName, CheckBox v) {

        if (fieldName.equals("all") && filtersChecked > 0 && v.isChecked()) {
            filtersChecked = 0;
        } else {
            if (v.isChecked()) {
                filtersChecked++;
            } else {
                if (filtersChecked != 0) filtersChecked--;
            }
        }
    }

    private void checkfilters() {
        if (filtersChecked > 0) {
            model.getFilter().setAll(null);
            filterCheckBoxes.get("all").setChecked(false);
        } else {
            newAllFilter();
        }
    }

    private void newAllFilter() {
        model.setFilter(getNewFilter());
        model.getFilter().setAll(true);
        for (CheckBox b : filterCheckBoxes.values()) {
            b.setChecked(false);
        }
        for (SearchableSpinner spinner : filterSpinners.keySet()) {
            spinner.setSelection(0);
        }
        filterCheckBoxes.get("all").setChecked(true);
    }






    protected void getItems() {

        Log.i(TAG, "************      getting products       *************");
        if (this.model == null) model = getNewIndexVM();
        checkItemsFromIntent();

        if (model.getPager() == null || !isLoadingMore) {
            model.setPager(new PagerVM());
            model.getPager().setPage(0);
            model.getPager().setItemsPerPage(10);
        }
        itemData.getAll(model);
    }

    protected void filterActivity(){

        Dialog filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_dialog_view);

        getWindowMwtrics();
        filterDialog.getWindow().setLayout(windowWidth-80,windowHeight-100);
        filterDialog.show();

        TextView filtersCount = filterDialog.findViewById(R.id.filters_count_dialog_text_view);//filters_count_dialog_text_view
        filtersCount.setText(filtersCount.getText().toString()+filtersChecked);

        Map<String, CheckBox>  dialogFilterCheckBoxes = new HashMap<>();
        Map<String, RadioGroup>  dialogFilterRadioGroups = new HashMap<>();
        Map<SearchableSpinner, Pair<String, List>> dialogFilterSpinners = new HashMap<>();
        LinearLayout dialogFilterLayout = filterDialog.findViewById(R.id.filter_dialog_linear_layout);
        setFilters(dialogFilterLayout,dialogFilterCheckBoxes,dialogFilterSpinners, FilterType.Dialog, dialogFilterRadioGroups);


    }

    private Object getValue(Field f , Object obj) {
        f.setAccessible(true);

        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setValue(Field f, Object value) {
        f.setAccessible(true);
        try {
            if (f.getType().isEnum()) {
                if(value == null)
                    f.set(model.getFilter(),null );
                else
                    f.set(model.getFilter(), Enum.valueOf((Class<Enum>) f.getType(), (String)value));
            }
            else{
                f.set(model.getFilter(), value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void getWindowMwtrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowHeight = displayMetrics.heightPixels;
        windowWidth = displayMetrics.widthPixels;
    }


    protected void logOut() {
        ((AuthenticationManager) this.getApplication()).logout();
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
