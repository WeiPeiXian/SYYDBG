package cn.leancloud.chatkit.utils;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.cache.LCIMProfileCache;

/**
 * Created by wli on 16/3/2.
 * 和 Conversation 相关的 Util 类
 */
public class LCIMConversationUtils {
  public static String  getConversationName(final AVIMConversation conversation){
    AVUser currentUser = AVUser.getCurrentUser();
    String name = currentUser.getUsername();
    int length  =  conversation.getMembers().size();
    Log.d("ss",conversation.getMembers().toString());
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
    return name;
  }
  public static String get(final String s){
    //从通讯录获取名字
    final String[] name = new String[1];
    AVQuery<AVUser> userQuery = new AVQuery<>("_User");
    userQuery.whereEqualTo("username",s);
    userQuery.findInBackground(new FindCallback<AVUser>() {
      @Override
      public void done(List<AVUser> list, AVException e) {
        if (e==null){
          AVUser user =list.get(0);
          String n =user.getString("name");
          if (n!=null){
            name[0] = n;
          }
          else
            name[0] = s;
        }
        else {
          Log.d("ds",e.getMessage());

        }
      }
    });
    return name[0];
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
      LCIMProfileCache.getInstance().getUserAvatar(peerId, callback);
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