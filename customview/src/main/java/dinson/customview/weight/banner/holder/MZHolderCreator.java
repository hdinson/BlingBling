package dinson.customview.weight.banner.holder;


public interface MZHolderCreator<T extends MZViewHolder> {
    /**
     * 创建ViewHolder
     */
    public T createViewHolder();
}
