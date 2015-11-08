package stephen.dungeonsanddragonscharactersheet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "data";
    public static final String TABLE_NAME = "characters_table";
    public static final String C_ID = "_id";
    public static final String CHARACTER_NAME = "character_name"; //1
    public static final String CHARACTER_HP = "character_hp"; //2
    public static final String CHARACTER_SPEED = "character_speed"; //3
    public static final String CHARACTER_STR = "character_str"; //4
    public static final String CHARACTER_DEX = "character_dex"; //5
    public static final String CHARACTER_CON = "character_con"; //6
    public static final String CHARACTER_INT = "character_int"; //7
    public static final String CHARACTER_WIS = "character_wis"; //8
    public static final String CHARACTER_CHA = "character_cha"; //9
    public static final String CHARACTER_BARBARIAN = "character_barbarian"; //10
    public static final String CHARACTER_BARD = "character_bard"; //11
    public static final String CHARACTER_CLERIC = "character_cleric"; //12
    public static final String CHARACTER_DRUID = "character_druid"; //13
    public static final String CHARACTER_FIGHTER = "character_fighter"; //14
    public static final String CHARACTER_MONK = "character_monk"; //15
    public static final String CHARACTER_PALADIN = "character_paladin"; //16
    public static final String CHARACTER_RANGER = "character_ranger"; //17
    public static final String CHARACTER_ROGUE = "character_rogue"; //18
    public static final String CHARACTER_SORCERER = "character_sorcerer"; //19
    public static final String CHARACTER_WARLOCK = "character_warlock"; //20
    public static final String CHARACTER_WIZARD = "character_wizard"; //21
    public static final String CHARACTER_SAVE_PROF_STR = "character_save_proficiency_str"; //22
    public static final String CHARACTER_SAVE_PROF_DEX = "character_save_proficiency_dex"; //23
    public static final String CHARACTER_SAVE_PROF_CON = "character_save_proficiency_con"; //24
    public static final String CHARACTER_SAVE_PROF_INT = "character_save_proficiency_int"; //25
    public static final String CHARACTER_SAVE_PROF_WIS = "character_save_proficiency_wis"; //26
    public static final String CHARACTER_SAVE_PROF_CHA = "character_save_proficiency_cha"; //27
    public static final String CHARACTER_SKILL_ACROBATICS = "character_skill_acrobatics"; //28
    public static final String CHARACTER_SKILL_ANIMAL_HANDLING = "character_skill_animal_handling"; //29
    public static final String CHARACTER_SKILL_ARCANA = "character_skill_arcana"; //30
    public static final String CHARACTER_SKILL_ATHLETICS = "character_skill_athletics"; //31
    public static final String CHARACTER_SKILL_DECEPTION = "character_skill_deception"; //32
    public static final String CHARACTER_SKILL_HISTORY = "character_skill_history"; //33
    public static final String CHARACTER_SKILL_INSIGHT = "character_skill_insight"; //34
    public static final String CHARACTER_SKILL_INTIMIDATION = "character_skill_intimidation"; //35
    public static final String CHARACTER_SKILL_INVESTIGATION = "character_skill_investigation"; //36
    public static final String CHARACTER_SKILL_MEDICINE = "character_skill_medicine"; //37
    public static final String CHARACTER_SKILL_NATURE = "character_skill_nature"; //38
    public static final String CHARACTER_SKILL_PERCEPTION = "character_skill_perception"; //39
    public static final String CHARACTER_SKILL_PERFORMANCE = "character_skill_performance"; //39
    public static final String CHARACTER_SKILL_PERSUASION = "character_skill_persuasion"; //40
    public static final String CHARACTER_SKILL_RELIGION = "character_skill_religion"; //41
    public static final String CHARACTER_SKILL_SLIGHT_OF_HAND = "character_skill_slight_of_hand"; //42
    public static final String CHARACTER_SKILL_STEALTH = "character_skill_stealth"; //43
    public static final String CHARACTER_SKILL_SURVIVAL = "character_skill_survival"; //44
    public static final String CHARACTER_WEAPON_PROFICIENCIES = "character_weapon_proficiencies"; //44
    public static final String CHARACTER_ARMOR_PROFICIENCIES = "character_armor_proficiencies"; //45
    public static final String CHARACTER_TOOL_PROFICIENCIES = "character_tool_proficiencies"; //46
    public static final String CHARACTER_LANGUAGE_PROFICIENCIES = "character_language_proficiencies"; //47
    public static final String CHARACTER_ALIGNMENT = "character_alignment"; //48
    public static final String CHARACTER_EYE_COLOR = "character_eye_color"; //49
    public static final String CHARACTER_SKIN_COLOR = "character_skin_color"; //50
    public static final String CHARACTER_HAIR_COLOR = "character_hair_color"; //51
    public static final String CHARACTER_AGE = "character_age"; //52
    public static final String CHARACTER_HEIGHT = "character_height"; //53
    public static final String CHARACTER_WEIGHT = "character_weight"; //54
    public static final String CHARACTER_BACKGROUND = "character_background"; //55
    public static final String CHARACTER_BACKSTORY = "character_backstory"; //56
    public static final String CHARACTER_ALLIES_AND_ORGANISATIONS = "character_allies_and_organisations"; //57
    public static final String CHARACTER_GENERAL_INVENTORY = "character_general_inventory"; //58   NOT USED
    public static final String CHARACTER_RACE = "character_race"; //59
    public static final String CHARACTER_MAX_HP = "character_max_hp";
    public static final String CHARACTER_INITIATIVE = "character_initiative";
    public static final String CHARACTER_PP = "character_pp";//
    public static final String CHARACTER_GP = "character_gp";//
    public static final String CHARACTER_EP = "character_ep";//
    public static final String CHARACTER_SP = "character_sp";//
    public static final String CHARACTER_CP = "character_cp";//
    public static final String CHARACTER_PERSONALITY_TRAITS = "character_personality_traits";//
    public static final String CHARACTER_IDEALS = "character_ideals";//
    public static final String CHARACTER_BONDS = "character_bonds";//
    public static final String CHARACTER_FLAWS = "character_flaws";//
    public static final String CHARACTER_NOTES = "character_notes";//
    public static final String CHARACTER_L1_SPELL_MIN = "character_l1_spell_min";
    public static final String CHARACTER_L2_SPELL_MIN = "character_l2_spell_min";
    public static final String CHARACTER_L3_SPELL_MIN = "character_l3_spell_min";
    public static final String CHARACTER_L4_SPELL_MIN = "character_l4_spell_min";
    public static final String CHARACTER_L5_SPELL_MIN = "character_l5_spell_min";
    public static final String CHARACTER_L6_SPELL_MIN = "character_l6_spell_min";
    public static final String CHARACTER_L7_SPELL_MIN = "character_l7_spell_min";
    public static final String CHARACTER_L8_SPELL_MIN = "character_l8_spell_min";
    public static final String CHARACTER_L9_SPELL_MIN = "character_l9_spell_min";
    public static final String CHARACTER_L1_SPELL_MAX = "character_l1_spell_max";
    public static final String CHARACTER_L2_SPELL_MAX = "character_l2_spell_max";
    public static final String CHARACTER_L3_SPELL_MAX = "character_l3_spell_max";
    public static final String CHARACTER_L4_SPELL_MAX = "character_l4_spell_max";
    public static final String CHARACTER_L5_SPELL_MAX = "character_l5_spell_max";
    public static final String CHARACTER_L6_SPELL_MAX = "character_l6_spell_max";
    public static final String CHARACTER_L7_SPELL_MAX = "character_l7_spell_max";
    public static final String CHARACTER_L8_SPELL_MAX = "character_l8_spell_max";
    public static final String CHARACTER_L9_SPELL_MAX = "character_l9_spell_max";
    public static final String CHARACTER_IMAGE = "character_image";
    public static final String CHARACTER_ROTATION = "character_rotation";
    public static final String CHARACTER_STRENGTH_VISIBLE = "character_strength_visible";
    public static final String CHARACTER_DEXTERITY_VISIBLE = "character_dexterity_visible";
    public static final String CHARACTER_CONSTITUTION_VISIBLE = "character_constitution_visible";
    public static final String CHARACTER_INTELLIGENCE_VISIBLE = "character_intelligence_visible";
    public static final String CHARACTER_WISDOM_VISIBLE = "character_wisdom_visible";
    public static final String CHARACTER_CHARISMA_VISIBLE = "character_charisma_visible";
    public static final String CHARACTER_ABILITIES_VISIBLE = "character_abilities_visible";
    public static final String CHARACTER_CANTRIP_VISIBLE = "character_cantrip_visible";
    public static final String CHARACTER_SPELL_LEVEL_1 = "character_spell_level_1";
    public static final String CHARACTER_SPELL_LEVEL_2 = "character_spell_level_2";
    public static final String CHARACTER_SPELL_LEVEL_3 = "character_spell_level_3";
    public static final String CHARACTER_SPELL_LEVEL_4 = "character_spell_level_4";
    public static final String CHARACTER_SPELL_LEVEL_5 = "character_spell_level_5";
    public static final String CHARACTER_SPELL_LEVEL_6 = "character_spell_level_6";
    public static final String CHARACTER_SPELL_LEVEL_7 = "character_spell_level_7";
    public static final String CHARACTER_SPELL_LEVEL_8 = "character_spell_level_8";
    public static final String CHARACTER_SPELL_LEVEL_9 = "character_spell_level_9";
    public static final String CHARACTER_SPELL_CASTING = "character_spell_casting";
    public static final String CHARACTER_PROFICIENCIES_VISIBLE = "character_proficiencies";
    public static final String CHARACTER_WEAPONS_VISIBLE = "character_weapons_visible";
    public static final String CHARACTER_ARMOR_VISIBLE = "character_armor_visible";
    public static final String CHARACTER_INVENTORY = "character_inventory_visible";
    public static final String CHARACTER_TEMPORARY_HP = "character_temporary_hp";
    public static final String CHARACTER_HIT_DICE_D6 = "character_hit_dice_d6";
    public static final String CHARACTER_HIT_DICE_D8 = "character_hit_dice_d8";
    public static final String CHARACTER_HIT_DICE_D10 = "character_hit_dice_d10";
    public static final String CHARACTER_HIT_DICE_D12 = "character_hit_dice_d12";
    public static final String CHARACTER_DEATH_SUCCESS_1 = "character_death_success_1";
    public static final String CHARACTER_DEATH_SUCCESS_2 = "character_death_success_2";
    public static final String CHARACTER_DEATH_SUCCESS_3 = "character_death_success_3";
    public static final String CHARACTER_DEATH_FAIL_1 = "character_death_fail_1";
    public static final String CHARACTER_DEATH_FAIL_2 = "character_death_fail_2";
    public static final String CHARACTER_DEATH_FAIL_3 = "character_death_fail_3";
    public static final String CHARACTER_OTHER_1 = "character_other_1";
    public static final String CHARACTER_OTHER_2 = "character_other_2";
    public static final String CHARACTER_OTHER_3 = "character_other_3";
    public static final String CHARACTER_OTHER_4 = "character_other_4";
    public static final String CHARACTER_OTHER_5 = "character_other_5";
    public static final String CHARACTER_OTHER_6 = "character_other_6";
    public static final String CHARACTER_OTHER_7 = "character_other_7";
    public static final String CHARACTER_OTHER_8 = "character_other_8";
    public static final String CHARACTER_OTHER_9 = "character_other_9";
    public static final String CHARACTER_OTHER_10 = "character_other_10";
    public static final int VERSION = 19;

    final String createDB = "create table if not exists " + TABLE_NAME + " ( "
            + C_ID + " integer primary key autoincrement, "
            + CHARACTER_NAME + " text, "
            + CHARACTER_AGE + " text, "
            + CHARACTER_ALLIES_AND_ORGANISATIONS + " text, "
            + CHARACTER_ARMOR_PROFICIENCIES + " text, "
            + CHARACTER_BACKGROUND + " text, "
            + CHARACTER_BACKSTORY + " text, "
            + CHARACTER_BARBARIAN + " text, "
            + CHARACTER_BARD + " text, "
            + CHARACTER_CHA + " text, "
            + CHARACTER_CLERIC + " text, "
            + CHARACTER_CON + " text, "
            + CHARACTER_DEX + " text, "
            + CHARACTER_DRUID + " text, "
            + CHARACTER_EYE_COLOR + " text, "
            + CHARACTER_FIGHTER + " text, "
            + CHARACTER_GENERAL_INVENTORY + " text, " //not currently used
            + CHARACTER_HAIR_COLOR + " text, "
            + CHARACTER_HEIGHT + " text, "
            + CHARACTER_HP + " text, "
            + CHARACTER_INT + " text, "
            + CHARACTER_LANGUAGE_PROFICIENCIES + " text, "
            + CHARACTER_MONK + " text, "
            + CHARACTER_PALADIN + " text, "
            + CHARACTER_RANGER + " text, "
            + CHARACTER_ROGUE + " text, "
            + CHARACTER_SAVE_PROF_CHA + " text, "
            + CHARACTER_SAVE_PROF_CON + " text, "
            + CHARACTER_SAVE_PROF_DEX + " text, "
            + CHARACTER_SAVE_PROF_INT + " text, "
            + CHARACTER_SAVE_PROF_STR + " text, "
            + CHARACTER_SAVE_PROF_WIS + " text, "
            + CHARACTER_SKILL_ACROBATICS + " text, "
            + CHARACTER_SKILL_ANIMAL_HANDLING + " text, "
            + CHARACTER_SKILL_ARCANA + " text, "
            + CHARACTER_SKILL_ATHLETICS + " text, "
            + CHARACTER_SKILL_DECEPTION + " text, "
            + CHARACTER_SKILL_HISTORY + " text, "
            + CHARACTER_SKILL_INSIGHT + " text, "
            + CHARACTER_SKILL_INTIMIDATION + " text, "
            + CHARACTER_SKILL_INVESTIGATION + " text, "
            + CHARACTER_SKILL_MEDICINE + " text, "
            + CHARACTER_SKILL_NATURE + " text, "
            + CHARACTER_SKILL_PERCEPTION + " text, "
            + CHARACTER_SKILL_PERFORMANCE + " text, "
            + CHARACTER_SKILL_PERSUASION + " text, "
            + CHARACTER_SKILL_RELIGION + " text, "
            + CHARACTER_SKILL_SLIGHT_OF_HAND + " text, "
            + CHARACTER_SKILL_STEALTH + " text, "
            + CHARACTER_SKILL_SURVIVAL + " text, "
            + CHARACTER_SKIN_COLOR + " text, "
            + CHARACTER_SORCERER + " text, "
            + CHARACTER_SPEED + " text, "
            + CHARACTER_STR + " text, "
            + CHARACTER_TOOL_PROFICIENCIES + " text, "
            + CHARACTER_WARLOCK + " text, "
            + CHARACTER_ALIGNMENT + " text, "
            + CHARACTER_WEAPON_PROFICIENCIES + " text, "
            + CHARACTER_WEIGHT + " text, "
            + CHARACTER_WIS + " text, "
            + CHARACTER_WIZARD + " text, "
            + CHARACTER_RACE + " text, "
            + CHARACTER_MAX_HP + " text, "
            + CHARACTER_INITIATIVE + " text, "
            + CHARACTER_PP + " text, "
            + CHARACTER_GP + " text, "
            + CHARACTER_EP + " text, "
            + CHARACTER_SP + " text, "
            + CHARACTER_CP + " text, "
            + CHARACTER_PERSONALITY_TRAITS + " text, "
            + CHARACTER_IDEALS + " text, "
            + CHARACTER_BONDS + " text, "
            + CHARACTER_FLAWS + " text, "
            + CHARACTER_NOTES + " text, "
            + CHARACTER_L1_SPELL_MIN + " text, "
            + CHARACTER_L2_SPELL_MIN + " text, "
            + CHARACTER_L3_SPELL_MIN + " text, "
            + CHARACTER_L4_SPELL_MIN + " text, "
            + CHARACTER_L5_SPELL_MIN + " text, "
            + CHARACTER_L6_SPELL_MIN + " text, "
            + CHARACTER_L7_SPELL_MIN + " text, "
            + CHARACTER_L8_SPELL_MIN + " text, "
            + CHARACTER_L9_SPELL_MIN + " text, "
            + CHARACTER_L1_SPELL_MAX + " text, "
            + CHARACTER_L2_SPELL_MAX + " text, "
            + CHARACTER_L3_SPELL_MAX + " text, "
            + CHARACTER_L4_SPELL_MAX + " text, "
            + CHARACTER_L5_SPELL_MAX + " text, "
            + CHARACTER_L6_SPELL_MAX + " text, "
            + CHARACTER_L7_SPELL_MAX + " text, "
            + CHARACTER_L8_SPELL_MAX + " text, "
            + CHARACTER_L9_SPELL_MAX + " text, "
            + CHARACTER_IMAGE + " text, "
            + CHARACTER_ROTATION + " text, "
            + CHARACTER_STRENGTH_VISIBLE + " text, "
            + CHARACTER_DEXTERITY_VISIBLE + " text, "
            + CHARACTER_CONSTITUTION_VISIBLE + " text, "
            + CHARACTER_INTELLIGENCE_VISIBLE + " text, "
            + CHARACTER_WISDOM_VISIBLE + " text, "
            + CHARACTER_CHARISMA_VISIBLE + " text, "
            + CHARACTER_ABILITIES_VISIBLE + " text, "
            + CHARACTER_CANTRIP_VISIBLE + " text, "
            + CHARACTER_SPELL_LEVEL_1 + " text, "
            + CHARACTER_SPELL_LEVEL_2 + " text, "
            + CHARACTER_SPELL_LEVEL_3 + " text, "
            + CHARACTER_SPELL_LEVEL_4 + " text, "
            + CHARACTER_SPELL_LEVEL_5 + " text, "
            + CHARACTER_SPELL_LEVEL_6 + " text, "
            + CHARACTER_SPELL_LEVEL_7 + " text, "
            + CHARACTER_SPELL_LEVEL_8 + " text, "
            + CHARACTER_SPELL_LEVEL_9 + " text, "
            + CHARACTER_SPELL_CASTING + " text, "
            + CHARACTER_PROFICIENCIES_VISIBLE + " text, "
            + CHARACTER_WEAPONS_VISIBLE + " text, "
            + CHARACTER_ARMOR_VISIBLE + " text, "
            + CHARACTER_INVENTORY + " text, "
            + CHARACTER_EXPERIENCE + " text, "
            + CHARACTER_TEMPORARY_HP + " text, "
            + CHARACTER_HIT_DICE_D6 + " text, "
            + CHARACTER_HIT_DICE_D8 + " text, "
            + CHARACTER_HIT_DICE_D10 + " text, "
            + CHARACTER_HIT_DICE_D12 + " text, "
            + CHARACTER_DEATH_SUCCESS_1 + " text, "
            + CHARACTER_DEATH_SUCCESS_2 + " text, "
            + CHARACTER_DEATH_SUCCESS_3 + " text, "
            + CHARACTER_DEATH_FAIL_1 + " text, "
            + CHARACTER_DEATH_FAIL_2 + " text, "
            + CHARACTER_DEATH_FAIL_3 + " text, "
            + CHARACTER_OTHER_1 + " text, "
            + CHARACTER_OTHER_2 + " text, "
            + CHARACTER_OTHER_3 + " text, "
            + CHARACTER_OTHER_4 + " text, "
            + CHARACTER_OTHER_5 + " text, "
            + CHARACTER_OTHER_6 + " text, "
            + CHARACTER_OTHER_7 + " text, "
            + CHARACTER_OTHER_8 + " text, "
            + CHARACTER_OTHER_9 + " text, "
            + CHARACTER_OTHER_10 + " text "
            + ")";

    public static final String ITEM_TABLE_NAME = "held_equipment";
    public static final String ITEM_C_ID = "_id";
    public static final String ITEM_CHARACTER_ID_LINK = "item_character_id_link";
    public static final String CHARACTER_NAME_LINK = "character_name_link";
    public static final String ITEM_CATEGORY = "item_category";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_DESCRIPTION = "item_description";
    public static final String ITEM_WEAPON_RANGE = "item_weapon_range";
    public static final String ITEM_WEAPON_DAMAGE_TYPE = "item_weapon_damage_type";
    public static final String ITEM_WEAPON_DAMAGE_ROLL = "item_weapon_damage_roll";
    public static final String ITEM_ARMOR_AC = "item_armor_ac";
    public static final String ITEM_WEIGHT = "item_weight";
    public static final String ITEM_COST = "item_cost";
    public static final String ITEM_QUANTITY = "item_quantity"; //not currently used
    public static final String ITEM_ATTRIBUTE = "_item_attribute";
    public static final String ITEM_PROFICIENT = "item_proficient";
    public static final String ITEM_OTHER_1 = "item_other_1";
    public static final String ITEM_OTHER_2 = "item_other_2";
    public static final String ITEM_OTHER_3 = "item_other_3";
    public static final String ITEM_OTHER_4 = "item_other_4";
    public static final String ITEM_OTHER_5 = "item_other_5";
    public static final String ITEM_OTHER_6 = "item_other_6";
    public static final String ITEM_OTHER_7 = "item_other_7";
    public static final String ITEM_OTHER_8 = "item_other_8";
    public static final String ITEM_OTHER_9 = "item_other_9";
    public static final String ITEM_OTHER_10 = "item_other_10";

    final String createDBWeapons = "create table if not exists " + ITEM_TABLE_NAME + " ( "
            + ITEM_C_ID + " integer primary key autoincrement, "
            + ITEM_CHARACTER_ID_LINK + " text, "
            + CHARACTER_NAME_LINK + " text, "
            + ITEM_CATEGORY + " text, "
            + ITEM_NAME + " text, "
            + ITEM_DESCRIPTION + " text, "
            + ITEM_WEAPON_RANGE + " text, "
            + ITEM_WEAPON_DAMAGE_TYPE + " text, "
            + ITEM_WEAPON_DAMAGE_ROLL + " text, "
            + ITEM_ARMOR_AC + " text, "
            + ITEM_WEIGHT + " text, "
            + ITEM_COST + " text, "
            + ITEM_QUANTITY + " text, "
            + ITEM_ATTRIBUTE + " text, "
            + ITEM_PROFICIENT + " text, "
            + ITEM_OTHER_1 + " text, "
            + ITEM_OTHER_2 + " text, "
            + ITEM_OTHER_3 + " text, "
            + ITEM_OTHER_4 + " text, "
            + ITEM_OTHER_5 + " text, "
            + ITEM_OTHER_6 + " text, "
            + ITEM_OTHER_7 + " text, "
            + ITEM_OTHER_8 + " text, "
            + ITEM_OTHER_9 + " text, "
            + ITEM_OTHER_10 + " text "
            + ")";

    public static final String SPELL_TABLE_NAME = "spell_list";
    public static final String SPELL_C_ID = "_id";
    public static final String SPELL_CHARACTER_ID_LINK = "spell_character_name_link";
    public static final String SPELL_CHARACTER_NAME_LINK = "character_name_link";
    public static final String SPELL_LEVEL = "spell_level";
    public static final String SPELL_NAME = "spell_name";
    public static final String SPELL_SCHOOL = "spell_school";
    public static final String SPELL_CASTING_TIME = "spell_casting_time";
    public static final String SPELL_RANGE = "spell_range";
    public static final String SPELL_COMPONENTS = "spell_components";
    public static final String SPELL_DURATION = "spell_duration";
    public static final String SPELL_PREPARED = "spell_prepared";
    public static final String SPELL_DESCRIPTION = "spell_description";
    public static final String SPELL_OTHER_1 = "spell_other_1";
    public static final String SPELL_OTHER_2 = "spell_other_2";
    public static final String SPELL_OTHER_3 = "spell_other_3";
    public static final String SPELL_OTHER_4 = "spell_other_4";
    public static final String SPELL_OTHER_5 = "spell_other_5";
    public static final String SPELL_OTHER_6 = "spell_other_6";
    public static final String SPELL_OTHER_7 = "spell_other_7";
    public static final String SPELL_OTHER_8 = "spell_other_8";
    public static final String SPELL_OTHER_9 = "spell_other_9";
    public static final String SPELL_OTHER_10 = "spell_other_10";

    final String createdDBSpells = "create table if not exists " + SPELL_TABLE_NAME + " ( "
            + SPELL_C_ID + " integer primary key autoincrement, "
            + SPELL_CHARACTER_ID_LINK + " text, "
            + SPELL_CHARACTER_NAME_LINK + " text, "
            + SPELL_LEVEL + " text, "
            + SPELL_NAME + " text, "
            + SPELL_SCHOOL + " text, "
            + SPELL_CASTING_TIME + " text, "
            + SPELL_RANGE + " text, "
            + SPELL_COMPONENTS + " text, "
            + SPELL_DURATION + " text, "
            + SPELL_PREPARED + " text, "
            + SPELL_DESCRIPTION + " text, "
            + SPELL_OTHER_1 + " text, "
            + SPELL_OTHER_2 + " text, "
            + SPELL_OTHER_3 + " text, "
            + SPELL_OTHER_4 + " text, "
            + SPELL_OTHER_5 + " text, "
            + SPELL_OTHER_6 + " text, "
            + SPELL_OTHER_7 + " text, "
            + SPELL_OTHER_8 + " text, "
            + SPELL_OTHER_9 + " text, "
            + SPELL_OTHER_10 + " text "
            + ")";

    public static final String SETTING_TABLE_NAME = "setting_table_name";
    public static final String SETTING_C_ID = "setting_id";
    public static final String SETTING_CURRENT_CHARACTER_NAME = "setting_current_character_name";
    public static final String SETTING_CURRENT_CHARACTER_ID = "setting_current_character_id";

    final String createSettings = "create table if not exists " + SETTING_TABLE_NAME + " ( "
            + SETTING_C_ID + " integer primary key autoincrement, "
            + SETTING_CURRENT_CHARACTER_NAME + " text, "
            + SETTING_CURRENT_CHARACTER_ID + " text "
            + ")";
    public static final String CHARACTER_EXPERIENCE = "character_experience";

    public SQLHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            db.execSQL(createDB);
            db.execSQL(createDBWeapons);
            db.execSQL(createdDBSpells);
            db.execSQL(createSettings);
        }
        catch (Exception e){
            Log.e("SQLHelper","error creating database tables");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion){
        try {
            db.execSQL("drop table " + TABLE_NAME);
        }catch (Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
        try{
            db.execSQL("drop table " + ITEM_TABLE_NAME);
        }catch (Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
        try{
            db.execSQL("drop table " + SPELL_TABLE_NAME);
        }catch (Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
        try{
            db.execSQL("drop table " + SETTING_TABLE_NAME);
        }catch (Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
    }

    static int settingUpdate(Context context, String characterName, String characterID){
        SQLHelper settingSQLHelper = new SQLHelper(context);
        SQLiteDatabase settingDB = settingSQLHelper.getWritableDatabase();
        ContentValues settingCV = new ContentValues();
        settingCV.put(SQLHelper.SETTING_CURRENT_CHARACTER_NAME,characterName);
        settingCV.put(SQLHelper.SETTING_CURRENT_CHARACTER_ID, characterID);
        Cursor settingCursor = settingDB.rawQuery("select * from " + SQLHelper.SETTING_TABLE_NAME,null);
        if (settingCursor.getCount() == 0){
            settingDB.insertOrThrow(SQLHelper.SETTING_TABLE_NAME,null,settingCV);
        }
        else {
            settingDB.update(SQLHelper.SETTING_TABLE_NAME, settingCV, null,null);
        }
        settingDB = settingSQLHelper.getWritableDatabase();
        settingCursor = settingDB.rawQuery("select * from " + SQLHelper.SETTING_TABLE_NAME,null);
        settingCursor.moveToFirst();
        settingDB.close();
        return settingCursor.getCount();
    }

    public void outputAll(SQLiteDatabase db){
        Cursor cursor = null;
        try{
            cursor = db.query(SQLHelper.TABLE_NAME,null,null,null,null,null,null);
            cursor.moveToFirst();
        }catch(Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}

        if (cursor != null) {
            do {
                Log.i("SQLHelper", cursor.getString(cursor.getColumnIndex(SQLHelper.CHARACTER_NAME)));
            } while (cursor.moveToNext());
        }

        try{
            cursor = db.query(SQLHelper.ITEM_TABLE_NAME,null,null,null,null,null,null);
            cursor.moveToFirst();
        }catch(Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
        if(cursor != null) {
            do {
                Log.v("SQLHelper", cursor.getString(cursor.getColumnIndex(SQLHelper.ITEM_NAME)));
            } while (cursor.moveToNext());
        }
        try{
            cursor = db.query(SQLHelper.SPELL_TABLE_NAME,null,null,null,null,null,null);
            cursor.moveToFirst();
        }catch(Exception e){Log.e("SQLHelper", "onupgrade error: " + e.toString());}
        if(cursor != null) {
            do {
                Log.v("SQLHelper", cursor.getString(cursor.getColumnIndex(SQLHelper.SPELL_NAME)));
            } while (cursor.moveToNext());
        }
    }

    static Cursor setupDatabase(Context context, String table, String[] columns, String where, String[] searchTerms){
        Cursor cursor;
        SQLHelper sqlhelper = new SQLHelper(context);
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        try{
            sqlhelper.onCreate(db);
            Log.i("SQLHelper","Database created");
        } catch (Exception e){
            Log.e("SQLHelper","Database failed to create");
        }
        cursor = db.query(table,columns, where,searchTerms,null,null,null);
        cursor.moveToFirst();
//        db.close();
        return cursor;
    }

    static void databaseIDUpdate(Context context, String table, String[] columns, String where, String[] searchTerms, String charID){
        Cursor dataBaseCursorItem = setupDatabase(context,ITEM_TABLE_NAME,null, CHARACTER_NAME_LINK + "=?", searchTerms);
        SQLHelper sqlhelper = new SQLHelper(context);
        SQLiteDatabase db = sqlhelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_CHARACTER_ID_LINK,charID);
        Integer countI = 0;
        Integer countS = 0;
        if (dataBaseCursorItem.getCount()>0) {
            String a = dataBaseCursorItem.getString(dataBaseCursorItem.getColumnIndex(ITEM_CHARACTER_ID_LINK));
            do {
                if (dataBaseCursorItem.getString(dataBaseCursorItem.getColumnIndex(ITEM_CHARACTER_ID_LINK)) == null) {
                    try {
                        db.update(ITEM_TABLE_NAME, cv, ITEM_C_ID + "=?", new String[]{dataBaseCursorItem.getString(dataBaseCursorItem.getColumnIndex(ITEM_C_ID))});
                        countI++;
                    } catch (Exception e) {
                        String test = e.toString();
                    }
                }
            } while (dataBaseCursorItem.moveToNext());
        }


        //NEED TO CHECK THIS WORKS FOR OLD DATABASES!!!!
        Cursor dataBaseCursorSpell = setupDatabase(context,SPELL_TABLE_NAME,null, SPELL_CHARACTER_NAME_LINK + "=?", searchTerms);
        cv.clear();
        cv.put(SPELL_CHARACTER_ID_LINK,charID);
        if (dataBaseCursorSpell.getCount()>0) {
            do {
                if (dataBaseCursorSpell.getString(dataBaseCursorSpell.getColumnIndex(SPELL_CHARACTER_ID_LINK)) == null) {
                    try {
                        db.update(SPELL_TABLE_NAME, cv, SPELL_C_ID + "=?", new String[]{dataBaseCursorSpell.getString(dataBaseCursorSpell.getColumnIndex(SPELL_C_ID))});
                        countS++;
                    } catch (Exception e) {
                        String test = e.toString();
                    }
                }
            } while (dataBaseCursorSpell.moveToNext());
        }
        if((countI>0)||(countS>0)) {
            Toast.makeText(context, "Items Updated:" + String.valueOf(countI) + ", Spells Updated:" + String.valueOf(countS), Toast.LENGTH_LONG).show();
        }
    }
}
