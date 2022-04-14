package the.bee.wazwan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurentResgisterActivity extends AppCompatActivity {


    private EditText RestaurantName, RestaurantAddress, RestaurantPhone;
    private Button register;
    private DatabaseReference storeUserDefaultDataReference;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_resgister);


        RestaurantName    = findViewById(R.id.rs_name);
        RestaurantAddress = findViewById(R.id.rs_address);
        RestaurantPhone   = findViewById(R.id.rs_phone);
        register          = findViewById(R.id.btn_register);
        //mAuth           = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ResName = RestaurantName.getText().toString();
                String ResAdd  = RestaurantAddress.getText().toString();
                String ResPhn  = RestaurantPhone.getText().toString();
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                Toast.makeText(RestaurentResgisterActivity.this, ResName + android_id, Toast.LENGTH_SHORT).show();
                sendValue(ResName, ResAdd, ResPhn, android_id);
            }
        });
    }

    private void sendValue(String ResName, String ResAdd, String ResPhn, String android_id) {

       // String current_user_Id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataReference = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("restaurantdetails").child(android_id);
        storeUserDefaultDataReference.child("name").setValue(ResName);
        storeUserDefaultDataReference.child("address").setValue(ResAdd);
        storeUserDefaultDataReference.child("phone").setValue(ResPhn);
        storeUserDefaultDataReference.child("id").setValue(android_id);

        Intent intent = new Intent(RestaurentResgisterActivity.this,SetProfileImageActivity.class);
        startActivity(intent);
    }


}
