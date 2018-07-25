package cn.leancloud.chatkit.event;

/**
 * Created by wli on 15/7/29.
 * InputBottomBar 相关的 EventBus 事件
 */
public class LCIMInputBottomBarEvent {
  public static final int INPUTBOTTOMBAR_SEND_TEXT_ACTION = 3;
  public int eventAction;
  public Object tag;
  public LCIMInputBottomBarEvent(int action, Object tag) {
    eventAction = action;
    this.tag = tag;
  }
}
