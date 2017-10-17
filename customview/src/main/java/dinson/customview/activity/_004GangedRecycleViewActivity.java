package dinson.customview.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;

import dinson.customview.R;
import dinson.customview._global.BaseActivity;
import dinson.customview.adapter._004LeftAdapter;
import dinson.customview.adapter._004RightAdapter;
import dinson.customview.entity.MonsterHunter;
import dinson.customview.entity.MonsterHunter.DataBean.MonsterBean;
import dinson.customview.utils.FileUtils;
import dinson.customview.utils.GlideUtils;
import dinson.customview.utils.StringUtils;
import dinson.customview.weight.recycleview.LinearItemDecoration;

public class _004GangedRecycleViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__004_ganged_recycle_view);


        ImageView ivImg = (ImageView) findViewById(R.id.ivImg);
        GlideUtils.setImage(this, "http://ondlsj2sn.bkt.clouddn.com/FpUVRI5ACAX7YkTOXLuD7mP_3BPg.webp", ivImg);

        initUI();
    }

    private void initUI() {
        String textFromAssets = FileUtils.getTextFromAssets(this, "mh.json");
        MonsterHunter datas = new Gson().fromJson(textFromAssets, MonsterHunter.class);

        _004LeftAdapter leftAdapter = new _004LeftAdapter(this, datas.getData());

        //添加标题数据
        final ArrayList<MonsterBean> rightDatas = new ArrayList<>();
        String currentType = "";
        for (int i = 0; i < datas.getData().size(); i++) {
            if (!StringUtils.equals(currentType, datas.getData().get(i).getFamily())) {
                currentType = datas.getData().get(i).getFamily();
                rightDatas.add(new MonsterBean(currentType, true));
            }
            rightDatas.addAll(datas.getData().get(i).getMonster());
        }
        _004RightAdapter rightAdapter = new _004RightAdapter(this, rightDatas);

        RecyclerView rvLeft = (RecyclerView) findViewById(R.id.rv_left);
        RecyclerView rvRight = (RecyclerView) findViewById(R.id.rv_right);

        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridManager = new GridLayoutManager(this, 3);
        rvRight.setLayoutManager(gridManager);
        gridManager.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return rightDatas.get(position).isTitle() ? 3 : 1;
            }
        });

        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);
        rvLeft.addItemDecoration(new LinearItemDecoration(this));
    }

    @Override
    public int setWindowBackgroundColor() {
        return android.R.color.white;
    }
}
