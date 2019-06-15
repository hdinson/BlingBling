package dinson.customview.event;

public class _004CheckSelectorAllEvent {

    public _004CheckSelectorAllEvent(boolean isSelectAll, int selectorCount) {
        this.isSelectAll = isSelectAll;
        this.selectorCount = selectorCount;
    }

    private boolean isSelectAll;
    private int selectorCount;

    public int getSelectorCount() {
        return selectorCount;
    }

    public boolean isSelectAll() {
        return isSelectAll;
    }
}
