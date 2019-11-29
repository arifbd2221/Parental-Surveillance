package com.keyboard.arifbd2221.keyboardexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.InputMethodService;
import android.media.AudioManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keyboard.arifbd2221.keyboardexample.model.Child;
import com.keyboard.arifbd2221.keyboardexample.model.FilterWords;

import static android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private AudioManager am;
    private Vibrator v;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Families/BlQXWtR4YlWuBg6QlTGdS1Ny5Q43/wordList");

    //SharedPreferences sharedPreferences = this.getSharedPreferences("username", Context.MODE_PRIVATE);
    private boolean caps = false;

    InputConnection inputConnection;


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        String userId = "ciIfmLGMKha9Kgy9NZaC1as9yUC2"; //sharedPreferences.getString("userid",null);
        //String familyKey = sharedPreferences.getString("key", null);
        //reference.child("BlQXWtR4YlWuBg6QlTGdS1Ny5Q43").child("wordList");


        inputConnection = getCurrentInputConnection();

        if (inputConnection != null) switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                Log.d("KEYCODE_DELETE", "123456");
                CharSequence selectedText = inputConnection.getSelectedText(0);
                Log.d("selectedText", selectedText + "123");
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                keyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:

/*                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                myRef.push().setValue(extractedText.text+"l");*/


                if (userId != null)
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.e("onDataChange",""+dataSnapshot.getKey());
                            ExtractedText extractedText = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                FilterWords filterWords = ds.getValue(FilterWords.class);
                                Log.e("dataSnapshot","dataSnapshot");
                                Log.e("word",filterWords.word);
                                Log.e("extractedText",extractedText.text+"l");
                                String typed = extractedText.text+"l";
                                String[] words = typed.split(" ");
                                for (String w : words){
                                    //w.trim();
                                    if (w.equals(filterWords.word)){
                                        Log.e("found","Found +1");
                                        DatabaseReference databaseReference = ds.getRef().child("quantity");
                                        databaseReference.setValue(filterWords.quantity+1);
                                    }
                                }
                            }


/*                            for (DataSnapshot d : dataSnapshot.getChildren()){
                                Log.e("DataSnapshot","getChildren");
                                FilterWords filterWords = d.getValue(FilterWords.class);

                                Log.e("word", filterWords.word);
                            }*/


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                Log.d("onDestroy-done", "123456");
                //Log.d("extractedText", extractedText.text + "lol");
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                Log.d("onDestroy-done", "123456");
                break;

            case Keyboard.KEYCODE_CANCEL:
                ExtractedText extractedText2 = inputConnection.getExtractedText(new ExtractedTextRequest(), 0);
                FirebaseDatabase database2= FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference("message");
                myRef2.push().setValue(extractedText2.text+"l");
                break;
            default:

                Log.d("onDestroy-default", "123456");
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                inputConnection.commitText(String.valueOf(code), 1);

        }

    }

    @Override
    public void onText(CharSequence text) {
        //InputConnection inputConnection = getCurrentInputConnection();
        Log.d("onText","123456");
    }

    @Override
    public void swipeLeft() {
        Log.d("onText","hello");
    }

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void swipeRight() {
        Log.d("onText","hello");
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void playSound(int keyCode){
        v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(20);
        am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }


}
