package dinson.customview.model;

import java.util.ArrayList;
import java.util.List;

import dinson.customview._global.ConstantsUtils;

/**
 * google VR 数据模型
 */
public class _009_ModelUtil {

    private static final String[] titleArr = {
        "北京故宫",
        "上海魔都",
        "广州妖都",
        "香港香江",
        "地球",
        "月球",
        "哈利法塔",
        "圣托里尼岛",
        "马尔代夫全景",
        "马尔代夫瑞喜敦岛",
        "海底世界",
        "星空与极光",
        "尼亚加拉瀑布"
    };

    private static final String[] descArr = {
        "北京故宫于明成祖永乐四年（1406年）开始建设，以南京故宫为蓝本营建，到永乐十八年（1420年）建成。它是一座长方形城池，南北长961米，东西宽753米，四面围有高10米的城墙，城外有宽52米的护城河。紫禁城内的建筑分为外朝和内廷两部分。外朝的中心为太和殿、中和殿、保和殿，统称三大殿，是国家举行大典礼的地方。内廷的中心是乾清宫、交泰殿、坤宁宫，统称后三宫，是皇帝和皇后居住的正宫。",
        "春秋战国时期，上海是楚国春申君黄歇的封邑，故别称申。四、五世纪晋朝时期，因渔民创造捕鱼工具“扈”，江流入海处称“渎”，因此松江下游一带称为“扈渎”，以后又改“沪”，故上海简称沪。唐朝置华亭县。上海是国家历史文化名城，拥有深厚的近代城市文化底蕴和众多历史古迹。江浙吴越文化与西方传入的工业文化相融合形成上海特有的海派文化。1843年后上海成为对外开放的商埠并迅速发展成为远东第一大城市。",
        "广州是广东省省会、国家中心城市、超大城市、南部战区司令部驻地。是国务院定位的国际大都市、国际商贸中心、国际综合交通枢纽、国家综合性门户城市、国家历史文化名城。从秦朝开始，广州一直是华南地区的政治、军事、经济、文化和科教中心。广州从3世纪30年代起成为海上丝绸之路的主港，唐宋时期成为中国第一大港，明清两代成为中国唯一的对外贸易大港。",
        "香港，全称为中华人民共和国香港特别行政区（HKSAR）。地处中国华南地区，珠江口以东，南海沿岸，北接广东省深圳市，西接珠江，与澳门特别行政区、珠海市以及中山市隔着珠江口相望。高度繁荣的国际大都市，全境由香港岛、九龙半岛、新界等3大区域组成，人口密度居全世界第三。1842-1997年间，香港曾为英国殖民地。二战以后，香港经济和社会迅速发展，不仅被誉为“亚洲四小龙”之一，更成为全球最富裕、经济最发达和生活水准最高的地区之一。",
        "地球（Earth）是太阳系八大行星之一，按离太阳由近及远的次序排为第三颗，也是太阳系中直径、质量和密度最大的类地行星，距离太阳1.5亿公里。地球自西向东自转，同时围绕太阳公转。现有40~46亿岁，[1]  它有一个天然卫星——月球，二者组成一个天体系统——地月系统。46亿年以前起源于原始太阳星云。",
        "月球，俗称月亮，古时又称太阴、玄兔、玉盘，是地球唯一的天然卫星，并且是太阳系中第五大的卫星。月球的直径是地球的四分之一，质量是地球的八十分之一，相对于所环绕的行星，它是质量最大的卫星，也是太阳系内密度第二高的卫星，仅次于木卫一。月球表面布满了由陨石撞击形成的环形山。月球现在与地球的距离，大约是地球直径的30倍。",
        "哈利法塔最初的原名叫迪拜塔，完工之后才改以哈利法塔为正式名称。在古阿拉伯世界中，哈利法为“伊斯兰世界最高领袖”之意，同时也是历史上阿拉伯帝国统治者的称号。高828米，楼层总数162层，造价15亿美元，共使用33万立方米混凝土、6.2万吨强化钢筋，14.2万平方米玻璃。共调用了大约4000名工人和100台起重机，把混凝土垂直泵上逾606米的地方。大厦内设有56部升降机，速度最高达17.4米/秒，另外还有双层的观光升降机，每次最多可载42人。",
        "圣托里尼岛(Santorini)古名为希拉(Thera)，后来为纪念圣·爱莲（SaintIrene），于1207年被改为圣托里尼。圣托里尼（Santorini）是在希腊大陆东南200公里的爱琴海上由一群火山组成的岛环，圣托里尼岛环上最大的一个岛也叫圣托里尼岛，别名锡拉岛（Thira）。",
        "马尔代夫全称：马尔代夫共和国位于南亚，是印度洋上的一个岛国，也是世界上最大的珊瑚岛国。由1200余个小珊瑚岛屿组成，其中202个岛屿有人居住，从空中鸟瞰就像一串珍珠撒在印度洋上。面积298平方公里（不计算领海），是亚洲最小的国家。",
        "瑞喜敦岛座纯净天然的小岛位于世界上最大及最深的环礁Caafu Alifu Atoll之中，棕榈树摇曳生姿,沙滩洁白细软。绵延无尽:珊瑚礁色彩斑谰。各种绮丽的海洋植物丛生,与周围海面交织．蔚为壮观.岛上．郁郁葱葱，清凉舒适．伊然另番景色；在这里，您可以探索自然的植物和动物亦对拜访岛屿附近的村庄,体验传统马尔代夫文化。此岛位1千米,宽120米，景色优美,举世无双,乃一座绝以桃花源,可谓美而不娇,悠然静谧,惊为仙境。居于此岛,远离尘嚣,遗世而独立。",
        "在距离我们很近，又很遥远的地方，有一个广阔的深蓝色的海底世界。在这个世界的海底，生活着无数的小鱼和大鱼，他们天真烂漫、和平友好，生活的无忧无虑。他们住着珊瑚和贝壳建造成的小屋、吃着五颜六色的海底美食。",
        "极光出现于星球的高磁纬地区上空，是一种绚丽多彩的发光现象。而地球的极光，来自地球磁层和太阳的高能带电粒子流（太阳风）使高层大气分子或原子激发（或电离）而产生。极光产生的条件有三个：大气、磁场、高能带电粒子。这三者缺一不可。极光不只在地球上出现，太阳系内的其他一些具有磁场的行星上也有极光。",
        "尼亚加拉瀑布(Niagara Falls)位于加拿大安大略省和美国纽约州的交界处，瀑布源头为尼亚加拉河，主瀑布位于加拿大境内，是瀑布的最佳观赏地；在美国境内瀑布由月亮岛隔开，观赏的是瀑布侧面。同时，该瀑布也是世界第一大跨国瀑布。尼亚加拉瀑布也直译作拉格科瀑布，“尼亚加拉”在印第安语中意为“雷神之水”，印第安人认为瀑布的轰鸣是雷神说话的声音。"
    };

    private static final String[] localArr = {
        ConstantsUtils.SDCARD_PRIVATE + "FpCHLyRU6LxkV8qD0LciJKN5oU3h.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FqCreoHlRPVcwdNuyFSrLwYuF9wI.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "Fg8vhnZAkC9ITYCXTppsgzvNWtBW.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FnxcAXag_6QchrQENPSClfN6BUaV.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FgbAq1Qz8rnMzXJOn-cXQt6XLyeZ.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FqK9Tjpd6Q88HrtHuS7HFA66CStO.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FhKebFIN8NU4Im3JoX9bR5K47eWC.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FgCjnCZJ0UQPRvYw-MfX3nKBiQMe.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FvZG8R_rYVsZJj88meyFELWS5D7_.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "Fo4p7zd-lO5pDZh1SkHLOcyjb0iK.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FovJM3t22TeURDiCdM41aG3-i_LW.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "Fnr-FHQoaoXmIu4XJ-8Rogr5u_76.jpg",
        ConstantsUtils.SDCARD_PRIVATE + "FnRKaCfPjrnDwWQWkqF8MFJDCTca.jpg"
    };
    private static final String[] urlArr = {
        "http://ondlsj2sn.bkt.clouddn.com/FpCHLyRU6LxkV8qD0LciJKN5oU3h.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FqCreoHlRPVcwdNuyFSrLwYuF9wI.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/Fg8vhnZAkC9ITYCXTppsgzvNWtBW.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FnxcAXag_6QchrQENPSClfN6BUaV.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FgbAq1Qz8rnMzXJOn-cXQt6XLyeZ.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FqK9Tjpd6Q88HrtHuS7HFA66CStO.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FhKebFIN8NU4Im3JoX9bR5K47eWC.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FgCjnCZJ0UQPRvYw-MfX3nKBiQMe.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FvZG8R_rYVsZJj88meyFELWS5D7_.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/Fo4p7zd-lO5pDZh1SkHLOcyjb0iK.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FovJM3t22TeURDiCdM41aG3-i_LW.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/Fnr-FHQoaoXmIu4XJ-8Rogr5u_76.jpg",
        "http://ondlsj2sn.bkt.clouddn.com/FnRKaCfPjrnDwWQWkqF8MFJDCTca.jpg"
    };

    public static List<_009PanoramaImageModel> getPanoramaImageList() {
        List<_009PanoramaImageModel> list = new ArrayList<>();
        for (int i = 0; i < titleArr.length; i++) {
            list.add(new _009PanoramaImageModel(0, titleArr[i], descArr[i], localArr[i], urlArr[i]));
        }
        return list;
    }

}
