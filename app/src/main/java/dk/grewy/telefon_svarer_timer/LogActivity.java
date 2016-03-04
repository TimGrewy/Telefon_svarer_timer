package dk.grewy.telefon_svarer_timer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.grewy.telefon_svarer_timer.R;

public class LogActivity extends AppCompatActivity {

    private static List<String> commands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ListView lv = (ListView) findViewById(R.id.logList);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                commands );

        lv.setAdapter(arrayAdapter);
    }

    public static void logCommand(String command){
        commands.add(getTimestamp() + ": " + command);
    }

    private static String getTimestamp() {
        CharSequence format = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date());
        return format.toString();
    }
}
