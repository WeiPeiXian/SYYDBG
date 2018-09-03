package com.example.weipeixian.MYYDBG.util;

import android.text.TextUtils;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.example.weipeixian.MYYDBG.BaseApplication;
import com.example.weipeixian.MYYDBG.model.PhoneInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.cache.LCIMProfileCache;

/**
 * Created by wli on 16/3/2.
 * 和 Conversation 相关的 Util 类
 */
public class LCIMConversationUtils {
    private static Map<String,String> map = new LinkedHashMap();
    static PhoneUtil phoneUtil = new PhoneUtil(BaseApplication.getContext());
    static {
        List<PhoneInfo> list =  phoneUtil.getPhone();
        for (PhoneInfo info :list){
            map.put(info.getTelPhone(),info.getName());
        }
    }


  public static String  getConversationName(final AVIMConversation conversation){
    AVUser currentUser = AVUser.getCurrentUser();
    String name = currentUser.getUsername();
    int length  =  conversation.getMembers().size();
    if(length == 2){
      String members[] = new String[2];
      List<String> mem = conversation.getMembers();
      members[0] = mem.get(0);
      members[1] = mem.get(1);
      if (name.equals(members[0]) ){
        name = members[1];
      }
      else{
        name = members[0];
      }
    }
    String m = get(name);
    if (m!=null){
        name = m;
    }
    return name;
  }
  public static String get(final String s){
    return map.get(s);
  }


  /**
   * 获取单聊会话的 icon
   * 单聊：对方用户的头像
   * 群聊：返回 null
   *
   * @param conversation
   * @param callback
   */
  public static void getConversationPeerIcon(final AVIMConversation conversation, AVCallback<String> callback) {
    if (null != conversation && !conversation.isTransient() && !conversation.getMembers().isEmpty()) {
      String peerId = getConversationPeerId(conversation);
      if (1 == conversation.getMembers().size()) {
        peerId = conversation.getMembers().get(0);
      }
    } else if (null != conversation) {
      callback.internalDone("", null);
    } else {
      callback.internalDone(null, new AVException(new Throwable("cannot find icon!")));
    }
  }

  /**
   * 获取 “对方” 的用户 id，只对单聊有效，群聊返回空字符串
   *
   * @param conversation
   * @return
   */
  private static String getConversationPeerId(AVIMConversation conversation) {
    if (null != conversation && 2 == conversation.getMembers().size()) {
      String currentUserId = LCChatKit.getInstance().getCurrentUserId();
      String firstMemeberId = conversation.getMembers().get(0);
      return conversation.getMembers().get(firstMemeberId.equals(currentUserId) ? 1 : 0);
    }
    return "";
  }
}