package com.example.inventoryui.Activities.Test;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Products.MyOnScrollListner;
import com.example.inventoryui.Models.Shared.PagerVM;
import com.example.inventoryui.R;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    final String TAG="MyActivity_Test";
   RecyclerView testRecyclerView;
   ArrayList<String> strings;
   TestAdapter testAdapter;
   LinearLayoutManager layoutManager;
   private TestData testData;

    private MyOnScrollListner scrollListener;

    int page=0,itemsPerPage=40;// test 2

    PagerVM pager; //test 3

    TestClass testClass; //test 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        strings = new ArrayList<>();
        testRecyclerView=findViewById(R.id.testRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        testRecyclerView.setLayoutManager(layoutManager);

        testAdapter = new TestAdapter(strings);
        testRecyclerView.setAdapter(testAdapter);

        testData = new TestData();

        scrollListener = new MyOnScrollListner(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

               //*******loadMoreData(); //first test pass
                // fillstrings(page,itemsPerPage);  // second test
                /*pager.setPage(page); fillStrings(); */  // third test
                Log.i(TAG,"testClass.getPager().getItemsCount() = "+testClass.getPager().getItemsCount());
                Log.i(TAG,"layoutManager.getItemCount() = "+layoutManager.getItemCount());

                if(testClass.getPager().getItemsCount()>layoutManager.getItemCount()) {
                    testClass.getPager().setPage(page);
                    test4();
                }
            }
        };

        testRecyclerView.addOnScrollListener(scrollListener);

        //fillstrings();
        //fillstrings(page,itemsPerPage); //second test

        //fillStrings(); // third test
        test4();


    }

    /* //************ first test pass ***************
    private void loadMoreData(){
        Log.i(TAG,"**** loading more **********");
        ArrayList<String> strings2=new ArrayList<>();
        int count = layoutManager.getItemCount();

        if(count<70)
            for(int i=count;i<count+10;i++){
                strings2.add("element "+i);
            }
        strings.addAll(strings2);
        testAdapter.notifyDataSetChanged();
        count = layoutManager.getItemCount();
        Log.i(TAG,"*****c0unt********"+count);

    }

    private void fillstrings(){

        for(int i=0;i<40;i++){
            strings.add(i,"element \n"+i + "\n");
        }
        testAdapter.notifyDataSetChanged();
        Log.i(TAG,"loaded first 50"+layoutManager.getItemCount());

    }*/


     // second test //pass
   private void fillstrings(int page, int itemsPerPage){

        Log.i(TAG," ******** loading page = "+page +" with items = "+ itemsPerPage);
        strings.addAll(testData.getFromList(page,itemsPerPage));
        testAdapter.notifyDataSetChanged();
        Log.i(TAG,"total count = "+layoutManager.getItemCount());

    }


   private void fillStrings(){ //test 3


       if (pager==null) {
           pager = new PagerVM();
           pager.setPage(0);
           pager.setItemsPerPage(40);
       }
       Log.i(TAG," ******** loading page = "+pager.getPage() +" with items = "+ pager.getItemsPerPage());
       strings.addAll(testData.getFromList(pager));
       testAdapter.notifyDataSetChanged();

       Log.i(TAG,"total count = "+layoutManager.getItemCount());
   }

   private void test4(){

        if(testClass == null)testClass=new TestClass();
        if(testClass.getPager()==null){
            testClass.setPager(new PagerVM());
            testClass.getPager().setPage(0);
            testClass.getPager().setItemsPerPage(40);
        }

        Log.i(TAG," ******** loading page = "+testClass.getPager().getPage() +" with items = "+ testClass.getPager().getItemsPerPage());
        testClass = testData.getData(testClass);
        strings.addAll(testClass.getStrings());
        testAdapter.notifyDataSetChanged();

        Log.i(TAG,"total count = "+layoutManager.getItemCount());

   }

}
