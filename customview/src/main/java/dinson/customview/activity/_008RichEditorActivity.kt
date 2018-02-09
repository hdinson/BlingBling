package dinson.customview.activity

import android.os.Bundle
import android.view.View

import dinson.customview.R
import dinson.customview._global.BaseActivity
import dinson.customview.utils.SoftHideKeyBoardUtil
import dinson.customview.weight._008richeditor.widget.EmojiLayout
import dinson.customview.weight._008richeditor.widget.RichEditor

import dinson.customview.R.id.emojiLayout
import kotlinx.android.synthetic.main.activity__008__rich_editor.*

class _008RichEditorActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__008__rich_editor)
        SoftHideKeyBoardUtil.assistActivity(this)

        initUI()
    }

    private fun initUI() {
        cancel.setOnClickListener { onBackPressed() }
        ib_emoji.setOnClickListener {
            if (emojiLayout.visibility == View.VISIBLE) {
                emojiLayout.visibility = View.GONE
                emojiLayout.showKeyboard()
            } else {
                emojiLayout.visibility = View.VISIBLE
                emojiLayout.hideKeyboard()
            }
        }
        emojiLayout.setEditTextSmile(richEditor)
    }
}
