package com.example.inventoryui;

import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.PagerVM;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSeriazation() throws Exception{

        final ObjectMapper mapper;
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");
        mapper.setDateFormat(df);

        IndexVM model=new IndexVM();
        List<Product> prds = new ArrayList<>();
        Product pr = new Product((long)3);
        pr.setName("name");
        prds.add(pr);
        model.setItems(prds);

        FilterVM filter= new FilterVM();
        filter.setName("name");
        filter.setIsDiscarded(true);
        model.setFilter(filter);

        // SER
        String serialized =null;
        try {
            serialized =  mapper.writeValueAsString(model);
        }catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(serialized);
        System.out.println("Serialized JSON: \n"+serialized);

        // DSER
       IndexVM deserialized = mapper.readValue(serialized, IndexVM.class);


        // MATCH
        assertEquals(model.getFilter().getName(), deserialized.getFilter().getName());
        assertEquals(model.getFilter().getIsDiscarded(),deserialized.getFilter().getIsDiscarded());
        System.out.println(deserialized.getItems().get(0).getName());
        System.out.println(deserialized.getFilter().getName());
        System.out.println(deserialized.getFilter().getIsDiscarded());
        System.out.println("Match!");

    }

    @Test
    public void testReflection(){

        BaseFilterVM filter=new FilterVM();

        Field[] allFields = FilterVM.class.getDeclaredFields();

        assertEquals(21, allFields.length);

        assertTrue(Arrays.stream(allFields).anyMatch(new Predicate<Field>() {
                    @Override
                    public boolean test(Field field) {
                        return field.getName().equals("name")
                                && field.getType().equals(String.class);

                    }
                })
        );
        assertTrue(Arrays.stream(allFields).anyMatch(new Predicate<Field>() {
                    @Override
                    public boolean test(Field field) {
                        return field.getName().equals("userId")
                                && field.getType().equals(Long.class);
                    }
                })
        );

    }

    @Test
    public void testReflectionAccessebility() throws IllegalAccessException {
        FilterVM filter = new FilterVM();
       /* filter.setName("name");
        filter.setIsDiscarded(true);*/

        StringBuilder sb = new StringBuilder();
        String prefix = "Filter";
        for (Field f : filter.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(filter) == null) {
                continue;
            }
            sb.append(prefix);
            sb.append(".");
            sb.append(f.getName());
            sb.append("=");
            //f.get(this);
            sb.append(f.get(filter));
            sb.append("&");
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }
        String sbUrl=sb.toString();
        System.out.println("fieldValue = " + sbUrl);
        sb.setLength(0);

        /*String url =filter.getUrl(sb);
        System.out.println(url);
        assertEquals(sbUrl,url);*/
    }

    @Test
    public void testFieldTypes() throws NoSuchFieldException, IllegalAccessException {
       IndexVM model = new IndexVM();

        /*Field[] declaredFields = model.getClass().getDeclaredFields();
        for(Field f : declaredFields){
            System.out.println(f.getName()+" type = "+f.getType());
        }*/
        PagerVM pager = new PagerVM();
        pager.setPrefix("Pager");
        pager.setItemsCount(5);
        model.setPager(pager);

        FilterVM filter = new FilterVM();
        filter.setEmployeeId((long)4);
        filter.setIsDiscarded(true);
        model.setFilter(filter);
        String url= model.getUrl();
        System.out.println("url = "+url);


    }

    @Test
    public void listtostringtest(){

        List<Long> ids = new ArrayList<>();
        ids.add((long)4);
        ids.add((long)400);
        String idsStr = ids.toString();
        System.out.println(idsStr);
        String listToString =idsStr.substring(1, idsStr.length() - 1);
        listToString=listToString.replaceAll("\\s", "");
        //listToString.replace(" ","");
        System.out.println(listToString);

    }
}

