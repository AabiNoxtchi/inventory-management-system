package com.example.inventoryui;


import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.User.FilterVM;
import com.example.inventoryui.Models.User.IndexVM;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
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
    public void testSerialization() throws Exception{

        final ObjectMapper mapper;
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");
        mapper.setDateFormat(df);

        //User(Long id,String userName,String role)

        IndexVM model=new IndexVM();
        List<User> users = new ArrayList<>();
        Long id = Long.valueOf(88);
        User user = new User( id, "user name", Role.ROLE_Mol.name());

        users.add(user);
        model.setItems(users);

        FilterVM filter = new FilterVM();
        filter.setUserName("user name");
        filter.setLastName("last name");
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
        assertEquals(model.getFilter().getUserName(), deserialized.getFilter().getUserName());
        assertEquals(model.getFilter().getLastName(), deserialized.getFilter().getLastName());

        assertEquals(model.getItems().get(0).getId() ,deserialized.getItems().get(0).getId());
        assertEquals(model.getItems().get(0).getUserName() ,deserialized.getItems().get(0).getUserName());
        assertEquals(model.getItems().get(0).getRole() ,deserialized.getItems().get(0).getRole());


    }

    @Test
    public void testReflectionFields(){

        // User Filter VM
        BaseFilterVM filter = new FilterVM();

        Field[] allFields = FilterVM.class.getDeclaredFields();

        assertEquals(15, allFields.length);

        assertTrue(Arrays.stream(allFields).anyMatch(new Predicate<Field>() {
                    @Override
                    public boolean test(Field field) {
                        return field.getName().equals("firstName")
                                && field.getType().equals(String.class);

                    }
                })
        );
        assertTrue(Arrays.stream(allFields).anyMatch(new Predicate<Field>() {
                    @Override
                    public boolean test(Field field) {
                        return field.getName().equals("cityId")
                                && field.getType().equals(Long.class);
                    }
                })
        );

    }


    @Test
    public void listtostringtest(){

        List<Long> ids = new ArrayList<>();
        ids.add((long)4);
        ids.add((long)400);

        String idsStr = ids.toString();

        String listToString = idsStr.substring(1, idsStr.length() - 1);
        listToString = listToString.replaceAll("\\s", "");

        assertEquals("4,400", listToString);

    }
}

