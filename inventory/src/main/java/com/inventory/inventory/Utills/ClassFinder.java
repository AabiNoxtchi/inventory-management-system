package com.inventory.inventory.Utills;

public class ClassFinder{
	  public static final String[] searchPackages = {
	    "com.inventory.inventory.Model.QProduct",
	    "com.inventory.inventory.Model.QEmployee",
	    "com.inventory.inventory.Model.QUser",
	    };

	  public Class<?> findClassByName(String name) {
	    for(int i=0; i<searchPackages.length; i++){
	      try{
	        return Class.forName(searchPackages[i] + "." + name);
	      } catch (ClassNotFoundException e){
	        //not in this package, try another	     
	      }	    
	    }
	  //nothing found: return null or throw ClassNotFoundException
	    return null;
	}
}