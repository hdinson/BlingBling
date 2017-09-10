package dinson.customview.activity;

import android.os.Bundle;
import android.view.View;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.utils.SoftHideKeyBoardUtil;
import dinson.customview.weight.richeditor.widget.EmojiLayout;
import dinson.customview.weight.richeditor.widget.RichEditor;

import static dinson.customview.R.id.emojiLayout;

public class _008_RichEditorActivity extends BaseActivity implements View.OnClickListener {

    private EmojiLayout mEmojiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__008__rich_editor);
        SoftHideKeyBoardUtil.assistActivity(this);

        initUI();
    }

    @Override
    public int setWindowBackgroundColor() {
        return android.R.color.white;
    }

    private void initUI() {
        findViewById(R.id.ib_emoji).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        mEmojiLayout = (EmojiLayout) findViewById(emojiLayout);
        RichEditor richEditor = (RichEditor) findViewById(R.id.richEditor);
        mEmojiLayout.setEditTextSmile(richEditor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_emoji:
                if (mEmojiLayout.getVisibility() == View.VISIBLE) {
                    mEmojiLayout.setVisibility(View.GONE);
                    mEmojiLayout.showKeyboard();
                } else {
                    mEmojiLayout.setVisibility(View.VISIBLE);
                    mEmojiLayout.hideKeyboard();
                }
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }
}
