package dk.grewy.telefon_svarer_timer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Tim on 29-01-2016.
 */
public class Pop extends Activity {
    List<String> nameList = new ArrayList<>();
    List<String> phoneList = new ArrayList<>();

    ListView lv1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("asd", "Opening popup");
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        lv1 = (ListView) findViewById(R.id.listView);
        if (isPermissionNeeded()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            showContactsWithPermission();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void closePopup(View view) {
        closePopup();
    }

    public void closePopup() {
        Intent intent = new Intent(Pop.this, MainActivity.class);
        TextView telefonSvarerFelt = (TextView) findViewById(R.id.telefonsvarernummer);
        Log.i("asd", "closePop! With value: " + telefonSvarerFelt.getText());
        intent.putExtra("number", telefonSvarerFelt.getText());
        startActivity(intent);
    }

    private boolean isPermissionNeeded() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                showToast("Du har afvist denne rettighed og vi kan derfor ikke læse dine kontakter");
                return true;
            }
        } else {
            return false;
        }
        return true;
    }

    private Cursor showContactsWithPermission() {
        SortedMap<String, String> sortedMap= new TreeMap<String, String>();
        Cursor curser = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (curser.moveToNext()) {
            String name = curser.getString(curser.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = curser.getString(curser.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(name.equalsIgnoreCase("telefonsvarer")) {
                TextView telefonSvarerFelt = (TextView) findViewById(R.id.telefonsvarernummer);
                telefonSvarerFelt.setText(phoneNumber);
                showToast("Telefonsvarer nummer '"+phoneNumber+"' fundet i dine kontakter!");
            }
            if (name != null) {
                sortedMap.put(name, phoneNumber);
            }
        }
        curser.close();


        Set<Map.Entry<String, String>> entries = sortedMap.entrySet();
        for (Map.Entry<String, String> entry: sortedMap.entrySet()) {
            nameList.add(entry.getKey());
            phoneList.add(entry.getValue());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, nameList);

        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNumber = phoneList.get(position);
                TextView telefonSvarerFelt = (TextView) findViewById(R.id.telefonsvarernummer);
                telefonSvarerFelt.setText(selectedNumber);
                closePopup();
            }
        });
        return curser;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("PermissionsResult", "Permission granted!");
                    showContactsWithPermission();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    showToast("Du har afvist rettighed til at læse kontakter");
                }
                return;
            }
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://dk.grewy.telefon_svarer_timer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://dk.grewy.telefon_svarer_timer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}