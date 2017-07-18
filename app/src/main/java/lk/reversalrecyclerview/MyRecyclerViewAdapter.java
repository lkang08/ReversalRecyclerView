package lk.reversalrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LK on 2017-7-17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static  final int DEFAULT_SHOW_SIZE = 3;
    private List<GroupItem> datas;
    private Context mContext;

    public interface GroupOnItemClickListener {
        void onItemClick(View view, int position);
    }

    private GroupOnItemClickListener mGroupOnItemClickListener;
    private MyDecoration myDecoration;

    public MyDecoration getMyDecoration() {
        return myDecoration;
    }

    public MyRecyclerViewAdapter(Context context) {
        this.mContext = context;
        myDecoration = new MyDecoration();
        mGroupOnItemClickListener = new GroupOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GroupItem item = getGroupItem(position);
                item.onClick();
                notifyDataSetChanged();
            }
        };
    }

    public void setDatas(List<GroupItem> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MainActivity.ITEM_TYPE.GROUP.ordinal()) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_group_group, parent, false);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGroupOnItemClickListener != null) {
                        mGroupOnItemClickListener.onItemClick(v, (Integer) v.getTag());
                    }
                }
            };
            return new GroupViewHolder(view, listener);
        } else if (viewType == MainActivity.ITEM_TYPE.CHILD.ordinal()) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_group_item, parent, false);
            return new ChildViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder) {
            ((GroupViewHolder) holder).bindData(position);
        } else {
            ((ChildViewHolder) holder).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int index = 0;
        MainActivity.ITEM_TYPE type = MainActivity.ITEM_TYPE.GROUP;
        for (GroupItem item : datas) {
            if (index == position) {
                type = MainActivity.ITEM_TYPE.GROUP;
                break;
            }
            index++;
            if (position < index + item.childSize) {
                type = MainActivity.ITEM_TYPE.CHILD;
                break;
            }
            index += item.childSize;
        }
        return type.ordinal();
    }

    public ChildItem getChildItem(int position) {
        int index = 0;
        int groupIndex = 0;
        MainActivity.ITEM_TYPE type = MainActivity.ITEM_TYPE.GROUP;
        ChildItem result = null;
        for (GroupItem item : datas) {
            if (position <= index + item.childSize) {
                result = item.childItems.get(position - index - 1);
                break;
            }
            index++;
            groupIndex++;
            index += item.childSize;
        }
        return result;
    }

    public GroupItem getGroupItem(int position) {
        int index = 0;
        int groupIndex = 0;
        MainActivity.ITEM_TYPE type = MainActivity.ITEM_TYPE.GROUP;
        GroupItem result;
        for (GroupItem item : datas) {
            if (index == position) {
                result = datas.get(groupIndex);
                break;
            }
            index++;
            groupIndex++;
            index += item.childSize;
        }
        return datas.get(groupIndex);
    }

    @Override
    public int getItemCount() {
        if (datas.size() == 0)
            return 0;
        int totalCount = 0;
        for (GroupItem item : datas) {
            totalCount++;
            totalCount += item.childSize;
        }
        return totalCount;
    }

    class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int type = getItemViewType(position);
            if (position == 0)
                return;
            if (type == MainActivity.ITEM_TYPE.GROUP.ordinal()) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 1, 0, 0);
            }
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView operation;
        private View layout;

        public GroupViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            operation = (TextView) itemView.findViewById(R.id.operation);
            layout = itemView.findViewById(R.id.group_layout);
            operation.setOnClickListener(listener);
        }

        public void bindData(final int position) {
            GroupItem item = getGroupItem(position);
            operation.setTag(position);
            title.setText(item.title);
            if (position > 0)
                layout.setContentDescription(String.valueOf(position));
            if (item.childItems.size() <= DEFAULT_SHOW_SIZE) {
                operation.setText("Open");
                operation.setVisibility(View.INVISIBLE);
            } else if (item.isOpen) {
                operation.setText("Close");
                operation.setVisibility(View.VISIBLE);
            } else {
                operation.setText("Open");
                operation.setVisibility(View.VISIBLE);
            }
            operation.setText(item.operation);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;

        public ChildViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }

        void bindData(final int position) {
            ChildItem item = getChildItem(position);
            title.setText(item.title);
            content.setText(item.content);
        }
    }
}
