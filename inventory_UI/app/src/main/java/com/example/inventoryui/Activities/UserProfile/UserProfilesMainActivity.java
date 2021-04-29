package com.example.inventoryui.Activities.UserProfile;

import android.util.Pair;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.DataAccess.UserProfilesData;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.UserProfile.FilterVM;
import com.example.inventoryui.Models.UserProfile.IndexVM;
import com.example.inventoryui.Models.UserProfile.UserProfile;
import com.example.inventoryui.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserProfilesMainActivity extends BaseMainActivity<UserProfile, IndexVM, FilterVM, UserProfilesData> {

    @Override
    protected UserProfilesData getItemData() {
        return new ViewModelProvider(this).get(UserProfilesData.class);
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

        return new UserProfilesAdapter(this, super.items, new UserProfilesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserProfile item, int position) {

                if (((UserProfilesAdapter) adapter).multiSelect) {

                    ifAdapterMultiSelect(item, position );

                } else {

                    redirectToActivity(item);
                }
            }
        }, new UserProfilesAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(UserProfile item, int position) {

                onLongClick(item,position);
            }
        }, getActionMode());
    }

    private void redirectToActivity(UserProfile item) {
        /********************************/
    }

    @Override
    protected void checkAddFabForLoggedUser() {

        if(loggedUser.getRole().equals(Role.ROLE_Employee))return;
        addFab = findViewById(R.id.addFab);
        addFab.show();
        addFabOnClick();
    }

    @Override
    protected void checkItemsFromIntent() {

    }

    @Override
    protected void setAdapterMultiSelectFalse() {
        ((UserProfilesAdapter)adapter).setMultiSelect(false);
    }

    protected boolean arrangeFilterCheckBoxes(Map<String, CheckBox> filterCheckBoxes, LinearLayout filterLayout){

        getCheckBox("all", filterCheckBoxes, filterLayout);
        getCheckBox("current", filterCheckBoxes, filterLayout);

        if(loggedUser.getRole().equals(Role.ROLE_Mol)){
            getCheckBox("myProfile", filterCheckBoxes, filterLayout);
            getCheckBox("allUser", filterCheckBoxes, filterLayout);
        }

        getCheckBox("withDetail", filterCheckBoxes, filterLayout);
        return true;
    }

    protected boolean arrangeFilterSpinners
            (Map<SearchableSpinner, Pair<String, List>> filterSpinners, LinearLayout filterLayout){

        List<String> targets = new ArrayList<>();
        if(loggedUser.getRole().equals(Role.ROLE_Mol)){
            targets.add("userId");
        }
        targets.add("productId");
        targets.add("productDetailId");

        for(String target : targets){
            SearchableSpinner s = spinner(filterSpinners, target);
            if(s != null) addSpinnerToLayout(s , filterLayout);
        }
        return true;
    }

    private void getCheckBox(String find, Map<String, CheckBox> filterCheckBoxes, LinearLayout filterLayout) {

        Map.Entry<String, CheckBox>  entry = filterCheckBoxes.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(find)).findAny().orElse(null);

        if(entry != null) filterLayout.addView(entry.getValue());//
    }

    private SearchableSpinner spinner
            (Map<SearchableSpinner, Pair<String, List>> filterSpinners, String target){

        SearchableSpinner s = null;
        Map.Entry<SearchableSpinner, Pair<String, List>>  m_e =
                filterSpinners.entrySet().stream()
                        .filter(e -> e.getValue().first.contains(target))
                .findAny().orElse(null);

        if( m_e != null ) s = m_e.getKey() ;
        return s;
    }

    private void addFabOnClick() {
        /******************************/
    }

}
