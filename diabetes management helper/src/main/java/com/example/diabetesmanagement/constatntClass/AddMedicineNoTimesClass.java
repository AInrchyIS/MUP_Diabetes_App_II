package com.example.diabetesmanagement.constatntClass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.diabetesmanagement.R;
import com.example.diabetesmanagement.ui.home.user.data.management.MedicationFragment;
import com.example.diabetesmanagement.ui.home.user.data.management.MedicationSetupFragment;

public class AddMedicineNoTimesClass {
    Context context;

    public AddMedicineNoTimesClass(Context context) {
        this.context = context;
    }

    public void medicationDialogue(String medicineName) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_medicine_no_times_dialogue);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final CardView doneBtn = dialog.findViewById(R.id.okayBtnMedication);
        final TextView medicineNameTV = dialog.findViewById(R.id.takeMedicineName);
        medicineNameTV.setText("You Take '" + medicineName + "' Medicine");
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
