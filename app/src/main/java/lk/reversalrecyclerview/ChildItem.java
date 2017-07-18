package lk.reversalrecyclerview;


/**
 * Created by LK on 2017-7-17.
 */

public class ChildItem {
    public String title;
    public String content;
    public int type;
    public GroupItem father;

    public ChildItem(String title, String content,GroupItem father) {
        this.title = title;
        this.content = content;
        type = MainActivity.ITEM_TYPE.CHILD.ordinal();
        this.father = father;
    }
}
