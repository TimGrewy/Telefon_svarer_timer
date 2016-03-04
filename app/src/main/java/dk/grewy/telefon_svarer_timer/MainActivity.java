package dk.grewy.telefon_svarer_timer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RadioGroup radioGroup;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setNumberFromPreviusIntent();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.teleselskaber, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.e("somecraphere", pos + "");
        Resources res = getResources();
        String[] telefonselskaberMedNummer = res.getStringArray(R.array.teleselskabermednummer);
        String selected = telefonselskaberMedNummer[pos];
        String[] split = selected.split("#");
        if(split.length > 1) {
            String nummer = split[1];
            EditText nummerField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
            nummerField.setText(nummer + "");
        }
        if(split[0].equalsIgnoreCase("Mit selskab er ikke på listen")) {
            String msg = "Se dit nummer ved at vælge \"Tjek nuværende tid\" og tryk UDFØR";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing...

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

    public void showPopupWindow(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        Log.i("asd", "showPop!!!");
        startActivity(new Intent(MainActivity.this, Pop.class));
    }

    public void doCommand(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        String number = numberField.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadio = (RadioButton) findViewById(selectedId);
        if(isTelefonSvarerNumberNeeded(selectedRadio) && isEmpty(number)) {
            showToast("Du skal indtaste eller fremsøge nummer til din telefonsvarer");
            return;
        }

        String dialCommand = getDailCommand(number);
        LogActivity.logCommand(dialCommand);

        startDailerOrRequestPermission(dialCommand);
    }

    public void showLog(View view) {
        startActivity(new Intent(MainActivity.this, LogActivity.class));
    }

    public void showCommand(View view) {
        EditText numberField = (EditText) findViewById(R.id.editTextTelefonSvarerNummer);
        String number = numberField.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadio = (RadioButton) findViewById(selectedId);
        if(isTelefonSvarerNumberNeeded(selectedRadio) && isEmpty(number)) {
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

    private boolean isTelefonSvarerNumberNeeded(RadioButton radioSexButton) {
        if (radioSexButton.getId() == R.id.radioButtonTjek) {
            return false;
        } else if (radioSexButton.getId() == R.id.radioButton15) {
            return true;
        } else if (radioSexButton.getId() == R.id.radioButton20) {
            return true;
        } else if (radioSexButton.getId() == R.id.radioButton30) {
            return true;
        } else if (radioSexButton.getId() == R.id.radioButtonDisable) {
            return false;
        }
        return false;
    }

    private boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }
}