package com.example.monopolyclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    protected static int i = 0;
    private static TextToSpeech TTS;
    private boolean ttsEnabled;
    private EditText edTx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button first = (Button)findViewById(R.id.button);
        final TextView txv = (TextView)findViewById(R.id.text);
        edTx = (EditText)findViewById(R.id.editText);
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override public void onInit(int initStatus) {
                if (initStatus == TextToSpeech.SUCCESS)
                {
                    if (TTS.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                            == TextToSpeech.LANG_AVAILABLE) {
                        TTS.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                    } else {
                        TTS.setLanguage(Locale.US);
                    }
                    TTS.setPitch(1.3f);
                    TTS.setSpeechRate(0.7f);
                    ttsEnabled = true;
                }
            }
        });
        first.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //speak(edTx.getText().toString());
                String money = edTx.getText().toString();
                int point = money.indexOf('.');
                String rub = money.substring(0, point);
                String cents = money.substring(point + 1, money.length());
                speak(rub);
                speak("рублей");
                speak(cents);
                speak("копеек");
                switch (i)
                {
                    case 0:
                        txv.setText("Push");
                        i++;
                        break;
                    case 1:
                        txv.setText("Next");
                        i--;
                        break;
                }
            }
        });
    }

    public void speak(String text) {
        if (!ttsEnabled) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }
    }

    @SuppressWarnings("deprecation") private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        TTS.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        TTS.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }
}
