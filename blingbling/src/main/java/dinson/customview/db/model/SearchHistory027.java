package dinson.customview.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated; 

@Entity
public class SearchHistory027 {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String pinyin;
    @Generated(hash = 750821718)
    public SearchHistory027(Long id, @NotNull String name, @NotNull String pinyin) {
        this.id = id;
        this.name = name;
        this.pinyin = pinyin;
    }
    @Generated(hash = 1779234741)
    public SearchHistory027() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPinyin() {
        return this.pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
   

}
