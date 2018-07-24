package com.example.weipeixian.syydbg.ui;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;


import com.example.weipeixian.syydbg.BaseFragment;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.adapter.ConversationListAdapter;
import com.example.weipeixian.syydbg.model.ChatListData;
import com.example.weipeixian.syydbg.model.ConversationListData;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentPhone extends BaseFragment  {

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<String> nameList = new ArrayList<>();
    List<ConversationListData> conversationList = new ArrayList<>();
    ConversationListAdapter adapter;
    private static final DateFormat FORMATTER_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Button send;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, send.class));
            }
        });
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                swipeRefreshLayout.setRefreshing(false);
                adapter = new ConversationListAdapter(mContext, conversationList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(mContext,
                                ChatDetailActivity.class).putExtra("userid",
                                conversationList.get(position).getUsername()));
                    }
                });

            }
        };

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);

        recyclerView = (RecyclerView) view.findViewById(R.id.conversation_recycleView);
        initRecyclerview();
////        EMGroupManager.getInstance().loadAllGroups();
//       EMClient.getInstance().chatManager().loadAllConversations();
//        loadData();
//        //加载数据
//        //刷新加载数据//
//        // 待增加监听  收到消息加载数据
//
//
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                loadData();

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                loadData();
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                //发送成功
                loadData();

            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                loadData();
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        EMConversationListener convListener = new EMConversationListener(){
            @Override
            public void onCoversationUpdate() {
                loadData();
            }

        };
        EMClient.getInstance().chatManager().addConversationListener(convListener);
//        messageListener = this;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        EMClient.getInstance().chatManager().loadAllConversations();
        loadData();
        return view;
    }


    private void loadData() {
        nameList.clear();
        conversationList.clear();
        //加载数据
        List<Map.Entry<String,EMConversation>> conversations =  loadConversationList();

        //构建适配器
        for (Map.Entry<String,EMConversation> conversationEntry : conversations) {
            String m = conversationEntry .getKey();
            EMConversation emConversation = conversationEntry.getValue();
            ConversationListData data = new ConversationListData();
            data.setUsername(m);
            data.setTime(FORMATTER_ALL.format(new Date(emConversation.getLastMessage().getMsgTime())));
            if (emConversation.getLastMessage().getType() == EMMessage.Type.TXT){
                EMMessageBody tmBody = emConversation.getLastMessage().getBody();
                data.setMessage(((EMTextMessageBody) tmBody).getMessage());
            }
            conversationList.add(data);
        }
        mHandler.sendEmptyMessage(0);
    }
    protected List<Map.Entry<String,EMConversation>> loadConversationList () {

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        System.out.println("xxxxxxxxxxxxx");
        System.out.println(conversations);
        Set<Map.Entry<String, EMConversation>> set = conversations.entrySet();

        ArrayList<Map.Entry<String, EMConversation>> list = new ArrayList<Map.Entry<String, EMConversation>>(set);

        Collections.sort(list, new Comparator<Map.Entry<String, EMConversation>>() {
            @Override
            public int compare(Map.Entry<String, EMConversation> o1, Map.Entry<String, EMConversation> o2) {

                long value1 = o1.getValue().getLastMessage().getMsgTime();
                long value2 = o2.getValue().getLastMessage().getMsgTime();
                if (value1 == value2) {
                    return 0;
                } else if (value1 > value2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        for (Map.Entry<String, EMConversation> i:list){
            System.out.println(i.getKey());
            System.out.println(i.getValue());
        }
        return list;
    }

    /**
     * 初始化主列表
     */
    private void initRecyclerview() {
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,
                R.color.orange,
                R.color.red);

        // RecycleView初始化配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        recyclerView.addItemDecoration(new MainDividerItemDecoration(
//                TravelCalendarActivity.this, MainDividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    loadData();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }
//    public int getUnreadMsgCountTotal() {
//        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
//    }


}