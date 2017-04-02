package de.lukas.wannistfeierabend;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    SharedPreferences sharedPreferences;
    Context context;

    public static int getHour(String time) {
        String[] pieces=time.split(" ");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(" ");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);
        this.context = ctxt;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String time  = sharedPreferences.getString("key_pref_trigger_time", "6 30");
        String parts[] = time.split(" ");
        lastHour = Integer.valueOf(parts[0]);
        lastMinute = Integer.valueOf(parts[1]);

        setPositiveButtonText("Ok");
        setNegativeButtonText("Abbrechen");

    }

    @Override
    public CharSequence getSummary() {

        String lastHourString = "";
        String lastMinuteString = "";

        if (lastHour< 10){
            lastHourString = "0" + lastHour;
        }else{
            lastHourString = lastHour + "";
        }

        if (lastMinute < 10){
            lastMinuteString = "0" + lastMinute;
        }else{
            lastMinuteString = "" + lastMinute;
        }

        return  lastHourString + ":" + lastMinuteString;
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(context);

        picker.setIs24HourView(true);


        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        // get last saved time and display it



        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();

            String time=String.valueOf(lastHour)+" "+String.valueOf(lastMinute);

            // save new time
            sharedPreferences.edit().putString("cb_enable_notifications", time).apply();

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString("00 00");
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }
}
