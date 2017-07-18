package lk.reversalrecyclerview;

import java.util.ArrayList;
import java.util.List;

import static lk.reversalrecyclerview.MyRecyclerViewAdapter.DEFAULT_SHOW_SIZE;


/**
 * Created by LK on 2017-7-17.
 */

public class GroupItem {
    public GroupItem(String title) {
        this.title = title;
        type = MainActivity.ITEM_TYPE.GROUP.ordinal();
        childItems = new ArrayList<>();
    }

    public void setChildItems(List<ChildItem> list) {
        childItems.addAll(list);
        isOpen = false;
        childSize = childItems.size() <= DEFAULT_SHOW_SIZE ? childItems.size() : DEFAULT_SHOW_SIZE;
        operation = "Open";
    }

    public void onClick() {
        if (isOpen) {
            childSize = DEFAULT_SHOW_SIZE;
            operation = "Open";
        } else {
            childSize = childItems.size();
            operation = "Close";
        }
        isOpen = !isOpen;
    }

    public int childSize;
    public String title;
    public boolean isOpen;
    public String operation;
    public int type;
    public List<ChildItem> childItems;

}
