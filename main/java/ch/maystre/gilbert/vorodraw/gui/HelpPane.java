package ch.maystre.gilbert.vorodraw.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashSet;

import ch.maystre.gilbert.vorodraw.R;

public class HelpPane extends LinearLayout {

    private WebView helpText;
    private ImageButton closeButton;

    private final HashSet<HelpPaneListener> listeners = new HashSet<>();

    private void init(){
        inflate(getContext(), R.layout.help_pane,this);
        helpText = findViewById(R.id.help_view);

        helpText.setVerticalScrollBarEnabled(true);
        helpText.loadUrl("file:///android_asset/help.html");

        closeButton = findViewById(R.id.help_pane_close_button);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for(HelpPaneListener l : listeners)
                    l.closeButtonPressed();
            }
        });

        // this is to let no touch pass when the help pane is visible
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void registerListener(HelpPaneListener listener){
        listeners.add(listener);
    }

    public HelpPane(Context context) {
        super(context);
        init();
    }

    public HelpPane(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelpPane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HelpPane(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}




