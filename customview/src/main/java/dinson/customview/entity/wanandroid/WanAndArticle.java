package dinson.customview.entity.wanandroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 玩安卓的文章实体类
 */
public class WanAndArticle implements Parcelable {


    /**
     * apkLink :
     * author : niniloveyou
     * chapterId : 331
     * chapterName : TextView
     * collect : false
     * courseId : 13
     * desc : 自定义控件 ：drawable 跟随TextView居中 The drawable follows the text centered

     * envelopePic : http://www.wanandroid.com/blogimgs/4fc0cdb8-8254-445f-a534-13d1eb51fd18.png
     * fresh : false
     * id : 2489
     * link : http://www.wanandroid.com/blog/show/2084
     * niceDate : 2018-03-16
     * origin :
     * projectLink : https://github.com/niniloveyou/DrawableTextView
     * publishTime : 1521132575000
     * superChapterId : 294
     * superChapterName : 开源项目主Tab
     * tags : [{"name":"项目","url":"/project/list/1?cid=331"}]
     * title : 开源 Textview Drawable跟随文字居中
     * type : 0
     * visible : 1
     * zan : 0
     */

    private String apkLink;
    private String author;
    private int chapterId;
    private String chapterName;
    private boolean collect;
    private int courseId;
    private String desc;
    private String envelopePic;
    private boolean fresh;
    private int id;
    private String link;
    private String niceDate;
    private String origin;
    private String projectLink;
    private long publishTime;
    private int superChapterId;
    private String superChapterName;
    private String title;
    private int type;
    private int visible;
    private int zan;
    private List<TagsBean> tags;

    protected WanAndArticle(Parcel in) {
        apkLink = in.readString();
        author = in.readString();
        chapterId = in.readInt();
        chapterName = in.readString();
        collect = in.readByte() != 0;
        courseId = in.readInt();
        desc = in.readString();
        envelopePic = in.readString();
        fresh = in.readByte() != 0;
        id = in.readInt();
        link = in.readString();
        niceDate = in.readString();
        origin = in.readString();
        projectLink = in.readString();
        publishTime = in.readLong();
        superChapterId = in.readInt();
        superChapterName = in.readString();
        title = in.readString();
        type = in.readInt();
        visible = in.readInt();
        zan = in.readInt();
        tags = in.createTypedArrayList(TagsBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apkLink);
        dest.writeString(author);
        dest.writeInt(chapterId);
        dest.writeString(chapterName);
        dest.writeByte((byte) (collect ? 1 : 0));
        dest.writeInt(courseId);
        dest.writeString(desc);
        dest.writeString(envelopePic);
        dest.writeByte((byte) (fresh ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(link);
        dest.writeString(niceDate);
        dest.writeString(origin);
        dest.writeString(projectLink);
        dest.writeLong(publishTime);
        dest.writeInt(superChapterId);
        dest.writeString(superChapterName);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeInt(visible);
        dest.writeInt(zan);
        dest.writeTypedList(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WanAndArticle> CREATOR = new Creator<WanAndArticle>() {
        @Override
        public WanAndArticle createFromParcel(Parcel in) {
            return new WanAndArticle(in);
        }

        @Override
        public WanAndArticle[] newArray(int size) {
            return new WanAndArticle[size];
        }
    };

    public String getApkLink() {
        return apkLink;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isCollect() {
        return collect;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getSuperChapterId() {
        return superChapterId;
    }

    public void setSuperChapterId(int superChapterId) {
        this.superChapterId = superChapterId;
    }

    public String getSuperChapterName() {
        return superChapterName;
    }

    public void setSuperChapterName(String superChapterName) {
        this.superChapterName = superChapterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean implements Parcelable {
        /**
         * name : 项目
         * url : /project/list/1?cid=331
         */

        private String name;
        private String url;

        protected TagsBean(Parcel in) {
            name = in.readString();
            url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TagsBean> CREATOR = new Creator<TagsBean>() {
            @Override
            public TagsBean createFromParcel(Parcel in) {
                return new TagsBean(in);
            }

            @Override
            public TagsBean[] newArray(int size) {
                return new TagsBean[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
