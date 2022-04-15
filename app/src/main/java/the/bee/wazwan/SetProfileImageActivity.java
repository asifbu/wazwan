package the.bee.wazwan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetProfileImageActivity extends AppCompatActivity {

    private CircleImageView settingDisplayProfileImage;
    private Button settingChangeProfileImageButton;
    private TextView displayName;
    private final int Gallery_Pick = 1;
    //private FirebaseAuth mAuth;
    private DatabaseReference getUserDataReference;
    private StorageReference storeProfileImage;
    private String imageUrl;
    String android_id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_image);


        settingDisplayProfileImage =  findViewById(R.id.setting_profile_image);
        settingChangeProfileImageButton = findViewById(R.id.setting_profile_image_button);
        displayName = findViewById(R.id.display_name);




        //mAuth = FirebaseAuth.getInstance();
       // String online_user_id = mAuth.getCurrentUser().getUid();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getUserDataReference = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("restaurantdetails").child(android_id);
        storeProfileImage = FirebaseStorage.getInstance().getReference().child("profile_images");

        getUserDataReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                       displayName.setText(name);
                    Picasso.get().load(image).into(settingDisplayProfileImage);
                }
                else
                {
                    Toast.makeText(SetProfileImageActivity.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
            }
        });


        settingChangeProfileImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "select picture"), Gallery_Pick);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Pick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            Uri ImageUri=data.getData();
            final StorageReference  filepath = storeProfileImage.child(android_id +".jpg");

                filepath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            final Uri downUri = task.getResult();

                            //String current_user_Id = mAuth.getCurrentUser().getUid();
                            getUserDataReference = FirebaseDatabase.getInstance("https://wazwan-fdbbf-default-rtdb.firebaseio.com/").getReference().child("restaurantdetails").child(android_id);

                            // Map<String,String> map = new HashMap<>();
                            //  map.put("applicants_image",downUri.toString());


                            getUserDataReference.child("image").setValue(downUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        //   Picasso.get().load(downUri.toString()).into(settingDisplayProfileImage);
                                        Toast.makeText(SetProfileImageActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                        else {
                            Toast.makeText(SetProfileImageActivity.this, "here Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                Toast.makeText(SetProfileImageActivity.this, "not go in if", Toast.LENGTH_SHORT).show();
            }
        }

    }