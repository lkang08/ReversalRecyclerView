package lk.reversalrecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static lk.reversalrecyclerview.MyRecyclerViewAdapter.DEFAULT_SHOW_SIZE;

public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;
    MyRecyclerViewAdapter mAdapter;
    View mGroupHeadLayout;
    TextView mGroupTitle;
    TextView mGroupOperation;
    int mGroupHeadLayoutTop = 0;
    Button mAdd;
    List<GroupItem> mDataList;
    boolean mTag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdd = (Button) findViewById(R.id.add);
        mGroupHeadLayout = findViewById(R.id.common_group_layout);
        mGroupTitle = (TextView) findViewById(R.id.title);
        mGroupOperation = (TextView) findViewById(R.id.operation);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        mGroupHeadLayoutTop = dm.heightPixels - getResources().getDimensionPixelSize(R.dimen.search_layout_height) -
                getResources().getDimensionPixelSize(R.dimen.group_height) - statusBarHeight;
        mGroupOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null && v.getTag() instanceof GroupItem) {
                    GroupItem item = (GroupItem) v.getTag();
                    item.onClick();
                    mAdapter.notifyDataSetChanged();
                    mGroupOperation.setText(item.operation);
                }
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupItem groupItem = new GroupItem("New Group ");
                int childCount = mTag ? 5 : 2;
                mTag = !mTag;
                List<ChildItem> list = new ArrayList<ChildItem>();
                for (int i = 0; i < childCount; i++) {
                    ChildItem childItem = new ChildItem("new Child Item " + i, "I am new coming [" + i + "]", groupItem);
                    list.add(childItem);
                }
                groupItem.setChildItems(list);
                if (mDataList.size() > 2) {
                    mDataList.add(2, groupItem);
                } else {
                    mDataList.add(0, groupItem);
                }
                mAdapter.setDatas(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        });

        initData();

        mAdapter = new MyRecyclerViewAdapter(getApplicationContext());
        mAdapter.setDatas(mDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(mAdapter.getMyDecoration());
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerViewScrollListener();
    }

    private void setRecyclerViewScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mGroupHeadLayoutTop == 0) {
                    mGroupHeadLayoutTop = mGroupHeadLayout.getTop();
                }
                View stickInfoView = recyclerView.findChildViewUnder(mGroupHeadLayout.getMeasuredWidth() / 2, mGroupHeadLayoutTop - 1);
                if (stickInfoView != null) {
                    int position = recyclerView.getChildAdapterPosition(stickInfoView);
                    if (position == 0) {
                        updateGroupViewLayout(mAdapter.getGroupItem(0));
                    }
                    GroupItem item;
                    if (mAdapter.getItemViewType(position) == ITEM_TYPE.GROUP.ordinal()) {
                        if (mAdapter.getItemViewType(position - 1) == ITEM_TYPE.CHILD.ordinal()) {
                            item = mAdapter.getChildItem(position - 1).father;
                        } else {
                            item = mAdapter.getGroupItem(position - 1);
                        }
                        int dealtY = stickInfoView.getBottom() - mGroupHeadLayoutTop;
                        mGroupHeadLayout.setTranslationY(dealtY);
                    } else {
                        item = mAdapter.getChildItem(position).father;
                        mGroupHeadLayout.setTranslationY(0);
                    }
                    updateGroupViewLayout(item);
                } else {
                    mGroupHeadLayout.setTranslationY(0);
                }
            }
        });
    }

    private void updateGroupViewLayout(GroupItem item) {
        mGroupTitle.setText(item.title);
        if (item.childItems.size() <= DEFAULT_SHOW_SIZE) {
            mGroupOperation.setVisibility(View.GONE);
        } else {
            mGroupOperation.setText(item.operation);
            mGroupOperation.setVisibility(View.VISIBLE);
            mGroupOperation.setTag(item);
            mGroupOperation.invalidate();
        }

    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GroupItem groupItem = new GroupItem("Group " + i);
            List<ChildItem> childItems = new ArrayList<>();
            for (int j = 0; j < i + 4; j++) {
                if (i == 0 && j == 2)
                    break;
                ChildItem item = new ChildItem("Group " + i + "-Child " + j, "My content : the " + j, groupItem);
                childItems.add(item);
            }
            groupItem.setChildItems(childItems);
            mDataList.add(groupItem);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public enum ITEM_TYPE {
        GROUP, CHILD
    }


}
