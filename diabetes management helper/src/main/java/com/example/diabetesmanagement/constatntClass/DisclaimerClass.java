package com.example.diabetesmanagement.constatntClass;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.diabetesmanagement.FirebaseAuthActivity;
import com.example.diabetesmanagement.R;

public class DisclaimerClass {
    FirebaseAuthActivity authActivity;
    SharedPreferenceClass sharedPreferenceClass;

    public DisclaimerClass(FirebaseAuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    public void disclaimerDialogue(final FirebaseAuthActivity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.disclaimer_dialogue_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        sharedPreferenceClass = new SharedPreferenceClass(context);
        final CardView okayBtn = dialog.findViewById(R.id.okayBtn);
        final TextView disAllowBtn = dialog.findViewById(R.id.disallowBtn);
        final CheckBox disclaimerCheckbox = dialog.findViewById(R.id.disclaimerCheckBox);
        disAllowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sharedPreferenceClass.setDisclaimerValue(0);
                context.finishAffinity();
            }
        });
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (disclaimerCheckbox.isChecked()) {
                    sharedPreferenceClass.setDisclaimerValue(1);
                    context.createSignInIntent();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please check the box to agree", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}