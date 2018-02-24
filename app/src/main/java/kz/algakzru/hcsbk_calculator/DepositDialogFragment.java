package kz.algakzru.hcsbk_calculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class DepositDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb =  new  AlertDialog.Builder(getActivity())
                .setTitle("Ваши взносы")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_vashi_vznosi, null);

        ArrayList<Vznos> vznosi = new ArrayList<Vznos>();
        for (int i = 1; i <= 5; i++) {
            vznosi.add(new Vznos("Product " + i, i * 1000, false));
        }
        VznosAdapter vznosAdapter = new VznosAdapter(getActivity(), vznosi);
        ListView lvVashiVznosi = (ListView) v.findViewById(R.id.lvVashiVznosi);
        lvVashiVznosi.setAdapter(vznosAdapter);

        adb.setView(v);
        return adb.create();
    }
}
