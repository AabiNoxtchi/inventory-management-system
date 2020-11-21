package com.example.inventoryui.Activities.Test;

import android.util.Log;

import com.example.inventoryui.Models.Shared.PagerVM;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    final String TAG="MyActivity_Test";

    List<String> strings;

    private void generateInitialString(){
        strings = new ArrayList<>();
        for(int i = 0; i<=100;i++){
            strings.add("element : "+i+"\n\n");
        }
    }

      // second test pass
   public List<String> getFromList(Integer page,Integer itemsPerPage){

        if(strings==null)generateInitialString();

        if( page==null )page=0;
        if(itemsPerPage==null)itemsPerPage= 20;

        int fromIndex = page * itemsPerPage;
        int toIndex = fromIndex + itemsPerPage;
        int stringsSize = strings.size();

        if( fromIndex > stringsSize - 1 ) fromIndex = stringsSize - 1;
        if( toIndex > stringsSize - 1 ) toIndex = stringsSize - 1;

        Log.i(TAG," from index ="+fromIndex + " toIndex = "+toIndex);
        List<String> subList = strings.subList(fromIndex, toIndex);
        return subList;
    }

    public List<String> getFromList(PagerVM pager){

        if(strings==null)generateInitialString();

        if( pager.getPage()==null )pager.setPage(0);
        if(pager.getItemsPerPage()==null)pager.setItemsPerPage(20);

        int fromIndex = pager.getPage() * pager.getItemsPerPage();
        int toIndex = fromIndex + pager.getItemsPerPage();
        int stringsSize = strings.size();

        if( fromIndex > stringsSize - 1 ) fromIndex = stringsSize - 1;
        if( toIndex > stringsSize - 1 ) toIndex = stringsSize - 1;

        Log.i(TAG," from index ="+fromIndex + " toIndex = "+toIndex);
        List<String> subList = strings.subList(fromIndex, toIndex);
        return subList;
    }

    public TestClass getData(TestClass testClass){

        if(strings==null)generateInitialString();

        if(testClass.getPager()==null)testClass.setPager(new PagerVM());
        if( testClass.getPager().getPage()==null )testClass.getPager().setPage(0);
        if(testClass.getPager().getItemsPerPage()==null)testClass.getPager().setItemsPerPage(20);

        int fromIndex = testClass.getPager().getPage() * testClass.getPager().getItemsPerPage();
        int toIndex = fromIndex + testClass.getPager().getItemsPerPage();
        int stringsSize = strings.size();

        if( fromIndex > stringsSize - 1 ) fromIndex = stringsSize - 1;
        if( toIndex > stringsSize - 1 ) toIndex = stringsSize - 1;

        Log.i(TAG," from index ="+fromIndex + " toIndex = "+toIndex);
        List<String> subList = strings.subList(fromIndex, toIndex);
        testClass.setStrings(subList);

        testClass.getPager().setItemsCount(stringsSize-1);
        return testClass;

    }
}
