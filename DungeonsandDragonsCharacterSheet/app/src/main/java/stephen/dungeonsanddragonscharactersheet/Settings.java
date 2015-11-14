package stephen.dungeonsanddragonscharactersheet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class Settings extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        //sets the theme from settings
        int chosenTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt("theme",R.style.blue_grey_theme);
        setTheme(chosenTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //sets the onclick for the save button
        Button saveButton = (Button) findViewById(R.id.settings_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this,CharacterSelectionScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
                startActivity(intent);
            }
        });

        //spinner for theme selector
        Spinner themeSpinner = (Spinner) findViewById(R.id.settings_spinner_color_theme);
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                SharedPreferences.Editor preferencesEditor = settings.edit();

                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                switch (adapterView.getItemAtPosition(i).toString()){
                    case "Select Theme":
                        break;
                    case "Blue/Grey Theme":
                        preferencesEditor.putInt("theme",R.style.blue_grey_theme);
                        finish();
                        startActivity(intent);
                        break;
                    case "Red/Blue Theme":
                        preferencesEditor.putInt("theme", R.style.red_blue_theme);
                        finish();
                        startActivity(intent);
                        break;
                    case "Green/Orange Theme":
                        preferencesEditor.putInt("theme", R.style.green_orange_theme);
                        finish();
                        startActivity(intent);
                        break;
                    case "Yellow/Red Theme":
                        preferencesEditor.putInt("theme", R.style.yellow_red_theme);
                        finish();
                        startActivity(intent);
                        break;
                    case "Original Parchment":
                        preferencesEditor.putInt("theme", R.style.parchment);
                        finish();
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

                preferencesEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}