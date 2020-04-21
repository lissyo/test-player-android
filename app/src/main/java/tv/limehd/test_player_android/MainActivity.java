package tv.limehd.test_player_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    public static String PLAYER_URL = "player_url";

    private Button startPlayerActivity;
    private EditText editTextPlayerUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPlayerActivity = findViewById(R.id.start_player_activity);
        editTextPlayerUrl = findViewById(R.id.edit_text_player_url);

        startPlayerActivity.setOnClickListener(v -> openPlayerActivity());
    }

    private void openPlayerActivity() {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra(PLAYER_URL, editTextPlayerUrl.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
