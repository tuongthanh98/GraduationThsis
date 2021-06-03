package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gradutionthsis.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    private static final String TAG= "SendOTPActivity";

    private FirebaseAuth mAuth;

    private EditText edtInputMoblie;
    private Button btnGetOTP;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);

        mAuth = FirebaseAuth.getInstance();
        edtInputMoblie = (EditText) findViewById(R.id.inputMobile);
        btnGetOTP = (Button) findViewById(R.id.buttonGetOTP);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnGetOTP.setOnClickListener(view -> {
            String mobile = edtInputMoblie.getText().toString().trim();
            if (valid()) {
                progressBar.setVisibility(View.VISIBLE);
                btnGetOTP.setVisibility(View.INVISIBLE);

                sendVerificationCode(mobile);
            }
        });
    }

    /*
     * @author: Nguyễn Thanh Tường
     * date: 04/4/2021 : 18h50p
     * Gửi mã xác thực
     * */
    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + mobile)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(SendOTPActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                        btnGetOTP.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        btnGetOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(SendOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "onVerificationFailed: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Log.d(TAG, "onCodeSent: " + verificationId);
                        progressBar.setVisibility(View.GONE);
                        btnGetOTP.setVisibility(View.VISIBLE);
                        
                        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

    /*
     * @author: Nguyễn Thanh Tường
     * date: 04/4/2021 : 17h45p
     * */
    // [START valid]
    private boolean valid() {
        if (edtInputMoblie.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter your moblie", Toast.LENGTH_SHORT).show();
            edtInputMoblie.requestFocus();
            return false;
        }
        if (edtInputMoblie.getText().toString().trim().length() != 10) {
            Toast.makeText(this, "Phone number must be 10 numbers!!!", Toast.LENGTH_SHORT).show();
            edtInputMoblie.requestFocus();
            edtInputMoblie.selectAll();
            return false;
        }
        return true;
    }
    // [END valid]
}