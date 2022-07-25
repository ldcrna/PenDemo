package com.example.pendrawdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.jacky.commondraw.views.doodleview.DoodleEnum;
import com.jacky.commondraw.views.doodleview.DoodleView;
import com.jacky.commondraw.wigets.DrawToolAttribute;
import com.jacky.commondraw.wigets.IDrawtoolsChanged;
import com.jacky.commondraw.wigets.drawpickers.DrawToolsPicker;
import com.jacky.commondraw.wigets.drawpickers.IPickerControl;
import com.jacky.commondraw.wigets.erasepopup.ErasePopupWindow;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private DoodleView mDoodleView;
    private FrameLayout undoView;
    private FrameLayout redoView;
    private FrameLayout addImageView;
    private DrawToolsPicker mDrawToolsPicker;
    private RadioButton eraseRadioButton;
    private RadioButton brushRadioButton;
    private ErasePopupWindow mErasePopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gb_main);
        mContext = MainActivity.this;

        mDoodleView = findViewById(R.id.doodleView);
        undoView = findViewById(R.id.undoView);
        redoView = findViewById(R.id.redoView);
        addImageView = findViewById(R.id.addImageView);
        brushRadioButton = findViewById(R.id.brushRadioButton);
        eraseRadioButton = findViewById(R.id.eraseRadioButton);


        mDrawToolsPicker = new DrawToolsPicker(mContext);
        mErasePopupWindow = new ErasePopupWindow(mContext);
        mDrawToolsPicker.initDrawToolPicker();
        IPickerControl drawToolPickerControl = mDrawToolsPicker.getDrawToolPickerControl();
        mDrawToolsPicker.add(new IDrawtoolsChanged() {
            @Override
            public void drawToolInit(DrawToolAttribute drawToolAtt) {
                mDoodleView.setStrokeType(drawToolAtt.type);
                mDoodleView.setInputMode(DoodleEnum.InputMode.DRAW);
            }

            @Override
            public void onDrawToolChanged(DrawToolAttribute newToolAtt, DrawToolAttribute oldToolAtt, AttType[] attTypesChanged) {
                mDoodleView.setStrokeAttrs(newToolAtt.type, newToolAtt.color, newToolAtt.width, newToolAtt.alpha);
                mDoodleView.setStrokeType(newToolAtt.type);
                mDoodleView.setInputMode(DoodleEnum.InputMode.DRAW);
            }
        });

        mErasePopupWindow.addOnEraseChangedListener(width -> {
            mDoodleView.setEraseWidth(width);
            mDoodleView.setInputMode(DoodleEnum.InputMode.ERASE);
        });

        undoView.setOnClickListener(v -> mDoodleView.undo());
        redoView.setOnClickListener(v -> mDoodleView.redo());
        eraseRadioButton.setOnClickListener(v -> {
            if(!mErasePopupWindow.isShowing()){
                mErasePopupWindow.showAsDropDown(eraseRadioButton);
            }
        });
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawToolPickerControl!=null){
                    try {
                        drawToolPickerControl.showPicker(brushRadioButton);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}