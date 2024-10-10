package ca.uqac.projetmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.uqac.projetmobile.Model.User;

public class LoginActivity extends AppCompatActivity {
    public EditText emailId;
    public EditText password;
    public EditText name;
    public Button btnSignUp;
    public TextView tvSignIn;
    public FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        name=findViewById(R.id.editTextName);
        emailId = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        btnSignUp = findViewById(R.id.buttonSignIn);
        tvSignIn = findViewById(R.id.notRegisteredTextView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailId.getText().toString();
                final String pwd = password.getText().toString();

                final String nom =name.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Provide enter email id");
                    emailId.requestFocus();
                }
                else if (nom.isEmpty() ){
                   name.setError("Please enter your First Name");
                    name.requestFocus();
                }

                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT);
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && nom.isEmpty() )){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)

                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                if(pwd.length()<6){
                                    Toast.makeText(LoginActivity.this, "Password needs to have 6 character minimum", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                FirebaseUser user = task.getResult().getUser();
                                DatabaseReference newRef = mDatabase.child("users").child(user.getUid());
                                User u = new User(email,nom);

                                newRef.setValue(u);
                                startActivity(new Intent(LoginActivity.this, InterestChoice.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
