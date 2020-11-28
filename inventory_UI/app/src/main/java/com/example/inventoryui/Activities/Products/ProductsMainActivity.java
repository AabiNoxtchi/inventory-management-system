package com.example.inventoryui.Activities.Products;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Employees.EmployeesMainActivity;
import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Role;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ProductsMainActivity extends BaseMainActivity<Product,IndexVM,FilterVM, ProductsData> {


    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected IndexVM getNewIndexVM() {
        return new IndexVM();
    }

    @Override
    protected FilterVM getNewFilter() {
        return new FilterVM();
    }

    @Override
    protected ProductsData getItemData() {
        return  new ViewModelProvider(this).get(ProductsData.class);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsAdapter(this, super.items, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                i.putExtra("productForUpdate", item);
                startActivity(i);
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item) {

            }
        });
    }

    @Override
    protected void checkAddFabForLoggedUser() {
        if(loggedUser.getRole().equals(Role.ROLE_Mol))
        {
            addFab=findViewById(R.id.addFab);
            addFab.show();
            addFabOnClick();
        }
    }

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void checkIntentAndGetItems() {
        Intent i = getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntent)) {
            productsIdsFromIntentList =  i.getStringExtra(discardedProductsIdsFromIntent);
            getItems();
        }else
            getItems();
    }

    @Override
    protected void checkItemsFromIntent() {
        if(productsIdsFromIntentList != null && productsIdsFromIntentList.length()>1){
            model.setFilter(getNewFilter());
            /***********/
            model.getFilter().setIds( getList(productsIdsFromIntentList) );
        }
    }

    protected boolean arrangeFilterDateTextsInLayout(Map<String, TextInputLayout> dialogFilterDateTexts, LinearLayout filterLayout){

        filterLayout.addView(dialogFilterDateTexts.get("dateCreatedBefore"));
        filterLayout.addView(dialogFilterDateTexts.get("dateCreatedAfter"));
        return true;
    }
    protected boolean arrangeFilterIntegerInputsInLayout(Map<String, TextInputLayout> dialogFilterIntegerInputs, LinearLayout filterLayout){
        filterLayout.addView(dialogFilterIntegerInputs.get("yearsToDiscardFromStartMoreThan"));
        filterLayout.addView(dialogFilterIntegerInputs.get("yearsToDiscardFromStartLessThan"));
        addLine(filterLayout);

        filterLayout.addView(dialogFilterIntegerInputs.get("yearsLeftToDiscardMoreThan"));
        filterLayout.addView(dialogFilterIntegerInputs.get("yearsLeftToDiscardLessThan"));
        addLine(filterLayout);

        filterLayout.addView(dialogFilterIntegerInputs.get("amortizationPercentMoreThan"));
        filterLayout.addView(dialogFilterIntegerInputs.get("amortizationPercentLessThan"));
        addLine(filterLayout);

        filterLayout.addView(dialogFilterIntegerInputs.get("yearsToMAConvertionMoreThan"));
        filterLayout.addView(dialogFilterIntegerInputs.get("yearsToMAConvertionLessThan"));
        addLine(filterLayout);

        filterLayout.addView(dialogFilterIntegerInputs.get("yearsLeftToMAConvertionMoreThan"));
        filterLayout.addView(dialogFilterIntegerInputs.get("yearsLeftToMAConvertionLessThan"));
        addLine(filterLayout);
        return true;
    }

   /* private void addTextChangeListner(TextInputEditText editTxt, TextInputLayout txtLayout, String target, String title) {

        editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                String target1 = setTarget1(target);
                String target2Url = setTarget2Url(target,target1);

                if(s.toString().length()>0) {

                   Integer target1Date = getDate(s.toString());

                    boolean error = checkIfError(target1, target2Url, target1Date);
                    getErrorText(title, error, txtLayout);
                    if(!error){

                        if(txtLayout.isErrorEnabled())txtLayout.setError(null);

                        if(dialogFilterDateTexts.containsKey(target2Url)) {
                            TextInputLayout tl = dialogFilterDateTexts.get(target2Url);
                            if(tl.isErrorEnabled())tl.setError(null);
                        }
                    }
                    if(!dialogUrlParameters.containsKey(target)){
                        baseMainActivity.addFiltersCount(FilterType.Dialog);
                    }
                    dialogUrlParameters.put(target, target1Date);

                }else{

                    if(dialogFilterDateTexts.containsKey(target2Url)) {
                        TextInputLayout tl = dialogFilterDateTexts.get(target2Url);
                        if(tl.isErrorEnabled())tl.setError(null);
                    }

                    dialogUrlParameters.remove(target);
                    baseMainActivity.substractFiltersCount(FilterType.Dialog);

                    if(txtLayout.isErrorEnabled()){
                        txtLayout.setError(null);
                    }
                }
            }
        });
    }

    private boolean checkIfError(String target1, String target2Url, Integer target1Date) {

        if( !(target2Url!=null && dialogUrlParameters.keySet().contains(target2Url))) return null;
        boolean error =false;

        Integer target2Date = (Integer)dialogUrlParameters.get(target2Url);

        if(target1Date.equals(target2Date)) error = DateError.Equal;
        else if( target1.equals(before) && target1Date.before(target2Date)) error = DateError.ErrorBefore;
        else if( target1.equals(after) && target1Date.after(target2Date))  error = DateError.ErrorAfter;

        return error;
    }

    private String setTarget1(String target) {
        if(target.contains(before) || target.contains(after))
            return  target.contains(before) ? before : after;
        return null;
    }

    private String setTarget2Url(String target,String target1) {
        if (target1 == null) target1 = setTarget1(target);
        if(target1 !=null) {
            String target2 = target1.equals(before) ? after : before;
            return target.replace(target1, target2);
        }
        return null;
    }

    private void getErrorText(String title, DateError error, TextInputLayout txtLayout) {

        if(error == null ) return;

        String title1 = title.contains("before") ? "before" : "after";
        String title2 = title1.equals("before") ? "after" : "before";
        String title2Url = title.replace(title1,title2);

        String errorText = error.equals(DateError.Equal) ? (title+" can't be equal to "+title2Url) : (title+" can't be "+title2+" "+title2Url);

        txtLayout.setError(errorText);
        txtLayout.setErrorTextAppearance(R.style.ErrorTextAppearance);
    }

    private Integer getDate(String s) {

        Integer date = null;

            date = Integer.parseInt(s);

        return date;
    }
*/

    public void updateUifromThread(final String event, final String newMsg, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ProductsMainActivity.this)
                        .setTitle(event).setMessage(newMsg+"\n\n"+"show discarded products separately ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productsIdsFromIntentList =  message;
                                getItems();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Long> ids = getList( message );
                        for(Product product:items){
                            for(Long discardedProductId : ids){
                                if(product.getId() == discardedProductId)
                                {
                                    ids.remove(discardedProductId);
                                    product.setDiscarded(true);
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }


    private List getList(String idsString) {
        List<Long> ids = new ArrayList<>();
        idsString = idsString.substring(1, idsString.length() - 1);
        List<String> idsStringList = new ArrayList<>(Arrays.asList(idsString.split(",")));
        for( String id : idsStringList){
            ids.add(Long.valueOf(id));
        }
        return ids;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_main_menu,menu);
        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
             menu.findItem(R.id.employees).setVisible(false);
           // menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logOut();
                /*Intent i=new Intent(ProductsMainActivity.this, MainActivity.class);
                startActivity(i);*/
                return true;
            case R.id.employees:
                //to employees
                Intent toEmployees=new Intent(ProductsMainActivity.this, EmployeesMainActivity.class);
                startActivity(toEmployees);
                return true;

            case R.id.filter_icon:
                filterActivity();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


   /* final String TAG="MyActivity_ProductsMain";
    FloatingActionButton addFab;
    RecyclerView productsRecyclerView ;
    ProductsAdapter productsAdapter;
    ProductsData productsData;
    ArrayList<Product> products;

    *//******************//*
    String discardedProductsIdsFromIntent = "discardedProductsIds";
    String productsIdsFromIntentList;
    *//******************//*
    User loggedUser;
    IndexVM model;

    LinearLayoutManager layoutManager;
    private MyOnScrollListner scrollListener;

    boolean isLoadingMore = false;

    private NestedScrollView productsNestedScrollView;
    //private HorizontalScrollView productsHorizontalBar;
    LinearLayout filterLayout;

    int filtersChecked = 0;
    boolean filtersSet = false;

    List<CheckBox> filterCheckBoxes;
    List<SearchableSpinner> filterSpinners;

    Map<SearchableSpinner, Pair<String, Pair<ArrayAdapter,List>>> filterSpinners2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        setSupportActionBar(toolbar);

        *//*************************************************//*
        loggedUser=((AuthenticationManager)this.getApplication()).getLoggedUser();
        if(loggedUser.getRole().equals(Role.ROLE_Mol))
        {
            addFab=findViewById(R.id.addFab);
            addFab.show();
            addFabOnClick();
        }
        *//******************************//*

        productsData= new ViewModelProvider(this).get(ProductsData.class);

        productsRecyclerView=findViewById(R.id.productsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(layoutManager);

        products=new ArrayList<>();

        *//**************//*
        productsAdapter = new ProductsAdapter(ProductsMainActivity.this, products, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                i.putExtra("productForUpdate", item);
                startActivity(i);
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item) {

            }
        });
        *//*************************//*


        productsRecyclerView.setAdapter(productsAdapter);
        productsNestedScrollView= findViewById(R.id.productsNestedScrollView);
        productsNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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
                                Log.i(TAG, "getting products from nested scroll on scroll ");
                                getProducts();
                            }
                        }
                    }
                }
            }
        });

        Intent i = getIntent();
        if(i.hasExtra(discardedProductsIdsFromIntent)) {
            productsIdsFromIntentList =  i.getStringExtra(discardedProductsIdsFromIntent);
            getProducts();
        }else
            getProducts();

        productsData.getIndexVM().observe(this, new Observer<IndexVM>() {
            @Override
            public void onChanged(IndexVM indexVM) {
                model=indexVM;
                if(!filtersSet) {
                    setFilters();
                    setFilterListners();
                    filtersSet=true;
                }
                if(!isLoadingMore){
                    products.clear();
                    products.addAll(model.getItems());
                    productsAdapter.notifyDataSetChanged();
                    productsNestedScrollView.scrollTo(0,0);
                }else {
                    isLoadingMore = false;
                    products.addAll(model.getItems());
                    productsRecyclerView.post(new Runnable() {
                        public void run() {
                            productsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        } );

        productsRecyclerView.setPadding(0, getToolBarHeight(), 0, 0);



    }

    private void setFilters(){
        filterLayout = findViewById(R.id.products_filter_linear_layout);
        filterCheckBoxes = new ArrayList<>();
        filterSpinners = new ArrayList<>();
        filterSpinners2 = new HashMap<>();

        for (Field f : FilterVM.class.getDeclaredFields()) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) { continue;}
                else if(annotation instanceof ChechBoxAnnotation)
                {
                    ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                    CheckBox chckbox = new CheckBox(getApplicationContext());
                    chckbox.setText(chckboxAnnotation.title());
                    chckbox.setPadding(0,0,50,0);
                    filterCheckBoxes.add(chckbox);
                    if(chckboxAnnotation.title().equals("all"))chckbox.setChecked(true);
                    filterLayout.addView(chckbox);

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
                    searchableSpinner.setSelection(0);
                   // filterSpinners.add(searchableSpinner);
                    filterSpinners2.put(searchableSpinner,new Pair( dropDownAnnotation.target() , new Pair(adapter, list)));
                    filterLayout.addView(searchableSpinner);

                }
            }
        }
    }

    private void setFilterListners(){
        for(CheckBox b : filterCheckBoxes) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fieldName = ((CheckBox) v).getText().toString();
                    Log.i(TAG, "pressed field name = " + fieldName);
                    if ( fieldName.equals("all") && filtersChecked >0 && ((CheckBox) v).isChecked()) {
                        model.setFilter(new FilterVM());
                        model.getFilter().setAll(true);
                        for (CheckBox b : filterCheckBoxes) {
                            if (b.getText().equals("all")) continue;
                            b.setChecked(false);
                        }
                        for(SearchableSpinner spinner : filterSpinners2.keySet()){
                            spinner.setSelection(0);
                        }
                        filtersChecked = 0;
                    } else {
                        if (((CheckBox) v).isChecked()) {
                            filtersChecked++;
                        } else {
                            if(filtersChecked!=0)filtersChecked--;
                        }
                        checkfilterBoxes();

                    }
                    for (Field f : FilterVM.class.getDeclaredFields()) {
                        f.setAccessible(true);
                        Annotation[] annotations = f.getDeclaredAnnotations();
                        for (Annotation annotation : annotations) {
                            if (annotation instanceof ChechBoxAnnotation) {
                                ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                                if (chckboxAnnotation.target().equals(fieldName)) {
                                    try {
                                        f.set(model.getFilter(), ((CheckBox) v).isChecked() ? true : null);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    Log.i(TAG,"filters checked = "+filtersChecked);
                    Log.i(TAG," getting products from checkbox listners");
                    getProducts();
                }
            });
        }


        for(final SearchableSpinner spinner : filterSpinners2.keySet()){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG,"from spinner item selected ");
                    Log.i(TAG,"position = "+position+" id = "+id);
                    Log.i(TAG,"position >0= "+(position>0));
                   // if(position>0){
                       // Log.i(TAG," in position >0= ");
                        filtersChecked++;
                        Pair spinnerOuterPair = filterSpinners2.get(spinner);
                        String fieldTarget = (String)spinnerOuterPair.first;
                        Log.i(TAG,"fieldTarget = "+fieldTarget);
                        for (Field f : FilterVM.class.getDeclaredFields()) {
                            f.setAccessible(true);
                            Log.i(TAG,"f.getname = "+f.getName());
                            if (fieldTarget.equals(f.getName())) {
                                try {
                                    if(position>0) {
                                        Pair pair = (Pair) spinnerOuterPair.second;
                                        SelectItem selectItem = (SelectItem) ((ArrayList) pair.second).get(position);
                                        String value = selectItem.getValue();
                                        Log.i(TAG, "string value = " + value);
                                        if (f.getType().isAssignableFrom(Long.class)) {
                                            f.set(model.getFilter(), Long.parseLong(value));
                                            Log.i(TAG, "f.name = " + f.getName());
                                            Log.i(TAG, "f.value = " + f.get(model.getFilter()));
                                        } else if (f.getType().isAssignableFrom(String.class)) {
                                            f.set(model.getFilter(), value);
                                        }
                                    }else{
                                        if(filtersChecked!=0)filtersChecked--;
                                        f.set(model.getFilter(), null);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                        Log.i(TAG,"getting products from spinners on item selected ");
                        getProducts();
                    *//*}else{
                        if(filtersChecked!=0)filtersChecked--;
                    }*//*
                    Log.i(TAG,"filters checked = "+filtersChecked);
                    checkfilterBoxes();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        Log.i(TAG,"filters checked = "+filtersChecked);
    }

    private void checkfilterBoxes() {
        if (filtersChecked > 0) {
            model.getFilter().setAll(null);
            for (CheckBox b : filterCheckBoxes) {
                if (b.getText().equals("all")) {
                    b.setChecked(false);
                    break;
                }
            }
        } else {
            model.setFilter(new FilterVM());
            model.getFilter().setAll(true);
            for (CheckBox b : filterCheckBoxes) {
                if (b.getText().equals("all")) {
                    b.setChecked(true);
                    break;
                }
            }
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

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                startActivity(i);
            }
        });
    }

    private void getProducts() {

        Log.i(TAG,"+++++++++++++++-getting products +++++++++++++++++++++");
        if(this.model==null) model = new IndexVM();
        *//*if(model.getFilter()==null ){
            model.setFilter(new FilterVM());

        }*//*
        if(productsIdsFromIntentList != null && productsIdsFromIntentList.length()>1){
            model.setFilter(new FilterVM());
            model.getFilter().setIds( getList(productsIdsFromIntentList) ); }
        if(model.getPager()==null || !isLoadingMore){
            model.setPager(new PagerVM());
            model.getPager().setPage(0);
            model.getPager().setItemsPerPage(10);
        }
        productsData.getAll( model);
    }


    private List getList(String idsString) {
        List<Long> ids = new ArrayList<>();
        idsString = idsString.substring(1, idsString.length() - 1);
        List<String> idsStringList = new ArrayList<>(Arrays.asList(idsString.split(",")));
       for( String id : idsStringList){
           ids.add(Long.valueOf(id));
        }
       return ids;
    }

    public void updateUifromThread(final String event, final String newMsg, final String message){
       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                         new AlertDialog.Builder(ProductsMainActivity.this)
                        .setTitle(event).setMessage(newMsg+"\n\n"+"show discarded products separately ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                productsIdsFromIntentList =  message;
                                getProducts();
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Long> ids = getList( message );
                                for(Product product:products){
                                    for(Long discardedProductId : ids){
                                        if(product.getId() == discardedProductId)
                                        {
                                            ids.remove(discardedProductId);
                                            product.setDiscarded(true);
                                            break;
                                        }
                                    }
                                }
                                productsAdapter.notifyDataSetChanged();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.products_main_menu,menu);
        if(loggedUser.getRole().equals(Role.ROLE_Employee)){
           // menu.findItem(R.id.employees).setVisible(false);
            menu.clear();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                ( (AuthenticationManager)this.getApplication()).logout();
                *//*Intent i=new Intent(ProductsMainActivity.this, MainActivity.class);
                startActivity(i);*//*
                return true;
            case R.id.employees:
                //to employees
                Intent toEmployees=new Intent(ProductsMainActivity.this, EmployeesMainActivity.class);
                startActivity(toEmployees);
                return true;
            case R.id.all:
                productsData.getProductsForUser();
                return true;
            case R.id.dma:
                productsData.getProductByType(ProductType.DMA);
                return true;
            case R.id.ma:
                productsData.getProductByType(ProductType.MA);
                return true;
            case R.id.discarded:
                productsData.getDiscardedProducts(true);
                return true;
            case R.id.notDiscarded:
                productsData.getDiscardedProducts(false);
                return true;
            case R.id.available:
                productsData.getAvailableProductsForUser(true);
                return true;
            case R.id.missing:
                productsData.getAvailableProductsForUser(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
    }*/
}

