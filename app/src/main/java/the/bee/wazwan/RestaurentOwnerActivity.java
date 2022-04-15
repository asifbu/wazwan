package the.bee.wazwan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class RestaurentOwnerActivity extends AppCompatActivity {

    TextView RestaurantName, RestaurantPhone,
             RestaurantAddress, EditProfile, EditImage,
             AddNewItem, EditItem;

    ImageView RestaurantImage;
    private DatabaseReference getUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_owner);

        RestaurantName      = findViewById(R.id.profile_name);
        RestaurantPhone     = findViewById(R.id.profile_phone);
        RestaurantAddress   = findViewById(R.id.profile_address);
        EditProfile         = findViewById(R.id.profile_edit);
        EditImage           = findViewById(R.id.profile_image_edit);

        AddNewItem          =findViewById(R.id.add_new_item);
        EditItem            =findViewById(R.id.edit_item);
        RestaurantImage     = findViewById(R.id.profile_image);

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getUserData = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("restaurantdetails").child(android_id);

        getUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String name    = dataSnapshot.child("name").getValue().toString();
                String phone   = dataSnapshot.child("phone").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();

                if (dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(RestaurantImage);
                    }
                else
                    {
                        Toast.makeText(RestaurentOwnerActivity.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();
                    }

                RestaurantName.setText(name);
                RestaurantPhone.setText(phone);
                RestaurantAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}