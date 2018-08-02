package cn.leancloud.chatkit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessageRecalledCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessageUpdatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMRecalledMessage;

import java.io.IOException;
import java.util.List;

import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.adapter.LCIMChatAdapter;
import cn.leancloud.chatkit.event.LCIMConversationReadStatusEvent;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.event.LCIMMessageResendEvent;
import cn.leancloud.chatkit.event.LCIMMessageUpdateEvent;
import cn.leancloud.chatkit.event.LCIMMessageUpdatedEvent;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import cn.leancloud.chatkit.utils.LCIMNotificationUtils;
import cn.leancloud.chatkit.view.LCIMInputBottomBar;
import de.greenrobot.event.EventBus;

/**
 * Created by wli on 15/8/27.
 * 将聊天相关的封装到此 Fragment 里边，只需要通过 setConversation 传入 Conversation 即可
 */
public class LCIMConversationFragment extends Fragment {
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_PICK = 2;
  protected AVIMConversation imConversation;
  protected LCIMChatAdapter itemAdapter;
  protected RecyclerView recyclerView;
  protected LinearLayoutManager layoutManager;
  /**
   * 负责下拉刷新的 SwipeRefreshLayout
   */
  protected SwipeRefreshLayout refreshLayout;

  /**
   * 底部的输入栏
   */
  protected LCIMInputBottomBar inputBottomBar;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.lcim_conversation_fragment, container, false);
    recyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_rv_chat);
    refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_srl_pullrefresh);
    refreshLayout.setEnabled(false);
    inputBottomBar = (LCIMInputBottomBar) view.findViewById(R.id.fragment_chat_inputbottombar);
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = getAdpter();
    itemAdapter.resetRecycledViewPoolSize(recyclerView);
    recyclerView.setAdapter(itemAdapter);

    EventBus.getDefault().register(this);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        AVIMMessage message = itemAdapter.getFirstMessage();
        if (null == message) {
          refreshLayout.setRefreshing(false);
        } else {
          imConversation.queryMessages(message.getMessageId(), message.getTimestamp(), 20, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
              refreshLayout.setRefreshing(false);
              if (filterException(e)) {
                if (null != list && list.size() > 0) {
                  itemAdapter.addMessageList(list);
                  itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
                    imConversation.getLastReadAt());
                  itemAdapter.notifyDataSetChanged();
                  layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                }
              }
            }
          });
        }
      }
    });
  }

  protected LCIMChatAdapter getAdpter() {
    return new LCIMChatAdapter();
  }

  @Override
  public void onResume() {
    super.onResume();
    if (null != imConversation) {
      LCIMNotificationUtils.addTag(imConversation.getConversationId());
    }
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  public void setConversation(final AVIMConversation conversation) {
    imConversation = conversation;
    refreshLayout.setEnabled(true);
    inputBottomBar.setTag(imConversation.getConversationId());
    fetchMessages();
    LCIMNotificationUtils.addTag(conversation.getConversationId());
    if (!conversation.isTransient()) {
      if (conversation.getMembers().size() == 0) {
        conversation.fetchInfoInBackground(new AVIMConversationCallback() {
          @Override
          public void done(AVIMException e) {
            if (null != e) {
              LCIMLogUtils.logException(e);
            }
            itemAdapter.showUserName(conversation.getMembers().size() > 2);
          }
        });
      } else {
        itemAdapter.showUserName(conversation.getMembers().size() > 2);
      }
    } else {
      itemAdapter.showUserName(true);
    }
  }
  /**
   * 拉取消息，必须加入 conversation 后才能拉取消息
   */
  private void fetchMessages() {

    imConversation.queryMessages(new AVIMMessagesQueryCallback() {
      @Override
      public void done(List<AVIMMessage> messageList, AVIMException e) {
        if (filterException(e)) {
          itemAdapter.setMessageList(messageList);
          recyclerView.setAdapter(itemAdapter);
          itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
            imConversation.getLastReadAt());
          itemAdapter.notifyDataSetChanged();
          scrollToBottom();
          clearUnreadConut();
        }
      }
    });
  }

  /**
   * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
   * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
   */
  public void onEvent(LCIMInputBottomBarTextEvent textEvent) {
    if (null != imConversation && null != textEvent) {
      if (!TextUtils.isEmpty(textEvent.sendContent) && imConversation.getConversationId().equals(textEvent.tag)) {
        sendText(textEvent.sendContent);
      }
    }
  }

  /**
   * 处理推送过来的消息
   * 同理，避免无效消息，此处加了 conversation id 判断
   */
  public void onEvent(LCIMIMTypeMessageEvent messageEvent) {
    if (null != imConversation && null != messageEvent &&
      imConversation.getConversationId().equals(messageEvent.conversation.getConversationId())) {
      itemAdapter.addMessage(messageEvent.message);
      itemAdapter.notifyDataSetChanged();
      scrollToBottom();
      clearUnreadConut();
    }
  }


  public void onEvent(LCIMMessageResendEvent resendEvent) {
    if (null != imConversation && null != resendEvent &&
      null != resendEvent.message && imConversation.getConversationId().equals(resendEvent.message.getConversationId())) {
      if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == resendEvent.message.getMessageStatus()
        && imConversation.getConversationId().equals(resendEvent.message.getConversationId())) {
        sendMessage(resendEvent.message, false);
      }
    }
  }
  /**
   * 更新对方已读的位置事件
   * @param readEvent
   */
  public void onEvent(LCIMConversationReadStatusEvent readEvent) {
    if (null != imConversation && null != readEvent &&
      imConversation.getConversationId().equals(readEvent.conversationId)) {
      itemAdapter.setDeliveredAndReadMark(imConversation.getLastDeliveredAt(),
        imConversation.getLastReadAt());
      itemAdapter.notifyDataSetChanged();
    }
  }

  public void onEvent(final LCIMMessageUpdateEvent event) {
    if (null != imConversation && null != event &&
      null != event.message && imConversation.getConversationId().equals(event.message.getConversationId())) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle("操作").setItems(new String[]{"删除", "修改消息内容"}, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          if (0 == which) {
            recallMessage(event.message);
          } else if (1 == which) {
            showUpdateMessageDialog(event.message);
          }
        }
      });

      builder.create().show();
    }
  }

  public void onEvent(final LCIMMessageUpdatedEvent event) {
    if (null != imConversation && null != event &&
      null != event.message && imConversation.getConversationId().equals(event.message.getConversationId())) {
      itemAdapter.updateMessage(event.message);
    }
  }

  private void showUpdateMessageDialog(final AVIMMessage message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    final EditText editText = new EditText(getActivity());
    builder.setView(editText);
    builder.setTitle("修改消息内容");
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        String content = editText.getText().toString();
        updateMessage(message, content);
      }
    });
    builder.show();
  }

  private void recallMessage(AVIMMessage message) {
    imConversation.recallMessage(message, new AVIMMessageRecalledCallback() {
      @Override
      public void done(AVIMRecalledMessage recalledMessage, AVException e) {
        if (null == e) {
          itemAdapter.updateMessage(recalledMessage);
        } else {
          Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void updateMessage(AVIMMessage message, String newContent) {
    AVIMTextMessage textMessage = new AVIMTextMessage();
    textMessage.setText(newContent);
    imConversation.updateMessage(message, textMessage, new AVIMMessageUpdatedCallback() {
        @Override
        public void done(AVIMMessage message, AVException e) {
          if (null == e) {
            itemAdapter.updateMessage(message);
          } else {
            Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
          }
        }
      });
  }





  /**
   * 滚动 recyclerView 到底部
   */
  private void scrollToBottom() {
    layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
  }



  /**
   * 发送文本消息
   *
   * @param content
   */
  protected void sendText(String content) {
    AVIMTextMessage message = new AVIMTextMessage();
    message.setText(content);
    sendMessage(message);
  }

  public void sendMessage(AVIMMessage message) {
    sendMessage(message, true);
  }

  /**
   * 发送消息
   *
   * @param message
   */
  public void sendMessage(AVIMMessage message, boolean addToList) {
    if (addToList) {
      itemAdapter.addMessage(message);
    }
    itemAdapter.notifyDataSetChanged();
    scrollToBottom();

    AVIMMessageOption option = new AVIMMessageOption();
    option.setReceipt(true);
    imConversation.sendMessage(message, option, new AVIMConversationCallback() {
      @Override
      public void done(AVIMException e) {
        itemAdapter.notifyDataSetChanged();
        if (null != e) {
          LCIMLogUtils.logException(e);
        }
      }
    });
  }

  private boolean filterException(Exception e) {
    if (null != e) {
      LCIMLogUtils.logException(e);
      Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    return (null == e);
  }

  private void clearUnreadConut() {
    if (imConversation.getUnreadMessagesCount() > 0) {
      imConversation.read();
    }
  }
}
