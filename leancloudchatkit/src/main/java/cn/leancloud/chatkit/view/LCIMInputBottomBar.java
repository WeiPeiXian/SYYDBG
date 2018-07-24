package cn.leancloud.chatkit.view;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.utils.LCIMSoftInputUtils;
import de.greenrobot.event.EventBus;


/**
 * Created by wli on 15/7/24.
 * 专门负责输入的底部操作栏，与 activity 解耦
 * 当点击相关按钮时发送 InputBottomBarEvent，需要的 View 可以自己去订阅相关消息
 */
public class LCIMInputBottomBar extends LinearLayout {


  /**
   * 文本输入框
   */
  private EditText contentEditText;

  /**
   * 发送文本的Button
   */
  private View sendTextBtn;
  /**
   * 切换到文本输入的 Button
   */

  /**
   * 底部的layout，包含 emotionLayout 与 actionLayout
   */
  private View moreLayout;

  /**
   * action layout
   */
  private LinearLayout actionLayout;

  /**
   * 最小间隔时间为 1 秒，避免多次点击
   */
  private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

  public LCIMInputBottomBar(Context context) {
    super(context);
    initView(context);
  }

  public LCIMInputBottomBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context);
  }


  /**
   * 隐藏底部的图片、emtion 等 layout
   */
  public void hideMoreLayout() {
    moreLayout.setVisibility(View.GONE);
  }


  private void initView(Context context) {
    View.inflate(context, R.layout.lcim_chat_input_bottom_bar_layout, this);
    contentEditText = (EditText) findViewById(R.id.input_bar_et_content);
    sendTextBtn = findViewById(R.id.input_bar_btn_send_text);
//    keyboardBtn = findViewById(R.id.input_bar_btn_keyboard);

    actionLayout = (LinearLayout) findViewById(R.id.input_bar_layout_action);

    setEditTextChangeListener();


    contentEditText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        moreLayout.setVisibility(View.GONE);
        LCIMSoftInputUtils.showSoftInput(getContext(), contentEditText);
      }
    });



    sendTextBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
          Toast.makeText(getContext(), R.string.lcim_message_is_null, Toast.LENGTH_SHORT).show();
          return;
        }

        contentEditText.setText("");
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            sendTextBtn.setEnabled(true);
          }
        }, MIN_INTERVAL_SEND_MESSAGE);

        EventBus.getDefault().post(
                new LCIMInputBottomBarTextEvent(LCIMInputBottomBarEvent.INPUTBOTTOMBAR_SEND_TEXT_ACTION,
                        content, getTag()));
      }
    });

  }





  /**
   * 展示文本输入框及相关按钮，隐藏不需要的按钮及 layout
   */
  private void showTextLayout() {
    contentEditText.setVisibility(View.VISIBLE);
    sendTextBtn.setVisibility(contentEditText.getText().length() > 0 ? VISIBLE : GONE);
    moreLayout.setVisibility(View.GONE);
    contentEditText.requestFocus();
    LCIMSoftInputUtils.showSoftInput(getContext(), contentEditText);
  }

  /**
   * 展示录音相关按钮，隐藏不需要的按钮及 layout
   */
  private void showAudioLayout() {
    contentEditText.setVisibility(View.GONE);
    moreLayout.setVisibility(View.GONE);
    LCIMSoftInputUtils.hideSoftInput(getContext(), contentEditText);
  }

  /**
   * 设置 text change 事件，有文本时展示发送按钮，没有文本时展示切换语音的按钮
   */
  private void setEditTextChangeListener() {
    contentEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        boolean showSend = charSequence.length() > 0;
        sendTextBtn.setVisibility(showSend ? View.VISIBLE : GONE);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    });
  }
}
