package com.example.foodiffin.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiffin.R;
import com.example.foodiffin.adapters.HomepageCategoriesAdapter;
import com.example.foodiffin.adapters.HomepageFoodListingAdapter;
import com.example.foodiffin.models.HomepageCategoriesModel;
import com.example.foodiffin.models.HomepageFoodListingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    RecyclerView homepageCategoriesRecyclerView;
    List<HomepageCategoriesModel> homepageCategoriesModelList;
    HomepageCategoriesAdapter homepageCategoriesAdapter;
    EditText searchBox;
    FirebaseFirestore db;

    RecyclerView homepageFoodListingRecyclerView;
    List<HomepageFoodListingModel> homepageFoodListingModelList;
    HomepageFoodListingAdapter homepageFoodListingAdapter;
    boolean status = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment,container,false);
        searchBox = root.findViewById(R.id.searchEditText);
        db = FirebaseFirestore.getInstance();

        homepageCategoriesRecyclerView = root.findViewById(R.id.categoriesView); // It will get recycler view and store it in a recycler view instance
        homepageCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homepageCategoriesModelList = new ArrayList<>(); //array list for category name
        homepageCategoriesAdapter = new HomepageCategoriesAdapter(getActivity(),homepageCategoriesModelList); //create instance of adapter class


        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomepageCategoriesModel homepageCategoriesModel = document.toObject(HomepageCategoriesModel.class);
                                Log.i("Category","Category" + document);
                                homepageCategoriesModelList.add(homepageCategoriesModel);
                                homepageCategoriesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        homepageCategoriesRecyclerView.setAdapter(homepageCategoriesAdapter); //set adapter for the recycler view
        homepageCategoriesRecyclerView.setHasFixedSize(true);
        homepageCategoriesRecyclerView.setNestedScrollingEnabled(false);

        /* Handling Food Listing stuff in Homepage*/
        homepageFoodListingRecyclerView = root.findViewById(R.id.foodListingRecyclerView); // It will get recycler view and store it in a recycler view instance
        homepageFoodListingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        homepageFoodListingModelList = new ArrayList<>(); //array list for category name
        homepageFoodListingAdapter = new HomepageFoodListingAdapter(getContext(),homepageFoodListingModelList);
        homepageFoodListingRecyclerView.setAdapter(homepageFoodListingAdapter); //set adapter for the recycler view

        if(searchBox.getText().toString().isEmpty() && status == true)
        {
            Log.i("Test","Search box empty");
            Query query = db.collection("food").orderBy("foodTitle");
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HomepageFoodListingModel homepageFoodListingModel = document.toObject(HomepageFoodListingModel.class);
                            homepageFoodListingModelList.add(homepageFoodListingModel);
                            homepageFoodListingAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            status = false;
        }


        homepageFoodListingRecyclerView.setHasFixedSize(true);
        homepageFoodListingRecyclerView.setNestedScrollingEnabled(false);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty())
                {
                    Log.i("Test","Empty String");
                    Query query = db.collection("food").orderBy("foodTitle");
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                homepageFoodListingModelList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    HomepageFoodListingModel homepageFoodListingModel = document.toObject(HomepageFoodListingModel.class);
                                    homepageFoodListingModelList.add(homepageFoodListingModel);
                                    homepageFoodListingAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    searchProduct(editable.toString().toLowerCase(Locale.ROOT));
                }
            }
        });
        return root;
    }

    private void searchProduct(String pattern) {
        Query query = db.collection("food").whereLessThanOrEqualTo("foodTitle",pattern + "\uf8ff")
                .whereGreaterThanOrEqualTo("foodTitle",pattern);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    homepageFoodListingModelList.clear();
                    homepageFoodListingAdapter.notifyDataSetChanged();
                    Log.i("Test","Search String");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        HomepageFoodListingModel homepageFoodListingModel = document.toObject(HomepageFoodListingModel.class);
                        homepageFoodListingModelList.add(homepageFoodListingModel);
                        homepageFoodListingAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(),"Hello " + task.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}