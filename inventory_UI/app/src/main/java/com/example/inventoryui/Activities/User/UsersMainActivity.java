package com.example.inventoryui.Activities.User;

import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.User.FilterVM;
import com.example.inventoryui.Models.User.IndexVM;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersMainActivity extends BaseMainActivity<User, IndexVM, FilterVM, UsersData> {

    @Override
    protected UsersData getItemData() {
        return new ViewModelProvider(this).get(UsersData.class);
    }

    @Override
    protected FilterVM getNewFilter() {
        return new FilterVM();
    }

    @Override
    protected IndexVM getNewIndexVM() {
        return new IndexVM();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {

        return new UsersAdapter(this, super.items, loggedUser.getRole(), new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User item, int position) {

                if (((UsersAdapter) adapter).multiSelect) {

                    ifAdapterMultiSelect(item, position );

                } else {

                    redirectToActivity(item);

                }
            }
        }, new UsersAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(User item, int position) {

                onLongClick(item,position);
            }
        }, getActionMode());
    }

    private void redirectToActivity(User item) {

        Intent i = new Intent(UsersMainActivity.this, UserAddActivity.class);
        i.putExtra("userIdForUpdate", item.getId());
        startActivity(i);
    }

    @Override
    protected void checkAddFabForLoggedUser() {
        addFab = findViewById(R.id.addFab);
        addFab.show();
        addFabOnClick();
    }

    @Override
    protected void checkItemsFromIntent() {
    }

    @Override
    protected void setAdapterMultiSelectFalse(){((UsersAdapter)adapter).setMultiSelect(false);
    }

    protected boolean arrangeFilterDateLayouts(Map<String, TextInputLayout> filterDateTexts, LinearLayout filterLayout){
        if(loggedUser.getRole().equals(Role.ROLE_Admin))
            return false;
        else return true;

    }
    protected boolean arrangeFilterSpinners(Map< SearchableSpinner, Pair<String, List>> filterSpinners, LinearLayout filterLayout){

        List<String> targets = new ArrayList<>();
        targets.add("userName");
        targets.add("firstName");
        targets.add("lastName");
        targets.add("email");

        if(loggedUser.getRole().equals(Role.ROLE_Admin)){
            targets.add("countryId");
            targets.add("cityId");
        }

        for(String target : targets){
            SearchableSpinner s = spinner(filterSpinners, target);
            if(s != null) addSpinnerToLayout(s , filterLayout);
        }
        return true;
    }

    private SearchableSpinner spinner(Map<SearchableSpinner, Pair<String, List>> filterSpinners, String target){
        SearchableSpinner s = null;
                     Map.Entry<SearchableSpinner, Pair<String, List>>  m_e =   filterSpinners.entrySet().stream().filter(
                                e -> e.getValue().first.contains(target) )
                                .findAny().orElse(null);
                   if( m_e != null ) s = m_e.getKey() ;
                   return s;
    }

    private void addFabOnClick() {

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UsersMainActivity.this, UserAddActivity.class);
                i.putExtra("userIdForUpdate", -1);
                startActivity(i);
            }
        });
    }

}
