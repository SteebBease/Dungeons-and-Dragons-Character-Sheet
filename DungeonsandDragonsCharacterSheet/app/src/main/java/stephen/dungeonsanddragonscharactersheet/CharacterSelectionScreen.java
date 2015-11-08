//This activity displays the current list of character names from the SQLHelper.TABLE_NAME
//in the database. This list is displayed using a list view and an array adapter. On click method
//is used to move onto the basic stats method. The on long click method is used to edit the
//character using the new character screen. This class also contains the new character button
//which moves the user to the new character screen to input a new character.
//
//


//bug list
//
//
//when deleting a character returns to previous screen.


//Latest update
//Short press now loads the character
//Long press allows you to change/delete the character
//Improved dice rolls, hold to roll multiple dice
//Restructured spell data
//Removed blank ability/spell groups
//

package stephen.dungeonsanddragonscharactersheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CharacterSelectionScreen extends Activity {

    ArrayList<String> characterIDList = new ArrayList<>();
    ArrayList<String> characterNameList = new ArrayList<>();
    ArrayAdapter characterLoadAdapter = null;
    ListView characterListView;
    String imageString = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection_screen);

//        Button importButton = (Button) findViewById(R.id.button_export);
//        importButton.setVisibility(View.GONE);

        //Database Admin
        //Upgrades database
/*        SQLHelper sqlhelper = new SQLHelper(this);
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        sqlhelper.onUpgrade(db,5,6);*/

        //Creates database
        //SQLHelper sqlhelper = new SQLHelper(this);
        //SQLiteDatabase db = sqlhelper.getWritableDatabase();
        //sqlhelper.onCreate(db);

        //displays the database version
        //TextView databaseVersionDisplay = (TextView) findViewById(R.id.title_character);
        //databaseVersionDisplay.setText(databaseVersionDisplay.getText().toString() + System.getProperty("line.separator") + SQLHelper.VERSION);

        //puts the data into the arrays
        Cursor characterLoadCursor = null;
        try {
            characterLoadCursor = SQLHelper.setupDatabase(this, SQLHelper.TABLE_NAME, null, null, null);
        }
        catch(Exception e){
            Log.e(e.toString(),"e");
        }

        //sets the listview
        characterListView = (ListView) findViewById(R.id.listviewCharacterSelect);

        //populates the arrays from the listview
        if ((characterLoadCursor != null) &&
                (characterLoadCursor.getCount()>0)){
            do {
                characterIDList.add(characterLoadCursor.getString(characterLoadCursor.getColumnIndex(SQLHelper.C_ID)));
                characterNameList.add(characterLoadCursor.getString(characterLoadCursor.getColumnIndex(SQLHelper.CHARACTER_NAME)));
            } while (characterLoadCursor.moveToNext());
        }

        //sets up the adapter for the list view
        characterLoadAdapter = new ArrayAdapter(this,R.layout.list_view_row,R.id.listViewRowText,characterNameList);

        //sets the listview adapter
        characterListView.setAdapter(characterLoadAdapter);

        if (characterLoadCursor != null) {
            characterLoadCursor.close();
        }

        Button addCharacter = (Button) findViewById(R.id.button_new_character);
        addCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addModifyCharacter(0,null,0);
            }
        });

        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SQLHelper.settingUpdate(CharacterSelectionScreen.this,characterNameList.get(position), characterIDList.get(position));
                SQLHelper.databaseIDUpdate(CharacterSelectionScreen.this,null,null,SQLHelper.CHARACTER_NAME_LINK + "=?",new String[]{characterNameList.get(position)},characterIDList.get(position));
                Intent intentBasicStats = new Intent(CharacterSelectionScreen.this,BasicStats.class);
                startActivity(intentBasicStats);
                CharacterSelectionScreen.this.overridePendingTransition(0,0);
            }
        });

        characterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            addModifyCharacter(1,characterIDList.get(position),position);
            return true;
            }
        });

        Button exportButton = (Button) findViewById(R.id.button_export);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBasicStats = new Intent(CharacterSelectionScreen.this,ImportExport.class);
                startActivity(intentBasicStats);
                CharacterSelectionScreen.this.overridePendingTransition(0,0);
            }
        });
    }

    EditText charname = null;
    EditText charage = null;
    EditText charano = null;
    EditText charap = null;
    EditText charbackground = null;
    EditText charbackstory = null;
    EditText charbarbarianlvl = null;
    EditText charbardlvl = null;
    EditText charcha = null;
    EditText charclericlvl = null;
    EditText charcon = null;
    EditText chardex = null;
    EditText chardruidlvl = null;
    EditText chareye = null;
    EditText charfighterlvl = null;
    EditText charhair= null;
    EditText charheight = null;
    EditText charhp = null;
    EditText charint = null;
    EditText charlp = null;
    EditText charmonklvl = null;
    EditText charpaladinlvl = null;
    EditText charrangerlvl = null;
    EditText charroguelvl = null;
    CheckBox charspcha = null;
    CheckBox charspcon = null;
    CheckBox charspdex = null;
    CheckBox charspint = null;
    CheckBox charspstr = null;
    CheckBox charspwis = null;
    CheckBox characrobatics = null;
    CheckBox charanimalhandling = null;
    CheckBox chararcana = null;
    CheckBox charathletics = null;
    CheckBox chardeception = null;
    CheckBox charhistory = null;
    CheckBox charinsight = null;
    CheckBox charintimidation = null;
    CheckBox charinvestigation = null;
    CheckBox charmedicine = null;
    CheckBox charnature = null;
    CheckBox charperception = null;
    CheckBox charperformance = null;
    CheckBox charpersuasion = null;
    CheckBox charreligion = null;
    CheckBox charslightofhand = null;
    CheckBox charstealth = null;
    CheckBox charsurvival = null;
    EditText charskin = null;
    EditText charsorcerer = null;
    EditText charspeed = null;
    EditText charstr = null;
    EditText chartp = null;
    EditText charwarlocklvl = null;
    EditText charalignment = null;
    EditText charwp = null;
    EditText charweight = null;
    EditText charwis = null;
    EditText charwizardlvl = null;
    EditText charrace = null;
    EditText charpt = null;
    EditText charideals = null;
    EditText charbonds = null;
    EditText charflaws = null;
    EditText charL1 = null;
    EditText charL2 = null;
    EditText charL3 = null;
    EditText charL4 = null;
    EditText charL5 = null;
    EditText charL6 = null;
    EditText charL7 = null;
    EditText charL8 = null;
    EditText charL9 = null;
    EditText charinitiative = null;
    
    public void addModifyCharacter(final int modify, final String characterID, final int position){
        Cursor characterCursor = null;
        if (characterID != null) {
            characterCursor = SQLHelper.setupDatabase(CharacterSelectionScreen.this,SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?",new String[]{characterID});
        }
        SQLHelper sqlhelper = new SQLHelper(CharacterSelectionScreen.this);
        final SQLiteDatabase characterDB = sqlhelper.getWritableDatabase();
        final AlertDialog.Builder addModifyCharacterDialog = new AlertDialog.Builder(CharacterSelectionScreen.this);

        //all the text boxes in the new character view
        View inflaterView = getLayoutInflater().inflate(R.layout.new_character_screen,null);

        charname = (EditText) inflaterView.findViewById(R.id.new_char_char_name);
        charage = (EditText) inflaterView.findViewById(R.id.new_char_age);
        charano = (EditText) inflaterView.findViewById(R.id.new_char_allies_and_organisations);
        charap = (EditText) inflaterView.findViewById(R.id.new_char_armor_proficiencies);
        charbackground = (EditText) inflaterView.findViewById(R.id.new_char_background);
        charbackstory = (EditText) inflaterView.findViewById(R.id.new_char_backstory);
        charbarbarianlvl = (EditText) inflaterView.findViewById(R.id.new_char_barbarian);
        charbardlvl = (EditText) inflaterView.findViewById(R.id.new_char_bard);
        charcha = (EditText) inflaterView.findViewById(R.id.new_char_charisma);
        charclericlvl = (EditText) inflaterView.findViewById(R.id.new_char_cleric);
        charcon = (EditText) inflaterView.findViewById(R.id.new_char_constitution);
        chardex = (EditText) inflaterView.findViewById(R.id.new_char_dexterity);
        chardruidlvl = (EditText) inflaterView.findViewById(R.id.new_char_druid);
        chareye = (EditText) inflaterView.findViewById(R.id.new_char_eye_color);
        charfighterlvl = (EditText) inflaterView.findViewById(R.id.new_char_fighter);
        charhair= (EditText) inflaterView.findViewById(R.id.new_char_hair_color);
        charheight = (EditText) inflaterView.findViewById(R.id.new_char_height);
        charhp = (EditText) inflaterView.findViewById(R.id.new_char_hp);
        charint = (EditText) inflaterView.findViewById(R.id.new_char_intelligence);
        charlp = (EditText) inflaterView.findViewById(R.id.new_char_language_proficiencies);
        charmonklvl = (EditText) inflaterView.findViewById(R.id.new_char_monk);
        charpaladinlvl = (EditText) inflaterView.findViewById(R.id.new_char_paladin);
        charrangerlvl = (EditText) inflaterView.findViewById(R.id.new_char_ranger);
        charroguelvl = (EditText) inflaterView.findViewById(R.id.new_char_rogue);
        charspcha = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_charisma);
        charspcon = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_constitution);
        charspdex = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_dexterity);
        charspint = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_intelligence);
        charspstr = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_strength);
        charspwis = (CheckBox) inflaterView.findViewById(R.id.new_char_saving_throw_wisdom);
        characrobatics = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_acrobatics);
        charanimalhandling = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_animal_handling);
        chararcana = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_arcana);
        charathletics = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_athletics);
        chardeception = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_deception);
        charhistory = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_history);
        charinsight = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_insight);
        charintimidation = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_intimidation);
        charinvestigation = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_investigation);
        charmedicine = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_medicine);
        charnature = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_nature);
        charperception = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_perception);
        charperformance = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_performance);
        charpersuasion = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_persuasion);
        charreligion = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_religion);
        charslightofhand = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_slight_of_hand);
        charstealth = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_stealth);
        charsurvival = (CheckBox) inflaterView.findViewById(R.id.new_char_skill_survival);
        charskin = (EditText) inflaterView.findViewById(R.id.new_char_skin_color);
        charsorcerer = (EditText) inflaterView.findViewById(R.id.new_char_sorcerer);
        charspeed = (EditText) inflaterView.findViewById(R.id.new_char_speed);
        charstr = (EditText) inflaterView.findViewById(R.id.new_char_strength);
        chartp = (EditText) inflaterView.findViewById(R.id.new_char_tool_proficiencies);
        charwarlocklvl = (EditText) inflaterView.findViewById(R.id.new_char_warlock);
        charalignment = (EditText) inflaterView.findViewById(R.id.new_char_alignment);
        charwp = (EditText) inflaterView.findViewById(R.id.new_char_weapon_proficiencies);
        charweight = (EditText) inflaterView.findViewById(R.id.new_char_weight);
        charwis = (EditText) inflaterView.findViewById(R.id.new_char_wisdom);
        charwizardlvl = (EditText) inflaterView.findViewById(R.id.new_char_wizard);
        charrace = (EditText) inflaterView.findViewById(R.id.new_char_race);
        charpt = (EditText) inflaterView.findViewById(R.id.new_char_personality);
        charideals = (EditText) inflaterView.findViewById(R.id.new_char_ideals);
        charbonds = (EditText) inflaterView.findViewById(R.id.new_char_bonds);
        charflaws = (EditText) inflaterView.findViewById(R.id.new_char_flaws);
        charL1 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L1);
        charL2 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L2);
        charL3 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L3);
        charL4 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L4);
        charL5 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L5);
        charL6 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L6);
        charL7 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L7);
        charL8 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L8);
        charL9 = (EditText) inflaterView.findViewById(R.id.new_char_spell_L9);
        charinitiative = (EditText) inflaterView.findViewById(R.id.new_char_initiative);

        addModifyCharacterDialog.setView(inflaterView);
        
        //decides the text for the positive button
        String positiveButtonText = "Initialised";
        if (modify == 1){
            positiveButtonText = "Update";
            addModifyCharacterDialog.setTitle("Update Character");
        }
        else if (modify == 0){
            positiveButtonText = "Save";
            addModifyCharacterDialog.setTitle("Save Character");
        }
        
        //populates the text boxes if they are being modified.
        if (modify == 1){
            charname.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_NAME)));
            charage.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_AGE)));
            charano.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_ALLIES_AND_ORGANISATIONS)));
            charap.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_ARMOR_PROFICIENCIES)));
            charbackground.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_BACKGROUND)));
            charbackstory.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_BACKSTORY)));
            charbarbarianlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN)));
            charbardlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_BARD)));
            charcha.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)));
            charclericlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_CLERIC)));
            charcon.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_CON)));
            chardex.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)));
            chardruidlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_DRUID)));
            chareye.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_EYE_COLOR)));
            charfighterlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_FIGHTER)));
            charhair.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_HAIR_COLOR)));
            charheight.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_HEIGHT)));
            charhp.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
            charint.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_INT)));
            charlp.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_LANGUAGE_PROFICIENCIES)));
            charmonklvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_MONK)));
            charpaladinlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_PALADIN)));
            charrangerlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_RANGER)));
            charroguelvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_ROGUE)));
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CHA)) == 1) {
                charspcha.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CON)) == 1) {
                charspcon.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_DEX)) == 1) {
                charspdex.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_INT)) == 1) {
                charspint.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_STR)) == 1) {
                charspstr.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_WIS)) == 1) {
                charspwis.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ACROBATICS)) == 1) {
                characrobatics.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING)) == 1) {
                charanimalhandling.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ARCANA)) == 1) {
                chararcana.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ATHLETICS)) == 1) {
                charathletics.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_DECEPTION)) == 1) {
                chardeception.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_HISTORY)) == 1) {
                charhistory.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INSIGHT)) == 1) {
                charinsight.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INTIMIDATION)) == 1) {
                charintimidation.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INVESTIGATION)) == 1) {
                charinvestigation.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_MEDICINE)) == 1) {
                charmedicine.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_NATURE)) == 1) {
                charnature.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERCEPTION)) == 1) {
                charperception.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERFORMANCE)) == 1) {
                charperformance.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERSUASION)) == 1) {
                charpersuasion.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_RELIGION)) == 1) {
                charreligion.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND)) == 1) {
                charslightofhand.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_STEALTH)) == 1) {
                charstealth.setChecked(true);
            }
            if (characterCursor.getInt(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SURVIVAL)) == 1) {
                charsurvival.setChecked(true);
            }
            charskin.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SKIN_COLOR)));
            charsorcerer.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SORCERER)));
            charspeed.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_SPEED)));
            charstr.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_STR)));
            chartp.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_TOOL_PROFICIENCIES)));
            charwarlocklvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_WARLOCK)));
            charalignment.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_ALIGNMENT)));
            charwp.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_WEAPON_PROFICIENCIES)));
            charweight.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_WEIGHT)));
            charwis.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)));
            charwizardlvl.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_WIZARD)));
            charrace.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_RACE)));
            charpt.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_PERSONALITY_TRAITS)));
            charideals.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_IDEALS)));
            charbonds.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_BONDS)));
            charflaws.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_FLAWS)));
            charL1.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)));
            charL2.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)));
            charL3.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)));
            charL4.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)));
            charL5.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)));
            charL6.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)));
            charL7.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)));
            charL8.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)));
            charL9.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)));
            charinitiative.setText(characterCursor.getString(characterCursor.getColumnIndex(SQLHelper.CHARACTER_INITIATIVE)));
            if (!characterCursor.isClosed()) {
                characterCursor.close();
            }
        }

        addModifyCharacterDialog.setPositiveButton(positiveButtonText,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        addModifyCharacterDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (modify == 1){
            addModifyCharacterDialog.setNeutralButton("Delete Character", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //sets up the database to delete the items
                    SQLHelper sqlhelperDelete = new SQLHelper(CharacterSelectionScreen.this);
                    final SQLiteDatabase characterDBDelete = sqlhelperDelete.getWritableDatabase();


                    //builds the delete confirmation dialog box
                    AlertDialog.Builder deleteConfirmationDialog = new AlertDialog.Builder(CharacterSelectionScreen.this);
                    deleteConfirmationDialog.setTitle("Delete Character?");
                    deleteConfirmationDialog.setMessage("Are you sure you want to delete " + characterNameList.get(position) + "? This cannot be undone!");

                    //deletes the selected icon if yes is pressed
                    deleteConfirmationDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //removes character information from all tables
                            try {
                                characterDBDelete.delete(SQLHelper.TABLE_NAME, SQLHelper.C_ID + "=?", new String[]{characterIDList.get(position)});
//                                characterDBDelete.delete(SQLHelper.ITEM_TABLE_NAME, SQLHelper.CHARACTER_NAME_LINK + "=?", new String[]{characterNameList.get(position)});
//                                characterDBDelete.delete(SQLHelper.SPELL_TABLE_NAME, SQLHelper.SPELL_CHARACTER_NAME_LINK + "=?", new String[]{characterNameList.get(position)});
                                characterDBDelete.delete(SQLHelper.ITEM_TABLE_NAME, SQLHelper.ITEM_CHARACTER_ID_LINK + "=?", new String[]{characterIDList.get(position)});
                                characterDBDelete.delete(SQLHelper.SPELL_TABLE_NAME, SQLHelper.SPELL_CHARACTER_ID_LINK + "=?", new String[]{characterIDList.get(position)});
                                characterIDList.remove(position);
                                characterNameList.remove(position);
                            }
                            catch(Exception e){

                            }

                            //refreshes the screen
                            Intent intentCharacterSelection = new Intent(CharacterSelectionScreen.this,CharacterSelectionScreen.class);
                            startActivity(intentCharacterSelection);
                            CharacterSelectionScreen.this.overridePendingTransition(0,0);
                            finish();
                            dialog.dismiss();
                        }
                    });

                    deleteConfirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    //shows the dialog box
                    AlertDialog confirmationDialog = deleteConfirmationDialog.create();
                    confirmationDialog.show();

                }
            });
        }

        final AlertDialog addModifyCharacterAlert = addModifyCharacterDialog.create();
        addModifyCharacterAlert.setCanceledOnTouchOutside(false);
        addModifyCharacterAlert.show();

        Button cancelButton = addModifyCharacterAlert.getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setHeight(200);

        Button neutralButton = addModifyCharacterAlert.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setHeight(200);

        Button validationButton = addModifyCharacterAlert.getButton(DialogInterface.BUTTON_POSITIVE);
        validationButton.setText(positiveButtonText);
        validationButton.setHeight(200);
        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCharacterInformation(modify, characterDB, characterID);
                addModifyCharacterAlert.dismiss();
            }
        });

    }

    public void saveCharacterInformation(int modify, SQLiteDatabase characterDB, String characterID){
        //This requires some Validation in here!!!
        if (charname.length() == 0){
            Toast.makeText(CharacterSelectionScreen.this,"Your character needs a name",Toast.LENGTH_SHORT).show();
            return;
        }
        if (charhp.length() == 0){
            Toast.makeText(CharacterSelectionScreen.this,"Your character needs HP",Toast.LENGTH_SHORT).show();
            return;
        }
        if (charspeed.length() == 0){
            Toast.makeText(CharacterSelectionScreen.this,"Your character needs a speed",Toast.LENGTH_SHORT).show();
            return;
        }
        if ((charstr.length() == 0) ||
                (chardex.length() == 0) ||
                (charcon.length() == 0) ||
                (charint.length() == 0) ||
                (charwis.length() == 0) ||
                (charcha.length() == 0)){
            Toast.makeText(CharacterSelectionScreen.this,"Your character needs some stats",Toast.LENGTH_SHORT).show();
            return;
        }
        if ((charbarbarianlvl.length() > 0) &&
                (charbardlvl.length() > 0) &&
                (charclericlvl.length() > 0) &&
                (chardruidlvl.length() > 0) &&
                (charfighterlvl.length() > 0) &&
                (charmonklvl.length() > 0) &&
                (charpaladinlvl.length() > 0) &&
                (charrangerlvl.length() > 0) &&
                (charroguelvl.length() > 0) &&
                (charsorcerer.length() > 0) &&
                (charwarlocklvl.length() > 0) &&
                (charwizardlvl.length() > 0)){
            Toast.makeText(CharacterSelectionScreen.this,"Your character needs a level",Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues characterCV = new ContentValues();

        characterCV.put(SQLHelper.CHARACTER_NAME, charname.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_AGE, charage.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_ALLIES_AND_ORGANISATIONS, charano.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_ARMOR_PROFICIENCIES, charap.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_BACKGROUND, charbackground.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_BACKSTORY, charbackstory.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_BARBARIAN, charbarbarianlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_BARD, charbardlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_CHA, charcha.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_CLERIC, charclericlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_CON, charcon.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_DEX, chardex.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_DRUID, chardruidlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_EYE_COLOR, chareye.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_FIGHTER, charfighterlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_HAIR_COLOR, charhair.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_HEIGHT, charheight.getText().toString());
        if(modify == 0) {
            characterCV.put(SQLHelper.CHARACTER_HP, charhp.getText().toString());
        }
        characterCV.put(SQLHelper.CHARACTER_MAX_HP, charhp.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_INT, charint.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_LANGUAGE_PROFICIENCIES, charlp.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_MONK, charmonklvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_PALADIN, charpaladinlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_RANGER, charrangerlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_ROGUE, charroguelvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_CHA, charspcha.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_CON, charspcon.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_DEX, charspdex.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_INT, charspint.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_STR, charspstr.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SAVE_PROF_WIS, charspwis.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_ACROBATICS, characrobatics.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING, charanimalhandling.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_ARCANA, chararcana.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_ATHLETICS, charathletics.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_DECEPTION, chardeception.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_HISTORY, charhistory.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_INSIGHT, charinsight.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_INTIMIDATION, charintimidation.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_INVESTIGATION, charinvestigation.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_MEDICINE, charmedicine.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_NATURE, charnature.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_PERCEPTION, charperception.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_PERFORMANCE, charperformance.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_PERSUASION, charpersuasion.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_RELIGION, charreligion.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND, charslightofhand.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_STEALTH, charstealth.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKILL_SURVIVAL, charsurvival.isChecked());
        characterCV.put(SQLHelper.CHARACTER_SKIN_COLOR, charskin.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_SORCERER, charsorcerer.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_SPEED, charspeed.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_STR, charstr.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_TOOL_PROFICIENCIES, chartp.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_WARLOCK, charwarlocklvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_ALIGNMENT, charalignment.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_WEAPON_PROFICIENCIES, charwp.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_WEIGHT, charweight.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_WIS, charwis.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_WIZARD, charwizardlvl.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_RACE, charrace.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_INITIATIVE, charinitiative.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_PERSONALITY_TRAITS, charpt.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_IDEALS, charideals.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_BONDS, charbonds.getText().toString());
        characterCV.put(SQLHelper.CHARACTER_FLAWS, charflaws.getText().toString());
        if(charL1.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L1_SPELL_MAX, charL1.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L1_SPELL_MIN, charL1.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L1_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L1_SPELL_MIN, "0");
        }
        if(charL2.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L2_SPELL_MAX, charL2.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L2_SPELL_MIN, charL2.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L2_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L2_SPELL_MIN, "0");
        }
        if(charL3.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L3_SPELL_MAX, charL3.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L3_SPELL_MIN, charL3.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L3_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L3_SPELL_MIN, "0");
        }
        if(charL4.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L4_SPELL_MAX, charL4.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L4_SPELL_MIN, charL4.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L4_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L4_SPELL_MIN, "0");
        }
        if(charL5.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L5_SPELL_MAX, charL5.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L5_SPELL_MIN, charL5.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L5_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L5_SPELL_MIN, "0");
        }
        if(charL6.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L6_SPELL_MAX, charL6.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L6_SPELL_MIN, charL6.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L6_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L6_SPELL_MIN, "0");
        }
        if(charL7.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L7_SPELL_MAX, charL7.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L7_SPELL_MIN, charL7.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L7_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L7_SPELL_MIN, "0");
        }
        if(charL8.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L8_SPELL_MAX, charL8.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L8_SPELL_MIN, charL8.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L8_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L8_SPELL_MIN, "0");
        }
        if(charL9.getText().toString() != null){
            characterCV.put(SQLHelper.CHARACTER_L9_SPELL_MAX, charL9.getText().toString());
            characterCV.put(SQLHelper.CHARACTER_L9_SPELL_MIN, charL9.getText().toString());
        }
        else
        {
            characterCV.put(SQLHelper.CHARACTER_L9_SPELL_MAX, "0");
            characterCV.put(SQLHelper.CHARACTER_L9_SPELL_MIN, "0");
        }
        String test = null;
        if (imageString != null){
            characterCV.put(SQLHelper.CHARACTER_IMAGE, imageString);
        }
        else if (modify == 0){
            characterCV.put(SQLHelper.CHARACTER_IMAGE,"");
        }

        String toastText = null;
        if (modify == 1){
            characterDB.update(SQLHelper.TABLE_NAME,characterCV,SQLHelper.C_ID + "=?",new String[]{characterID});
            toastText = "Character Updated";
        }
        else if (modify == 0){
            characterDB.insert(SQLHelper.TABLE_NAME,null,characterCV);
            toastText = "Character Saved";
        }
        characterDB.close();

        Intent refreshCharacters = new Intent(CharacterSelectionScreen.this,CharacterSelectionScreen.class);
        startActivity(refreshCharacters);
        CharacterSelectionScreen.this.overridePendingTransition(0,0);
        Toast.makeText(CharacterSelectionScreen.this,toastText,Toast.LENGTH_SHORT).show();

    }

    //selects the image to put into the image view
    public static final int RESULT_LOAD_IMAGE = 1;
    Bitmap bitmap;

    public void chooseImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imageString = cursor.getString(columnIndex);
            cursor.close();
        }
    }
}
