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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory extends MenuAndDatabase {

    String characterName;
    SQLHelper sqlhelper;
    SQLiteDatabase itemDB;
    Cursor itemCursor;

    //expandable list view
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int screenWidth;
    int screenHeight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;

        try {
            Cursor settingCursor = SQLHelper.setupDatabase(this, SQLHelper.SETTING_TABLE_NAME, null, null,null);
            settingCursor.moveToFirst();
            characterName = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_NAME));
            characterID = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_ID));
        }
        catch(Exception e){
            Log.e("Inventory","Error getting settings data");
        }

        try {
            //sets up the gesture detector for onFling method.
            detector = new GestureDetector(this, this);

            dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME +"=?",new String[]{characterName});
            setTextView(R.id.inventory_proficiency_title,getResources().getString(R.string.inventory_proficiency) + " (Bonus: +" + MenuAndDatabase.proficiencyCalc(dataCursor) + ")");

            //Sets up the adapter and gets the data for the list view
            expListView = (ExpandableListView) findViewById(R.id.itemExpandableListView);
            prepareListData();
            listAdapter = new ItemExpandableListAdapter(this,listDataHeader,listDataChild);
            expListView.setAdapter(listAdapter);
            Log.i("Inventory","List View adapter setup");

            //on click for the list view which displays. Would be great if this rolled for the user
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            final int groupPosition, final int childPosition, long id) {
                    Log.i("Inventory", "Listview clicked");

                    //sets up the custom dialog view
                    final View inflaterView = getLayoutInflater().inflate(R.layout.add_inventory_item, null);
                    final AlertDialog.Builder addAWeaponDialogBox = new AlertDialog.Builder(Inventory.this);

                    //sets up the views on the custom dialog view
                    addAWeaponDialogBox.setView(inflaterView);
                    final ContentValues finalCV = new ContentValues();
                    final RadioButton radioWeapon = (RadioButton) inflaterView.findViewById(R.id.radio_group_weapon);
                    final RadioButton radioArmor = (RadioButton) inflaterView.findViewById(R.id.radio_group_armor);
                    final RadioButton radioItem = (RadioButton) inflaterView.findViewById(R.id.radio_group_item);
                    final EditText itemName = (EditText) inflaterView.findViewById(R.id.item_name);
                    final EditText itemCost = (EditText) inflaterView.findViewById(R.id.item_cost);
                    final EditText itemWeight = (EditText) inflaterView.findViewById(R.id.item_weight);
                    final EditText itemDescription = (EditText) inflaterView.findViewById(R.id.item_description);
                    final EditText itemRangeValue = (EditText) inflaterView.findViewById(R.id.item_range);
                    final EditText itemDamageType = (EditText) inflaterView.findViewById(R.id.item_type);
                    final EditText itemDamageRoll = (EditText) inflaterView.findViewById(R.id.item_roll);
                    final EditText itemAC = (EditText) inflaterView.findViewById(R.id.item_ac);
                    final RadioButton radioStrength = (RadioButton) inflaterView.findViewById(R.id.item_radio_strength);
                    final RadioButton radioDexterity = (RadioButton) inflaterView.findViewById(R.id.item_radio_dexterity);
                    final CheckBox checkboxProficient = (CheckBox) inflaterView.findViewById(R.id.item_proficient);

                    try {
                        Cursor itemCursor = SQLHelper.setupDatabase(Inventory.this, SQLHelper.ITEM_TABLE_NAME,null,SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_NAME + "=?",new String[]{characterName,listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)});

                        itemName.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_NAME)));
                        itemCost.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_COST)));
                        itemWeight.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_WEIGHT)));
                        itemDescription.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_DESCRIPTION)));
                        itemRangeValue.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_RANGE)));
                        itemDamageType.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE)));
                        itemDamageRoll.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL)));
                        itemAC.setText(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_ARMOR_AC)));

                        if ("strength".equals(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_ATTRIBUTE)))){
                            radioStrength.setChecked(true);
                        }
                        else if ("dexterity".equals(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_ATTRIBUTE)))){
                            radioDexterity.setChecked(true);
                        }

                        if ("1".equals(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_PROFICIENT)))){
                            checkboxProficient.setChecked(true);
                        }

                        final EditText armorInfo = (EditText) inflaterView.findViewById(R.id.item_ac);
                        final LinearLayout weaponInfo = (LinearLayout) inflaterView.findViewById(R.id.item_weapon_data);

                        //sets the custom dialog view up so that you only see what's relevant
                        if(groupPosition==0){
                            radioWeapon.setChecked(true);
                            weaponInfo.setVisibility(View.VISIBLE);
                            armorInfo.setVisibility(View.GONE);
                        }
                        else if(groupPosition==1) {
                            radioArmor.setChecked(true);
                            weaponInfo.setVisibility(View.GONE);
                            armorInfo.setVisibility(View.VISIBLE);
                        }
                        else if(groupPosition==2) {
                            radioItem.setChecked(true);
                            weaponInfo.setVisibility(View.GONE);
                            armorInfo.setVisibility(View.GONE);
                        }
                        Log.i("Inventory", "update dialog populated and setup");

                        //Sets up the radio buttons which shows & hides various views on the custom dialog view
                        radioWeapon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                weaponInfo.setVisibility(View.VISIBLE);
                                armorInfo.setVisibility(View.GONE);
                                Log.i("Inventory", "update weapon radio button selected");
                            }
                        });

                        radioArmor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                weaponInfo.setVisibility(View.GONE);
                                armorInfo.setVisibility(View.VISIBLE);
                                Log.i("Inventory", "update armor radio button selected");
                            }
                        });

                        radioItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                weaponInfo.setVisibility(View.GONE);
                                armorInfo.setVisibility(View.GONE);
                                Log.i("Inventory", "update item radio button selected");
                            }
                        });

                    }catch(Exception e){
                        Log.e("Inventory", "Error in update item: " + e.toString());
                    }

                    addAWeaponDialogBox.setNeutralButton("ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Determins whether a weapon, armor or item has been added.
                            if (radioWeapon.isChecked()){
                                finalCV.put(SQLHelper.ITEM_CATEGORY,"weapon");
                            }
                            else if (radioArmor.isChecked()){
                                finalCV.put(SQLHelper.ITEM_CATEGORY,"armor");
                            }
                            else if (radioItem.isChecked()){
                                finalCV.put(SQLHelper.ITEM_CATEGORY, "item");
                            }

                            if (radioStrength.isChecked()){
                                finalCV.put(SQLHelper.ITEM_ATTRIBUTE, "strength");
                            }
                            else if (radioDexterity.isChecked()){
                                finalCV.put(SQLHelper.ITEM_ATTRIBUTE, "dexterity");

                            }

                            if (checkboxProficient.isChecked()){
                                finalCV.put(SQLHelper.ITEM_PROFICIENT, "1");
                            }


                            //builds the content value to be added to the database.
                            finalCV.put(SQLHelper.CHARACTER_NAME_LINK,characterName);
                            finalCV.put(SQLHelper.ITEM_NAME,itemName.getText().toString());
                            finalCV.put(SQLHelper.ITEM_COST,itemCost.getText().toString());
                            finalCV.put(SQLHelper.ITEM_WEIGHT,itemWeight.getText().toString());
                            finalCV.put(SQLHelper.ITEM_DESCRIPTION,itemDescription.getText().toString());
                            finalCV.put(SQLHelper.ITEM_WEAPON_RANGE,itemRangeValue.getText().toString());
                            finalCV.put(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE,itemDamageType.getText().toString());
                            finalCV.put(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL,itemDamageRoll.getText().toString());
                            finalCV.put(SQLHelper.ITEM_ARMOR_AC,itemAC.getText().toString());
                            Log.i("Inventory", "updated data populated into content value: " + finalCV.size());

                            try {
                                dataDB.update(SQLHelper.ITEM_TABLE_NAME, finalCV, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_NAME + "=?", new String[]{characterName, listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)});
                                finish();
                                startActivity(getIntent());
                                Inventory.this.overridePendingTransition(0,0);
//                                Toast.makeText(getBaseContext(), "Your item has been updated", Toast.LENGTH_SHORT).show();
                                Log.i("Inventory", "Your item has been updated");

                            } catch (Exception e) {
                                Log.e("Inventory", "Error updating item: " + e.toString());
                            }
                            dialog.dismiss();
                        }
                    });

                    AlertDialog editDialog = addAWeaponDialogBox.create();
                    editDialog.show();
                    editDialog.getWindow().setLayout((int)Math.round(screenWidth),(int)Math.round(screenHeight));
                    return false;
                }
            });

            //long press listener for the listview. Pops up a delete confirmation
            expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemType = ExpandableListView.getPackedPositionType(id);
                    int childPosition;
                    int groupPosition;
                    //detects the long press
                    if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        childPosition = ExpandableListView.getPackedPositionChild(id);
                        groupPosition = ExpandableListView.getPackedPositionGroup(id);

                        //do your per-item callback here
                        //Creates the alert
                        final int finalGroupPosition = groupPosition;
                        final int finalChildPosition = childPosition;

                        //builds and displays the delete confirmation dialog box.
                        AlertDialog.Builder deleteConfirmationDialog = new AlertDialog.Builder(Inventory.this);
                        deleteConfirmationDialog.setTitle("Delete Item?");
                        deleteConfirmationDialog.setMessage("Are you sure you want to delete "+ listDataChild.get(listDataHeader.get(finalGroupPosition)).get(finalChildPosition) +"? This cannot be undone.");

                        //deletes selected if yes is pressed
                        deleteConfirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if yes has been clicked then try to delete the record using the char name edittext
                                try{
                                    itemDB.delete(SQLHelper.ITEM_TABLE_NAME, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_NAME + "=?", new String[]{characterName, listDataChild.get(listDataHeader.get(finalGroupPosition)).get(finalChildPosition)});
                                    Log.i("Inventory", "Item was deleted");
                                }
                                catch (Exception e){
                                    Log.e("Inventory", "Did not delete item: " + e.toString());
                                }
                                //Move back to the character select screen
                                Intent intentInventory = new Intent(Inventory.this, Inventory.class);
                                intentInventory.putExtra("characterName", characterName);
                                startActivity(intentInventory);
                                finish();
                                dialog.dismiss();
                            }
                        });
                        //just dismisses the dialog box if nothing is selected
                        deleteConfirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if they don't want to delete their character, do nothing
                                dialog.dismiss();
                                Log.i("Inventory","No pressed on the delete dialog");
                            }
                        });
                        //Activates the dialog box
                        AlertDialog confirmationDialog = deleteConfirmationDialog.create();
                        confirmationDialog.show();
                        Log.i("Inventory", "Listview long pressed");
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
            //sets up the character information database
            dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?",new String[]{characterName});
            dataDB = sqlhelper.getWritableDatabase();
            Log.i("Inventory","character database setup. Number of items: " + dataCursor.getCount());

            //sets up the character name, hp value
            setTextView(R.id.character_name,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_NAME)));
            setTextView(R.id.character_hp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP))+"/"+dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
            setTextView(R.id.inventory_speed, dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SPEED))+"ft");
            setTextView(R.id.weapon_proficiencies,"Weapon Proficiencies" + System.getProperty("line.separator") + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WEAPON_PROFICIENCIES)));
            setTextView(R.id.armor_proficiencies,"Armor Proficiencies" + System.getProperty("line.separator") + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ARMOR_PROFICIENCIES)));
            setTextView(R.id.tool_proficiencies,"Tool Proficiencies" + System.getProperty("line.separator") + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_TOOL_PROFICIENCIES)));
            setTextView(R.id.language_proficiencies,"Languages" + System.getProperty("line.separator") + dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_LANGUAGE_PROFICIENCIES)));

            //money stuff
            setEditView(R.id.inventory_money_pp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_PP)));
            setEditView(R.id.inventory_money_gp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_GP)));
            setEditView(R.id.inventory_money_ep,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_EP)));
            setEditView(R.id.inventory_money_sp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SP)));
            setEditView(R.id.inventory_money_cp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_CP)));

            final EditText moneyPP = (EditText) findViewById(R.id.inventory_money_pp);
            final EditText moneyGP = (EditText) findViewById(R.id.inventory_money_gp);
            final EditText moneyEP = (EditText) findViewById(R.id.inventory_money_ep);
            final EditText moneySP = (EditText) findViewById(R.id.inventory_money_sp);
            final EditText moneyCP = (EditText) findViewById(R.id.inventory_money_cp);

            //saves the edits to the database
            moneyPP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLHelper.CHARACTER_PP,moneyPP.getText().toString());
                        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
                    }catch(Exception e){
                        Log.e("Inventory", "Error setting PP");
                    }
                }
            });
            moneyGP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLHelper.CHARACTER_GP,moneyGP.getText().toString());
                        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
                    }catch(Exception e){
                        Log.e("Inventory", "Error setting GP");
                    }

                }
            });
            moneyEP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLHelper.CHARACTER_EP,moneyEP.getText().toString());
                        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
                    }catch(Exception e){
                        Log.e("Inventory", "Error setting EP");
                    }

                }
            });
            moneySP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLHelper.CHARACTER_SP,moneySP.getText().toString());
                        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
                    }catch(Exception e){
                        Log.e("Inventory", "Error setting SP");
                    }

                }
            });
            moneyCP.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLHelper.CHARACTER_CP,moneyCP.getText().toString());
                        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
                    }catch(Exception e){
                        Log.e("Inventory", "Error setting CP");
                    }

                }
            });

                //initiative calculation
                int dexValue = modifierCalculatorInteger(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),0,"0");
                int initiativeValue = dexValue;
                try {
                    initiativeValue = dexValue + Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INITIATIVE)));
                }
                catch (Exception e){

                    Log.e("Inventory","error calculating bonus initiative");
                }
                setTextView(R.id.inventory_initiative,Integer.toString(initiativeValue));

                //armor class
                Cursor acCalc = dataDB.query(SQLHelper.ITEM_TABLE_NAME, null, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_CATEGORY + "=?", new String[]{characterName,"armor"}, null, null, null);
                acCalc.moveToFirst();
                int intACCalc = 0;
                do{
                    try{
                        String tempStringAC = acCalc.getString(acCalc.getColumnIndex(SQLHelper.ITEM_ARMOR_AC));
                        int tempAC = Integer.parseInt(tempStringAC);
                        intACCalc = intACCalc + tempAC;
                    }
                    catch(Exception e){
                        Log.e("Inventory","Error calculating ac: " + e.toString());
                    }
                }while(acCalc.moveToNext());

                if (intACCalc == 0){
                    intACCalc = 10 + dexValue;
                }
                setTextView(R.id.inventory_armor_class,"" + intACCalc);
                Log.i("Inventory", "Character items setup");
            }catch (Exception e) {
                Log.e("Inventory","There was an error in onCreate: " + e.toString());
            }
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                ContentValues cv = new ContentValues();
                switch (groupPosition){
                    case 0:
                        cv.put(SQLHelper.CHARACTER_WEAPONS_VISIBLE,"0");
                        break;
                    case 1:
                        cv.put(SQLHelper.CHARACTER_ARMOR_VISIBLE,"0");
                        break;
                    case 2:
                        cv.put(SQLHelper.CHARACTER_INVENTORY,"0");
                        break;
                    default:
                        return;
                }
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                ContentValues cv = new ContentValues();
                switch (groupPosition){
                    case 0:
                        cv.put(SQLHelper.CHARACTER_WEAPONS_VISIBLE,"1");
                        break;
                    case 1:
                        cv.put(SQLHelper.CHARACTER_ARMOR_VISIBLE,"1");
                        break;
                    case 2:
                        cv.put(SQLHelper.CHARACTER_INVENTORY,"1");
                        break;
                    default:
                        return;
                }
                dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            }
        });

        //sets whether the groups are expanded or not
        if("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WEAPONS_VISIBLE)))){
            expListView.expandGroup(0);
        }
        else{
            expListView.collapseGroup(0);
        }
        if("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ARMOR_VISIBLE)))){
            expListView.expandGroup(1);
        }
        else{
            expListView.collapseGroup(1);
        }
        if("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_INVENTORY)))){
            expListView.expandGroup(2);
        }
        else{
            expListView.collapseGroup(2);
        }

        LinearLayout profLL = (LinearLayout) findViewById(R.id.inventory_proficiencies);
        if("0".equals(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_PROFICIENCIES_VISIBLE)))){
            profLL.setVisibility(View.VISIBLE);
        }
        else
        {
            profLL.setVisibility(View.GONE);
        }

    }

    //This shows and hides the proficiencies when the proficiency button is pressed
    public void proficiencyMagic(View v){
        LinearLayout proficiencyLinearLayout = (LinearLayout) findViewById(R.id.inventory_proficiencies);
        if (proficiencyLinearLayout.getVisibility() == View.VISIBLE){
            proficiencyLinearLayout.setVisibility(View.GONE);
            ContentValues cv = new ContentValues();
            cv.put(SQLHelper.CHARACTER_PROFICIENCIES_VISIBLE,"1");
            dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            Log.i("Inventory", "Proficiencies hidden");
        }
        else {
            proficiencyLinearLayout.setVisibility(View.VISIBLE);
            ContentValues cv = new ContentValues();
            cv.put(SQLHelper.CHARACTER_PROFICIENCIES_VISIBLE,"0");
            dataDB.update(SQLHelper.TABLE_NAME,cv,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            Log.i("Inventory", "Proficiencies visible");
        }
    }

    public void addWeapon (View v) {
        //sets up the custom dialog view
        final View inflaterView = getLayoutInflater().inflate(R.layout.add_inventory_item, null);
        final AlertDialog.Builder addAWeaponDialogBox = new AlertDialog.Builder(this);

        //sets up the views on the custom dialog view
        RadioButton radioWeapon = (RadioButton) inflaterView.findViewById(R.id.radio_group_weapon);
        RadioButton radioArmor = (RadioButton) inflaterView.findViewById(R.id.radio_group_armor);
        RadioButton radioItem = (RadioButton) inflaterView.findViewById(R.id.radio_group_item);
        RadioButton radioStrength = (RadioButton) inflaterView.findViewById(R.id.item_radio_strength);
        RadioButton radioDexterity = (RadioButton) inflaterView.findViewById(R.id.item_radio_dexterity);
        final EditText armorInfo = (EditText) inflaterView.findViewById(R.id.item_ac);
        final LinearLayout weaponInfo = (LinearLayout) inflaterView.findViewById(R.id.item_weapon_data);

        //sets the custom dialog view up so that you only see what's relevant
        radioWeapon.setChecked(true);
        weaponInfo.setVisibility(View.VISIBLE);
        armorInfo.setVisibility(View.GONE);
        Log.i("Inventory", "basic dialog setup complete");

        //Sets up the radio buttons which shows & hides various views on the custom dialog view
        radioWeapon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weaponInfo.setVisibility(View.VISIBLE);
                armorInfo.setVisibility(View.GONE);
                Log.i("Inventory", "weapon radio button selected");
            }
        });

        radioArmor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weaponInfo.setVisibility(View.GONE);
                armorInfo.setVisibility(View.VISIBLE);
                Log.i("Inventory", "armor radio button selected");
            }
        });

        radioItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weaponInfo.setVisibility(View.GONE);
                armorInfo.setVisibility(View.GONE);
                Log.i("Inventory", "item radio button selected");
            }
        });


        addAWeaponDialogBox.setView(inflaterView);
        //Sets up and adds the data in the edit boxes to the contentvalue then adds to the database.
        addAWeaponDialogBox.setPositiveButton("Add",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ContentValues finalCV = new ContentValues();
                    RadioButton radioWeapon = (RadioButton) inflaterView.findViewById(R.id.radio_group_weapon);
                    RadioButton radioArmor = (RadioButton) inflaterView.findViewById(R.id.radio_group_armor);
                    RadioButton radioItem = (RadioButton) inflaterView.findViewById(R.id.radio_group_item);
                    EditText itemName = (EditText) inflaterView.findViewById(R.id.item_name);
                    EditText itemCost = (EditText) inflaterView.findViewById(R.id.item_cost);
                    EditText itemWeight = (EditText) inflaterView.findViewById(R.id.item_weight);
                    EditText itemDescription = (EditText) inflaterView.findViewById(R.id.item_description);
                    EditText itemRangeValue = (EditText) inflaterView.findViewById(R.id.item_range);
                    EditText itemDamageType = (EditText) inflaterView.findViewById(R.id.item_type);
                    EditText itemDamageRoll = (EditText) inflaterView.findViewById(R.id.item_roll);
                    EditText itemAC = (EditText) inflaterView.findViewById(R.id.item_ac);
                    RadioButton radioStrength = (RadioButton) inflaterView.findViewById(R.id.item_radio_strength);
                    RadioButton radioDexterity = (RadioButton) inflaterView.findViewById(R.id.item_radio_dexterity);
                    CheckBox checkboxProficient = (CheckBox) inflaterView.findViewById(R.id.item_proficient);

                    if (checkboxProficient.isChecked()){
                        finalCV.put(SQLHelper.ITEM_PROFICIENT,"1");
                    }
                    else
                    {
                        finalCV.put(SQLHelper.ITEM_PROFICIENT,"0");
                    }

                    if ("".equals(itemName.getText().toString())){
                        Toast.makeText(Inventory.this, "Please enter an item name.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        return;
                    }

                    //Determins whether a weapon, armor or item has been added.
                    if (radioWeapon.isChecked()){
                        finalCV.put(SQLHelper.ITEM_CATEGORY,"weapon");
                    }
                    else if (radioArmor.isChecked()){
                        finalCV.put(SQLHelper.ITEM_CATEGORY,"armor");
                    }
                    else if (radioItem.isChecked()){
                        finalCV.put(SQLHelper.ITEM_CATEGORY, "item");
                    }

                    if (radioStrength.isChecked()){
                        finalCV.put(SQLHelper.ITEM_ATTRIBUTE,"strength");
                    }
                    else if (radioDexterity.isChecked()){
                        finalCV.put(SQLHelper.ITEM_ATTRIBUTE,"dexterity");
                    }

                    if (checkboxProficient.isChecked()){
                        finalCV.put(SQLHelper.ITEM_PROFICIENT,"1");
                    }

                    //builds the content value to be added to the database.
                    finalCV.put(SQLHelper.CHARACTER_NAME_LINK,characterName);
                    finalCV.put(SQLHelper.ITEM_NAME,itemName.getText().toString());
                    finalCV.put(SQLHelper.ITEM_COST,itemCost.getText().toString());
                    finalCV.put(SQLHelper.ITEM_WEIGHT,itemWeight.getText().toString());
                    finalCV.put(SQLHelper.ITEM_DESCRIPTION,itemDescription.getText().toString());
                    finalCV.put(SQLHelper.ITEM_WEAPON_RANGE,itemRangeValue.getText().toString());
                    finalCV.put(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE,itemDamageType.getText().toString());
                    finalCV.put(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL,itemDamageRoll.getText().toString());
                    finalCV.put(SQLHelper.ITEM_ARMOR_AC,itemAC.getText().toString());
                    Log.i("Inventory", "save data populated into content value: " + finalCV.size());

                    try {
                        dataDB.insert(SQLHelper.ITEM_TABLE_NAME, null, finalCV);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(getBaseContext(), "Your item has been added", Toast.LENGTH_SHORT).show();
                        Log.i("Inventory", "Your item has been added");

                    } catch (Exception e) {
                        Log.e("Inventory", "Error adding item: " + e.toString());
                    }
                }catch(Exception e){
                    Log.e("Inventory", "Error in addWeapon: " + e.toString());
                }
            }
        });

        //sets up the Cancel button. Currently does nothing.
        addAWeaponDialogBox.setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            }
        });
        //Activates the dialog box
        AlertDialog weaponDialog = addAWeaponDialogBox.create();
//        weaponDialog.setContentView(R.layout.add_inventory_item);
        weaponDialog.show();
        weaponDialog.getWindow().setLayout((int)Math.round(screenWidth),(int)Math.round(screenHeight));
        Log.i("Inventory", "addWeapon completed");
    }

    // My custom listview adapter.
    public class ItemExpandableListAdapter extends BaseExpandableListAdapter{
        private Context _context;
        private List<String> _listDataHeader; //header titles
        //child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ItemExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData){
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_view_child, null);
            }

            //sets up the database
            Cursor setChildItemsCursor = SQLHelper.setupDatabase(Inventory.this,SQLHelper.ITEM_TABLE_NAME,null,SQLHelper.CHARACTER_NAME_LINK+ "=? AND " + SQLHelper.ITEM_NAME + "=?",new String[] {characterName,childText});
            /*SQLHelper setChildItems = new SQLHelper(Inventory.this);
            SQLiteDatabase setChildItemsDB = setChildItems.getWritableDatabase();*/
//            Cursor setChildItemsCursor = setChildItemsDB.query(SQLHelper.ITEM_TABLE_NAME,null,SQLHelper.CHARACTER_NAME_LINK+ "=? AND " + SQLHelper.ITEM_NAME + "=?", new String[] {characterName,childText},null,null,null);
//            setChildItemsCursor.moveToFirst();

            //adds the information to the textviews
            TextView itemName = (TextView) convertView.findViewById(R.id.expandableItemTextViewName);
            TextView itemCost = (TextView) convertView.findViewById(R.id.expandableItemTextViewCost);
            TextView itemWeight = (TextView) convertView.findViewById(R.id.expandableItemTextViewWeight);
            TextView itemRange = (TextView) convertView.findViewById(R.id.expandableItemTextViewRange);
            TextView itemRoll = (TextView) convertView.findViewById(R.id.expandableItemTextViewDamageRoll);
            TextView itemType = (TextView) convertView.findViewById(R.id.expandableItemTextViewDamageType);
            TextView itemAC = (TextView) convertView.findViewById(R.id.expandableItemTextViewAC);
            TextView itemDescription = (TextView) convertView.findViewById(R.id.expandableItemTextViewDescription);
            TextView itemAttribute = (TextView) convertView.findViewById(R.id.expandableItemTextViewAttackAttribute);

            itemName.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_NAME)));
            itemCost.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_COST)));
            itemWeight.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_WEIGHT)));
            itemRange.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_RANGE)));
            itemRoll.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_ROLL)));
            itemType.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_WEAPON_DAMAGE_TYPE)));
            itemAC.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_ARMOR_AC)));
            itemDescription.setText(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_DESCRIPTION)));

            //is or isn't proficient and using strength
            if ("strength".equals(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_ATTRIBUTE)))){
                String mod = "Atk:" + modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_STR)),proficiencyCalc(dataCursor),setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_PROFICIENT)));
                itemAttribute.setText(mod);
            }
            //is or isn't proficient and using dexterity
            else if ("dexterity".equals(setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_ATTRIBUTE)))){
                String mod = "Atk:" + modifierCalculator(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_DEX)),proficiencyCalc(dataCursor),setChildItemsCursor.getString(setChildItemsCursor.getColumnIndex(SQLHelper.ITEM_PROFICIENT)));
                itemAttribute.setText(mod);
            }

            LinearLayout weaponLayout = (LinearLayout) convertView.findViewById(R.id.expandableItemWeaponRow);
            itemAC.setVisibility(View.GONE);
            weaponLayout.setVisibility(View.GONE);

            //displays different views depending on weapon or armor
            if (getGroup(groupPosition).toString().contains("Weapons")){
                weaponLayout.setVisibility(View.VISIBLE);

            }
            else if (getGroup(groupPosition).toString().contains("Armor")){
                itemAC.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
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

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null){
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_view_parent, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.expandableListViewParent);
            lblListHeader.setText(headerTitle);

            return convertView;
        }
    }

    //Sets up the data to populate the listview
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        //creates the parent groups
        List<String> itemWeapon = new ArrayList<>();
        List<String> itemArmor = new ArrayList<>();
        List<String> itemInventory = new ArrayList<>();

        sqlhelper = new SQLHelper(this);
        itemDB = sqlhelper.getWritableDatabase();

        // Adding child data
        //weapons
        try {
            itemCursor = itemDB.query(SQLHelper.ITEM_TABLE_NAME, null, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_CATEGORY + "=?", new String[]{characterName,"weapon"}, null, null, null);
            listDataHeader.add("Weapons (" + Integer.toString(itemCursor.getCount()) + ")");
            itemCursor.moveToFirst();
            do {
                itemWeapon.add(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_NAME)));
            } while (itemCursor.moveToNext());
            Log.i("Inventory", "weapons added: " + itemWeapon.size());
        } catch (Exception e) {
            Log.e("Inventory","Error adding weapons: " + e.toString());
        }

        //armor
        try {
            itemCursor = itemDB.query(SQLHelper.ITEM_TABLE_NAME, null, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_CATEGORY + "=?", new String[]{characterName,"armor"}, null, null, null);
            listDataHeader.add("Armor (" + Integer.toString(itemCursor.getCount()) + ")");
            itemCursor.moveToFirst();
            do {
                itemArmor.add(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_NAME)));
            } while (itemCursor.moveToNext());
            Log.i("Inventory", "armor added: " + itemArmor.size());
        } catch (Exception e) {
            Log.e("Inventory","Error adding armor: " + e.toString());
        }

        //general
        try {
            itemCursor = itemDB.query(SQLHelper.ITEM_TABLE_NAME, null, SQLHelper.CHARACTER_NAME_LINK + "=? AND " + SQLHelper.ITEM_CATEGORY + "=?", new String[]{characterName,"item"}, null, null, null);
            listDataHeader.add("Inventory (" + Integer.toString(itemCursor.getCount()) + ")");
            itemCursor.moveToFirst();
            do {
                itemInventory.add(itemCursor.getString(itemCursor.getColumnIndex(SQLHelper.ITEM_NAME)));
            } while (itemCursor.moveToNext());
            Log.i("Inventory", "item added: " + itemInventory.size());
        } catch (Exception e) {
            Log.e("Inventory","Error adding items: " + e.toString());
        }

        listDataChild.put(listDataHeader.get(0), itemWeapon); // Header, Child data
        listDataChild.put(listDataHeader.get(1), itemArmor);
        listDataChild.put(listDataHeader.get(2), itemInventory);
        Log.i("Inventory", "data is prepared");
    }

    //Increases and decreases the hp value
    public void hpUp(View v){
        hpChange(this,R.id.character_hp,true,characterName);
    }
    public void hpDown(View v){
        hpChange(this,R.id.character_hp,false,characterName);
    }

    //Detects the motion flick and moves to relevant activity.
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if ((e1.getX() > e2.getX()) && (e1.getX() - e2.getX() > 300)) {
            Intent intentBackstory = new Intent(this, Backstory.class);
            intentBackstory.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentBackstory);
            this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            Log.i("Inventory", "onFling to backstory");
        }
        else if((e1.getX() < e2.getX()) && (e2.getX() - e1.getX() > 300)) {
            Intent intentSpells = new Intent(this, Spells.class);
            intentSpells.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentSpells);
            this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            Log.i("Inventory", "onFling to spells");
        }
        return false;
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
                    d6Count = Integer.parseInt(MenuAndDatabase.d6Calculation(Inventory.this,characterName));
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
                    d8Count = Integer.parseInt(MenuAndDatabase.d8Calculation(Inventory.this,characterName));
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
                    d10Count = Integer.parseInt(MenuAndDatabase.d10Calculation(Inventory.this,characterName));
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
                    d12Count = Integer.parseInt(MenuAndDatabase.d12Calculation(Inventory.this,characterName));
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
}