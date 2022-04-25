package the.bee.wazwan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductShowActivity extends AppCompatActivity {

    TextView ProductName, ProductDesc, ProductPrice, ProductDiscount, ProductTotal;
    ImageView imageView, SendComment;
    EditText AddComment;
    private DatabaseReference getUserData, storeUserDefaultDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);

        ProductName    = findViewById(R.id.ps_name);
        ProductDesc    = findViewById(R.id.ps_desc);
        ProductPrice   = findViewById(R.id.ps_price);
        ProductTotal = findViewById(R.id.ps_total);
        ProductDiscount   = findViewById(R.id.ps_off);
        AddComment   = findViewById(R.id.add_comment);

        imageView   = findViewById(R.id.ps_imageview);
        SendComment   = findViewById(R.id.send_comment);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");

        ProductTotal.setText("200 tk");
        ProductTotal.setPaintFlags(ProductTotal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String android_id = id;
        getUserData = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("products").child(android_id);

        getUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String name    = dataSnapshot.child("name").getValue().toString();
                String phone   = dataSnapshot.child("price").getValue().toString();
              //  String address = dataSnapshot.child("address").getValue().toString();

                if (dataSnapshot.hasChild("image"))
                {
                    String image = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(imageView);
                }
                else
                {
                    Toast.makeText(ProductShowActivity.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();
                }

                ProductName.setText(name);
                ProductPrice.setText(phone);
              //  RestaurantAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addComment = AddComment.getText().toString();

                AddComment.getText().clear();

                sendValue(addComment, android_id);
            }
        });

        Toast.makeText(ProductShowActivity.this, "hello"+android_id, Toast.LENGTH_SHORT).show();
    }

    private void sendValue(String addComment, String android_id) {

        long currentTime= System.currentTimeMillis();
        String customer_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        storeUserDefaultDataReference = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("review").child(android_id+currentTime);
        storeUserDefaultDataReference.child("comment").setValue(addComment);
        storeUserDefaultDataReference.child("product_id").setValue(android_id);
        storeUserDefaultDataReference.child("customer_id").setValue(customer_id);

        Toast.makeText(ProductShowActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
    }
}