package dk.grewy.telefon_svarer_timer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    }

    public void doStuff(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        Log.i("someTag", "Selected: " + selectedId);
        if (selectedId == -1) {
            Log.i("bla", "Du har ikke valgt noget!!!");
        } else {
            RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
            if (radioSexButton.getId() == R.id.radioButtonTjek) {
                Log.i("bla bla", "Tjek tid");
                startDailer("*#61#");
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
                startDailer("##61#");
            } else {
                Log.i("bla bla", "UKENDT valgt");
            }
        }
    }

    private void startDailerWithPermission(String dial) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Log.i("startDailer", "tel:" + dial);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("startDailer", "Missing permissions!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            startActivity(intent);

        }
    }
    private void startDailer(String dial) {
        Log.i("startDialer", "Dialing: " + dial);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // Show an expanation to the user *asynchronously* -- don't block this thread waiting for the user's response! After the user sees the explanation, try again to request the permission.
                Toast.makeText(this, "Brug for rettighed", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
            startDailerWithPermission(dial);
        }
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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
// TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
