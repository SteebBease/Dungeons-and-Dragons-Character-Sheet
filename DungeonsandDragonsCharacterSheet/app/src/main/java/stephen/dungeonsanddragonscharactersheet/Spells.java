package stephen.dungeonsanddragonscharactersheet;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//To add spell ordering
//
//when a spell is added find last order number
//up will reduce order number for selected by 1 then increase the item above by 1
//down will do the opposite
//
//need to create a routine to update the existing values which will have a blank column.
//


public class Spells extends MenuAndDatabase {

    //need to find out what spell level is and whether it's an ability
    //need to setup the children.
    public String characterName;
    public String characterID;
    public SQLHelper spellSqlhelper;
    public SQLiteDatabase spellDB;

    //expandable list view
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int screenWidth;
    int screenHeight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spells);

        try {
            Cursor settingCursor = SQLHelper.setupDatabase(this, SQLHelper.SETTING_TABLE_NAME, null, null,null);
            settingCursor.moveToFirst();
            characterName = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_NAME));
            characterID = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_ID));
        }
        catch(Exception e){
            Log.e("BasicStats","Error getting settings data");
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        try {
            detector = new GestureDetector(this, this);

            //sets up the character information database
            dataSqlhelper = new SQLHelper(this);
            dataDB = dataSqlhelper.getWritableDatabase();
            dataCursor = dataDB.query(SQLHelper.TABLE_NAME, null, SQLHelper.C_ID + "=?", new String[]{characterID}, null, null, null);
            dataCursor.moveToFirst();

            //expandable list view
            expListView = (ExpandableListView) findViewById(R.id.spellListView);
            prepareSpellListData();
            listAdapter = new Spells.SpellExpandableListAdapter(this, listDataChild);
            expListView.setAdapter(listAdapter);

            //spell slot levels created here
            setTextView(R.id.spell_level_1, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)));
            setTextView(R.id.spell_level_2, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)));
            setTextView(R.id.spell_level_3, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)));
            setTextView(R.id.spell_level_4, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)));
            setTextView(R.id.spell_level_5, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)));
            setTextView(R.id.spell_level_6, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)));
            setTextView(R.id.spell_level_7, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)));
            setTextView(R.id.spell_level_8, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)));
            setTextView(R.id.spell_level_9, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN)) + "/" + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)));

            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_1)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_1T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_2)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_2T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_3)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_3T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_4)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_4T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_5)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_5T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_6)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_6T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_7)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_7T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_8)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_8T)).setVisibility(View.GONE);
            }
            if ("".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)))){
                ((TextView) findViewById(R.id.spell_level_9)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.spell_level_9T)).setVisibility(View.GONE);
            }


            LinearLayout hideSpells = (LinearLayout) findViewById(R.id.spell_casting_title_linear_layout);
            LinearLayout hideSpellsTitles = (LinearLayout) findViewById(R.id.spell_attack_titles);
            LinearLayout hideSpellsData = (LinearLayout) findViewById(R.id.spell_attack_data);

            if(dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)) > 0){
                hideSpells.setVisibility(View.VISIBLE);
                hideSpellsTitles.setVisibility(View.VISIBLE);
                hideSpellsData.setVerticalGravity(View.VISIBLE);
            }
            else {
                hideSpells.setVisibility(View.GONE);
                hideSpellsTitles.setVisibility(View.GONE);
                hideSpellsData.setVisibility(View.GONE);
            }

            //sets the spell titles
            setTextView(R.id.spell_casting_ability, spellAbility(dataCursor));
            setTextView(R.id.spell_attack_bonus, spellAttackModifier(dataCursor));
            setTextView(R.id.spell_save_dc,spellSaveDC(dataCursor));

            spellSqlhelper = new SQLHelper(this);
            spellDB = spellSqlhelper.getWritableDatabase();

            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    ContentValues cv = new ContentValues();
                    switch(groupPosition){
                        case 0:
                            cv.put(SQLHelper.CHARACTER_ABILITIES_VISIBLE,"0");
                            break;
                        case 1:
                            cv.put(SQLHelper.CHARACTER_CANTRIP_VISIBLE,"0");
                            break;
                        case 2:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_1,"0");
                            break;
                        case 3:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_2,"0");
                            break;
                        case 4:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_3,"0");
                            break;
                        case 5:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_4,"0");
                            break;
                        case 6:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_5,"0");
                            break;
                        case 7:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_6,"0");
                            break;
                        case 8:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_7,"0");
                            break;
                        case 9:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_8,"0");
                            break;
                        case 10:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_9,"0");
                            break;
                        default:
                            return;
                    }
                    dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[]{characterID});
                }
            });

            expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    ContentValues cv = new ContentValues();
                    switch(groupPosition){
                        case 0:
                            cv.put(SQLHelper.CHARACTER_ABILITIES_VISIBLE,"1");
                            break;
                        case 1:
                            cv.put(SQLHelper.CHARACTER_CANTRIP_VISIBLE,"1");
                            break;
                        case 2:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_1,"1");
                            break;
                        case 3:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_2,"1");
                            break;
                        case 4:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_3,"1");
                            break;
                        case 5:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_4,"1");
                            break;
                        case 6:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_5,"1");
                            break;
                        case 7:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_6,"1");
                            break;
                        case 8:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_7,"1");
                            break;
                        case 9:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_8,"1");
                            break;
                        case 10:
                            cv.put(SQLHelper.CHARACTER_SPELL_LEVEL_9,"1");
                            break;
                        default:
                            return;
                    }
                    dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[]{characterID});
                }
            });

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            final int groupPosition, final int childPosition, long id) {
                    Log.i("Spell", "Listview clicked");

                    //sets up the custom dialog view
                    final View inflaterView = getLayoutInflater().inflate(R.layout.add_spell, null);
                    final AlertDialog.Builder modifySpellDialogBox = new AlertDialog.Builder(Spells.this);

                    //sets up the views on the custom dialog view
                    modifySpellDialogBox.setView(inflaterView);
                    final ContentValues finalCV = new ContentValues();
                    final RadioButton radioAbility = (RadioButton) inflaterView.findViewById(R.id.spell_ability);
                    final RadioButton radioSpell = (RadioButton) inflaterView.findViewById(R.id.spell_spell);
                    final EditText spellName = (EditText) inflaterView.findViewById(R.id.spell_name);
                    final EditText spellLevel = (EditText) inflaterView.findViewById(R.id.spell_add_level);
                    final EditText spellSchool = (EditText) inflaterView.findViewById(R.id.spell_school);
                    final EditText spellCastingTime = (EditText) inflaterView.findViewById(R.id.spell_casting_time);
                    final EditText spellRange = (EditText) inflaterView.findViewById(R.id.spell_range);
                    final EditText spellDuration = (EditText) inflaterView.findViewById(R.id.spell_duration);
                    final EditText spellDescription = (EditText) inflaterView.findViewById(R.id.spell_description);

                    try {
                        Cursor spellCursor = SQLHelper.setupDatabase(Spells.this, SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_ID_LINK  + "=? AND " + SQLHelper.SPELL_NAME+ "=?",new String[]{characterID,listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)});

                        spellName.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                        spellLevel.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_LEVEL)));
                        spellSchool.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_SCHOOL)));
                        spellCastingTime.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_CASTING_TIME)));
                        spellRange.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_RANGE)));
                        spellDuration.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_DURATION)));
                        spellDescription.setText(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_DESCRIPTION)));

                        //sets the custom dialog view up so that you only see what's relevant
                        if (groupPosition == 0){
                            spellLevel.setVisibility(View.GONE);
                            spellSchool.setVisibility(View.GONE);
                            spellCastingTime.setVisibility(View.GONE);
                            spellRange.setVisibility(View.GONE);
                            spellDuration.setVisibility(View.GONE);
                            radioAbility.setChecked(true);
                        }
                        else {
                            spellLevel.setVisibility(View.VISIBLE);
                            spellSchool.setVisibility(View.VISIBLE);
                            spellCastingTime.setVisibility(View.VISIBLE);
                            spellRange.setVisibility(View.VISIBLE);
                            spellDuration.setVisibility(View.VISIBLE);
                            radioSpell.setChecked(true);
                        }
                        Log.i("Spell", "update dialog populated and setup");

                        //Sets up the radio buttons which shows & hides various views on the custom dialog view
                        radioAbility.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spellLevel.setVisibility(View.GONE);
                                spellSchool.setVisibility(View.GONE);
                                spellCastingTime.setVisibility(View.GONE);
                                spellRange.setVisibility(View.GONE);
                                spellDuration.setVisibility(View.GONE);
                            }
                        });

                        radioSpell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spellLevel.setVisibility(View.VISIBLE);
                                spellSchool.setVisibility(View.VISIBLE);
                                spellCastingTime.setVisibility(View.VISIBLE);
                                spellRange.setVisibility(View.VISIBLE);
                                spellDuration.setVisibility(View.VISIBLE);
                            }
                        });

                    }catch(Exception e){
                        Log.e("Spell", "Error in update item: " + e.toString());
                    }

                    modifySpellDialogBox.setNeutralButton("ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            modifySpellDialogBox.setView(inflaterView);
                                try {
                                ContentValues finalCV = new ContentValues();

                                finalCV.put(SQLHelper.SPELL_CHARACTER_ID_LINK ,characterID);
                                finalCV.put(SQLHelper.SPELL_NAME,spellName.getText().toString());
                                if (radioAbility.isChecked()){
                                    finalCV.put(SQLHelper.SPELL_LEVEL,"ability");
                                }
                                else{
                                    finalCV.put(SQLHelper.SPELL_LEVEL,spellLevel.getText().toString());
                                }
                                finalCV.put(SQLHelper.SPELL_SCHOOL,spellSchool.getText().toString());
                                finalCV.put(SQLHelper.SPELL_CASTING_TIME,spellCastingTime.getText().toString());
                                finalCV.put(SQLHelper.SPELL_RANGE,spellRange.getText().toString());
                                finalCV.put(SQLHelper.SPELL_DURATION,spellDuration.getText().toString());
                                finalCV.put(SQLHelper.SPELL_DESCRIPTION,spellDescription.getText().toString());

                                try {
                                    spellDB.update(SQLHelper.SPELL_TABLE_NAME, finalCV, SQLHelper.SPELL_CHARACTER_ID_LINK  + "=? AND " + SQLHelper.SPELL_NAME + "=?", new String[]{characterID, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)});
                                    finish();
                                    startActivity(getIntent());
                                    Spells.this.overridePendingTransition(0,0);
                                } catch (Exception e) {
                                    MenuAndDatabase.errorNotification(getBaseContext(), e.toString());
                                }
                            }catch(Exception e){
                                MenuAndDatabase.errorNotification(getBaseContext(), e.toString());
                            }
                        }
                    });

                    AlertDialog modifySpellDialog = modifySpellDialogBox.create();
                    modifySpellDialog.show();
                    modifySpellDialog.getWindow().setLayout((int)Math.round(screenWidth),(int)Math.round(screenHeight));
                    return false;
                }
            });

            expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {
                    int itemType = ExpandableListView.getPackedPositionType(id);
                    final int childPosition;
                    final int groupPosition;

                    if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        childPosition = ExpandableListView.getPackedPositionChild(id);
                        groupPosition = ExpandableListView.getPackedPositionGroup(id);

//                        Toast.makeText(getApplicationContext(), "long press", Toast.LENGTH_SHORT).show();
                        //do your per-item callback here
                        //Creates the alert
                        final int finalGroupPosition = groupPosition;
                        final int finalChildPosition = childPosition;
                        AlertDialog.Builder deleteConfirmationDialog = new AlertDialog.Builder(Spells.this);
                        deleteConfirmationDialog.setTitle("Delete Spell?");
                        deleteConfirmationDialog.setMessage("Are you sure you want to delete " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) + "? This cannot be undone.");
                        deleteConfirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if yes has been clicked then try to delete the record using the char name edittext
                                try {
                                    spellDB.delete(SQLHelper.SPELL_TABLE_NAME, SQLHelper.SPELL_CHARACTER_ID_LINK  + "=? AND " + SQLHelper.SPELL_NAME + "=?", new String[]{characterID, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)});
                                } catch (Exception e) {
//                                    Toast.makeText(Spells.this, "Did not delete spell: " + e.toString(), Toast.LENGTH_LONG).show();
                                    MenuAndDatabase.errorNotification(Spells.this, e.toString());
                                }
                                //Move back to the character select screen
                                Intent intentSpells = new Intent(Spells.this, Spells.class);
//                                intentSpells.putExtra("characterName", characterName);
                                startActivity(intentSpells);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        deleteConfirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if they don't want to delete their character, do nothing
                                dialog.dismiss();
                            }
                        });
                        //Activates the dialog box
                        AlertDialog confirmationDialog = deleteConfirmationDialog.create();
                        confirmationDialog.show();
                        return true; //true if we consumed the click, false if not

                    } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
//                        groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        //do your per-group callback here
                        return false; //true if we consumed the click, false if not

                    } else {
                        // null item; we don't consume the click
                        return false;
                    }
                }

            });

            //sets up the character name
            TextView characterName = (TextView) findViewById(R.id.character_name);
            characterName.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_NAME)));

            //sets up the hp value
            TextView characterHP = (TextView) findViewById(R.id.character_hp);
            characterHP.setText(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP))+"/"+dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));

        } catch (Exception e) {
            MenuAndDatabase.errorNotification(this, e.toString());
        }

        //sets whether the groups are expanded or not.
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ABILITIES_VISIBLE)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Abilities"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CANTRIP_VISIBLE)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Cantrip"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_1)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 1"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_2)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 2"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_3)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 3"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_4)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 4"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_5)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 5"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_6)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 6"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_7)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 7"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_8)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 8"));
            }
        } catch(Exception e){}
        try {
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_LEVEL_9)).equals("0")) {
                expListView.expandGroup(listDataHeader.indexOf("Spell Level 9"));
            }
        } catch(Exception e) {
        }
        try{
            LinearLayout castingMagic = (LinearLayout) findViewById(R.id.spell_casting_linear_layout);
            if (dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPELL_CASTING)).equals("0")) {
                castingMagic.setVisibility(View.VISIBLE);
            }
            else
            {
                castingMagic.setVisibility(View.GONE);
            }
        }catch (Exception e){}
    }

    public void addSpell (View v) {
        final SQLHelper finalsqlhelper = new SQLHelper(getBaseContext());
        final SQLiteDatabase finalSpellDB = finalsqlhelper.getWritableDatabase();
        final View inflaterView = getLayoutInflater().inflate(R.layout.add_spell, null);
        final AlertDialog.Builder addASpellDialogBox = new AlertDialog.Builder(this);
        final EditText spellName = (EditText) inflaterView.findViewById(R.id.spell_name);
        final EditText spellLevel = (EditText) inflaterView.findViewById(R.id.spell_add_level);
        final EditText spellSchool = (EditText) inflaterView.findViewById(R.id.spell_school);
        final EditText spellCastingTime = (EditText) inflaterView.findViewById(R.id.spell_casting_time);
        final EditText spellRange = (EditText) inflaterView.findViewById(R.id.spell_range);
        final EditText spellDuration = (EditText) inflaterView.findViewById(R.id.spell_duration);
        final EditText spellDescription = (EditText) inflaterView.findViewById(R.id.spell_description);

        //see if ability or spell is selected
        final RadioButton abilityButton = (RadioButton) inflaterView.findViewById(R.id.spell_ability);
        final RadioButton spellButton = (RadioButton) inflaterView.findViewById(R.id.spell_spell);
        spellButton.setChecked(true);

        abilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spellLevel.setVisibility(View.GONE);
                spellSchool.setVisibility(View.GONE);
                spellCastingTime.setVisibility(View.GONE);
                spellRange.setVisibility(View.GONE);
                spellDuration.setVisibility(View.GONE);
            }
        });

        spellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spellLevel.setVisibility(View.VISIBLE);
                spellSchool.setVisibility(View.VISIBLE);
                spellCastingTime.setVisibility(View.VISIBLE);
                spellRange.setVisibility(View.VISIBLE);
                spellDuration.setVisibility(View.VISIBLE);
            }
        });

        addASpellDialogBox.setView(inflaterView);
        addASpellDialogBox.setPositiveButton("Add",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ContentValues finalCV = new ContentValues();

                    finalCV.put(SQLHelper.SPELL_CHARACTER_ID_LINK ,characterID);
                    finalCV.put(SQLHelper.SPELL_NAME,spellName.getText().toString());
                    if (abilityButton.isChecked()){
                        finalCV.put(SQLHelper.SPELL_LEVEL,"ability");
                    }
                    else{
                        if (("".equals(spellLevel.getText().toString()))||
                                ("".equals(spellName.getText().toString()))){
                            Toast.makeText(Spells.this,"Please enter a level and a spell name. Enter 0 for cantrips",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        finalCV.put(SQLHelper.SPELL_LEVEL,spellLevel.getText().toString());
                    }
                    finalCV.put(SQLHelper.SPELL_SCHOOL,spellSchool.getText().toString());
                    finalCV.put(SQLHelper.SPELL_CASTING_TIME,spellCastingTime.getText().toString());
                    finalCV.put(SQLHelper.SPELL_RANGE,spellRange.getText().toString());
                    finalCV.put(SQLHelper.SPELL_DURATION,spellDuration.getText().toString());
                    finalCV.put(SQLHelper.SPELL_DESCRIPTION,spellDescription.getText().toString());

                    try {
                        finalSpellDB.insert(SQLHelper.SPELL_TABLE_NAME, null, finalCV);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(getBaseContext(), "Your spell has been added", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        MenuAndDatabase.errorNotification(getBaseContext(), e.toString());
                    }
                }catch(Exception e){
//                    Toast.makeText(getBaseContext(),e.toString(),Toast.LENGTH_LONG).show();
                    MenuAndDatabase.errorNotification(getBaseContext(), e.toString());
                }
            }
        });
        addASpellDialogBox.setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Activates the dialog box
        AlertDialog spellDialog = addASpellDialogBox.create();
//        weaponDialog.setContentView(R.layout.add_inventory_item);
        spellDialog.show();
        spellDialog.getWindow().setLayout((int)Math.round(screenWidth),(int)Math.round(screenHeight));
    }

    public class SpellExpandableListAdapter extends BaseExpandableListAdapter{
        private Context _context;
        //child data in format of header title, child title
        private HashMap<String, List<String>> listDataChildT;

        public SpellExpandableListAdapter(Context context, HashMap<String, List<String>> listChildData){
            this._context = context;
            listDataChildT = listChildData;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.spell_expandable_child, null);
            }

            //sets the variables to the views
            TextView spellName = (TextView) convertView.findViewById(R.id.spell_name);
            TextView spellLevel = (TextView) convertView.findViewById(R.id.spell_level);
            TextView spellSchool = (TextView) convertView.findViewById(R.id.spell_school);
            TextView spellCastingTime = (TextView) convertView.findViewById(R.id.spell_casting_time);
            TextView spellRange = (TextView) convertView.findViewById(R.id.spell_range);
            TextView spellDuration = (TextView) convertView.findViewById(R.id.spell_duration);
            TextView spellDescription = (TextView) convertView.findViewById(R.id.spell_description);
            ToggleButton spellPrepared = (ToggleButton) convertView.findViewById(R.id.spell_toggle);

            //sets up the database
            SQLHelper spellSqlHelper = new SQLHelper(Spells.this);
            SQLiteDatabase spellDB = spellSqlHelper.getWritableDatabase();
            Cursor cursor = spellDB.query(SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_ID_LINK  + "=? AND " + SQLHelper.SPELL_NAME + "=?",new String[] {characterID,childText},null,null,null);
            cursor.moveToFirst();

            //sets the views to the values
            try {
                spellName.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                spellLevel.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_LEVEL)));
                spellSchool.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_SCHOOL)));
                spellCastingTime.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_CASTING_TIME)));
                spellRange.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_RANGE)));
                spellDuration.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_DURATION)));
                spellDescription.setText(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_DESCRIPTION)));
                spellPrepared.setTag(cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_NAME)));

                if (cursor.getInt(cursor.getColumnIndex(SQLHelper.SPELL_PREPARED)) == 1){
                    spellPrepared.setChecked(true);
                }
                else
                {
                    spellPrepared.setChecked(false);
                }

                spellPrepared.setVisibility(View.VISIBLE);
                spellLevel.setVisibility(View.VISIBLE);
                spellSchool.setVisibility(View.VISIBLE);
                spellCastingTime.setVisibility(View.VISIBLE);
                spellRange.setVisibility(View.VISIBLE);
                spellDuration.setVisibility(View.VISIBLE);

                if (groupPosition == 0){
                    spellPrepared.setVisibility(View.GONE);
                    spellLevel.setVisibility(View.GONE);
                    spellSchool.setVisibility(View.GONE);
                    spellCastingTime.setVisibility(View.GONE);
                    spellRange.setVisibility(View.GONE);
                    spellDuration.setVisibility(View.GONE);
                }
                else if(groupPosition == 1){
                    spellPrepared.setVisibility(View.GONE);
                    spellLevel.setVisibility(View.GONE);
                }
                else{
                    spellLevel.setVisibility(View.GONE);
                }
            } catch(Exception e){
            MenuAndDatabase.errorNotification(Spells.this, e.toString());
            }

/*
            Button upButton = (Button) convertView.findViewById(R.id.moveSpellUp);
            Button downButton = (Button) convertView.findViewById(R.id.moveSpellDown);

            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listDataChildT.get(listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupTitle(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listDataChildT.get(listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        Cursor spellCount = null;
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
            String headerTitle = listDataHeader.get(groupPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.spells_expandable_parent, null);
            }

            String groupText = headerTitle + " (" + String.valueOf(getChildrenCount(groupPosition)) + ")";
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.spell_expandable_parent);
            lblListHeader.setText(groupText);

            return convertView;

        }
    }

    private void prepareSpellListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> spellAbilities = new ArrayList<>();
        List<String> spellCantrip = new ArrayList<>();
        List<String> spellLevel1 = new ArrayList<>();
        List<String> spellLevel2 = new ArrayList<>();
        List<String> spellLevel3 = new ArrayList<>();
        List<String> spellLevel4 = new ArrayList<>();
        List<String> spellLevel5 = new ArrayList<>();
        List<String> spellLevel6 = new ArrayList<>();
        List<String> spellLevel7 = new ArrayList<>();
        List<String> spellLevel8 = new ArrayList<>();
        List<String> spellLevel9 = new ArrayList<>();

        Cursor spellCursor = null;
        SQLHelper spellSqlhelper = new SQLHelper(this);
        SQLiteDatabase spellDB = spellSqlhelper.getWritableDatabase();

        //abilities
        spellCursor = SQLHelper.setupDatabase(this,SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_ID_LINK + "=? AND " + SQLHelper.SPELL_LEVEL + "=?", new String[] {characterID,"ability"});
        if (spellCursor.getCount() >0){

            do{
                spellAbilities.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
            }while (spellCursor.moveToNext());
            listDataHeader.add("Abilities");
            listDataChild.put("Abilities", spellAbilities);
        }

        for (int i=0; i<10; i++){
            spellCursor = SQLHelper.setupDatabase(this,SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_ID_LINK + "=? AND " + SQLHelper.SPELL_LEVEL + "=?", new String[] {characterID,String.valueOf(i)});
            if (spellCursor.getCount() >0){
                do{
                    if(i==0) {
                        spellCantrip.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==1) {
                        spellLevel1.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==2) {
                        spellLevel2.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==3) {
                        spellLevel3.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==4) {
                        spellLevel4.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==5) {
                        spellLevel5.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==6) {
                        spellLevel6.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==7) {
                        spellLevel7.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==8) {
                        spellLevel8.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                    else if(i==9) {
                        spellLevel9.add(spellCursor.getString(spellCursor.getColumnIndex(SQLHelper.SPELL_NAME)));
                    }
                }while (spellCursor.moveToNext());

                if(i==0) {
                    listDataHeader.add("Cantrip");
                    listDataChild.put("Cantrip", spellCantrip);
                }
                else if(i==1) {
                    listDataHeader.add("Spell Level 1");
                    listDataChild.put("Spell Level 1", spellLevel1);
                }
                else if(i==2) {
                    listDataHeader.add("Spell Level 2");
                    listDataChild.put("Spell Level 2", spellLevel2);
                }
                else if(i==3) {
                    listDataHeader.add("Spell Level 3");
                    listDataChild.put("Spell Level 3", spellLevel3);
                }
                else if(i==4) {
                    listDataHeader.add("Spell Level 4");
                    listDataChild.put("Spell Level 4", spellLevel4);
                }
                else if(i==5) {
                    listDataHeader.add("Spell Level 5");
                    listDataChild.put("Spell Level 5", spellLevel5);
                }
                else if(i==6) {
                    listDataHeader.add("Spell Level 6");
                    listDataChild.put("Spell Level 6", spellLevel6);
                }
                else if(i==7) {
                    listDataHeader.add("Spell Level 7");
                    listDataChild.put("Spell Level 7", spellLevel7);
                }
                else if(i==8) {
                    listDataHeader.add("Spell Level 8");
                    listDataChild.put("Spell Level 8", spellLevel8);
                }
                else if(i==9) {
                    listDataHeader.add("Spell Level 9");
                    listDataChild.put("Spell Level 9", spellLevel9);
                }
            }
        }
    }

    public void preparedSpell(View v){
        SQLHelper preparedSqlhelper = new SQLHelper(Spells.this);
        SQLiteDatabase preparedDB = preparedSqlhelper.getWritableDatabase();
        Cursor preparedCursor = preparedDB.query(SQLHelper.SPELL_TABLE_NAME,null,SQLHelper.SPELL_CHARACTER_ID_LINK + "=? AND " + SQLHelper.SPELL_NAME + "=?",new String[] {characterID,((ToggleButton)v).getTag().toString()},null,null,null);
        preparedCursor.moveToFirst();
        ContentValues cv = new ContentValues();
        if(((ToggleButton)v).isChecked()) {
            cv.put(SQLHelper.SPELL_PREPARED,1);
        }
        else
        {
            cv.put(SQLHelper.SPELL_PREPARED,0);
        }
        preparedDB.update(SQLHelper.SPELL_TABLE_NAME,cv,SQLHelper.SPELL_CHARACTER_ID_LINK

                + "=? AND " + SQLHelper.SPELL_NAME + "=?",new String[] {characterID,((ToggleButton)v).getTag().toString()});
    }

    //Increases and decreases the hp value
    public void hpUp(View v){
        hpChange(this,R.id.character_hp,true,characterName);
    }

    public void hpDown(View v){
        hpChange(this,R.id.character_hp,false,characterName);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if ((e1.getX() > e2.getX()) && (e1.getX() - e2.getX() > 300)) {
            Intent intentInventory = new Intent(this, Inventory.class);
            intentInventory.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentInventory);
            this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else if((e1.getX() < e2.getX()) && (e2.getX() - e1.getX() > 300)) {
            Intent intentBasicStats= new Intent(this, BasicStats.class);
            intentBasicStats.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentBasicStats);
            this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        return false;
    }

    public void spellCasting(View v){
        ContentValues cv = new ContentValues();
        int currentLevel = 0;
        String columnName = "";
        switch (v.getId()){
            case R.id.spell_level_1T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L1_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_2T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L2_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_3T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L3_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_4T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L4_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_5T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L5_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_6T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L6_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_7T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L7_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_8T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L8_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_9T:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L9_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_1:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L1_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_2:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L2_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_3:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L3_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_4:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L4_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_5:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L5_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_6:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L6_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_7:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L7_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_8:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L8_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            case R.id.spell_level_9:
                if (Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN))) <= 0){
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)));
                }
                else {
                    currentLevel = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MIN)))-1;
                }
                columnName = SQLHelper.CHARACTER_L9_SPELL_MIN;
                cv.put(columnName,currentLevel);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?",new String[] {characterID});
                finish();
                startActivity(getIntent());
                Spells.this.overridePendingTransition(0,0); break;
            default:
                return;
        }
    }

    public void spellCastingMagic(View v){
        LinearLayout spellCastingLinearLayout = (LinearLayout) findViewById(R.id.spell_casting_linear_layout);
        if (spellCastingLinearLayout.getVisibility() == View.VISIBLE){
            spellCastingLinearLayout.setVisibility(View.GONE);
            ContentValues cv = new ContentValues();
            cv.put(SQLHelper.CHARACTER_SPELL_CASTING,"1");
            dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
            Log.i("Spells", "Spell Casting hidden");
        }
        else {
            spellCastingLinearLayout.setVisibility(View.VISIBLE);
            ContentValues cv = new ContentValues();
            cv.put(SQLHelper.CHARACTER_SPELL_CASTING,"0");
            dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
            Log.i("Spells", "Spell Casting visible");
        }
    }
    
    public void resetSpells(View v){
        ContentValues cv = new ContentValues();

        try {
            cv.put(SQLHelper.CHARACTER_L1_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L1_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L2_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L2_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L3_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L3_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L4_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L4_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L5_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L5_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L6_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L6_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L7_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L7_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L8_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L8_SPELL_MAX)));
            cv.put(SQLHelper.CHARACTER_L9_SPELL_MIN, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_L9_SPELL_MAX)));

            dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.C_ID + "=?", new String[]{characterID});

            finish();
            startActivity(getIntent());
            Spells.this.overridePendingTransition(0,0);
        }
        catch (Exception e){
            Log.e("Spells", "Error resetting spells " + e.toString());
        }
    }

    public String spellAbility(Cursor dataPointer){
        String returnValue = "";
        String stringCharisma = "Charisma";
        String stringIntelligence = "Intelligence";
        String stringWisdom = "Wisdom";
        if (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD)))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK))))){
            returnValue = returnValue + stringCharisma;
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER))))){
            if(returnValue.length()>0){
                returnValue = returnValue + System.getProperty("line.separator") + stringWisdom;
            }
            else {
                returnValue = stringWisdom;
            }
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD))))){
            if(returnValue.length()>0){
                returnValue = returnValue + System.getProperty("line.separator") + stringIntelligence;
            }
            else {
                returnValue = stringIntelligence;
            }
        }

        return returnValue;
    }

    public String spellAttackModifier(Cursor dataPointer){
        String returnValue = "";
        int proficiency = MenuAndDatabase.proficiencyCalc(dataPointer);
        String stringCharisma = MenuAndDatabase.modifierCalculator(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataPointer),"1");
        String stringWisdom= MenuAndDatabase.modifierCalculator(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataPointer),"1");
        String stringIntelligence = MenuAndDatabase.modifierCalculator(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataPointer),"1");
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK))))){
            returnValue = returnValue + stringCharisma;
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER))))){
            if(returnValue.length()>0) {
                returnValue = returnValue + System.getProperty("line.separator") + stringWisdom;
            }
            else
            {
                returnValue = String.valueOf(stringWisdom);
            }
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD))))){
            if(returnValue.length()>0){
                returnValue = returnValue + System.getProperty("line.separator") + stringIntelligence;
            }
            else {
                returnValue = String.valueOf(stringIntelligence);
            }
        }
        return returnValue;
    }

    public String spellSaveDC(Cursor dataPointer){
        String returnValue = "";
        int saveDC = 8;
        int proficiency = MenuAndDatabase.proficiencyCalc(dataPointer);
        int intCharisma = saveDC + MenuAndDatabase.modifierCalculatorInteger(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CHA)),proficiencyCalc(dataPointer),"1");
        int intWisdom = saveDC + MenuAndDatabase.modifierCalculatorInteger(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIS)),proficiencyCalc(dataPointer),"1");
        int intIntelligence = saveDC + MenuAndDatabase.modifierCalculatorInteger(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_INT)),proficiencyCalc(dataPointer),"1");
        if ((!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK))))){
            returnValue = returnValue + intCharisma;
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER))))){
            if(returnValue.length()>0) {
                returnValue = returnValue + System.getProperty("line.separator") + intWisdom;
            }
            else
            {
                returnValue = String.valueOf(intWisdom);
            }
        }
        if (!"".equals((dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE))))||
                (!"".equals(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD))))){
            if(returnValue.length()>0){
                returnValue = returnValue + System.getProperty("line.separator") + intIntelligence;
            }
            else {
                returnValue = String.valueOf(intIntelligence);
            }
        }
        return returnValue;
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
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                int currentHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP));
                currentHP++;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HP,currentHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                TextView hpText = (TextView) inflaterView.findViewById(R.id.HP_hp);
                hpText.setText(currentHP + "/" + dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));

            }
        });
        Button hpNegativeButton = (Button) inflaterView.findViewById(R.id.HP_hp_minus);
        hpNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                int currentHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP));
                currentHP--;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HP,currentHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                TextView hpText = (TextView) inflaterView.findViewById(R.id.HP_hp);
                hpText.setText(currentHP + "/" + dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
            }
        });

        Button tempPositiveButton = (Button) inflaterView.findViewById(R.id.HP_temp_plus);
        tempPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                int tempHP = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_TEMPORARY_HP));
                tempHP++;
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_TEMPORARY_HP,tempHP);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                TextView tempText = (TextView) inflaterView.findViewById(R.id.HP_temp);
                tempText.setText("Temp Hit Points:" + String.valueOf(tempHP));
            }
        });
        Button tempNegativeButton = (Button) inflaterView.findViewById(R.id.HP_temp_minus);
        tempNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
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
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                TextView tempText = (TextView) inflaterView.findViewById(R.id.HP_temp);
                tempText.setText("Temp Hit Points:" + String.valueOf(tempHP));
            }
        });

        Button D6HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d6);
        D6HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d6Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d6Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D6));
                }
                catch(Exception e){
                    d6Count = 0;
                }
                if(d6Count <= 0) {
                    d6Count = Integer.parseInt(MenuAndDatabase.d6Calculation(Spells.this,characterName));
                }else{
                    d6Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D6,d6Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                Button d6Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d6);
                d6Button.setText(String.valueOf(d6Count));
            }
        });
        Button D8HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d8);
        D8HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d8Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d8Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D8));
                }
                catch(Exception e){
                    d8Count = 0;
                }
                if(d8Count <= 0) {
                    d8Count = Integer.parseInt(MenuAndDatabase.d8Calculation(Spells.this,characterName));
                }else{
                    d8Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D8,d8Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                Button d8Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d8);
                d8Button.setText(String.valueOf(d8Count));
            }
        });
        Button D10HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d10);
        D10HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d10Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d10Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D10));
                }
                catch(Exception e){
                    d10Count = 0;
                }
                if(d10Count <= 0) {
                    d10Count = Integer.parseInt(MenuAndDatabase.d10Calculation(Spells.this,characterName));
                }else{
                    d10Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D10,d10Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
                Button d10Button = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d10);
                d10Button.setText(String.valueOf(d10Count));
            }
        });
        Button D12HitDice = (Button) inflaterView.findViewById(R.id.HP_button_hit_dice_d12);
        D12HitDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d12Count;
                dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
                dataCursor.moveToFirst();
                try {
                    d12Count = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HIT_DICE_D12));
                }
                catch(Exception e){
                    d12Count = 0;
                }
                if(d12Count <= 0) {
                    d12Count = Integer.parseInt(MenuAndDatabase.d12Calculation(Spells.this,characterName));
                }else{
                    d12Count--;
                }
                //calculate how many hit dice the char should have
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper.CHARACTER_HIT_DICE_D12,d12Count);
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.C_ID + "=?", new String[] {characterID});
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

        dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.C_ID + "=?", new String[]{characterID},null,null,null);
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

    public String groupTitle (int groupPosition){
        String hashMapSelection = "";
        switch (groupPosition){
            case 0:
                hashMapSelection = "Abilities";
                break;
            case 1:
                hashMapSelection = "Cantrip";
                break;
            case 2:
                hashMapSelection = "Spell Level 1";
                break;
            case 3:
                hashMapSelection = "Spell Level 2";
                break;
            case 4:
                hashMapSelection = "Spell Level 3";
                break;
            case 5:
                hashMapSelection = "Spell Level 4";
                break;
            case 6:
                hashMapSelection = "Spell Level 5";
                break;
            case 7:
                hashMapSelection = "Spell Level 6";
                break;
            case 8:
                hashMapSelection = "Spell Level 7";
                break;
            case 9:
                hashMapSelection = "Spell Level 8";
                break;
            case 10:
                hashMapSelection = "Spell Level 9";
                break;
            default:
                break;
        }
        return hashMapSelection;
    }
}