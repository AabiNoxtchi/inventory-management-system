package com.example.inventoryui.Utils;

public class Utils {
    public static String ListStringToUrlString(String listString){
       // String listToString = list.toString();
        listString = (listString.substring(1, listString.length() - 1))
                .replaceAll("\\s", "");//replace white spaces

        return listString ;
    }
}
