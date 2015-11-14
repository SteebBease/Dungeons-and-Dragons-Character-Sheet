//This class displays the characters basic stats
//
//Displays the character name and hp level at the top of the screen
//Shows the character class levels
//displays the main attribute name, score, modifier and saving throw
//When the attribute name is clicked it displays the linear layout which contains the skill
//scores.
//When either a modifier, saving throw or skill modifier is clicked then it will roll a dice
//At the bottom of the screen is a Dice Rolls textview which when pressed calls the dice roll
//method, this displays a popup dialog with the roll on it.
//
package stephen.dungeonsanddragonscharactersheet;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class BasicStats extends MenuAndDatabase{

    //test
    //Constructor for the activity. This populates various textviews on the page
    public void onCreate(Bundle savedInstanceState){
        //sets the theme from settings
        int chosenTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt("theme",R.style.blue_grey_theme);
        setTheme(chosenTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_stats);

        try {
            Cursor settingCursor = SQLHelper.setupDatabase(this, SQLHelper.SETTING_TABLE_NAME, null, null,null);
            settingCursor.moveToFirst();
            characterName = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_NAME));
            characterID = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_ID));
        }
        catch(Exception e){
            Log.e("BasicStats","Error getting settings data");
        }

        try{
            detector = new GestureDetector(this, this);

            dataSqlhelper = new SQLHelper(this);
            dataDB = dataSqlhelper.getWritableDatabase();

            //retrieves the characters name pressed from previous screen. Sets up the database.
            /*Log.i("BasicStats","trying to retrieve character name");
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                characterName = extras.getString("characterName");
                Log.i("BasicStats","character name retrieved: " + characterName);
            }*/

            //sets up the database pointer
            dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
            Log.i("BasicStats","cursor has been setup, number of items: " + dataCursor.getCount());

            //recalls the settings
            LinearLayout strengthLL = (LinearLayout) findViewById(R.id.strength_skills);
            LinearLayout dexterityLL = (LinearLayout) findViewById(R.id.dexterity_skills);
            LinearLayout intelligenceLL = (LinearLayout) findViewById(R.id.intelligence_skills);
            LinearLayout wisdomLL = (LinearLayout) findViewById(R.id.wisdom_skills);
            LinearLayout charismaLL = (LinearLayout) findViewById(R.id.charisma_skills);

            //8 means invisible, 0 means visible
            if ("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STRENGTH_VISIBLE)))) {
                strengthLL.setVisibility(View.VISIBLE);
            }
            else {
                strengthLL.setVisibility(View.GONE);
            }
            if ("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEXTERITY_VISIBLE)))) {
                dexterityLL.setVisibility(View.VISIBLE);
            }
            else {
                dexterityLL.setVisibility(View.GONE);
            }
            if ("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INTELLIGENCE_VISIBLE)))) {
                intelligenceLL.setVisibility(View.VISIBLE);
            }
            else {
                intelligenceLL.setVisibility(View.GONE);
            }
            if ("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WISDOM_VISIBLE)))) {
                wisdomLL.setVisibility(View.VISIBLE);
            }
            else {
                wisdomLL.setVisibility(View.GONE);
            }
            if ("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHARISMA_VISIBLE)))) {
                charismaLL.setVisibility(View.VISIBLE);
            }
            else {
                charismaLL.setVisibility(View.GONE);
            }

            //sets up the character name, hp, class levels. Method returns false
            if((setTextView(R.id.character_name,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_NAME))))||
                    (setTextView(R.id.character_hp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP))+"/"+dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP))))||
                    (setTextView(R.id.character_class_levels,classLevelString(dataCursor)))){
                Log.e("BasicStats","Error setting up character name, hp or class levels");
            }
            else
            {
                Log.i("BasicStats","character name, hp, class levels setup correctly");
            }

            //sets up the attributes
            if((setTextView(R.id.strength_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR))))||
                    (setTextView(R.id.dexterity_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX))))||
                    (setTextView(R.id.constitution_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CON))))||
                    (setTextView(R.id.intelligence_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT))))||
                    (setTextView(R.id.wisdom_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS))))||
                    (setTextView(R.id.charisma_attributes, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA))))){
                Log.e("BasicStats","Error setting up attributes");
            }
            else
            {
                Log.i("BasicStats","Attributes setup correctly");
            }

            //sets up the modifiers
            if((setTextView(R.id.strength_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)),0,"0")))||
                    (setTextView(R.id.dexterity_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),0,"0")))||
                    (setTextView(R.id.constitution_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CON)),0,"0")))||
                    (setTextView(R.id.intelligence_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),0,"0")))||
                    (setTextView(R.id.wisdom_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),0,"0")))||
                    (setTextView(R.id.charisma_modifier, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),0,"0")))){
                Log.e("BasicStats","Error setting up modifiers"); 
            }
            else
            {
                Log.i("BasicStats","Modifiers setup correctly");
            }
            
            //sets the saving throws
            if((setTextView(R.id.strength_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_STR)))))||
                    (setTextView(R.id.dexterity_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_DEX)))))||
                    (setTextView(R.id.constitution_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CON)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CON)))))||
                    (setTextView(R.id.intelligence_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_INT)))))||
                    (setTextView(R.id.wisdom_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_WIS)))))||
                    (setTextView(R.id.charisma_saving,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CHA)))))){
                Log.e("BasicStats","Error setting up saving throws");
            }
            else
            {
                Log.i("BasicStats","Saving Throws setup correctly");
            }

            //sets up skills
            if((setTextView(R.id.basic_stats_acrobatics,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ACROBATICS)))))||
                    (setTextView(R.id.basic_stats_animal_handling,modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING)))))||
                    (setTextView(R.id.basic_stats_arcana, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ARCANA)))))||
                    (setTextView(R.id.basic_stats_athletics, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ATHLETICS)))))||
                    (setTextView(R.id.basic_stats_deception, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_DECEPTION)))))||
                    (setTextView(R.id.basic_stats_history, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_HISTORY)))))||
                    (setTextView(R.id.basic_stats_insight, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INSIGHT)))))||
                    (setTextView(R.id.basic_stats_intimidation, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INTIMIDATION)))))||
                    (setTextView(R.id.basic_stats_investigation, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INVESTIGATION)))))||
                    (setTextView(R.id.basic_stats_medicine, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_MEDICINE)))))||
                    (setTextView(R.id.basic_stats_nature, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_NATURE)))))||
                    (setTextView(R.id.basic_stats_perception, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERCEPTION)))))||
                    (setTextView(R.id.basic_stats_performance, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERFORMANCE)))))||
                    (setTextView(R.id.basic_stats_persuasion, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERSUASION)))))||
                    (setTextView(R.id.basic_stats_religion, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_RELIGION)))))||
                    (setTextView(R.id.basic_stats_slight_of_hand, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND)))))||
                    (setTextView(R.id.basic_stats_stealth, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_STEALTH)))))||
                    (setTextView(R.id.basic_stats_survival, modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SURVIVAL)))))){
                Log.e("BasicStats","Error setting up skills");
            }
            else
            {
                Log.i("BasicStats","Skills setup correctly");
            }

            //sets the edit text for XP
            final EditText xpEdit = (EditText) findViewById(R.id.basic_stats_experience);
            try {
                xpEdit.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_EXPERIENCE)));
            }catch(Exception e){
                Log.e("BasicStats","Error getting XP");
            }

        }catch (Exception e){
            Log.e("BasicStats","Basic Stats exception: " + e.toString());
        }
    }

    //Shows and hides the skills for each attribute
    public void linearLayoutMagic(View v){
        LinearLayout disappearingLinearLayout = null;
        ContentValues cv = new ContentValues();
        switch(v.getId()){
            case R.id.strength_header:
                disappearingLinearLayout = (LinearLayout) findViewById(R.id.strength_skills);
                break;
            case R.id.dexterity_header:
                disappearingLinearLayout = (LinearLayout) findViewById(R.id.dexterity_skills);
                break;
            case R.id.intelligence_header:
                disappearingLinearLayout = (LinearLayout) findViewById(R.id.intelligence_skills);
                break;
            case R.id.wisdom_header:
                disappearingLinearLayout = (LinearLayout) findViewById(R.id.wisdom_skills);
                break;
            case R.id.charisma_header:
                disappearingLinearLayout = (LinearLayout) findViewById(R.id.charisma_skills);
                break;
            default:
                break;
        }
        try {
            if ((disappearingLinearLayout != null) && (disappearingLinearLayout.getVisibility() == View.VISIBLE)) {
                disappearingLinearLayout.setVisibility(View.GONE);
                Log.i("BasicStats","disappearing layout hidden. ID: " + Integer.toString(disappearingLinearLayout.getId()));
            } else if((disappearingLinearLayout != null) && (disappearingLinearLayout.getVisibility() == View.GONE)){
                disappearingLinearLayout.setVisibility(View.VISIBLE);
                Log.i("BasicStats", "disappearing layout visible. ID: " + Integer.toString(disappearingLinearLayout.getId()));
            }
            switch(v.getId()){
                case R.id.strength_header:
                    cv.put(SQLHelper.CHARACTER_STRENGTH_VISIBLE, disappearingLinearLayout.getVisibility());
                    break;
                case R.id.dexterity_header:
                    cv.put(SQLHelper.CHARACTER_DEXTERITY_VISIBLE, disappearingLinearLayout.getVisibility());
                    break;
                case R.id.intelligence_header:
                    cv.put(SQLHelper.CHARACTER_INTELLIGENCE_VISIBLE, disappearingLinearLayout.getVisibility());
                    break;
                case R.id.wisdom_header:
                    cv.put(SQLHelper.CHARACTER_WISDOM_VISIBLE, disappearingLinearLayout.getVisibility());
                    break;
                case R.id.charisma_header:
                    cv.put(SQLHelper.CHARACTER_CHARISMA_VISIBLE, disappearingLinearLayout.getVisibility());
                    break;
                default:
                    break;
            }
            dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        }
        catch (Exception e)
        {
            Log.e("BasicStats","Error skill visibilities");
        }
    }

    //Increases and decreases the hp value
    public void hpUp(View v){
        hpChange(this, R.id.character_hp, true, characterName);
    }
    public void hpDown(View v){
        hpChange(this, R.id.character_hp, false, characterName);
    }

    //calls the dice rolls using the diceRollPopup method in MenuAndDatabase for each of the
    //attirbutes, modifiers, saving throws, skills
    public void genericDiceRoll(View v){
        Log.i("BasicStats","genericdiceRoll method. Roll for ID: " + Integer.toString(v.getId()));
        switch(v.getId()){
            case R.id.strength_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)),0,"0"),"Strength");
                break;
            case R.id.dexterity_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),0,"0"),"Dexterity");
                break;
            case R.id.constitution_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CON)),0,"0"),"Constitution");
                break;
            case R.id.intelligence_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),0,"0"),"Intelligence");
                break;
            case R.id.wisdom_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),0,"0"),"Wisdom");
                break;
            case R.id.charisma_modifier:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),0,"0"),"Charisma");
                break;
            case R.id.strength_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_STR))),"Strength Saving Throw");
                break;
            case R.id.dexterity_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_DEX))),"Dexterity Saving Throw");
                break;
            case R.id.constitution_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CON)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CON))),"Constitution Saving Throw");
                break;
            case R.id.intelligence_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_INT))),"Intelligence Saving Throw");
                break;
            case R.id.wisdom_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_WIS))),"Wisdom Saving Throw");
                break;
            case R.id.charisma_saving:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SAVE_PROF_CHA))),"Charisma Saving Throw");
                break;
            case R.id.basic_stats_acrobatics:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ACROBATICS))),"Acrobatics Skill Check");
                break;
            case R.id.basic_stats_animal_handling:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ANIMAL_HANDLING))),"Animal Handling Skill Check");
                break;
            case R.id.basic_stats_arcana:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ARCANA))),"Arcana Skill Check");
                break;
            case R.id.basic_stats_athletics:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_ATHLETICS))),"AthleticsSkill Check");
                break;
            case R.id.basic_stats_deception:
                MenuAndDatabase.diceRollPopup(this,20,modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataCursor),dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_DECEPTION))),"Deception Skill Check");
                break;
            case R.id.basic_stats_history:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_HISTORY))), "History Skill Check");
                break;
            case R.id.basic_stats_insight:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INSIGHT))), "Insight Skill Check");
                break;
            case R.id.basic_stats_intimidation:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INTIMIDATION))), "Intimidation Skill Check");
                break;
            case R.id.basic_stats_investigation:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_INVESTIGATION))), "Investigation Skill Check");
                break;
            case R.id.basic_stats_medicine:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_MEDICINE))), "Medicine Skill Check");
                break;
            case R.id.basic_stats_nature:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_NATURE))), "Nature Skill Check");
                break;
            case R.id.basic_stats_perception:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERCEPTION))), "Perception Skill Check");
                break;
            case R.id.basic_stats_performance:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERFORMANCE))), "Performance Skill Check");
                break;
            case R.id.basic_stats_persuasion:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CHA)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_PERSUASION))), "Persuasion Skill Check");
                break;
            case R.id.basic_stats_religion:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INT)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_RELIGION))), "Religion Skill Check");
                break;
            case R.id.basic_stats_slight_of_hand:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SLIGHT_OF_HAND))), "Slight Of Hand Skill Check");
                break;
            case R.id.basic_stats_stealth:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_STEALTH))), "Stealth Skill Check");
                break;
            case R.id.basic_stats_survival:
                MenuAndDatabase.diceRollPopup(this, 20, modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WIS)), proficiencyCalc(dataCursor), dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKILL_SURVIVAL))), "Survival Skill Check");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if ((e1.getX() > e2.getX()) && (e1.getX() - e2.getX() > 300)) {
            finish();
            Intent intentSpells = new Intent(this, Spells.class);
            intentSpells.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentSpells);
            this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else if((e1.getX() < e2.getX()) && (e2.getX() - e1.getX() > 300)) {
            finish();
            Intent intentBackstory = new Intent(this, Backstory.class);
            intentBackstory.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentBackstory);
            this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        return false;
    }



    public void onDestroy(){

        final EditText xpEdit = (EditText) findViewById(R.id.basic_stats_experience);
        try {
            ContentValues cv = new ContentValues();
            cv.put(SQLHelper.CHARACTER_EXPERIENCE,xpEdit.getText().toString());
            dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        }catch(Exception e){
            Log.e("Basic Stats", "Error setting XP" + e.toString());
        }

        super.onDestroy();
    }

    public void HPModify(View v){
        //sets up the custom dialog view
        final View inflaterView = getLayoutInflater().inflate(R.layout.hp_dialog, null);
        final AlertDialog.Builder hpDialog = new AlertDialog.Builder(this);
        hpDialog.setTitle("Hit Points");
        hpDialog.setView(inflaterView);
        hpDialog.setNeutralButton(android.R.string.ok,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
                dialog.dismiss();
            }
        });

        Button hpPositiveButton = (Button) inflaterView.findViewById(R.id.HP_hp_plus);
        hpPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                int currentHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP));
                currentHP++;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HP,currentHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                TextView hpText = (TextView) inflaterView.findViewById(R.id.HP_hp);
                hpText.setText(currentHP + "/" + dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));

            }
        });
        Button hpNegativeButton = (Button) inflaterView.findViewById(R.id.HP_hp_minus);
        hpNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                int currentHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP));
                currentHP--;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HP,currentHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                TextView hpText = (TextView) inflaterView.findViewById(R.id.HP_hp);
                hpText.setText(currentHP + "/" + dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
            }
        });

        Button tempPositiveButton = (Button) inflaterView.findViewById(R.id.HP_temp_plus);
        tempPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                int tempHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_TEMPORARY_HP));
                tempHP++;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_TEMPORARY_HP,tempHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                TextView tempText = (TextView) inflaterView.findViewById(R.id.HP_temp);
                tempText.setText("Temp Hit Points:" + String.valueOf(tempHP));
            }
        });
        Button tempNegativeButton = (Button) inflaterView.findViewById(R.id.HP_temp_minus);
        tempNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                int tempHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_TEMPORARY_HP));
                if(tempHP <= 0) {
                    tempHP=0;
                }
                else{
                    tempHP--;
                }
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_TEMPORARY_HP,tempHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                TextView tempText = (TextView) inflaterView.findViewById(R.id.HP_temp);
                tempText.setText("Temp Hit Points:" + String.valueOf(tempHP));
            }
        });

        Button D6HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d6);
        D6HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d6Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d6Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D6));
                }
                catch(Exception e){
                    d6Count = 0;
                }
                if(d6Count <= 0) {
                    d6Count = Integer.parseInt(MenuAndDatabase.d6Calculation(BasicStats.this,characterName));
                }else{
                    d6Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D6,d6Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                Button d6Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d6);
                d6Button.setText(String.valueOf(d6Count));
            }
        });
        Button D8HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d8);
        D8HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d8Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d8Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D8));
                }
                catch(Exception e){
                    d8Count = 0;
                }
                if(d8Count <= 0) {
                    d8Count = Integer.parseInt(MenuAndDatabase.d8Calculation(BasicStats.this,characterName));
                }else{
                    d8Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D8,d8Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                Button d8Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d8);
                d8Button.setText(String.valueOf(d8Count));
            }
        });
        Button D10HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d10);
        D10HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d10Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d10Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D10));
                }
                catch(Exception e){
                    d10Count = 0;
                }
                if(d10Count <= 0) {
                    d10Count = Integer.parseInt(MenuAndDatabase.d10Calculation(BasicStats.this,characterName));
                }else{
                    d10Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D10,d10Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                Button d10Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d10);
                d10Button.setText(String.valueOf(d10Count));
            }
        });
        Button D12HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d12);
        D12HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d12Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d12Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D12));
                }
                catch(Exception e){
                    d12Count = 0;
                }
                if(d12Count <= 0) {
                    d12Count = Integer.parseInt(MenuAndDatabase.d12Calculation(BasicStats.this,characterName));
                }else{
                    d12Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D12,d12Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[] {characterName});
                Button d12Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d12);
                d12Button.setText(String.valueOf(d12Count));
            }
        });

        TextView d6Text = (TextView) inflaterView.findViewById(R.id.HP_title_hit_dice_d6);
        TextView d8Text = (TextView) inflaterView.findViewById(R.id.HP_title_hit_dice_d8);
        TextView d10Text = (TextView) inflaterView.findViewById(R.id.HP_title_hit_dice_d10);
        TextView d12Text = (TextView) inflaterView.findViewById(R.id.HP_title_hit_dice_d12);

        d6Text.setVisibility(View.GONE);
        d8Text.setVisibility(View.GONE);
        d10Text.setVisibility(View.GONE);
        d12Text.setVisibility(View.GONE);
        D6HitDice.setVisibility(View.GONE);
        D8HitDice.setVisibility(View.GONE);
        D10HitDice.setVisibility(View.GONE);
        D12HitDice.setVisibility(View.GONE);

        dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
        dataCursor.moveToFirst();

        D6HitDice.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D6)));
        D8HitDice.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D8)));
        D10HitDice.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D10)));
        D12HitDice.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D12)));

        if(Integer.parseInt(MenuAndDatabase.d6Calculation(this,characterName)) > 0){
            d6Text.setVisibility(View.VISIBLE);
            D6HitDice.setVisibility(View.VISIBLE);
        }
        if(Integer.parseInt(MenuAndDatabase.d8Calculation(this,characterName)) > 0){
            d8Text.setVisibility(View.VISIBLE);
            D8HitDice.setVisibility(View.VISIBLE);
        }
        if(Integer.parseInt(MenuAndDatabase.d10Calculation(this,characterName)) > 0){
            d10Text.setVisibility(View.VISIBLE);
            D10HitDice.setVisibility(View.VISIBLE);
        }
        if(Integer.parseInt(MenuAndDatabase.d12Calculation(this,characterName)) > 0){
            d12Text.setVisibility(View.VISIBLE);
            D12HitDice.setVisibility(View.VISIBLE);
        }
        TextView tempHP = (TextView) inflaterView.findViewById(R.id.HP_temp);
        String tempHitPoints = dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_TEMPORARY_HP));
        if (tempHitPoints == null){
            tempHitPoints = "0";
        }
        tempHP.setText("Temp Hit Points:" + tempHitPoints);
        TextView currentHP = (TextView) inflaterView.findViewById(R.id.HP_hp);
        currentHP.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
        AlertDialog hpPopup = hpDialog.create();

        hpPopup.show();

    }

    public void diceDialog (View v){
        View inflaterView = getLayoutInflater().inflate(R.layout.dice_dialog,null);
        AlertDialog.Builder diceDialog = new AlertDialog.Builder(this);
        diceDialog.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Button d4Button = (Button) inflaterView.findViewById(R.id.dice_d4);
        Button d6Button = (Button) inflaterView.findViewById(R.id.dice_d6);
        Button d8Button = (Button) inflaterView.findViewById(R.id.dice_d8);
        Button d10Button = (Button) inflaterView.findViewById(R.id.dice_d10);
        Button d12Button = (Button) inflaterView.findViewById(R.id.dice_d12);
        Button d20Button = (Button) inflaterView.findViewById(R.id.dice_d20);
        Button d50Button = (Button) inflaterView.findViewById(R.id.dice_d50);
        Button d100Button = (Button) inflaterView.findViewById(R.id.dice_d100);

        d4Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d6Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d8Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d10Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d12Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d20Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d50Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });
        d100Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                diceButtonHold(event);
                return false;
            }
        });

        diceOutputText = (TextView) inflaterView.findViewById(R.id.dice_output);
        diceOutputIndividual = (TextView) inflaterView.findViewById(R.id.diceOutputIndividual);
        diceDialog.setView(inflaterView);
        AlertDialog diceDialogLoader = diceDialog.create();
        diceDialogLoader.show();
    }

    public int diceQuantity = 1;
    public int buttonPosition = 0;
    public int buttonPressId = 0;
    final Handler mHandler = new Handler();
    TextView diceOutputText = null;
    TextView diceOutputIndividual = null;

    public void displayTime(int pressedID){
        if ((buttonPosition == 0)&&
                (pressedID==buttonPressId)){
            diceOutputText.setText(diceQuantity + "");
            diceQuantity = diceQuantity + 1;
        }
    }

    public void diceButtonHold(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            buttonPosition = 0;
            int timerValue;
            for (int i = 0; i<100; i+=1){
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final int idNumber = buttonPressId;

                    if (i<50){
                        timerValue = i*500;
                    }
                    //why doesn't this work?
                    else if (i<51){
                        timerValue = i*2500;
                    }
                    else {
                        timerValue = i*5000;
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            displayTime(idNumber);
                        }
                    }, timerValue);
                }
            }

//add an increment of 5 up to 50
            diceOutputText.setTextColor(Color.RED);
            diceOutputText.setText("");
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            buttonPosition = 1;
            buttonPressId++;
            diceOutputText.setTextColor(Color.BLACK);
        }
    }

    public void diceRoll (View v){
        Context context = BasicStats.this;
        int diceValue = 0;
        int criticalSuccess = 0;
        int criticalFailure = 0;
        switch (v.getId()){
            case R.id.dice_d4:
                diceValue = 4;
                break;
            case R.id.dice_d6:
                diceValue = 6;
                break;
            case R.id.dice_d8:
                diceValue = 8;
                break;
            case R.id.dice_d10:
                diceValue = 10;
                break;
            case R.id.dice_d12:
                diceValue = 12;
                break;
            case R.id.dice_d20:
                diceValue = 20;
                break;
            case R.id.dice_d50:
                diceValue = 50;
                break;
            case R.id.dice_d100:
                diceValue = 100;
                break;
            default:
                break;
        }

        Random randomNumberDiceBox = new Random();
        int totalNumber = 0;
        int trackingNumber = 0;
        String quantity = "";

        for (int i = 1; i < diceQuantity; i++){
            trackingNumber = randomNumberDiceBox.nextInt(diceValue) + 1;
            if (trackingNumber == diceValue) {
                criticalSuccess++;
            }
            if (trackingNumber == 1){
                criticalFailure++;
            }
            totalNumber = totalNumber + trackingNumber;
            if (quantity.length() == 0){
                quantity = String.valueOf(trackingNumber);
            }
            else{
                quantity = quantity + " - " + trackingNumber;
            }

        }
        diceOutputText.setText(String.valueOf(totalNumber));
        diceOutputIndividual.setText(quantity);

        Toast.makeText(BasicStats.this, "Critical Success: " + criticalSuccess + System.getProperty("line.separator") + "Critical Failure: " + criticalFailure, Toast.LENGTH_SHORT).show();

        diceQuantity = 1;
    }
}
