package dinson.customview.entity.gank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GankToday {

    @Override
    public String toString() {
        return "GankToday{" +
            "error=" + error +
            ", results='" + results + '\'' +
            ", category=" + category +
            '}';
    }

    /**
     * category : ["Android","拓展资源","App","瞎推荐","iOS","休息视频","福利"]
     * error : false
     * results : {"Android":[{"_id":"5b723c099d212275a00706be","createdAt":"2018-08-14T10:18:49.521Z","desc":"自定义View组织机构图 和层次图","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hlsb0nj30a00hsmyd","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hlxnbkj30a00hs75c"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/onlyloveyd/LazyOrgView","used":true,"who":"艾米"},{"_id":"5b7a36ef9d212201f4e1745a","createdAt":"2018-08-20T11:35:11.294Z","desc":"今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hm31eqj30lc0zk3z1","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hm7eh7j31401z4whv","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hmf54aj30u01hcaci","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hmlz9ej314028077t"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/JessYanCoding/AndroidAutoSize","used":true,"who":"jess"},{"_id":"5b7a3ff59d212201f707dd86","createdAt":"2018-08-20T12:13:41.989Z","desc":"EditDrawableText - An EditText which makes your Drawable Clickable","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hmxudyj30u01hcjsy","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hn48nmj30hs0zk0tc","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hn7rduj30hs0zkjrz","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hnct97j30u01hcwg0"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/MindorksOpenSource/EditDrawableText","used":true,"who":"AMIT SHEKHAR"},{"_id":"5b7a41c19d212201e982de6c","createdAt":"2018-08-20T12:21:21.210Z","desc":"卷尺系列控件，包含：普通卷尺（如：体重）、金额尺、时间尺","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hnlffxg30bo04tgzn","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hnrpaxg30bo05t7ig","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hnym65g30bo0aqwuv"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/zjun615/RuleView","used":true,"who":"番茄你个马铃薯"},{"_id":"5b7b87b39d212201f4e17460","createdAt":"2018-08-21T11:32:03.752Z","desc":"后端接口和文档自动化，前端(客户端) 定制返回JSON的数据和结构！","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hqvizkg307i0dc1l0","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hrk0f2g307i0dc1kz","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hs6v73g307i0dcu0y"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/TommyLemon/APIJSON","used":true,"who":"lijinshanmx"},{"_id":"5b7b87e39d212201f4e17461","createdAt":"2018-08-21T11:32:51.78Z","desc":"AndroidTreeView。 用于android的TreeView实现。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hsf4psj30u01hctcc","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hskuu6j30e20p0tbc","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hsozmzj30e20p0wfp"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/bmelnychuk/AndroidTreeView","used":true,"who":"lijinshanmx"}],"App":[{"_id":"5b7a3af49d212201f707dd85","createdAt":"2018-08-21T11:46:45.89Z","desc":"安卓版2048小游戏。","publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"App","url":"https://github.com/tpcstld/2048","used":true,"who":"夜尽天明"}],"iOS":[{"_id":"5b7a7ed19d212201f4e1745d","createdAt":"2018-08-21T11:10:47.247Z","desc":"一个slide-modeled的UI导航控制器。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hpwd7jg30oq0im4qw"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"iOS","url":"https://github.com/Ramotion/navigation-toolbar","used":true,"who":"Alex Mikhnev"},{"_id":"5b7b88eb9d212201ef03235b","createdAt":"2018-08-21T11:37:15.53Z","desc":"QMUI iOS\u2014\u2014致力于提高项目 UI 开发效率的解决方案。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hsthtzj30sg0sgjup"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"iOS","url":"https://github.com/QMUI/QMUI_iOS","used":true,"who":"lijinshanmx"},{"_id":"5b7b896d9d212201f707dd8b","createdAt":"2018-08-21T11:39:25.270Z","desc":"极其精美而又强大的 iOS 图表组件库,支持柱状图、条形图、折线图、等等图表","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6ht5tc7j30k00xa465","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6htykujj30qk1a8wn7","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hu5614j30ou16s134","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hudgrdj30qk1a87ed","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6huxt4ng30900fjx6p"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"iOS","url":"https://github.com/AAChartModel/AAChartKit","used":true,"who":"lijinshanmx"},{"_id":"5b7b89b49d212201e982de75","createdAt":"2018-08-21T11:40:36.447Z","desc":"开屏广告、启动广告解决方案-支持静态/动态图片广告,mp4视频广告,全屏/半屏广告、兼容iPhone/iPad.","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hvmvieg306y0cg7wh","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hvu2h1g306y0cgnhl","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hw12frg306y0cgk4r","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hw6eohg306y0cgjyt","https://ww1.sinaimg.cn/large/0073sXn7ly1fuh6hwe9z9g306y0cg13x"],"publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"iOS","url":"https://github.com/CoderZhuXH/XHLaunchAd","used":true,"who":"lijinshanmx"}],"休息视频":[{"_id":"5b7b834d9d212201f707dd89","createdAt":"2018-08-21T11:13:17.960Z","desc":"这三只，真心可爱😄是不是三胞胎呀？","publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"休息视频","url":"http://v.douyin.com/eE11wE/","used":true,"who":"lijinshanmx"}],"拓展资源":[{"_id":"5b727e599d212275a00706c0","createdAt":"2018-08-21T11:08:51.720Z","desc":"关于如何在 Android 开发中封装业务流程的经验分享","publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"拓展资源","url":"https://juejin.im/post/5b0a7088f265da0db721cf73","used":true,"who":"Prototype Z"},{"_id":"5b727e969d212275a78c26f5","createdAt":"2018-08-21T11:08:47.968Z","desc":"关于如何在 Android 开发中封装业务流程的经验分享，第二篇","publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"拓展资源","url":"https://juejin.im/post/5b6ede81f265da0f9c67d1c2","used":true,"who":"Prototype Z"},{"_id":"5b74ef5e9d21222c56644931","createdAt":"2018-08-21T11:41:33.653Z","desc":"详细讲解EventBus 3.0 源码，不容错过~","publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"拓展资源","url":"https://mp.weixin.qq.com/s/pZAbNe1pE1_JUUv4j9KAow","used":true,"who":"Aaron"}],"瞎推荐":[{"_id":"5b7a68879d212201ef032357","createdAt":"2018-08-20T15:06:47.62Z","desc":"灭霸命令。 可以随机删除您一半的文件。","publishedAt":"2018-08-21T00:00:00.0Z","source":"chrome","type":"瞎推荐","url":"https://github.com/hotvulcan/Thanos.sh","used":true,"who":"galois"}],"福利":[{"_id":"5b7b836c9d212201e982de6e","createdAt":"2018-08-21T11:13:48.989Z","desc":"2018-08-21","publishedAt":"2018-08-21T00:00:00.0Z","source":"web","type":"福利","url":"https://ws1.sinaimg.cn/large/0065oQSqly1fuh5fsvlqcj30sg10onjk.jpg","used":true,"who":"lijinshanmx"}]}
     */

    private boolean error;
    private Map<String, ArrayList<ProjectsInfo>> results;
    private List<String> category;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Map<String, ArrayList<ProjectsInfo>> getResults() {
        return results;
    }

    public void setResults(Map<String, ArrayList<ProjectsInfo>> results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
