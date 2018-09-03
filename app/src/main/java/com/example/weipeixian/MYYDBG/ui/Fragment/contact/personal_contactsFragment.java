package com.example.weipeixian.MYYDBG.ui.Fragment.contact;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.View.ClearEditText;
import com.example.weipeixian.MYYDBG.adapter.SortAdapter;
import com.example.weipeixian.MYYDBG.model.PhoneInfo;
import com.example.weipeixian.MYYDBG.model.SortModel;
import com.example.weipeixian.MYYDBG.ui.activity.contact.add_personal_contact;
import com.example.weipeixian.MYYDBG.ui.activity.contact.person_contact_detail;
import com.example.weipeixian.MYYDBG.ui.activity.news.news;
import com.example.weipeixian.MYYDBG.util.CharacterParser;
import com.example.weipeixian.MYYDBG.util.PhoneUtil;
import com.example.weipeixian.MYYDBG.util.PinyinComparator;
import com.example.weipeixian.MYYDBG.View.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.weipeixian.MYYDBG.util.contactUtil.share;


public class personal_contactsFragment extends BaseFragment {
	private ListView sortListView;
	SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private  List<PhoneInfo> phoneInfos;
	private PinyinComparator pinyinComparator;
	private View view;
	ImageButton back;
	ImageButton add;
	public personal_contactsFragment(){}
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.contacts_fragment, container, false);
		return view;
	}
	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		back=view.findViewById(R.id.back);
		add = view.findViewById(R.id.send);
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), add_personal_contact.class));
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		initViews();
	}
	private void initViews() {
		sideBar = view.findViewById(R.id.sidrbar);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		});
		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				String Phone_name= ((SortModel)adapter.getItem(position)).getName();
				String Phone_number= ((SortModel)adapter.getItem(position)).getNum();
				intent.setClass(getActivity(), person_contact_detail.class);
				intent.putExtra("extra_name",Phone_name);
				intent.putExtra("extra_number",Phone_number);
				startActivity(intent);
			}
		});
		sortListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(getActivity())
						.setItems(new String[]{"确认分享？"}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which){
									case 0:
										String Phone_name= ((SortModel)adapter.getItem(position)).getName();
										String Phone_number= ((SortModel)adapter.getItem(position)).getNum();
										share(Phone_name,Phone_number);
										break;
								}

							}
						}).create();
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				return true;
			}
		});


	}
	@Override
	public void onResume() {

		super.onResume();
		update();
	}
	public void update(){
		PhoneUtil phoneUtil = new PhoneUtil(getActivity());;
		phoneInfos = phoneUtil.getPhone();
		int size= phoneInfos.size();
		String[][] date=new String[size][2];
		for(int i = 0; i< phoneInfos.size(); i++){
			date[i][0]= phoneInfos.get(i).getName();
			date[i][1]= phoneInfos.get(i).getTelPhone();
		}
		SourceDateList = filledData(date);
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(adapter);
		mClearEditText = (ClearEditText)view.findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	/**
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [][] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i][0]);
            sortModel.setNum(date[i][1]);
			String pinyin = characterParser.getSelling(date[i][0]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}
            else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}
        else if (filterStr.matches("^([0-9]|[/+])*$")) {//正则表达式 匹配号码
            for (SortModel sortModel : SourceDateList) {
                if (sortModel.getNum() != null && sortModel.getName() != null) {
                    if (sortModel.getNum().contains(filterStr) || sortModel.getName().contains(filterStr)) {
                        if (!filterDateList.contains(sortModel)) {
                            filterDateList.add(sortModel);
                        }
                    }
                }
            }
            }else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
