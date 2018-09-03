package com.example.weipeixian.MYYDBG;

import android.content.Context;

import com.example.weipeixian.MYYDBG.model.PhoneInfo;
import com.example.weipeixian.MYYDBG.util.PhoneUtil;

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
  static BaseApplication application =new BaseApplication();
  static Context context= application.getContext();;
  private CustomUserProvider(){
  }
  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }

  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();
  private static PhoneUtil phoneUtil = new PhoneUtil(context);
  private static List<PhoneInfo> list =  phoneUtil.getPhone();
  static {
    for (PhoneInfo info :list){
      partUsers.add(new LCChatKitUser(info.getTelPhone(), info.getName(), ""));
    }
    partUsers.add(new LCChatKitUser("1677", "1677", ""));
    partUsers.add(new LCChatKitUser("15520760237", "魏培贤", ""));
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
