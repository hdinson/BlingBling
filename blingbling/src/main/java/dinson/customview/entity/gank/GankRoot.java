package dinson.customview.entity.gank;


public class GankRoot<T> {

    /**
     * error : false
     * results : [{"_id":"5b74e9409d21222c52ae4cb4","createdAt":"2018-08-16T11:02:24.289Z","desc":"2018-08-16","publishedAt":"2018-08-16T00:00:00.0Z","source":"api","type":"福利","url":"https://ws1.sinaimg.cn/large/0065oQSqly1fubd0blrbuj30ia0qp0yi.jpg","used":true,"who":"lijinshan"},{"_id":"5b7102749d2122341d563844","createdAt":"2018-08-13T12:00:52.458Z","desc":"2018-08-13","publishedAt":"2018-08-13T00:00:00.0Z","source":"api","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqly1fu7xueh1gbj30hs0uwtgb.jpg","used":true,"who":"lijinshan"},{"_id":"5b6bad449d21226f45755582","createdAt":"2018-08-09T10:56:04.962Z","desc":"2018-08-09","publishedAt":"2018-08-09T00:00:00.0Z","source":"web","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqgy1fu39hosiwoj30j60qyq96.jpg","used":true,"who":"lijinshanmx"},{"_id":"5b67b7fd9d2122195bdbd806","createdAt":"2018-08-06T10:52:45.809Z","desc":"2018-08-06","publishedAt":"2018-08-06T00:00:00.0Z","source":"api","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqly1ftzsj15hgvj30sg15hkbw.jpg","used":true,"who":"lijinshan"},{"_id":"5b63cd4e9d21225e0d3f58c9","createdAt":"2018-08-03T11:34:38.672Z","desc":"2018-08-03","publishedAt":"2018-08-03T00:00:00.0Z","source":"api","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqgy1ftwcw4f4a5j30sg10j1g9.jpg","used":true,"who":"lijinshan"},{"_id":"5b6151509d21225206860f08","createdAt":"2018-08-01T14:21:04.556Z","desc":"2018-08-01","publishedAt":"2018-08-01T00:00:00.0Z","source":"api","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqly1ftu6gl83ewj30k80tites.jpg","used":true,"who":"lijinshan"},{"_id":"5b60356a9d212247776a2e0e","createdAt":"2018-07-31T18:09:46.825Z","desc":"2018-07-31","publishedAt":"2018-07-31T00:00:00.0Z","source":"api","type":"福利","url":"http://ww1.sinaimg.cn/large/0065oQSqgy1ftt7g8ntdyj30j60op7dq.jpg","used":true,"who":"lijinshan"},{"_id":"5b5e93499d21220fc64181a9","createdAt":"2018-07-30T12:25:45.937Z","desc":"2018-07-30","publishedAt":"2018-07-30T00:00:00.0Z","source":"web","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqgy1ftrrvwjqikj30go0rtn2i.jpg","used":true,"who":"lijinshanmx"},{"_id":"5b50107f421aa917a31c0565","createdAt":"2018-07-19T12:15:59.226Z","desc":"2018-07-19","publishedAt":"2018-07-19T00:00:00.0Z","source":"web","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg","used":true,"who":"lijinshanmx"},{"_id":"5b4eaae4421aa93aa99bee16","createdAt":"2018-07-18T11:14:55.648Z","desc":"2018-07-18","publishedAt":"2018-07-18T00:00:00.0Z","source":"web","type":"福利","url":"https://ww1.sinaimg.cn/large/0065oQSqly1ftdtot8zd3j30ju0pt137.jpg","used":true,"who":"lijinshanmx"}]
     */

    private boolean error;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
