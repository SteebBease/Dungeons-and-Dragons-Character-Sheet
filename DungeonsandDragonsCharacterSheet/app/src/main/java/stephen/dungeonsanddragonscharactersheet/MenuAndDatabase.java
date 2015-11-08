package stephen.dungeonsanddragonscharactersheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MenuAndDatabase extends Activity implements OnGestureListener{

    public SQLHelper dataSqlhelper;
    public SQLiteDatabase dataDB;
    public Cursor dataCursor;
    public String characterName;
    public String characterID;

    //Creates the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        dataSqlhelper = new SQLHelper(this);
        dataDB = dataSqlhelper.getWritableDatabase();

        return true;
    }

    //decides what happens when different elements are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        dataCursor = dataDB.query(SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName},null,null,null);
//        dataCursor.moveToFirst();
        switch(item.getItemId()){
            case R.id.basic_stats:
                finish();
                Intent intentBasicStats = new Intent(getBaseContext(), BasicStats.class);
                startActivity(intentBasicStats);
                Log.i("MenuAndDatabase","Basic Stats Selected");
                break;
            case R.id.inventory:
                finish();
                Intent intentInventory = new Intent(getBaseContext(), Inventory.class);
                startActivity(intentInventory);
                Log.i("MenuAndDatabase","Inventory Selected");
                break;
            case R.id.changecharacter:
                finish();
                Intent intentChangeCharacter = new Intent(getBaseContext(), CharacterSelectionScreen.class);
                startActivity(intentChangeCharacter);
                Log.i("MenuAndDatabase","Change Character Selected");
                break;
            case R.id.spells:
                finish();
                Intent intentSpells = new Intent(getBaseContext(), Spells.class);
                startActivity(intentSpells);
                Log.i("MenuAndDatabase","Spells Selected");
                break;
            case R.id.backstory:
                finish();
                Intent intentBackstory = new Intent(getBaseContext(), Backstory.class);
                startActivity(intentBackstory);
                Log.i("MenuAndDatabase","Backstory Selected");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        return super.onOptionsItemSelected(item);
    }

    //Generates an onscreen error message. This is primarily for debug use.
    static void errorNotification(Context context,String exception) {
        AlertDialog.Builder errorDialogBox= new AlertDialog.Builder(context);
        errorDialogBox.setTitle("Exception");
        errorDialogBox.setMessage(exception);
        errorDialogBox.setNeutralButton(android.R.string.ok,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Activates the dialog box
        AlertDialog errorDialog = errorDialogBox.create();
        errorDialog.show();

    }

    //calculates the modifer value as a string including the + or -
    static String modifierCalculator(String attributeString, int proficiency, String proficient){
        int attribute = Integer.parseInt(attributeString);
        Double modCalcDbl = Math.floor((attribute- 10) / 2);
        int modCalc = modCalcDbl.intValue();

        //check for a 9 attribute, this is incorrectly calculated with the above math. -1/2 returns 0
        if(attribute == 9){
            modCalc = -1;
        }
        int areTheyProficient = Integer.parseInt(proficient);
        String returnValue;
        if (areTheyProficient == 1){
            modCalc = modCalc + proficiency;
        }
        if (modCalc >= 0){
            returnValue = "+" + modCalc;
        }
        else
        {
            returnValue = "" + modCalc;
        }
        Log.i("MenuAndDatabase","modifer Calculator complete. Return Value: " + returnValue);
        return returnValue;
    }

    //calculates the modifer value as a number
    static int modifierCalculatorInteger(String attributeString, int proficiency,String proficient){
        int attribute = Integer.parseInt(attributeString);
        Double modCalcDbl = Math.floor((attribute- 10) / 2);
        int modCalc = modCalcDbl.intValue();

        //check for a 9 attribute, this is incorrectly calculated with the above math. -1/2 returns 0
        if(attribute == 9){
            modCalc = -1;
        }

        int areTheyProficient = Integer.parseInt(proficient);
        if (areTheyProficient == 1){
            modCalc = modCalc + proficiency;
        }
        Log.i("MenuAndDatabase","modifer Calculator complete. Return Value: " + modCalc);
        return modCalc;
    }

    //returns the class level as a string
    static String classLevelString(Cursor dataPointer){
        String classString;
        String charLevelBarbarian = "";
        String charLevelBard = "";
        String charLevelCleric = "";
        String charLevelDruid = "";
        String charLevelFighter = "";
        String charLevelMonk = "";
        String charLevelPaladin = "";
        String charLevelRanger = "";
        String charLevelRogue = "";
        String charLevelSorcerer = "";
        String charLevelWarlock = "";
        String charLevelWizard = "";
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN)).length()!=0) {
            charLevelBarbarian = "Barbarian:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD)).length()!=0) {
            charLevelBard = " Bard:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC)).length()!=0) {
            charLevelCleric = " Cleric:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID)).length()!=0) {
            charLevelDruid = " Druid:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER)).length()!=0) {
            charLevelFighter = " Fighter:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_MONK)).length()!=0) {
            charLevelMonk = " Monk:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_MONK));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN)).length()!=0) {
            charLevelPaladin = " Paladin:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER)).length()!=0) {
            charLevelRanger = " Ranger:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE)).length()!=0) {
            charLevelRogue = " Rogue:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER)).length()!=0) {
            charLevelSorcerer = " Sorcerer:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK)).length()!=0) {
            charLevelWarlock = " Warlock:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK));
        }
        if(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD)).length()!=0) {
            charLevelWizard = " Wizard:" + dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD));
        }
        classString = charLevelBarbarian + charLevelBard + charLevelCleric + charLevelDruid + charLevelFighter + charLevelMonk + charLevelPaladin + charLevelRanger + charLevelRogue + charLevelSorcerer + charLevelWarlock + charLevelWizard;
        Log.i("MenuAndDatabase","classLevelString complete. Return Value: " + classString);
        return classString;
    }

    static int classLevelCalc(Cursor dataPointer){
        int levelValue;
        int charLevelBarbarian = 0;
        int charLevelBard = 0;
        int charLevelCleric = 0;
        int charLevelDruid = 0;
        int charLevelFighter = 0;
        int charLevelMonk = 0;
        int charLevelPaladin = 0;
        int charLevelRanger = 0;
        int charLevelRogue = 0;
        int charLevelSorcerer = 0;
        int charLevelWarlock = 0;
        int charLevelWizard = 0;
        if(!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN)).equals("")){
            charLevelBarbarian = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN)));
        }
        if(!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD)).equals("")){
            charLevelBard = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_BARD)));
        }
        if(!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC)).equals("")){
            charLevelCleric = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_CLERIC)));
        }
        if(!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID)).equals("")){
            charLevelDruid = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_DRUID)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER)).equals("")){
            charLevelFighter = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_FIGHTER)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_MONK)).equals("")){
            charLevelMonk = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_MONK)));
        }
        if(!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN)).equals("")){
            charLevelPaladin = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_PALADIN)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER)).equals("")){
            charLevelRanger = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_RANGER)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE)).equals("")){
            charLevelRogue = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_ROGUE)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER)).equals("")){
            charLevelSorcerer = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_SORCERER)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK)).equals("")){
            charLevelWarlock = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WARLOCK)));
        }
        if (!dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD)).equals("")){
            charLevelWizard = Integer.parseInt(dataPointer.getString(dataPointer.getColumnIndex(SQLHelper.CHARACTER_WIZARD)));
        }
        levelValue = charLevelBarbarian + charLevelBard + charLevelCleric + charLevelDruid + charLevelFighter + charLevelMonk + charLevelPaladin + charLevelRanger + charLevelRogue + charLevelSorcerer + charLevelWarlock + charLevelWizard;
        Log.i("MenuAndDatabase","classLevelInt complete. Return Value: " + levelValue);
        return levelValue;
    }

    //rolls a dice and pops up the result.
    static void diceRollPopup(Context context, int diceValue, int modifier, String message){
        try {
            Random randomNumberDiceBox = new Random();
            int randomNumberValue = randomNumberDiceBox.nextInt(diceValue) + 1;
            String longMessage;
            AlertDialog.Builder diceDialogBox = new AlertDialog.Builder(context);

            //creates a textview for the dice message
            TextView diceMessage = new TextView(context);
            diceMessage.setGravity(Gravity.CENTER_HORIZONTAL);
            diceMessage.setTextColor(Color.WHITE);
            diceMessage.setTextSize(20);

            //checks for whether it was a nat20 and adds text
            if (randomNumberValue == diceValue) {
                diceDialogBox.setTitle("Dice Roll (d" + diceValue + ") CRITICAL ROLL!");
            }
            else{
                diceDialogBox.setTitle("Dice Roll (d" + diceValue + ")");
            }
            randomNumberValue = randomNumberValue + modifier;
            //adds the message to the dialog
            if (message != null) {
                longMessage = "You have rolled " + Integer.toString(randomNumberValue) + " " + message;
            } else {
                longMessage = "You have rolled " + Integer.toString(randomNumberValue);
            }
            diceMessage.setText(longMessage);
            diceDialogBox.setView(diceMessage);
            //adds an ok button to the dialog box
            diceDialogBox.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //Activates the dialog box
            AlertDialog diceDialog = diceDialogBox.create();
            diceDialog.show();
            Log.i("MenuAndDatabase","dice roll popup. dice value: " + randomNumberValue);
        }
        catch(Exception e){
            MenuAndDatabase.errorNotification(context,e.toString());
            Log.e("MenuAndDatabase","error rolling dice: " + e.toString());
        }
    }

    //calculates the proficiency based on level and returns as an integer
    static int proficiencyCalc(Cursor dataPointer){
        int currentLevel = classLevelCalc(dataPointer);
        int proficiency = 0;
        if ((currentLevel>0)&&(currentLevel<5)){
            proficiency = 2;
        }
        else if ((currentLevel>4)&&(currentLevel<9)) {
            proficiency = 3;
        }
        else if ((currentLevel>8)&&(currentLevel<13)){
            proficiency = 4;
        }
        else if ((currentLevel>12)&&(currentLevel<17)){
            proficiency = 5;
        }
        else if ((currentLevel>16)&&(currentLevel<21)){
            proficiency = 6;
        }
        Log.i("MenuAndDatabase","proficiencyCalc complete. Return Value: " + proficiency);
        return proficiency;
    }

    //basic message popup
    static void messagePopup(Context context, String message, String title){
        AlertDialog.Builder errorDialogBox= new AlertDialog.Builder(context);
        errorDialogBox.setTitle(title);
        errorDialogBox.setMessage(message);
        errorDialogBox.setNeutralButton(android.R.string.ok,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Activates the dialog box
        AlertDialog errorDialog = errorDialogBox.create();
        errorDialog.show();
        Log.i("MenuAndDatabase","basic message popup. Message: " + message);
    }

    public GestureDetector detector;

    //The following methods are used to control screen gestures
    @Override
    public boolean onTouchEvent (MotionEvent me){
        detector.onTouchEvent(me);
        return super.onTouchEvent(me);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    //The following methods are used to control screen gestures
    @Override
    public void onLongPress(MotionEvent e) {
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return detector.onTouchEvent(ev);
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    //points to and sets the text in a text view. returns false if completed
    public boolean setTextView(int textViewID, String text){
        try {
            TextView textview = (TextView) findViewById(textViewID);
            textview.setText(text);
            Log.i("MenuAndDatabase","setTextView: " + textViewID + ", message: " + text);
            return false;
        }
        catch(Exception e){
            Log.e("MenuAndDatabase", "error setting textview: " + e.toString());
            return true;
        }
    }
    //points to and sets the text in an edit view. returns false if completed
    public boolean setEditView(int editViewID, String text){
        try {
            EditText editview = (EditText) findViewById(editViewID);
            editview.setText(text);
            Log.i("MenuAndDatabase","setEditText: " + editViewID + ", message: " + text);
            return false;
        }
        catch(Exception e){
            Log.e("MenuAndDatabase", "error setting editText: " + e.toString());
            return true;
        }
    }

    //changes the hp both up and down
    public void hpChange(Context context,int textview, boolean increase, String characterName){
        try {
            Cursor hpCursor = SQLHelper.setupDatabase(context, SQLHelper.TABLE_NAME, null, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            SQLHelper hpSqlhelper = new SQLHelper(context);
            SQLiteDatabase hpDB = hpSqlhelper.getWritableDatabase();
            TextView hpOutput = (TextView) findViewById(textview);
            int currentHP = Integer.parseInt(hpCursor.getString(hpCursor.getColumnIndex(SQLHelper.CHARACTER_HP)));
            int maxHP = Integer.parseInt(hpCursor.getString(hpCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP)));
            ContentValues cv = new ContentValues();
            if (increase) {
                currentHP++;
            } else if (!increase) {
                currentHP--;
            }
            cv.put(SQLHelper.CHARACTER_HP, currentHP);
            hpDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
            hpOutput.setText(currentHP + "/" + maxHP);
            Log.i("MenuAndDatabase", "HP has been modified new HP: " + currentHP);
        }
        catch (Exception e){
            Log.e("MenuAndDatabase", "error setting HP: " + e.toString());
        }
    }

    static String d6Calculation(Context context, String characterName){
        String returnValue = null;
        Cursor cursor = SQLHelper.setupDatabase(context,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        int diceCalc = cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_SORCERER))+cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_WIZARD));
        returnValue = String.valueOf(diceCalc);
        return returnValue;
    }
    static String d8Calculation(Context context, String characterName){
        String returnValue = null;
        Cursor cursor = SQLHelper.setupDatabase(context,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        int diceCalc = cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_BARD))+cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_CLERIC))+
                cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_DRUID))+cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_MONK))+
                cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_ROGUE))+cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_WARLOCK));
        returnValue = String.valueOf(diceCalc);
        return returnValue;
    }
    static String d10Calculation(Context context, String characterName){
        String returnValue = null;
        Cursor cursor = SQLHelper.setupDatabase(context,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        int diceCalc = cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_FIGHTER))+cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_PALADIN))+
                cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_RANGER));
        returnValue = String.valueOf(diceCalc);
        return returnValue;
    }
    static String d12Calculation(Context context, String characterName){
        String returnValue = null;
        Cursor cursor = SQLHelper.setupDatabase(context,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?", new String[]{characterName});
        int diceCalc = cursor.getInt(cursor.getColumnIndex(SQLHelper.CHARACTER_BARBARIAN));
        returnValue = String.valueOf(diceCalc);
        return returnValue;
    }

}
