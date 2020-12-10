package com.example.inventoryui.Activities.Products;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.User.UserAddActivity;
import com.example.inventoryui.DataAccess.ProductsData;
import com.example.inventoryui.DataAccess.UsersData;
import com.example.inventoryui.Models.Product.FilterVM;
import com.example.inventoryui.Models.Product.IndexVM;
import com.example.inventoryui.Models.Product.Product;
import com.example.inventoryui.Models.Product.SelectProduct;
import com.example.inventoryui.Models.Product.Selectable;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.example.inventoryui.Models.User.Role;
import com.example.inventoryui.Models.User.User;
import com.example.inventoryui.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductToUser2 extends BaseMainActivity<Product, IndexVM, FilterVM, ProductsData> {

    static String TAG = "productsToUser2";

    UsersData usersData;
    User employeeFromIntent;
    Long employeeFromIntentId;
    String selectedUserName;

    SearchableSpinner userNameSpinner ;
    int chkSpinner = 0;

    List<SelectItem> userNames;
    List<Product> freeProducts;

    Dialog dialog;
    SelectProductAdapter rAdapter;
    RecyclerView r;
    ArrayList<SelectProduct> selectProducts;


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
        return new ViewModelProvider(this).get(ProductsData.class);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsAdapter(this, super.items, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product item, int position) {

                if (((ProductsAdapter) adapter).multiSelect) {

                    ifAdapterMultiSelect(item, position );

                } else {
                   /* Intent i = new Intent(ProductsMainActivity.this, ProductAddActivity.class);
                    i.putExtra("productForUpdate", item);
                    startActivity(i);*/
                }
            }
        }, new ProductsAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Product item, int position) {

                onLongClick(item,position);

            }
        }, getActionMode());
    }

    @Override
    protected void setAdapterMultiSelectFalse() {
        ((ProductsAdapter)adapter).setMultiSelect(false);
    }

    @Override
    protected void checkAddFabForLoggedUser() {
        if(loggedUser.getRole().equals(Role.ROLE_Mol))
        {
            addFab = findViewById(R.id.addFab);
            addFab.show();
            addFabOnClick();
        }
    }

    private void addFabOnClick() {
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show select dialog

             itemData.getFreeProducts().observe(ProductToUser2.this, new Observer<Selectable>() {
                 @Override
                 public void onChanged(Selectable selectable) {

                     List<SelectProduct> selectProducts = selectable.getSelectProducts();
                    /* for(SelectProduct p : selectProducts){
                         Log.i(TAG,"p.name = "+p.getName());
                     }*/

                     // Log.i(TAG,"on change selectProducts.size  = "+selectProducts.size());

                     if (dialog == null) {
                         dialog = new Dialog(ProductToUser2.this);
                         getDialog(dialog, R.layout.filter_dialog_view);
                         initializeDialogFields(dialog);

                         TextView tv1 = dialog.findViewById(R.id.dialog_title_textView);
                         tv1.setVisibility(View.GONE);
                        // tv1.setText("Add Products");

                         SearchView sv = dialog.findViewById(R.id.NameSearch);
                         sv.setVisibility(View.VISIBLE);
                         sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                             @Override
                             public boolean onQueryTextSubmit(String arg0) {
                                 return false;
                             }
                             @Override
                             public boolean onQueryTextChange(String query) {
                                 //Toast.makeText(StartActivity.this,query,Toast.LENGTH_SHORT);
                                 rAdapter.filter(query);

                                 return false;
                             }
                         });
                         TextView tv2 = dialog.findViewById(R.id.filters_count_dialog_label);
                         tv2.setText("total : ");
                         ImageView saveImg = dialog.findViewById(R.id.dialogSearchImageButton);
                         saveImg.setImageResource(R.drawable.save);



                         ImageButton dialogCancelImgBtn = dialog.findViewById(R.id.dialogCancelImageButton);
                         dialogCancelImgBtn.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 dialog.dismiss();
                             }
                         });

                         saveImg.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 List<SelectProduct> selected = rAdapter.getSelected();
                                 if (selected != null)
                                     for (SelectProduct p : selected)
                                         Log.i(TAG, "p.name = " + p.getName() + " p.count = " + p.getCount());
                                 selectable.setSelectProducts(selected);
                                 selectable.setEmpId(employeeFromIntentId);
                                 itemData.fillIds(selectable);
                                 itemData.getFilledIds().observe(ProductToUser2.this, new Observer<Selectable>() {
                                     @Override
                                     public void onChanged(Selectable s) {
                                         if (s.getSelectProducts().size() > 0) getItems();
                                         dialog.dismiss();
                                     }
                                 });
                             }
                         });
                     }

                     setAddDialogContent(selectProducts);
                     dialogFiltersCount.setText(selectable.getCount() + "");
                     dialog.show();
                 }
             });
            }
        });
    }

    private void setAddDialogContent(List<SelectProduct> list) {

        if(r == null) {
            r = new RecyclerView(ProductToUser2.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            r.setLayoutManager(layoutManager);

            selectProducts = new ArrayList<>();
            selectProducts.addAll(list);
            rAdapter = new SelectProductAdapter(ProductToUser2.this, selectProducts, dialogFiltersCount);
            r.setAdapter(rAdapter);

            dialogFilterLayout.addView(r);
        }else{
            selectProducts.clear();
            selectProducts.addAll(list);
            rAdapter.notifyDataSetChanged();
        }

    }

    protected void completeInitialization() {
        usersData = new ViewModelProvider(this).get(UsersData.class);

        userNameSpinner = findViewById(R.id.userNameSpinner);
        userNameSpinner.setVisibility(View.VISIBLE);
        userNameSpinner.setTitle("select employee");



        Intent i=getIntent();
        if(i.hasExtra("userForUpdate")) {
            employeeFromIntent = (User) i.getSerializableExtra("userForUpdate");
            employeeFromIntentId = employeeFromIntent.getId();
            selectedUserName = employeeFromIntent.getUserName();

        }else if(i.hasExtra("userIdForUpdate")) {//employeeIdForUpdate
            employeeFromIntentId = (long) i.getLongExtra("userIdForUpdate", 0);
            usersData.getById(employeeFromIntentId);
            usersData.getItem().observe(ProductToUser2.this, new Observer<User>() {
                @Override
                public void onChanged(User employee) {
                    employeeFromIntent = employee;
                    employeeFromIntentId = employee.getId();
                    selectedUserName = employee.getUserName();
                }
            });
        }

       itemData.getNullifiedIds().observe(ProductToUser2.this, new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> longs) {
                finishActionMode();
            }
        });

    }

    protected void removeUnwantedCheckBoxes(Map<String, CheckBox> filterCheckBoxes) {
        String freeProduct = "freeProducts";
        if(filterCheckBoxes.containsKey(freeProduct))filterCheckBoxes.remove(freeProduct);
    }

    @Override
    protected void checkItemsFromIntent() {

        if (employeeFromIntentId != null) {
            if(model.getFilter()==null)model.setFilter(new FilterVM());
            model.getFilter().getUrlParameters().put("employeeId",employeeFromIntentId);

        }
    }

    protected void CheckModel(IndexVM indexVM) {}

    protected void completeSetUp() {

        if(userNames != null) return;
        userNames = model.getFilter().getEmployeenames();
        ArrayAdapter adapter = new ArrayAdapter(ProductToUser2.this, android.R.layout.simple_spinner_dropdown_item, userNames);
        userNameSpinner.setAdapter(adapter);
        int selectedIndex = userNames.indexOf( new SelectItem(selectedUserName) );
        userNameSpinner.setSelection(selectedIndex);
         chkSpinner-=1;

        userNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(++chkSpinner<1)return;
                if(position == 0) return;
                SelectItem selected = userNames.get(position);
                employeeFromIntentId = Long.parseLong(selected.getValue());
                employeeFromIntent=null;
                selectedUserName = selected.getName();
                getItems();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void deleteItems(List<Long> ids){
        itemData.nullifyIds(ids);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_add_menu2,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.icon_delete_user:
                new AlertDialog.Builder(ProductToUser2.this)
                        .setTitle("Delete User").setMessage(" sure you want to delete "
                        +  selectedUserName+" ?")
                        .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //// delete employee
                                usersData.deleteId(employeeFromIntentId);
                                usersData.getDeletedId().observe(ProductToUser2.this, new Observer<Long>() {
                                    @Override
                                    public void onChanged(Long aLong) {
                                        if(Objects.equals(aLong,employeeFromIntentId)) {
                                            Toast.makeText(ProductToUser2.this, "User has been deleted !!!", Toast.LENGTH_LONG);

                                            userNames = null;
                                            employeeFromIntent=null;
                                            employeeFromIntentId=null;
                                            selectedUserName=null;
                                            getItems();

                                        }else{
                                            Toast.makeText(ProductToUser2.this, "User could't be deleted !!!", Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
                return true;
            case R.id.icon_edit_user:
                Intent i = new Intent(ProductToUser2.this, UserAddActivity.class);
                i.putExtra("userIdForUpdate", employeeFromIntentId);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
