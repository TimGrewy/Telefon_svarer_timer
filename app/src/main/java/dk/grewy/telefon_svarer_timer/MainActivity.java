package dk.grewy.telefon_svarer_timer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNumberFromPreviusIntent();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

    }

    private void setNumberFromPreviusIntent() {
        Intent intent = getIntent();
        String number = intent.getStringExtra("number");
        if(number != null) {
            number = number.replace(" ", "");
            number = number.replace("-", "");
            number = number.replace("(", "");
            number = number.replace(")", "");
            EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
            numberField.setText(number);
        }
    }

    public void doCommand(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        String number = numberField.getText().toString();
        if(number == null || number.trim().length() == 0) {
            showToast("Du skal indtaste eller fremsøge nummer til din telefonsvarer");
            return;
        }

        String dialCommand = getDailCommand(number);

        startDailerOrRequestPermission(dialCommand);
    }

    public void showPopupWindow(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        Log.i("asd", "showPop!!!");
        startActivity(new Intent(MainActivity.this, Pop.class));
    }

    public void showCommand(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        String number = numberField.getText().toString();
        if(number == null || number.trim().length() == 0) {
            showToast("Du skal indtaste eller fremsøge nummer til din telefonsvarer");
            return;
        }

        String dialCommand = getDailCommand(number);

        showToast(dialCommand);
    }

    private String getDailCommand(String number) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        Log.i("someTag", "Selected: " + selectedId);
        if (selectedId == -1) {
            Log.i("bla", "Du har ikke valgt noget!!!");
            return null;
        } else {
            RadioButton selectedRadio = (RadioButton) findViewById(selectedId);
            return getTeleCommand(number, selectedRadio);
        }
    }

    private String getTeleCommand(String number, RadioButton radioSexButton) {
        if (radioSexButton.getId() == R.id.radioButtonTjek) {
            return "*#61#";
        } else if (radioSexButton.getId() == R.id.radioButton15) {
            return "**61*"+number+"**15#";
        } else if (radioSexButton.getId() == R.id.radioButton20) {
            return "**61*"+number+"**20#";
        } else if (radioSexButton.getId() == R.id.radioButton30) {
            return "**61*"+number+"**30#";
        } else if (radioSexButton.getId() == R.id.radioButtonDisable) {
            return "##61#";
        }
        return "Ukendt valg!";
    }

    private void startDailerOrRequestPermission(String dial) {
        Log.i("startDialer", "Dialing: " + dial);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                showToast("Brug for rettighed for at foretage opkald til din udbyder");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
            startDailerWithPermission(dial);
        }
    }

    private void startDailerWithPermission(String dial) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Log.i("startDailer", "tel:" + dial);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("startDailer", "Missing permissions!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            startActivity(intent);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("PermissionsResult", "Permission granted!");
                    startDailerWithPermission("1324");
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }
}