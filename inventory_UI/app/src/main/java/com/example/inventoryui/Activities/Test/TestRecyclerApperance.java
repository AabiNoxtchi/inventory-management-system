package com.example.inventoryui.Activities.Test;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Products.MyOnScrollListner;
import com.example.inventoryui.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class TestRecyclerApperance extends AppCompatActivity {

    final String TAG="MyActivity_Test2";
    ArrayList<String> strings;
    TestAdapter testAdapter;

    LinearLayoutManager layoutManager;
    private TestData testData;

    private MyOnScrollListner scrollListener;

    int page=0,itemsPerPage=40;// test 2

    RecyclerView bestRecyclerView;
    HorizontalScrollView horizontalScrollView;
    NestedScrollView nestedScrollView;

    private SearchableSpinner test_searchablespinner1;
   // gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner searchableSpinner;

    LinearLayout testFilterLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_apperance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
       bestRecyclerView = findViewById(R.id.test_appearance_recycler);
       /* GridLayoutManager mGrid = new GridLayoutManager(this, 2);
        bestRecyclerView.setLayoutManager(mGrid);
        bestRecyclerView.setHasFixedSize(true);
        PhoneAdapter mAdapter = new PhoneAdapter(TestRecyclerApperance.this, getProductTestData());
        bestRecyclerView.setAdapter(mAdapter);*/
        strings = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        bestRecyclerView.setLayoutManager(layoutManager);
        testAdapter = new TestAdapter(strings);
        bestRecyclerView.setAdapter(testAdapter);
        testData = new TestData();
        scrollListener = new MyOnScrollListner(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //*******loadMoreData(); //first test pass
                 fillstrings(page,itemsPerPage);  // second test
            }
        };
       // bestRecyclerView.addOnScrollListener(scrollListener);
        horizontalScrollView = findViewById(R.id.test_horizontal_bar);
        nestedScrollView= findViewById(R.id.testNestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                Log.i(TAG, "scrollX = "+scrollX);
                Log.i(TAG, "scrollY = "+scrollY);
                Log.i(TAG, "oldScrollY = "+oldScrollY);
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                   if (scrollY > oldScrollY)
                    {
                        if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()))
                        {
                            page++;
                            fillstrings(page,itemsPerPage);
                        }
                    }
                   else if(scrollY < oldScrollY)
                   {
                       Log.i(TAG, " ************* scrolling up ************ ");
                       Log.i(TAG, "scrollY = "+scrollY);
                       Log.i(TAG, "oldScrollY = "+oldScrollY);
                       //horizontalScrollView.setVisibility(View.VISIBLE);
                   }
                }
            }
        });
        nestedScrollView.setPadding(0, getToolBarHeight()*3, 0, 0);
        fillstrings(page,itemsPerPage); //second test
      // bestRecyclerView.setPadding(0, getToolBarHeight()*3, 0, 0);

       /* PartSpinner=findViewById(R.id.spinnerPart);

        searchableSpinner = findViewById(R.id.SearchableSpinner);
        //PartSpinner.setPadding(0, getToolBarHeight()*3, 0, 0);
        PartSpinner.setTitle("Select Item");
        //PartSpinner.onscroll
        PartSpinner.setPositiveButton("OK");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
        PartSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings);
        searchableSpinner.setAdapter(adapter2);

        PartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"selected = "+ view.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

       testFilterLayout =  findViewById(R.id.test_filter_layout);
        /* ViewGroup.LayoutParams layoutParams = ViewGroup.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;*/
        test_searchablespinner1 = findViewById(R.id.test_searchablespinner1);
        //test_searchablespinner1.setTitle("Select Item");
       // test_searchablespinner1.setPositiveButton("OK");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strings);
        test_searchablespinner1.setAdapter(adapter1);
       /* test_searchablespinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                {
                    Log.i(TAG,"selected = "+ view.toString());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        } );*/



                    SearchableSpinner searchableSpinner = new SearchableSpinner(this);
                    //searchableSpinner.setGravity(Gravity.CENTER_HORIZONTAL);


                   // searchableSpinner.setLayoutParams(layoutParams);

                   //searchableSpinner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    //f.setAccessible(true);
                   /* List<Object> list=new ArrayList<>();
                    try {
                        list = (ArrayList)f.get(model.getFilter());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }*/
                   //ArrayList<String> list = strings;

                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strings);
                    searchableSpinner.setAdapter(adapter);
                    testFilterLayout.addView(searchableSpinner);

       // gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner spinner2 = new gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner(this);
       /* ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        spinner2.setLayoutParams(layoutParams);
        spinner2.setGravity(Gravity.CENTER_HORIZONTAL);*/

       Spinner spinner2 = new Spinner(this);
        RelativeLayout.LayoutParams lpSpinner = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        spinner2 .setLayoutParams(lpSpinner);
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, strings);
        spinner2.setAdapter(adapter3);
        testFilterLayout.addView(spinner2);


    }

    public int getToolBarHeight() {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = this.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        Log.i(TAG,"tool bar height = "+toolBarHeight);
        return toolBarHeight;
    }
    // second test //pass
    private void fillstrings(int page, int itemsPerPage){
        Log.i(TAG," ******** loading page = "+page +" with items = "+ itemsPerPage);
        strings.addAll(testData.getFromList(page,itemsPerPage));
        bestRecyclerView.post(new Runnable() {
            public void run() {
                testAdapter.notifyDataSetChanged();
            }
        });
       // testAdapter.notifyDataSetChanged();
        Log.i(TAG,"total count = "+layoutManager.getItemCount());
    }
    private List<ProductObject> getProductTestData() {
        List<ProductObject> featuredProducts = new ArrayList<>();
        featuredProducts.add(new ProductObject("Iphone 6", "iphone2"));
        featuredProducts.add(new ProductObject("Iphone 6S", "iphone2"));
        featuredProducts.add(new ProductObject("Iphone 8S", "iphone2"));
        featuredProducts.add(new ProductObject("Iphone X", "iphone2"));
        featuredProducts.add(new ProductObject("Iphone XR", "iphone2"));
        featuredProducts.add(new ProductObject("Iphone XS", "iphone2"));
        return featuredProducts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_test, menu);
        return true;
    }
}
