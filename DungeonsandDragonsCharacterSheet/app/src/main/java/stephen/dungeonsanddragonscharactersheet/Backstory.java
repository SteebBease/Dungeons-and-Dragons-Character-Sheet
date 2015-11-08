package stephen.dungeonsanddragonscharactersheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Stephen.Beasley on 27/02/15.
 */
public class Backstory extends MenuAndDatabase {

    String characterName;
    Cursor dataCursor;

    int imageWidth;
    int imageHeight;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_story);

        try {
            Cursor settingCursor = SQLHelper.setupDatabase(this, SQLHelper.SETTING_TABLE_NAME, null, null,null);
            settingCursor.moveToFirst();
            characterName = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_NAME));
            characterID = settingCursor.getString(settingCursor.getColumnIndex(SQLHelper.SETTING_CURRENT_CHARACTER_ID));
        }
        catch(Exception e){
            Log.e("BasicStats","Error getting settings data");
        }

        detector = new GestureDetector(this, this);

        imageWidth = 400;
        imageHeight = 600;

        dataSqlhelper = new SQLHelper(this);
        dataDB = dataSqlhelper.getWritableDatabase();
        dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
        Log.i("Backstory", "Database cursor created. Number of items: " + dataCursor.getCount());

        if((setTextView(R.id.backstory_race,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_RACE))))||
            (setTextView(R.id.backstory_alignment,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ALIGNMENT))))||
            (setTextView(R.id.backstory_background,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_BACKGROUND))))||
            (setTextView(R.id.backstory_eye_color,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_EYE_COLOR))))||
            (setTextView(R.id.backstory_skin_color,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_SKIN_COLOR))))||
            (setTextView(R.id.backstory_hair_color,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HAIR_COLOR))))||
            (setTextView(R.id.backstory_age,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_AGE))))||
            (setTextView(R.id.backstory_height,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HEIGHT))))||
            (setTextView(R.id.backstory_weight,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_WEIGHT))))||
            (setTextView(R.id.backstory_allies_and_organisations,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ALLIES_AND_ORGANISATIONS))))||
            (setTextView(R.id.backstory_backstory,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_BACKSTORY))))||
            (setTextView(R.id.backstory_personality_traits,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_PERSONALITY_TRAITS))))||
            (setTextView(R.id.backstory_ideals,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_IDEALS))))||
            (setTextView(R.id.backstory_bonds,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_BONDS))))||
            (setTextView(R.id.backstory_flaws,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_FLAWS))))){
            Log.e("Backstory","Error setting up backstory data");
        }

        if((setTextView(R.id.character_name,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_NAME))))||
        (setTextView(R.id.character_hp,dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_HP))+"/"+dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_MAX_HP))))){
            Log.e("Backstory","Error setting up character data in backstory");
        }


        int rotationValue;
        try{
            rotationValue = Integer.parseInt(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ROTATION)));
        }
        catch(Exception e){
            rotationValue = 90;
        }
        ContentValues cv = new ContentValues();
        cv.put(SQLHelper.CHARACTER_ROTATION,rotationValue);
        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
        Matrix matrix = new Matrix();
        ImageView imageView = (ImageView) findViewById(R.id.backstory_image);
        matrix.postRotate(rotationValue);
        try {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_IMAGE))), imageWidth, imageHeight, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotatedBitmap);
        }
        catch(Exception e){
            Log.e("Backstory", e.toString());
        }
        dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
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
            Intent intentBasicStats = new Intent(this, BasicStats.class);
            intentBasicStats.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentBasicStats);
            this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else if((e1.getX() < e2.getX()) && (e2.getX() - e1.getX() > 300)) {
            Intent intentInventory = new Intent(this, Inventory.class);
            intentInventory.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intentInventory);
            this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        return false;
    }

    public void imageRotation(View v){
        int rotationValue = 0;
        dataCursor = SQLHelper.setupDatabase(this,SQLHelper.TABLE_NAME,null,SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
        try {
            rotationValue = dataCursor.getInt(dataCursor.getColumnIndex(SQLHelper.CHARACTER_ROTATION));
        } catch(Exception e){}
        Matrix matrix = new Matrix();
        ImageView imageView = (ImageView) findViewById(R.id.backstory_image);
        rotationValue = rotationValue + 90;
        if (rotationValue >= 360){
            rotationValue = 0;
        }
        ContentValues cv = new ContentValues();
        cv.put(SQLHelper.CHARACTER_ROTATION,rotationValue);
        dataDB.update(SQLHelper.TABLE_NAME, cv, SQLHelper.CHARACTER_NAME + "=?",new String[] {characterName});
        matrix.postRotate(rotationValue);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(dataCursor.getString(dataCursor.getColumnIndex(SQLHelper.CHARACTER_IMAGE))),imageWidth,imageHeight,true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(),scaledBitmap.getHeight(), matrix,true);
        imageView.setImageBitmap(rotatedBitmap);
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
                    d6Count = Integer.parseInt(MenuAndDatabase.d6Calculation(Backstory.this,characterName));
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
                    d8Count = Integer.parseInt(MenuAndDatabase.d8Calculation(Backstory.this,characterName));
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
                    d10Count = Integer.parseInt(MenuAndDatabase.d10Calculation(Backstory.this,characterName));
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
                    d12Count = Integer.parseInt(MenuAndDatabase.d12Calculation(Backstory.this,characterName));
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
