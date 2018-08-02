package com.example.weipeixian.MYYDBG;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {
  private static CustomUserProvider customUserProvider;
  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }
  private CustomUserProvider() {
  }
  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();
  static {
    partUsers.add(new LCChatKitUser("Tom", "Tom", ""));
    partUsers.add(new LCChatKitUser("1677", "1677", ""));
    partUsers.add(new LCChatKitUser("15520760237", "Harry", ""));
    partUsers.add(new LCChatKitUser("William", "William", ""));
    partUsers.add(new LCChatKitUser("Bob", "Bob", ""));
  }

  @Override
  public void fetchProfiles(List<String> list, LCChatProfilesCallBack callBack) {
    List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
    for (String userId : list) {
      for (LCChatKitUser user : partUsers) {
        if (user.getUserId().equals(userId)) {
          userList.add(user);
          break;
        }
      }
    }
    callBack.done(userList, null);
  }

  public List<LCChatKitUser> getAllUsers() {
    return partUsers;
  }
}
