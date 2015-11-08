package stephen.dungeonsanddragonscharactersheet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by stephen.beasley on 04/05/15.
 */


public class ImportExport extends Activity{

    ArrayList<String> charNames;
    Cursor allChars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_export);

        //gets the data and populates the array list
        allChars = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,null,null);
        charNames = new ArrayList<>();
        if(allChars.getCount()>0) {
            do {
                charNames.add(allChars.getString(allChars.getColumnIndex(SQLHelper.CHARACTER_NAME)));
            } while (allChars.moveToNext());
            //Adds the data to the spinner
            ArrayAdapter charSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,charNames);
            charSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner charSpinner = (Spinner) findViewById(R.id.IE_Spinner);
            charSpinner.setAdapter(charSpinnerAdapter);
        }

        //loads the characters from the directory
        File myDir = new File(Environment.getExternalStorageDirectory(), "Exported Characters");
        ArrayList<String> charactersImport = new ArrayList<>();
        for (File f : myDir.listFiles()){
            charactersImport.add(f.getName());
//            Toast.makeText(this,f.getName(), Toast.LENGTH_LONG).show();
        }

        //Adds the data to the spinner
        ArrayAdapter importSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,charactersImport);
        importSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner importSpinner = (Spinner) findViewById(R.id.IE_Spinner_Import);
        importSpinner.setAdapter(importSpinnerAdapter);

    }

    public void exportCharacter (View v){
        String selectedChar = null;
        String fileName = null;

        //gets the select character and creates the filename.
        Spinner charSpinner = (Spinner) findViewById(R.id.IE_Spinner);
        selectedChar = charSpinner.getSelectedItem().toString();
        fileName = selectedChar + " Export.txt";
        
        //Loads the cursors for the data
        Cursor characterInfo = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{selectedChar});
        Cursor spellInfo = SQLHelper.setupDatabase(this,SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_NAME_LINK + "=?", new String[]{selectedChar});
        Cursor itemInfo = SQLHelper.setupDatabase(this, SQLHelper.ITEM_TABLE_NAME,null,SQLHelper.CHARACTER_NAME_LINK + "=?", new String[]{selectedChar});

        //creates the file
        File file = null;
        File newFolder = null;
        try {
            newFolder = new File(Environment.getExternalStorageDirectory(),"Exported Characters");
            if (!newFolder.exists()){
                newFolder.mkdir();
            }
            try{
                file = new File(newFolder, fileName);
                try {
                    file.delete();
                } catch (Exception e){}
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"2",Toast.LENGTH_LONG).show();
        }

        //writes the data to the string
        String dataString = null;
        
        dataString = "Character " + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.C_ID)) + System.getProperty("line.separator");
        dataString = dataString + "Name:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_NAME)) + System.getProperty("line.separator");
        dataString = dataString + "Age:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_AGE)) + System.getProperty("line.separator");
        dataString = dataString + "Allies and Organisations:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ALLIES_AND_ORGANISATIONS)) + System.getProperty("line.separator");
        dataString = dataString + "Armor Proficiencies:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ARMOR_PROFICIENCIES)) + System.getProperty("line.separator");
        dataString = dataString + "Background:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_BACKGROUND)) + System.getProperty("line.separator");
        dataString = dataString + "Backstory:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_BACKSTORY)) + System.getProperty("line.separator");
        dataString = dataString + "Barbarian Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN)) + System.getProperty("line.separator");
        dataString = dataString + "Bard Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_BARD)) + System.getProperty("line.separator");
        dataString = dataString + "Charisma:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CHA)) + System.getProperty("line.separator");
        dataString = dataString + "Cleric Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CLERIC)) + System.getProperty("line.separator");
        dataString = dataString + "Constitution:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CON)) + System.getProperty("line.separator");
        dataString = dataString + "Dexterity:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEX)) + System.getProperty("line.separator");
        dataString = dataString + "Druid Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DRUID)) + System.getProperty("line.separator");
        dataString = dataString + "Eye Colour:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_EYE_COLOR)) + System.getProperty("line.separator");
        dataString = dataString + "Fighter Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_FIGHTER)) + System.getProperty("line.separator");
        dataString = dataString + "Hair Colour:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HAIR_COLOR)) + System.getProperty("line.separator");
        dataString = dataString + "Height:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HEIGHT)) + System.getProperty("line.separator");
        dataString = dataString + "Current HP:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HP)) + System.getProperty("line.separator");
        dataString = dataString + "Intelligence:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_INT)) + System.getProperty("line.separator");
        dataString = dataString + "Language Proficiencies:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_LANGUAGE_PROFICIENCIES)) + System.getProperty("line.separator");
        dataString = dataString + "Monk Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_MONK)) + System.getProperty("line.separator");
        dataString = dataString + "Paladin Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_PALADIN)) + System.getProperty("line.separator");
        dataString = dataString + "Ranger Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_RANGER)) + System.getProperty("line.separator");
        dataString = dataString + "Rogue Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ROGUE)) + System.getProperty("line.separator");
        dataString = dataString + "Charisma Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CHA)) + System.getProperty("line.separator");
        dataString = dataString + "Constitution Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CON)) + System.getProperty("line.separator");
        dataString = dataString + "Dexterity Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_DEX)) + System.getProperty("line.separator");
        dataString = dataString + "Intelligence Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_INT)) + System.getProperty("line.separator");
        dataString = dataString + "Strength Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_STR)) + System.getProperty("line.separator");
        dataString = dataString + "Wisdom Saving Throw Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_WIS)) + System.getProperty("line.separator");
        dataString = dataString + "Acrobatics Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_ACROBATICS)) + System.getProperty("line.separator");
        dataString = dataString + "Animal Handling Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING)) + System.getProperty("line.separator");
        dataString = dataString + "Arcana Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_ARCANA)) + System.getProperty("line.separator");
        dataString = dataString + "Athletics Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_ATHLETICS)) + System.getProperty("line.separator");
        dataString = dataString + "Deception Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_DECEPTION)) + System.getProperty("line.separator");
        dataString = dataString + "History Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_HISTORY)) + System.getProperty("line.separator");
        dataString = dataString + "Insight Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_INSIGHT)) + System.getProperty("line.separator");
        dataString = dataString + "Intimidation Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_INTIMIDATION)) + System.getProperty("line.separator");
        dataString = dataString + "Investigation Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_INVESTIGATION)) + System.getProperty("line.separator");
        dataString = dataString + "Medicine Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_MEDICINE)) + System.getProperty("line.separator");
        dataString = dataString + "Nature Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_NATURE)) + System.getProperty("line.separator");
        dataString = dataString + "Perception Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERCEPTION)) + System.getProperty("line.separator");
        dataString = dataString + "Performance Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERFORMANCE)) + System.getProperty("line.separator");
        dataString = dataString + "Persuasion Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERSUASION)) + System.getProperty("line.separator");
        dataString = dataString + "Religion Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_RELIGION)) + System.getProperty("line.separator");
        dataString = dataString + "Sleight of Hand Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND)) + System.getProperty("line.separator");
        dataString = dataString + "Stealth Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_STEALTH)) + System.getProperty("line.separator");
        dataString = dataString + "Survival Proficiency:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKILL_SURVIVAL)) + System.getProperty("line.separator");
        dataString = dataString + "Skin Colour:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SKIN_COLOR)) + System.getProperty("line.separator");
        dataString = dataString + "Sorcerer Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SORCERER)) + System.getProperty("line.separator");
        dataString = dataString + "Speed:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPEED)) + System.getProperty("line.separator");
        dataString = dataString + "Strength:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_STR)) + System.getProperty("line.separator");
        dataString = dataString + "Tool Proficiencies:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_TOOL_PROFICIENCIES)) + System.getProperty("line.separator");
        dataString = dataString + "Warlock Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WARLOCK)) + System.getProperty("line.separator");
        dataString = dataString + "Alignment:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ALIGNMENT)) + System.getProperty("line.separator");
        dataString = dataString + "Weapon Proficiencies:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WEAPON_PROFICIENCIES)) + System.getProperty("line.separator");
        dataString = dataString + "Weight:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WEIGHT)) + System.getProperty("line.separator");
        dataString = dataString + "Wisdom:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WIS)) + System.getProperty("line.separator");
        dataString = dataString + "Wizard Level:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WIZARD)) + System.getProperty("line.separator");
        dataString = dataString + "Race:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_RACE)) + System.getProperty("line.separator");
        dataString = dataString + "Max HP:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)) + System.getProperty("line.separator");
        dataString = dataString + "Initiative Bonus:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_INITIATIVE)) + System.getProperty("line.separator");
        dataString = dataString + "PP:" + characterInfo.getInt(characterInfo.getColumnIndex(SQLHelper.CHARACTER_PP)) + System.getProperty("line.separator");
        dataString = dataString + "GP:" + characterInfo.getInt(characterInfo.getColumnIndex(SQLHelper.CHARACTER_GP)) + System.getProperty("line.separator");
        dataString = dataString + "EP:" + characterInfo.getInt(characterInfo.getColumnIndex(SQLHelper.CHARACTER_EP)) + System.getProperty("line.separator");
        dataString = dataString + "SP:" + characterInfo.getInt(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SP)) + System.getProperty("line.separator");
        dataString = dataString + "CP:" + characterInfo.getInt(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CP)) + System.getProperty("line.separator");
        dataString = dataString + "Personality Traits:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_PERSONALITY_TRAITS)) + System.getProperty("line.separator");
        dataString = dataString + "Ideals:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_IDEALS)) + System.getProperty("line.separator");
        dataString = dataString + "Bonds:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_BONDS)) + System.getProperty("line.separator");
        dataString = dataString + "Flaws:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_FLAWS)) + System.getProperty("line.separator");
        dataString = dataString + "Notes:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_NOTES)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L4:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L5:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L6:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L7:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L8:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Current Spell L9:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L4:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L5:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L6:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L7:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L8:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Max Spell L9:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)) + System.getProperty("line.separator");
        dataString = dataString + "Image:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_IMAGE)) + System.getProperty("line.separator");
        dataString = dataString + "Image Rotation:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ROTATION)) + System.getProperty("line.separator");
        dataString = dataString + "Option 1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_STRENGTH_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEXTERITY_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CONSTITUTION_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 4:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_INTELLIGENCE_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 5:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WISDOM_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 6:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CHARISMA_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 7:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ABILITIES_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 8:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_CANTRIP_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 9:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_1)) + System.getProperty("line.separator");
        dataString = dataString + "Option 10:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_2)) + System.getProperty("line.separator");
        dataString = dataString + "Option 11:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_3)) + System.getProperty("line.separator");
        dataString = dataString + "Option 12:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_4)) + System.getProperty("line.separator");
        dataString = dataString + "Option 13:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_5)) + System.getProperty("line.separator");
        dataString = dataString + "Option 14:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_6)) + System.getProperty("line.separator");
        dataString = dataString + "Option 15:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_7)) + System.getProperty("line.separator");
        dataString = dataString + "Option 16:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_8)) + System.getProperty("line.separator");
        dataString = dataString + "Option 17:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_9)) + System.getProperty("line.separator");
        dataString = dataString + "Option 18:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_SPELL_CASTING)) + System.getProperty("line.separator");
        dataString = dataString + "Option 19:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_PROFICIENCIES_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 20:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_WEAPONS_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 21:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_ARMOR_VISIBLE)) + System.getProperty("line.separator");
        dataString = dataString + "Option 22:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_INVENTORY)) + System.getProperty("line.separator");
        dataString = dataString + "XP:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_EXPERIENCE)) + System.getProperty("line.separator");
        dataString = dataString + "Temporary HP:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_TEMPORARY_HP)) + System.getProperty("line.separator");
        dataString = dataString + "Hit Dice D6:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D6)) + System.getProperty("line.separator");
        dataString = dataString + "Hit Dice D8:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D8)) + System.getProperty("line.separator");
        dataString = dataString + "Hit Dice D10:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D10)) + System.getProperty("line.separator");
        dataString = dataString + "Hit Dice D12:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D12)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Success 1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_SUCCESS_1)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Success 2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_SUCCESS_2)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Success 3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_SUCCESS_3)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Fail 1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_FAIL_1)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Fail 2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_FAIL_2)) + System.getProperty("line.separator");
        dataString = dataString + "Death Check Fail 3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_DEATH_FAIL_3)) + System.getProperty("line.separator");
        dataString = dataString + "Other 1:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_1)) + System.getProperty("line.separator");
        dataString = dataString + "Other 2:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_2)) + System.getProperty("line.separator");
        dataString = dataString + "Other 3:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_3)) + System.getProperty("line.separator");
        dataString = dataString + "Other 4:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_4)) + System.getProperty("line.separator");
        dataString = dataString + "Other 5:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_5)) + System.getProperty("line.separator");
        dataString = dataString + "Other 6:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_6)) + System.getProperty("line.separator");
        dataString = dataString + "Other 7:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_7)) + System.getProperty("line.separator");
        dataString = dataString + "Other 8:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_8)) + System.getProperty("line.separator");
        dataString = dataString + "Other 9:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_9)) + System.getProperty("line.separator");
        dataString = dataString + "Other 10:" + characterInfo.getString(characterInfo.getColumnIndex(SQLHelper.CHARACTER_OTHER_10)) + System.getProperty("line.separator");
        dataString = dataString + System.getProperty("line.separator");
        dataString = dataString + "Spells" + System.getProperty("line.separator");

        if (spellInfo.getCount()>0) {
            do {
//                dataString = dataString + "Character ID:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_CHARACTER_ID_LINK)) + System.getProperty("line.separator");
                dataString = dataString + "Character Name:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_CHARACTER_NAME_LINK)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Level:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_LEVEL)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Name:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_NAME)) + System.getProperty("line.separator");
                dataString = dataString + "Spell School:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_SCHOOL)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Casting Time:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_CASTING_TIME)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Range:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_RANGE)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Components:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_COMPONENTS)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Duration:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_DURATION)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Prepared:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_PREPARED)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Description:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_DESCRIPTION)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 1:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_1)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 2:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_2)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 3:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_3)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 4:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_4)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 5:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_5)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 6:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_6)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 7:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_7)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 8:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_8)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 9:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_9)) + System.getProperty("line.separator");
                dataString = dataString + "Spell Other 10:" + spellInfo.getString(spellInfo.getColumnIndex(SQLHelper.SPELL_OTHER_10)) + System.getProperty("line.separator");
                dataString = dataString + System.getProperty("line.separator");
            } while (spellInfo.moveToNext());
        }

        dataString = dataString + "Items" + System.getProperty("line.separator");

        if (itemInfo.getCount()>0) {
            do {
//                dataString = dataString + "Character ID Link:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_CHARACTER_ID_LINK)) + System.getProperty("line.separator");
                dataString = dataString + "Character Name Link:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.CHARACTER_NAME_LINK)) + System.getProperty("line.separator");
                dataString = dataString + "Category:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_CATEGORY)) + System.getProperty("line.separator");
                dataString = dataString + "Name:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_NAME)) + System.getProperty("line.separator");
                dataString = dataString + "Description:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_DESCRIPTION)) + System.getProperty("line.separator");
                dataString = dataString + "Range:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_WEAPON_RANGE)) + System.getProperty("line.separator");
                dataString = dataString + "Type:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE)) + System.getProperty("line.separator");
                dataString = dataString + "DamageRoll:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL)) + System.getProperty("line.separator");
                dataString = dataString + "AC:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_ARMOR_AC)) + System.getProperty("line.separator");
                dataString = dataString + "Weight:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_WEIGHT)) + System.getProperty("line.separator");
                dataString = dataString + "Cost:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_COST)) + System.getProperty("line.separator");
                dataString = dataString + "Quantity:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_QUANTITY)) + System.getProperty("line.separator");
                dataString = dataString + "Attribute:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_ATTRIBUTE)) + System.getProperty("line.separator");
                dataString = dataString + "Proficient:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_PROFICIENT)) + System.getProperty("line.separator");
                dataString = dataString + "Other 1:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_1)) + System.getProperty("line.separator");
                dataString = dataString + "Other 2:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_2)) + System.getProperty("line.separator");
                dataString = dataString + "Other 3:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_3)) + System.getProperty("line.separator");
                dataString = dataString + "Other 4:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_4)) + System.getProperty("line.separator");
                dataString = dataString + "Other 5:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_5)) + System.getProperty("line.separator");
                dataString = dataString + "Other 6:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_6)) + System.getProperty("line.separator");
                dataString = dataString + "Other 7:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_7)) + System.getProperty("line.separator");
                dataString = dataString + "Other 8:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_8)) + System.getProperty("line.separator");
                dataString = dataString + "Other 9:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_9)) + System.getProperty("line.separator");
                dataString = dataString + "Other 10:" + itemInfo.getString(itemInfo.getColumnIndex(SQLHelper.ITEM_OTHER_10)) + System.getProperty("line.separator");
                dataString = dataString + System.getProperty("line.separator");
            } while (itemInfo.moveToNext());
        }

        try {
            FileWriter out = new FileWriter(file);
            out.write(dataString);
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        Toast.makeText(this,"Your character has been exported to " + newFolder + "/" + fileName,Toast.LENGTH_LONG).show();
    }

    public void importCharacter(View v){
        Spinner importSpinner = (Spinner) findViewById(R.id.IE_Spinner_Import);
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/Exported Characters", importSpinner.getSelectedItem().toString());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            ArrayList<String> currentData = new ArrayList<>();
            ContentValues cvCharacter = new ContentValues();
            ContentValues cvSpell = new ContentValues();
            ContentValues cvItem = new ContentValues();

            SQLHelper sqlhelper = new SQLHelper(this);
            SQLiteDatabase db = sqlhelper.getWritableDatabase();
            Button importButton = (Button) findViewById(R.id.import_button);

            Integer charID = 0;
            do {
                //data is in line. enter the if statements in here
                try {
                    currentData.clear();
                    String[] splitData = null;
                    splitData = line.split(":");
                    currentData.add(splitData[0].toString() + "");
                    
                    if ((splitData != null)&&(splitData.length == 1)){
                        currentData.add("");
                    }
                    else if ((splitData != null)&&(splitData.length == 2)){
                        currentData.add(splitData[1].toString()+"");
                    }
                    else {

                    }
                } catch(Exception e){
                }

                importButton.setText("Importing Character");

//                if ((currentData != null)&&(currentData.size()>0)&&("Spells".equals(currentData.get(1)))){break;}

                if((currentData != null)&&(currentData.size()>1)){
                    if ("Spells".equals(currentData.get(0))){break;}
                    else if ("Name".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_NAME, currentData.get(1));}
                    else if ("Age".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_AGE, currentData.get(1));}
                    else if ("Allies and Organisations".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ALLIES_AND_ORGANISATIONS, currentData.get(1));}
                    else if ("Armor Proficiencies".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ARMOR_PROFICIENCIES, currentData.get(1));}
                    else if ("Background".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_BACKGROUND, currentData.get(1));}
                    else if ("Backstory".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_BACKSTORY, currentData.get(1));}
                    else if ("Barbarian Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_BARBARIAN, currentData.get(1));}
                    else if ("Bard Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_BARD, currentData.get(1));}
                    else if ("Charisma".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CHA, currentData.get(1));}
                    else if ("Cleric Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CLERIC, currentData.get(1));}
                    else if ("Constitution".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CON, currentData.get(1));}
                    else if ("Dexterity".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEX, currentData.get(1));}
                    else if ("Druid Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DRUID, currentData.get(1));}
                    else if ("Eye Colour".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_EYE_COLOR, currentData.get(1));}
                    else if ("Fighter Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_FIGHTER, currentData.get(1));}
                    else if ("Hair Colour".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HAIR_COLOR, currentData.get(1));}
                    else if ("Height".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HEIGHT, currentData.get(1));}
                    else if ("Current HP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HP, currentData.get(1));}
                    else if ("Intelligence".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_INT, currentData.get(1));}
                    else if ("Language Proficiencies".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_LANGUAGE_PROFICIENCIES, currentData.get(1));}
                    else if ("Monk Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_MONK, currentData.get(1));}
                    else if ("Paladin Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_PALADIN, currentData.get(1));}
                    else if ("Ranger Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_RANGER, currentData.get(1));}
                    else if ("Rogue Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ROGUE, currentData.get(1));}
                    else if ("Charisma Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_CHA, currentData.get(1));}
                    else if ("Constitution Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_CON, currentData.get(1));}
                    else if ("Dexterity Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_DEX, currentData.get(1));}
                    else if ("Intelligence Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_INT, currentData.get(1));}
                    else if ("Strength Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_STR, currentData.get(1));}
                    else if ("Wisdom Saving Throw Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SAVE_PROF_WIS, currentData.get(1));}
                    else if ("Acrobatics Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_ACROBATICS, currentData.get(1));}
                    else if ("Animal Handling Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING, currentData.get(1));}
                    else if ("Arcana Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_ARCANA, currentData.get(1));}
                    else if ("Athletics Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_ATHLETICS, currentData.get(1));}
                    else if ("Deception  Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_DECEPTION, currentData.get(1));}
                    else if ("History Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_HISTORY, currentData.get(1));}
                    else if ("Insight Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_INSIGHT, currentData.get(1));}
                    else if ("Intimidation Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_INTIMIDATION, currentData.get(1));}
                    else if ("Investigation Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_INVESTIGATION, currentData.get(1));}
                    else if ("Medicine Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_MEDICINE, currentData.get(1));}
                    else if ("Nature Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_NATURE, currentData.get(1));}
                    else if ("Perception Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_PERCEPTION, currentData.get(1));}
                    else if ("Performance Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_PERFORMANCE, currentData.get(1));}
                    else if ("Persuasion Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_PERSUASION, currentData.get(1));}
                    else if ("Religion Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_RELIGION, currentData.get(1));}
                    else if ("Sleight of Hand Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND, currentData.get(1));}
                    else if ("Stealth Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_STEALTH, currentData.get(1));}
                    else if ("Survival Proficiency".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKILL_SURVIVAL, currentData.get(1));}
                    else if ("Skin Colour".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SKIN_COLOR, currentData.get(1));}
                    else if ("Sorcerer Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SORCERER, currentData.get(1));}
                    else if ("Speed".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPEED, currentData.get(1));}
                    else if ("Strength".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_STR, currentData.get(1));}
                    else if ("Tool Proficiencies".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_TOOL_PROFICIENCIES, currentData.get(1));}
                    else if ("Warlock Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WARLOCK, currentData.get(1));}
                    else if ("Alignment".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ALIGNMENT, currentData.get(1));}
                    else if ("Weapon Proficiencies".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WEAPON_PROFICIENCIES, currentData.get(1));}
                    else if ("Weight".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WEIGHT, currentData.get(1));}
                    else if ("Wisdom".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WIS, currentData.get(1));}
                    else if ("Wizard Level".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WIZARD, currentData.get(1));}
                    else if ("Race".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_RACE, currentData.get(1));}
                    else if ("Max HP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_MAX_HP, currentData.get(1));}
                    else if ("Initiative Bonus".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_INITIATIVE, currentData.get(1));}
                    else if ("PP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_PP, currentData.get(1));}
                    else if ("GP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_GP, currentData.get(1));}
                    else if ("EP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_EP, currentData.get(1));}
                    else if ("SP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SP, currentData.get(1));}
                    else if ("CP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CP, currentData.get(1));}
                    else if ("Personality Traits".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_PERSONALITY_TRAITS, currentData.get(1));}
                    else if ("Ideals".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_IDEALS, currentData.get(1));}
                    else if ("Bonds".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_BONDS, currentData.get(1));}
                    else if ("Flaws".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_FLAWS, currentData.get(1));}
                    else if ("Notes".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_NOTES, currentData.get(1));}
                    else if ("Current Spell L1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L1_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L2_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L3_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L4".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L4_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L5".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L5_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L6".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L6_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L7".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L7_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L8".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L8_SPELL_MIN, currentData.get(1));}
                    else if ("Current Spell L9".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L9_SPELL_MIN, currentData.get(1));}
                    else if ("Max Spell L1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L1_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L2_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L3_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L4".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L4_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L5".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L5_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L6".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L6_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L7".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L7_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L8".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L8_SPELL_MAX, currentData.get(1));}
                    else if ("Max Spell L9".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_L9_SPELL_MAX, currentData.get(1));}
                    else if ("Image".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_IMAGE, currentData.get(1));}
                    else if ("Image Rotation".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ROTATION, currentData.get(1));}
                    else if ("Option 1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_STRENGTH_VISIBLE, currentData.get(1));}
                    else if ("Option 2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEXTERITY_VISIBLE, currentData.get(1));}
                    else if ("Option 3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CONSTITUTION_VISIBLE, currentData.get(1));}
                    else if ("Option 4".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_INTELLIGENCE_VISIBLE, currentData.get(1));}
                    else if ("Option 5".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WISDOM_VISIBLE, currentData.get(1));}
                    else if ("Option 6".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CHARISMA_VISIBLE, currentData.get(1));}
                    else if ("Option 7".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ABILITIES_VISIBLE, currentData.get(1));}
                    else if ("Option 8".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_CANTRIP_VISIBLE, currentData.get(1));}
                    else if ("Option 9".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_1, currentData.get(1));}
                    else if ("Option 10".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_2, currentData.get(1));}
                    else if ("Option 11".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_3, currentData.get(1));}
                    else if ("Option 12".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_4, currentData.get(1));}
                    else if ("Option 13".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_5, currentData.get(1));}
                    else if ("Option 14".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_6, currentData.get(1));}
                    else if ("Option 15".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_7, currentData.get(1));}
                    else if ("Option 16".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_8, currentData.get(1));}
                    else if ("Option 17".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_LEVEL_9, currentData.get(1));}
                    else if ("Option 18".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_SPELL_CASTING, currentData.get(1));}
                    else if ("Option 19".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_PROFICIENCIES_VISIBLE, currentData.get(1));}
                    else if ("Option 20".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_WEAPONS_VISIBLE, currentData.get(1));}
                    else if ("Option 21".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_ARMOR_VISIBLE, currentData.get(1));}
                    else if ("Option 22".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_INVENTORY, currentData.get(1));}
                    else if ("XP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_EXPERIENCE, currentData.get(1));}
                    else if ("Temporary HP".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_TEMPORARY_HP, currentData.get(1));}
                    else if ("Hit Dice D6".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HIT_DICE_D6, currentData.get(1));}
                    else if ("Hit Dice D8".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HIT_DICE_D8, currentData.get(1));}
                    else if ("Hit Dice D10".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HIT_DICE_D10, currentData.get(1));}
                    else if ("Hit Dice D12".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_HIT_DICE_D12, currentData.get(1));}
                    else if ("Death Check Success 1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_SUCCESS_1, currentData.get(1));}
                    else if ("Death Check Success 2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_SUCCESS_2, currentData.get(1));}
                    else if ("Death Check Success 3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_SUCCESS_3, currentData.get(1));}
                    else if ("Death Check Fail 1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_FAIL_1, currentData.get(1));}
                    else if ("Death Check Fail 2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_FAIL_2, currentData.get(1));}
                    else if ("Death Check Fail 3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_DEATH_FAIL_3, currentData.get(1));}
                    else if ("Other 1".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_1, currentData.get(1));}
                    else if ("Other 2".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_2, currentData.get(1));}
                    else if ("Other 3".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_3, currentData.get(1));}
                    else if ("Other 4".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_4, currentData.get(1));}
                    else if ("Other 5".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_5, currentData.get(1));}
                    else if ("Other 6".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_6, currentData.get(1));}
                    else if ("Other 7".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_7, currentData.get(1));}
                    else if ("Other 8".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_8, currentData.get(1));}
                    else if ("Other 9".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_9, currentData.get(1));}
                    else if ("Other 10".equals(currentData.get(0))){cvCharacter.put(SQLHelper.CHARACTER_OTHER_10, currentData.get(1));}
//                    Toast.makeText(this,String.valueOf(cvCharacter.size()),Toast.LENGTH_SHORT).show();
                }
            } while ((line = reader.readLine()) != null);

            try {
                db.insert(SQLHelper.TABLE_NAME,null,cvCharacter);
            } catch (Exception e){
                Toast.makeText(this,"Error adding character",Toast.LENGTH_SHORT).show();
                return;
            }

            //gets the ID of the last entry
            try {
                Cursor lastCursor = db.query(SQLHelper.TABLE_NAME,null,null, null,null,null,null);
                lastCursor.moveToLast();
                charID = lastCursor.getInt(lastCursor.getColumnIndex(SQLHelper.C_ID));
            } catch (Exception e){

            }

            do {
                //data is in line. enter the if statements in here
                importButton.setText("Importing Spells");
                try {
                    currentData.clear();
                    String[] splitData = null;
                    splitData = line.split(":");
                    currentData.add(splitData[0].toString());

                    if ((splitData != null)&&(splitData.length == 1)){
                        currentData.add("");
                    }
                    else if ((splitData != null)&&(splitData.length == 2)){
                        currentData.add(splitData[1].toString()+"");
                    }
                    else {

                    }
                } catch(Exception e){}

                if ((currentData != null)&&(currentData.size()>0)&&("Items".equals(currentData.get(1)))){break;}

                if ((currentData != null)&&(currentData.size()>1)) {
                    if ("Items".equals(currentData.get(0))){break;}
                    else if ("Character ID".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_CHARACTER_ID_LINK, charID);}
                    else if ("Character Name".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_CHARACTER_NAME_LINK, currentData.get(1));}
                    else if ("Spell Level".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_LEVEL, currentData.get(1));}
                    else if ("Spell Name".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_NAME, currentData.get(1));}
                    else if ("Spell School".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_SCHOOL, currentData.get(1));}
                    else if ("Spell Casting Time".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_CASTING_TIME, currentData.get(1));}
                    else if ("Spell Range".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_RANGE, currentData.get(1));}
                    else if ("Spell Components".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_COMPONENTS, currentData.get(1));}
                    else if ("Spell Duration".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_DURATION, currentData.get(1));}
                    else if ("Spell Prepared".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_PREPARED, currentData.get(1));}
                    else if ("Spell Description".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_DESCRIPTION, currentData.get(1));}
                    else if ("Spell Other 1".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_1, currentData.get(1));}
                    else if ("Spell Other 2".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_2, currentData.get(1));}
                    else if ("Spell Other 3".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_3, currentData.get(1));}
                    else if ("Spell Other 4".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_4, currentData.get(1));}
                    else if ("Spell Other 5".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_5, currentData.get(1));}
                    else if ("Spell Other 6".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_6, currentData.get(1));}
                    else if ("Spell Other 7".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_7, currentData.get(1));}
                    else if ("Spell Other 8".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_8, currentData.get(1));}
                    else if ("Spell Other 9".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_9, currentData.get(1));}
                    else if ("Spell Other 10".equals(currentData.get(0))){cvSpell.put(SQLHelper.SPELL_OTHER_10, currentData.get(1));}
                    else if ("".equals(currentData.get(0))){
                        try {
                            db.insert(SQLHelper.SPELL_TABLE_NAME,null,cvSpell);
                        } catch (Exception e){
                            Toast.makeText(this,"Error adding spells",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            } while ((line = reader.readLine()) != null);

            do {
                //data is in line. enter the if statements in here
                importButton.setText("Importing Items");
                try {
                    currentData.clear();
                    String[] splitData = null;
                    splitData = line.split(":");
                    currentData.add(splitData[0].toString());

                    if ((splitData != null)&&(splitData.length == 1)){
                        currentData.add("");
                    }
                    else if ((splitData != null)&&(splitData.length == 2)){
                        currentData.add(splitData[1].toString()+"");
                    }
                    else {

                    }
                } catch(Exception e){}

                if ((currentData != null)&&(currentData.size()>1)) {
                    if ("Character ID Link".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_CHARACTER_ID_LINK, charID);}
                    else if ("Character Name Link".equals(currentData.get(0))){cvItem.put(SQLHelper.CHARACTER_NAME_LINK, currentData.get(1));}
                    else if ("Category".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_CATEGORY, currentData.get(1));}
                    else if ("Name".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_NAME, currentData.get(1));}
                    else if ("Description".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_DESCRIPTION, currentData.get(1));}
                    else if ("Range".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_WEAPON_RANGE, currentData.get(1));}
                    else if ("Type".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE, currentData.get(1));}
                    else if ("DamageRoll".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL, currentData.get(1));}
                    else if ("AC".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_ARMOR_AC, currentData.get(1));}
                    else if ("Weight".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_WEIGHT, currentData.get(1));}
                    else if ("Cost".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_COST, currentData.get(1));}
                    else if ("Quantity".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_QUANTITY, currentData.get(1));}
                    else if ("Attribute".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_ATTRIBUTE, currentData.get(1));}
                    else if ("Proficient".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_PROFICIENT, currentData.get(1));}
                    else if ("Other 1".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_1, currentData.get(1));}
                    else if ("Other 2".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_2, currentData.get(1));}
                    else if ("Other 3".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_3, currentData.get(1));}
                    else if ("Other 4".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_4, currentData.get(1));}
                    else if ("Other 5".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_5, currentData.get(1));}
                    else if ("Other 6".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_6, currentData.get(1));}
                    else if ("Other 7".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_7, currentData.get(1));}
                    else if ("Other 8".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_8, currentData.get(1));}
                    else if ("Other 9".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_9, currentData.get(1));}
                    else if ("Other 10".equals(currentData.get(0))){cvItem.put(SQLHelper.ITEM_OTHER_10, currentData.get(1));}
                    else if ("".equals(currentData.get(0))){
                        //write to the database here.
                        try {
                            db.insert(SQLHelper.ITEM_TABLE_NAME,null,cvItem);
                        } catch (Exception e){
                            Toast.makeText(this,"Error adding items",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

            } while ((line = reader.readLine()) != null);
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this,e.toString() + "#" + importSpinner.getSelectedItem(),Toast.LENGTH_LONG).show();
        }

        Intent intentBasicStats = new Intent(this,CharacterSelectionScreen.class);
        startActivity(intentBasicStats);
        ImportExport.this.overridePendingTransition(0,0);
    }
}
