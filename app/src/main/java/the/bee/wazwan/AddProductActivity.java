package the.bee.wazwan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class AddProductActivity extends AppCompatActivity {

    private EditText ProductName, ProdcutDesc, ProductPrice;
    private Button addProduct;
    private DatabaseReference storeUserDefaultDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ProductName    = findViewById(R.id.ad_name);
        ProdcutDesc    = findViewById(R.id.ad_description);
        ProductPrice   = findViewById(R.id.ad_price);
        addProduct          = findViewById(R.id.btn_add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = ProductName.getText().toString();
                String prodcutDesc  = ProdcutDesc.getText().toString();
                String productPrice  = ProductPrice.getText().toString();
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                sendValue(productName, prodcutDesc, productPrice, android_id);
            }
        });
    }

    private void sendValue(String ProName, String ProDesc, String ProPrice, String android_id) {

        //Date currentTime = Calendar.getInstance().getTime();
        long currentTime= System.currentTimeMillis();
        // String current_user_Id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataReference = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("products").child(android_id+currentTime);
        storeUserDefaultDataReference.child("name").setValue(ProName);
        storeUserDefaultDataReference.child("description").setValue(ProDesc);
        storeUserDefaultDataReference.child("price").setValue(ProPrice);
        storeUserDefaultDataReference.child("id").setValue(android_id+currentTime);

        Intent intent = new Intent(AddProductActivity.this,SetProfileImageActivity.class);
        intent.putExtra("database","products");
        intent.putExtra("id",android_id+currentTime);
        startActivity(intent);
    }
}