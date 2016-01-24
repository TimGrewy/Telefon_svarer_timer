package dk.grewy.telefon_svarer_timer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
    }

    public void doStuff(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        Log.i("someTag", "Selected: " + selectedId);
        if(selectedId == -1) {
            Log.i("bla", "Du har ikke valgt noget!!!");
        } else {
            RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
            if (radioSexButton.getId() == R.id.radioButtonTjek) {
                Log.i("bla bla", "Tjek tid");
                startDailer("*#61#"); //Currently broken
                //startDailer("1##");
            } else if (radioSexButton.getId() == R.id.radioButton15) {
                Log.i("bla bla", "15 valgt");
                startDailer("**61*004540900700**15#");
            } else if (radioSexButton.getId() == R.id.radioButton20) {
                Log.i("bla bla", "20 valgt");
                startDailer("**61*004540900700**20#");
            } else if (radioSexButton.getId() == R.id.radioButton30) {
                Log.i("bla bla", "30 valgt");
                startDailer("**61*004540900700**30#");
            } else if (radioSexButton.getId() == R.id.radioButtonDisable) {
                Log.i("bla bla", "Disable valgt");
                startDailer("##61#"); //Currently broken
            } else {
                Log.i("bla bla", "UKENDT valgt");
            }
        }

    }

    private void startDailer(String dial) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Log.i("startDailer", "tel:"+dial);
        intent.setData(Uri.parse("tel:"+dial));
        startActivity(intent);
    }
}
