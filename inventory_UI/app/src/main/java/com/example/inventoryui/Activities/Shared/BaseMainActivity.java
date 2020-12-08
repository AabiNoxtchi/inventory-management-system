package com.example.inventoryui.Activities.Shared;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.FilterFactory.FilterFactory;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.BaseFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FilterType;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FirstFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.ComparableInputs;
import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.DataAccess.BaseData;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.BaseModel;
import com.example.inventoryui.Models.Shared.PagerVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseMainActivity
        < Item extends BaseModel,
        IndexVM extends BaseIndexVM,
        Filter extends BaseFilterVM,
        ItemData extends BaseData >
        extends AppCompatActivity {

    final String TAG = "MyActivity_baseMain";
    final String TAG2 = "MyActivity_2";

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NestedScrollView nestedScrollView;

    RelativeLayout second_filter_linear_layout ;
    TextView tv ;
    protected String specialFilters;

    ArrayList<Long> idsToDelete;
    int idsToDeleteCount = 0;

    ActionMode actionMode;
    String actionModeTitle = "items selected ";

    ProgressBar progressBar;

    FirstFilterClass filterObj;
    LinearLayout filterLayout;

    int filtersChecked = 0;

    DialogFilterClass dialogFilterObj;
    Dialog filterDialog;
    LinearLayout dialogFilterLayout;

    TextView filters_count_dialog_label;
    TextView dialogFiltersCount;
    TextView second_filters_item_count_dialog_label;

    FilterFactory filterFactory;

    Map<String,Object> lastUrlParameters;

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

    protected static boolean eventMsg = false;

    protected abstract ItemData getItemData();
    protected abstract Filter getNewFilter();
    protected abstract IndexVM getNewIndexVM();
    protected abstract RecyclerView.Adapter getAdapter();
    protected abstract void checkAddFabForLoggedUser();
    protected abstract void checkItemsFromIntent();
    protected abstract void setAdapterMultiSelectFalse();

    protected boolean arrangeFilterDateLayouts(Map<String, TextInputLayout> dialogFilterDateTexts, LinearLayout filterLayout){
       return false;
   }
    protected boolean arrangeFilterIntegerLayouts(Map<String, TextInputLayout> dialogFilterIntegerInputs, LinearLayout filterLayout){
       return false;
   }
    protected  boolean arrangeFilterComparableLayouts(List<ComparableInputs> inputs, LinearLayout filterLayout){return false;}
    protected boolean arrangeFilterSpinners(Map<SearchableSpinner, Pair<String, List>> filterSpinners, LinearLayout filterLayout){return false;}
    protected void handleMsg() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main);

        filterFactory = FilterFactory.getInstance();
        filterFactory.setVariables(this,this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        second_filter_linear_layout = findViewById(R.id.second_filter_linear_layout);
        tv = findViewById(R.id.second_filters_count_dialog_text_view);

        progressBar = findViewById(R.id.progress_bar);
        filters_count_dialog_label = findViewById(R.id.filters_count_dialog_label);
        second_filters_item_count_dialog_label = findViewById(R.id.second_filters_item_count_dialog_label);

        loggedUser = ((AuthenticationManager) this.getApplication()).getLoggedUser();
        checkAddFabForLoggedUser();

        itemData = getItemData();

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        getItems();

        itemData.getIndexVM().observe(this, new Observer<IndexVM>() {
            @Override
            public void onChanged(IndexVM indexVM) {
                model = indexVM;
                setCounts();
                progressBar.setVisibility(View.GONE);

                if (!filtersSet) {
                    filterClass =  model.getFilter().getClass();
                    setFirstFilters();
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

        itemData.getDeletedIds().observe(this, new Observer<ArrayList<Long>>() {
            @Override
            public void onChanged(ArrayList<Long> newProducts) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(BaseMainActivity.this,"deleted "+newProducts.size(),Toast.LENGTH_LONG);

                finishActionMode();
            }
        });
    }

    protected void finishActionMode(){
        idsToDeleteCount = 0;
        idsToDelete = null;
        actionMode.finish();
        model.getFilter().setUrlParameters(lastUrlParameters);
        getItems();
    }

    protected void checkItem(Item item, int position){
        idsToDelete.add(item.getId());
        idsToDeleteCount++;
        adapter.notifyItemChanged(position);
        actionMode.setTitle(actionModeTitle + idsToDeleteCount);
    }

    protected void onLongClick(Item item, int position){
        idsToDelete = new ArrayList<>();
        item.setSelected(true);
        checkItem(item, position);
    }

    protected void ifAdapterMultiSelect(Item item, int position ) {
        item.toggleSelected();
        if (item.isSelected()) {
            checkItem(item, position);
        } else {
            idsToDelete.remove(item.getId());
            idsToDeleteCount--;
            if(idsToDeleteCount == 0){
                finishActionMode();
                return;
            }
            adapter.notifyItemChanged(position);
            actionMode.setTitle(actionModeTitle + idsToDeleteCount);
        }
    }

    protected ActionMode.Callback getActionMode(){
        return
                new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                        MenuInflater inflater=mode.getMenuInflater();
                        inflater.inflate(R.menu.menu_option_delete,menu);
                        actionMode = mode;
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.DeleteAllBtn :
                                deleteItems(idsToDelete);
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                        setAdapterMultiSelectFalse();

                        if(idsToDeleteCount > 0) {
                            items.stream().forEach(i -> i.setSelected(false));
                            adapter.notifyDataSetChanged();
                            idsToDeleteCount = 0;
                            idsToDelete = null;
                        }
                    }
                };
    }

    protected void deleteItems(List<Long> ids){
        itemData.deleteIds(ids);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void getItems() {

        progressBar.setVisibility(View.VISIBLE);

        if (this.model == null) model = getNewIndexVM();
        else if(model.getFilter()!=null) lastUrlParameters = model.getFilter().getUrlParameters();

        checkItemsFromIntent();

        if (model.getPager() == null || !isLoadingMore) {
            model.setPager(new PagerVM());
            model.getPager().setPage(0);
            model.getPager().setItemsPerPage(10);
        }

        itemData.getAll(model);
    }

    protected void filterActivity(){
        if(filterDialog!=null)filterDialog.show();
        else {
            getDialog();
            initializeDialogFields();
            setDialogFilters();

            ImageButton dialogCancelImgBtn = filterDialog.findViewById(R.id.dialogCancelImageButton);
            dialogCancelImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterDialog.dismiss();
                }
            });

            ImageButton dialogSearchImgBtn = filterDialog.findViewById(R.id.dialogSearchImageButton);
            dialogSearchImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    model.getFilter().setUrlParameters(dialogFilterObj.getUrlParameters());//(dialogUrlParameters);
                    getItems();

                    String filtersCount = dialogFiltersCount.getText().toString();
                    setSecondFilterLayout(filtersCount);

                    filterDialog.hide();
                }
            });
        }
    }

    protected void setSecondFilterLayout(String filtersCount){

        filterLayout.setVisibility(View.GONE);
        second_filter_linear_layout.setVisibility(View.VISIBLE);

        tv.setText(filtersCount);//??


        ImageButton second_dialogCancelImgBtn = findViewById(R.id.second_dialogCancelImageButton);
        second_dialogCancelImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!filtersCount.equals(specialFilters)){

                    nullifyDialog();
                    filterDialog = null;
                }else{
                    specialFilters = null;
                }

                filterLayout.setVisibility(View.VISIBLE);
                second_filter_linear_layout.setVisibility(View.GONE);

                filterObj.getFilterCheckBoxes().get("all").performClick();
            }
        });
    }

    protected void addComparableLayout(LinearLayout filterLayout, ComparableInputs input) {
        addTextView(input.getName(), filterLayout);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        ll.addView(input.getMoreLayout());
        ll.addView(input.getLessLayout());

        filterLayout.addView(ll);
        addLine(filterLayout);
    }

    protected void addDateComparableLayout(LinearLayout filterLayout,ComparableInputs input){
        addTextView(input.getName(), filterLayout);
        filterLayout.addView(input.getMoreLayout());
        filterLayout.addView(input.getLessLayout());

        addLine(filterLayout);
    }

    protected void logOut() {

        ((AuthenticationManager) this.getApplication()).logout();
    }

    @Override
    protected void onResume() {
        Log.i(TAG," ***************** on resume ******************");
        super.onResume();
        AuthenticationManager.activityResumed();
        AuthenticationManager.setActiveActivity(this);

        Log.i(TAG," event msg = "+eventMsg);

        if(eventMsg) {
            eventMsg = false;
            handleMsg();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AuthenticationManager.activityPaused();
        AuthenticationManager.setActiveActivity(null);
    }

    public String getSpecialFilters(){
        return specialFilters;
    }

    public void goGetItems(FilterType filterType){

        if(!filterType.equals(FilterType.First)) return;
        if (model.getFilter() != null)
            model.getFilter().setUrlParameters(filterObj.getUrlParameters());
        getItems();
    }

    public Map<String,Object> getUrlParameters(){
        return filterObj.getUrlParameters();
    }

    public Object getUrlObject(String target){
        Map<String,Object> urlParameters = dialogFilterObj!=null && dialogFilterObj.getUrlParameters().size()>0 ?
                dialogFilterObj.getUrlParameters()
                : filterObj.getUrlParameters();

        return urlParameters.get(target);
    }

    public int getFilterCountInt(FilterType filterType) {
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

    public void checkfilterBoxes(int count,String target, CheckBox v, FilterType filterType) {

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

    public void checkfilters(int count,final Map<String,Object> urlParameters,FilterType filterType) {

        if (count > 0) {

            if(urlParameters.containsKey("all"))urlParameters.remove("all");
            if(filterType.equals(FilterType.First))filterObj.getFilterCheckBoxes().get("all").setChecked(false);
            else if(dialogFilterObj!=null)dialogFilterObj.getFilterCheckBoxes().get("all").setChecked(false);
        } else {

            urlParameters.clear();
            urlParameters.put("all",true);
            clearFirstLayout(filterType);
            clearDialogLayout(filterType);
        }
    }

    public String nameToBoolean(String buttonName, Type type){
        if(type.equals(Boolean.class)   ) {
            if (buttonName.startsWith("not")) {
                buttonName = "false";
            }else{
                buttonName = "true";
            }
        }
        return buttonName;
    }

    public void addLine(LinearLayout filterLayout) {

        RelativeLayout.LayoutParams lineparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);

        lineparams.topMargin = 40;
        lineparams.bottomMargin = 40;
        View line = new View(this);

        line.setLayoutParams(lineparams);
        line.setBackgroundColor(getResources().getColor(android.R.color.black));
        filterLayout.addView(line);
    }

    private void setFirstFilters(){
        filterLayout = findViewById(R.id.filter_linear_layout);
        filterObj = new FirstFilterClass(FilterType.First);
        setFilters(filterLayout, filterObj);
    }

    private void setFilters(
            LinearLayout filterLayout,
            BaseFilterClass filterObj ) {

        FilterType filterType = filterObj.getFilterType();

        for (Field f : filterClass.getDeclaredFields()) {

            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) {
                    continue;

                } else if (annotation instanceof CheckBoxAnnotation) {

                    CheckBoxAnnotation chckboxAnnotation = (CheckBoxAnnotation) annotation;

                    String title = chckboxAnnotation.title();

                    if( title.equals("all") || filterType.equals(FilterType.First)) {
                        filterFactory.checkBoxFilter(chckboxAnnotation,filterObj);

                    }else{
                        String target = chckboxAnnotation.target();
                        filterFactory.radioGroupFilter(title, target,f.getType(), filterObj);

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

                    filterFactory.radioGroupFilter(titles, target, f.getType(), filterObj);

                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DropDownAnnotation) {

                    DropDownAnnotation dropDownAnnotation = (DropDownAnnotation) annotation;

                    List<Object> list = (List)getValue(f,model.getFilter());
                    if (list == null) continue;

                    filterFactory.SpinnerFilter(dropDownAnnotation, (DialogFilterClass)filterObj, list);

                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DateAnnotation) {

                    DateAnnotation dateAnnotation = (DateAnnotation) annotation;
                    if(filterType.equals(FilterType.Dialog))
                        filterFactory.dateFilter(dateAnnotation, (DialogFilterClass)filterObj);

                }else if( filterType.equals(FilterType.Dialog) && annotation instanceof IntegerInputAnnotation){

                    IntegerInputAnnotation integerAnnotation = (IntegerInputAnnotation)annotation;

                    filterFactory.integerFilter(integerAnnotation, (DialogFilterClass)filterObj);
                }
            }
        }
        addToLayout(filterLayout,filterObj);
    }

    private void clearFirstLayout(FilterType filterType){

        if(!filterType.equals(FilterType.First)) return;
        for (CheckBox b : filterObj.getFilterCheckBoxes().values()) {
            b.setChecked(false);
        }
        filterObj.getFilterCheckBoxes().get("all").setChecked(true);
        for (RadioGroup rg : filterObj.getFilterRadioGroups().values()) {
            rg.clearCheck();
        }
        filterObj.getCheckedButtons().clear();

    }

    private void clearDialogLayout(FilterType filterType){

        if(!filterType.equals(FilterType.Dialog))return;
        for (CheckBox b : dialogFilterObj.getFilterCheckBoxes().values()) {
            b.setChecked(false);
        }
        dialogFilterObj.getFilterCheckBoxes().get("all").setChecked(true);
        for (RadioGroup rg : dialogFilterObj.getFilterRadioGroups().values()) {
            rg.clearCheck();
        }
        dialogFilterObj.getCheckedButtons().clear();

        for(SearchableSpinner spinner : dialogFilterObj.getFilterSpinners().keySet()){
            if(spinner.getSelectedItemPosition()>0)
                spinner.setSelection(0);
        }
    }

    private void addToLayout(LinearLayout filterLayout,
                             BaseFilterClass filterObj){

        for (CheckBox checkBox : filterObj.getFilterCheckBoxes().values()) {
            filterLayout.addView(checkBox);
            if(filterObj.getFilterType().equals(FilterType.Dialog))addLine(filterLayout);
        }

        for (RadioGroup rg : filterObj.getFilterRadioGroups().values()) {
            filterLayout.addView(rg);
            if(filterObj.getFilterType().equals(FilterType.Dialog))addLine(filterLayout);
        }

        if(filterObj.getFilterType().equals(FilterType.First))return;

        DialogFilterClass dialogFilterObj = (DialogFilterClass)filterObj;

        if(!arrangeFilterSpinners(dialogFilterObj.getFilterSpinners(), filterLayout)) {
            for (SearchableSpinner spinner : dialogFilterObj.getFilterSpinners().keySet()) {

                addSpinnerToLayout(spinner,filterLayout);
            }
        }

        Map<String, TextInputLayout>  filterDateTexts = dialogFilterObj.getFilterDateTexts();
        if(!arrangeFilterDateLayouts( filterDateTexts, filterLayout)) {
            for (TextInputLayout inputLayout : filterDateTexts.values()) {
                filterLayout.addView(inputLayout);
                addLine(filterLayout);
            }
        }

        Map<String,TextInputLayout> filterIntegerInputs = dialogFilterObj.getFilterIntegerInputs();
        if(! arrangeFilterIntegerLayouts(filterIntegerInputs, filterLayout) ) {
            for (TextInputLayout inputLayout : filterIntegerInputs.values()) {
                filterLayout.addView(inputLayout);
                addLine(filterLayout);
            }
        }

        List<ComparableInputs> inputs = dialogFilterObj.getComparableInputs();
        if(! arrangeFilterComparableLayouts(inputs, filterLayout) ) {

            for (ComparableInputs input : inputs) {
                addComparableLayout(filterLayout,input);
            }
        }
    }



    protected void addSpinnerToLayout(SearchableSpinner spinner, LinearLayout filterLayout) {

        TextView textView = new TextView(this);
        textView.setText(dialogFilterObj.getSpinnerTitles().get(spinner));
        textView.setTextAppearance(R.style.text_view_title);

        filterLayout.addView(textView);
        filterLayout.addView(spinner);
        addLine(filterLayout);
    }

    private void addTextView(String name, LinearLayout filterLayout){
        TextView txtView = new TextView(this);
        txtView.setText(name);
        txtView.setTextAppearance(R.style.text_view_title);
        filterLayout.addView(txtView);
    }

    private void setCounts(){
        filters_count_dialog_label.setText("total : "+model.getPager().getItemsCount());
        second_filters_item_count_dialog_label.setText(
                "total : "+model.getPager().getItemsCount());
    }

    private void getDialog(){

        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.filter_dialog_view);

        getWindowMwtrics();
        filterDialog.getWindow().setLayout(windowWidth , windowHeight );

        filterDialog.show();
    }

    private void initializeDialogFields(){

        dialogFilterLayout = filterDialog.findViewById(R.id.filter_dialog_linear_layout);
        dialogFiltersCount = filterDialog.findViewById(R.id.filters_count_dialog_text_view);//filters_count_dialog_text_view
        dialogFiltersCount.setText("0");
    }

    private void setDialogFilters() {

        dialogFilterObj = new DialogFilterClass(FilterType.Dialog);
        setFilters(dialogFilterLayout,dialogFilterObj);
    }

    private void nullifyDialog(){
        dialogFilterObj = null;

    }

    private void getWindowMwtrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        windowHeight = displayMetrics.heightPixels;
        windowWidth = displayMetrics.widthPixels;
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


}
