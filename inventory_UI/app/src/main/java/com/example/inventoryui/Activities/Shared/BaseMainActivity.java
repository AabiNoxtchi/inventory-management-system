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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Annotations.ChechBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
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
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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
    final String TAG2 = "MyActivity_2";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NestedScrollView nestedScrollView;

    ProgressBar progressBar;

    Map<String, CheckBox> filterCheckBoxes;
    Map<String, RadioGroup>  filterRadioGroups;
    List<RadioButton> checkedButtons;
    Map<String,Object> urlParameters;
    LinearLayout filterLayout;
    int filtersChecked = 0;

    Map<String, CheckBox>  dialogFilterCheckBoxes ;
    Map<String, RadioGroup>  dialogFilterRadioGroups ;
    List<RadioButton> dialogCheckedButtons;
    Map<SearchableSpinner, Pair<String, List>> dialogFilterSpinners;
    Map<String, TextInputLayout>  dialogFilterDateTexts ;
    Map<String,Object> dialogUrlParameters;
    TextView filters_count_dialog_label;

    Dialog filterDialog;
    LinearLayout dialogFilterLayout;
    TextView dialogFiltersCount;
    TextView second_filters_item_count_dialog_label;

    HashMap<SearchableSpinner,String> spinnerTitles;
    Map<String,Type> radioGroupTypeMap;
    int spinnersChck =0;

    int windowHeight=0;
    int windowWidth=0;

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

    SimpleDateFormat ft;//= new SimpleDateFormat("E yyyy.MM.dd ", Locale.ENGLISH);

    //final SimpleDateFormat ft= new SimpleDateFormat("E yyyy.MM.dd ", Locale.ENGLISH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main);

        ft = ((AuthenticationManager)this.getApplication()).ft;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loggedUser = ((AuthenticationManager) this.getApplication()).getLoggedUser();
        checkAddFabForLoggedUser();




        itemData = getItemData();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressBar = findViewById(R.id.progress_bar);

        filters_count_dialog_label = findViewById(R.id.filters_count_dialog_label);
        second_filters_item_count_dialog_label = findViewById(R.id.second_filters_item_count_dialog_label);

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
                setCounts();
                progressBar.setVisibility(View.GONE);
                if (!filtersSet) {
                    filterClass =  model.getFilter().getClass();
                    setFirstFilters();
                    setFilterListners(FilterType.First);
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

    private void setCounts(){
        filters_count_dialog_label.setText("total : "+model.getPager().getItemsCount());
        second_filters_item_count_dialog_label.setText(
                "total : "+model.getPager().getItemsCount());
    }

    private void setFirstFilters(){

        urlParameters = new HashMap<>();
        filterCheckBoxes = new HashMap<>();
        filterRadioGroups = new HashMap<>();
        filterLayout = findViewById(R.id.filter_linear_layout);
        setFilters(FilterType.First, filterLayout, filterCheckBoxes, filterRadioGroups,null);
    }

    private void setFilters( FilterType filterType,
                             LinearLayout filterLayout,
                             Map<String, CheckBox> filterCheckBoxes,
                             Map<String, RadioGroup>  filterRadioGroups,
                             @Nullable Map<SearchableSpinner, Pair<String, List>> filterSpinners ) {

        radioGroupTypeMap = new HashMap<>();
        spinnerTitles = null;

        for (Field f : filterClass.getDeclaredFields()) {

            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) {
                    continue;

                } else if (annotation instanceof ChechBoxAnnotation) {

                    ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                    String title = chckboxAnnotation.title();
                    String target = chckboxAnnotation.target();

                    Object obj = getUrlObject(target);

                    if( title.equals("all") || filterType.equals(FilterType.First)) {
                        CheckBox chckbox = giveMeCheckBox(title,obj);
                        filterCheckBoxes.put(target, chckbox);

                    }else{
                        RadioGroup rg = giveMeRadioGroup(title,filterType,obj);
                        filterRadioGroups.put(target, rg);
                        radioGroupTypeMap.put(target,f.getType());

                    }
                }else if(annotation instanceof EnumAnnotation) {

                    EnumAnnotation enumAnnotation = (EnumAnnotation) annotation;
                    String target = enumAnnotation.target();

                    List<String> titles = new ArrayList<>();
                    List<Object> objList = (List) getValue(f, model.getFilter());
                    for(Object obj : objList) {
                        String name = ((SelectItem) obj).getName();
                        titles.add(name);
                    }

                    Object obj = getUrlObject(target);

                    RadioGroup rg = giveMeRadioGroup(titles,filterType,obj);
                    filterRadioGroups.put(enumAnnotation.target(), rg);
                    radioGroupTypeMap.put(target,f.getType());

                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DropDownAnnotation) {

                    DropDownAnnotation dropDownAnnotation = (DropDownAnnotation) annotation;
                    String target = dropDownAnnotation.target();
                    String title = dropDownAnnotation.title();

                    List<Object> list = (List)getValue(f,model.getFilter());
                    if (list == null) continue;

                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
                    SearchableSpinner searchableSpinner = new SearchableSpinner(this);
                    searchableSpinner.setAdapter(adapter);

                    filterSpinners.put(searchableSpinner, new Pair(target, list));
                    if(spinnerTitles == null)spinnerTitles=new HashMap<>();
                    spinnerTitles.put(searchableSpinner,title);
                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DateAnnotation) {

                    DateAnnotation dateAnnotation = (DateAnnotation) annotation;
                    String target = dateAnnotation.target();
                    String title = dateAnnotation.title();

                    if (dialogFilterDateTexts == null)
                        dialogFilterDateTexts = new HashMap<>();

                    DateFilter dateFilter = new DateFilter(this,dialogFilterDateTexts,dialogUrlParameters,this);
                    dateFilter.giveMeDateTextInput(target,title);

                }
            }
        }
        addToLayout(filterLayout,filterCheckBoxes,filterRadioGroups,filterSpinners,spinnerTitles);
    }

    private void setFilterListners(FilterType filterType) {

        if(filterType.equals(FilterType.Dialog)){
            setCheCkBoxListners(filterType,dialogFilterCheckBoxes,dialogUrlParameters);
            setRadioGroupListners(filterType,dialogFilterRadioGroups,dialogCheckedButtons,dialogUrlParameters);
            setSpinnerListners(filterType,dialogFilterSpinners,dialogUrlParameters);
        }else {
            setCheCkBoxListners(filterType, filterCheckBoxes, urlParameters);
            if (checkedButtons == null)
                checkedButtons = new ArrayList<>();
            setRadioGroupListners(filterType, filterRadioGroups, checkedButtons, urlParameters);
        }

    }

    private void setCheCkBoxListners(
            final FilterType filterType,
            final Map<String, CheckBox> filterCheckBoxes,
            final Map<String,Object> urlParameters) {

        for (final Map.Entry<String, CheckBox> entry : filterCheckBoxes.entrySet()) {
            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String target = entry.getKey();

                    int count=getFilterCountInt(filterType);
                    checkfilterBoxes(count, target, (CheckBox) v, filterType);

                    count=getFilterCountInt(filterType);
                    checkfilters(count,urlParameters,filterType);

                    boolean isChecked = ((CheckBox) v).isChecked();
                    if (count > 0) {
                        if (isChecked) {
                            urlParameters.put(target, true);
                        } else if (urlParameters.containsKey(target)) {
                            urlParameters.remove(target);
                        }
                    }
                    goGetItems(filterType);

                }
            });
        }
    }

    // /********** for enum and Boolean ****/
    private void setRadioGroupListners(
            final FilterType filterType,
            Map<String, RadioGroup>  filterRadioGroups,
            final List<RadioButton> checkedButtons,
            final Map<String,Object> urlParameters) {

        for(final Map.Entry<String,RadioGroup> entry : filterRadioGroups.entrySet()) {

            final RadioGroup rg = entry.getValue();
            final int rbCount = rg.getChildCount();

            for(int i =0;i<rbCount;i++){
                RadioButton b = (RadioButton)rg.getChildAt(i);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RadioButton radioButton = (RadioButton)v;
                        if(checkedButtons.contains(radioButton)) {
                            rg.clearCheck();
                            checkedButtons.remove(radioButton);
                        }

                        int count = getFilterCountInt(filterType);
                        if(!(count==0 && !radioButton.isChecked())) {

                            String target = entry.getKey();
                            Type type = radioGroupTypeMap.get(target);

                            String buttonName = radioButton.getText().toString();
                            buttonName = nameToBoolean(buttonName, type);

                            if (radioButton.isChecked()) {
                                if(urlParameters.containsKey(target)) {

                                    for (int i = 0; i < rbCount; i++) {
                                        RadioButton b = (RadioButton) rg.getChildAt(i);
                                        String bName = b.getText().toString();

                                        bName = nameToBoolean(bName,type);

                                        if (!bName.equals(buttonName)) {
                                            checkedButtons.remove(b);
                                            b.setChecked(false);
                                        }
                                    }

                                }else{
                                    addFiltersCount(filterType);
                                }
                                checkedButtons.add(radioButton);
                                urlParameters.put(target, buttonName);

                            }else{

                                urlParameters.remove(target);
                                substractFiltersCount(filterType);
                            }
                            checkfilters(getFilterCountInt(filterType),urlParameters,filterType);
                            goGetItems(filterType);
                        }
                    }
                });
            }
        }
    }



    private void checkRadioIfChecked(FilterType filterType, Map<String, RadioGroup>  filterRadioGroups ) {

        if (dialogCheckedButtons == null)
            dialogCheckedButtons = new ArrayList<>();

        for (Map.Entry<String, RadioGroup> entry : dialogFilterRadioGroups.entrySet()) {

            String target = entry.getKey();
            if (urlParameters.containsKey(target)) {

                Object value = urlParameters.get(target);
                String btnValue = value.toString();

                Type type = radioGroupTypeMap.get(target);
                RadioGroup rg = entry.getValue();
                int count = rg.getChildCount();

                for (int i = 0; i < count; i++) {

                    RadioButton b = (RadioButton) rg.getChildAt(i);
                    String bName = b.getText().toString();
                    bName = nameToBoolean(bName, type);

                    Log.i(TAG,"btn==btn = "+bName +"=="+btnValue);
                    boolean equals = bName.equals(btnValue);
                    if (equals ) b.performClick();

                }
            }

        }
    }

    private void setSpinnerListners(
            final FilterType filterType,
            final Map<SearchableSpinner,Pair<String, List>> filterSpinners,
            final Map<String,Object> urlParameters) {

        spinnersChck = 0 - filterSpinners.size();
        for (final SearchableSpinner spinner : filterSpinners.keySet()) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if( ++spinnersChck > 0) {
                        int count = getFilterCountInt(filterType);

                        if (!(count == 0 && position == 0))  {
                            Pair pair = filterSpinners.get(spinner);
                            String target = (String) pair.first;
                            if (position > 0) {

                                addFiltersCount(filterType);
                                SelectItem selectItem = (SelectItem) ((ArrayList) pair.second).get(position);
                                String value = selectItem.getValue();
                                urlParameters.put(target, value);

                            } else {
                                if (count > 0) substractFiltersCount(filterType);
                                urlParameters.remove(target);
                            }

                            checkfilters(getFilterCountInt(filterType), urlParameters, filterType);

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private void addToLayout(LinearLayout filterLayout,
                             Map<String, CheckBox> filterCheckBoxes,
                             Map<String, RadioGroup> filterRadioGroups,
                             Map<SearchableSpinner, Pair<String, List>> filterSpinners,
                             Map<SearchableSpinner,String> spinnerTitles) {


        for (CheckBox checkBox : filterCheckBoxes.values()) {
            filterLayout.addView(checkBox);
            addLine(filterLayout);
        }

        if(filterRadioGroups != null) {
            for (RadioGroup rg : filterRadioGroups.values()) {
                filterLayout.addView(rg);
                addLine(filterLayout);
            }
        }

        if(filterSpinners!=null) {
            for (SearchableSpinner spinner : filterSpinners.keySet()) {
                TextView textView = new TextView(this);
                textView.setText(spinnerTitles.get(spinner));
                filterLayout.addView(textView);
                filterLayout.addView(spinner);
                addLine(filterLayout);
            }
        }

        if(dialogFilterDateTexts !=null){
            for(TextInputLayout inputLayout : dialogFilterDateTexts.values()){
                filterLayout.addView(inputLayout);
                addLine(filterLayout);
            }
        }
    }

    private String nameToBoolean(String buttonName, Type type){
        if(type.equals(Boolean.class)   ) {
            if (buttonName.startsWith("not")) {
                buttonName = "false";
            }else{
                buttonName = "true";
            }
        }

        return buttonName;
    }

    private Object getUrlObject(String target){
        if(dialogUrlParameters!=null && dialogUrlParameters.size()>0){
            if( dialogUrlParameters.containsKey(target))
                return dialogUrlParameters.get(target);
        }
        else if(urlParameters.containsKey(target))
            return urlParameters.get(target);
        return null;
    }

    private int getFilterCountInt(FilterType filterType) {
        int count =0;
        if(filterType.equals(FilterType.Dialog)){
            count = Integer.parseInt(dialogFiltersCount.getText().toString());
        }else{ count = filtersChecked; }
        return count;
    }

    public void addFiltersCount(FilterType filterType) {
        if(filterType.equals(FilterType.First)){
            filtersChecked++;
        } else if (filterType.equals(FilterType.Dialog)) {
            int count = Integer.parseInt(dialogFiltersCount.getText().toString());
            dialogFiltersCount.setText(""+(count+1));
        }
    }

    public void substractFiltersCount(FilterType filterType) {
        if(filterType.equals(FilterType.First)){
            filtersChecked--;
        } else if (filterType.equals(FilterType.Dialog)) {
            int count = Integer.parseInt(dialogFiltersCount.getText().toString());
            if(count>0)count--;
            dialogFiltersCount.setText(""+count);
        }
    }

    private RadioGroup giveMeRadioGroup(Object obj ,FilterType filterType, Object selected) {
        RadioGroup rg = new RadioGroup(this);

        List<String> names=null;
        if(obj instanceof String){

            names = new ArrayList<>();
            names.add((String)obj);
            names.add("not "+obj);

        }else{
            names = (List)obj;
        }

        for(String name : names){
            RadioButton rb1 =  new RadioButton(this);
            rb1.setText(name);
            rg.addView(rb1);
        }

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams( // width,height
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT );

        if(filterType.equals(FilterType.First)){
            rg.setOrientation(RadioGroup.HORIZONTAL);
            params.leftMargin=50;
            params.rightMargin=80;
        }
        else {
            rg.setOrientation(RadioGroup.VERTICAL);
        }

        rg.setLayoutParams(params);
        return rg;
    }

    private CheckBox giveMeCheckBox(String name,Object obj) {
        CheckBox chckbox = new CheckBox(getApplicationContext());
        chckbox.setText(name);
        chckbox.setPadding(0, 0, 50, 0);

        if (filtersChecked == 0 && name.equals("all")) chckbox.setChecked(true);
        else if (obj != null && obj.equals(true)) {
            chckbox.setChecked(true);
        }
        return chckbox;
    }

    private void addLine(LinearLayout filterLayout) {

        RelativeLayout.LayoutParams lineparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);

        lineparams.topMargin = 40;
        lineparams.bottomMargin = 40;
        View line = new View(this);

        line.setLayoutParams(lineparams);
        line.setBackgroundColor(getResources().getColor(android.R.color.black));
        filterLayout.addView(line);
    }

    private void checkfilterBoxes(int count,String target, CheckBox v, FilterType filterType) {

        if (target.equals("all") && count > 0 && v.isChecked()) {
            count = 0;
        } else {
            if (v.isChecked()) {
                count++;
            } else {
                if (count != 0) count--;
            }
        }
        if(filterType.equals(FilterType.Dialog)){
            dialogFiltersCount.setText(""+count);
        }else{  filtersChecked = count ;}
    }

    private void checkfilters(int count,final Map<String,Object> urlParameters,FilterType filterType) {

        if (count > 0) {

            if(urlParameters.containsKey("all"))urlParameters.remove("all");
            if(filterType.equals(FilterType.First))filterCheckBoxes.get("all").setChecked(false);
            else dialogFilterCheckBoxes.get("all").setChecked(false);
        } else {

            urlParameters.clear();
            urlParameters.put("all",true);
            clearFirstLayout(filterType);
            clearDialogLayout(filterType);
        }
    }

    private void clearFirstLayout(FilterType filterType){
        if(!filterType.equals(FilterType.First)) return;
        for (CheckBox b : filterCheckBoxes.values()) {
            b.setChecked(false);
        }
        filterCheckBoxes.get("all").setChecked(true);
        for (RadioGroup rg : filterRadioGroups.values()) {
            rg.clearCheck();
        }
        checkedButtons.clear();

    }

    private void clearDialogLayout(FilterType filterType){
        if(!filterType.equals(FilterType.Dialog))return;
        for (CheckBox b : dialogFilterCheckBoxes.values()) {
            b.setChecked(false);
        }
        dialogFilterCheckBoxes.get("all").setChecked(true);
        for (RadioGroup rg : dialogFilterRadioGroups.values()) {
            rg.clearCheck();
        }
        dialogCheckedButtons.clear();

        for(SearchableSpinner spinner : dialogFilterSpinners.keySet()){
            if(spinner.getSelectedItemPosition()>0)
                spinner.setSelection(0);
        }
    }

    private void goGetItems(FilterType filterType){
        if(!filterType.equals(FilterType.First)) return;
            if (model.getFilter() != null)
                model.getFilter().setUrlParameters(urlParameters);
            getItems();
    }

    protected void getItems() {

        progressBar.setVisibility(View.VISIBLE);
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

        if  (dialogUrlParameters==null)
            dialogUrlParameters = new HashMap<>();

        if(filterDialog!=null)filterDialog.show();
        else {
            getDialog();
            initializeDialogFields();
            initializeDialogMaps();
            setDialogFilters();


            ImageButton dialogCancelImgBtn = filterDialog.findViewById(R.id.dialogCancelImageButton);
            dialogCancelImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterDialog.hide();
                }
            });

            ImageButton dialogSearchImgBtn = filterDialog.findViewById(R.id.dialogSearchImageButton);
            dialogSearchImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i(TAG,"ürl parameters = "+dialogUrlParameters);
                    model.getFilter().setUrlParameters(dialogUrlParameters);
                    getItems();

                    filterLayout.setVisibility(View.GONE);

                    final RelativeLayout second_filter_linear_layout = findViewById(R.id.second_filter_linear_layout);
                    second_filter_linear_layout.setVisibility(View.VISIBLE);

                    TextView tv = findViewById(R.id.second_filters_count_dialog_text_view);
                    tv.setText(dialogFiltersCount.getText());



                    ImageButton second_dialogCancelImgBtn = findViewById(R.id.second_dialogCancelImageButton);
                    second_dialogCancelImgBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            filterLayout.setVisibility(View.VISIBLE);
                            second_filter_linear_layout.setVisibility(View.GONE);

                            nullifyDialog();
                            filterDialog = null;
                            filterCheckBoxes.get("all").performClick();
                        }
                    });

                    filterDialog.hide();
                }
            });
        }
    }

    private void getDialog(){

        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_dialog_view);

        getWindowMwtrics();
        filterDialog.getWindow().setLayout(windowWidth - 80, windowHeight - 100);

        filterDialog.show();
    }

    private void initializeDialogFields(){

        dialogFilterLayout = filterDialog.findViewById(R.id.filter_dialog_linear_layout);
        dialogFiltersCount = filterDialog.findViewById(R.id.filters_count_dialog_text_view);//filters_count_dialog_text_view
        dialogFiltersCount.setText("0");
    }

    private void setDialogFilters() {

        setFilters(FilterType.Dialog, dialogFilterLayout, dialogFilterCheckBoxes, dialogFilterRadioGroups, dialogFilterSpinners);
        setFilterListners(FilterType.Dialog);

        checkRadioIfChecked(FilterType.Dialog, dialogFilterRadioGroups);
        checkSpinnerIfChecked(FilterType.Dialog, dialogFilterSpinners);
        checkBoxesIfChecked(FilterType.Dialog, dialogFilterCheckBoxes);
    }

    private void nullifyDialog(){

        dialogUrlParameters = null;
        dialogFilterCheckBoxes = null;
        dialogFilterRadioGroups = null;
        dialogCheckedButtons = null;
        dialogFilterSpinners = null;
        dialogFilterLayout = null;

    }

    private void  initializeDialogMaps() {

        if(dialogCheckedButtons == null) {
            dialogFilterCheckBoxes = new HashMap<>();
        }
        if(dialogFilterRadioGroups == null)
            dialogFilterRadioGroups = new HashMap<>();
        if(dialogFilterSpinners == null)
             dialogFilterSpinners = new HashMap<>();
        if(dialogCheckedButtons == null)
            dialogCheckedButtons = new ArrayList<>();

    }

    private void checkBoxesIfChecked(FilterType filterType, Map<String, CheckBox> filterCheckBoxes){

        for (Map.Entry<String, CheckBox> entry : filterCheckBoxes.entrySet()){
            String target = entry.getKey();
            CheckBox chckbox = entry.getValue();
            if( filterType.equals(FilterType.Dialog) && urlParameters.containsKey(target)){
                if(chckbox.isChecked())
                    dialogUrlParameters.put(target,true);
                if(!target.equals("all"))chckbox.performClick();
            }
        }
    }

    private void checkSpinnerIfChecked(FilterType filterType,  Map<SearchableSpinner, Pair<String, List>> dialogFilterSpinners){

        for(Map.Entry<SearchableSpinner,Pair<String,List>> entry : dialogFilterSpinners.entrySet()){

            String target = entry.getValue().first;

            SearchableSpinner spinner = entry.getKey();
            List<SelectItem> list = entry.getValue().second;

            if(filterType.equals(FilterType.Dialog) && dialogUrlParameters.size()>0 && dialogUrlParameters.containsKey(target)) {
                spinner.setSelection(
                        list.indexOf(new SelectItem((String) dialogUrlParameters.get(target))));
                spinner.performClick();

            }
        }
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
