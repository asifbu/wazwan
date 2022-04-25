package the.bee.wazwan.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import the.bee.wazwan.ProductShowActivity;
import the.bee.wazwan.R;
import the.bee.wazwan.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private View root_view;
    private RecyclerView deptRecycleView;
    private DatabaseReference deptDatabase;
    private FirebaseRecyclerAdapter<Product, RouteViewHolder> mPeopleRVAdapter;

    private FragmentHomeBinding binding;
    

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;

        // start my point to show recycle view

        root_view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar =root_view.findViewById(R.id.progressBarLarge);
        progressBar.setVisibility(View.VISIBLE);


        deptDatabase = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("products");
        deptDatabase.keepSynced(true);

        deptRecycleView = root_view.findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("products");
        Query personsQuery = personsRef.orderByKey();

        deptRecycleView.hasFixedSize();
        deptRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Product>().setQuery(personsQuery, Product.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Product, HomeFragment.RouteViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(HomeFragment.RouteViewHolder holder, final int position, final Product model) {
                holder.setName(model.getName());
                holder.setDesc(model.getDescription());
                holder.setPrice(model.getPhone());

                progressBar.setVisibility(View.GONE);


                holder.food_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "food name", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String Id = model.getId();
                        Intent intent = new Intent(getContext(), ProductShowActivity.class);
                        intent.putExtra("id", Id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public HomeFragment.RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.foodcardview, parent, false);
                HomeFragment.RouteViewHolder viewHolder = new HomeFragment.RouteViewHolder(view);
                return viewHolder;


            }
        };



        deptRecycleView.setAdapter(mPeopleRVAdapter);
        return root_view;

    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView food_name;

        public RouteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            food_name = (TextView) mView.findViewById(R.id.food_name);
        }

        public void setName(String time) {
            TextView food_name = (TextView) mView.findViewById(R.id.food_name);
            food_name.setText(time);
        }

        public void setDesc(String bus) {
            TextView Bus = (TextView) mView.findViewById(R.id.food_desc);
            Bus.setText(bus);
        }

        public void setPrice(String place) {
            TextView Place = (TextView) mView.findViewById(R.id.food_price);
            Place.setText(place);
        }

    }
}
