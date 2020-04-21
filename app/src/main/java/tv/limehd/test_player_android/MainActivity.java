package tv.limehd.test_player_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    public static String PLAYER_URL = "player_url";
    public static String REDIRECT_IS_SUPPORT = "support_redirect";

    private Button startPlayerActivity;
    private EditText editTextPlayerUrl;

    private CheckBox checkBoxSupportLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPlayerActivity = findViewById(R.id.start_player_activity);
        editTextPlayerUrl = findViewById(R.id.edit_text_player_url);

        checkBoxSupportLabel = findViewById(R.id.check_box_support_label);

        startPlayerActivity.setOnClickListener(v -> openPlayerActivity());
    }

    private void openPlayerActivity() {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra(PLAYER_URL, editTextPlayerUrl.getText().toString());
        intent.putExtra(REDIRECT_IS_SUPPORT, checkBoxSupportLabel.isChecked());
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
