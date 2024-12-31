package dinson.customview.utils.haval;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public class SSOLoginEncryptionDto {
    @SerializedName("account")
    @Nullable
    private String account;


    @SerializedName("password")
    @Nullable
    private String password;


    @SerializedName("pushId")
    @Nullable
    private String pushId;

    @SerializedName("pushKey")
    @Nullable
    private String pushKey;

    @SerializedName("timestamp")
    @Nullable
    private String timestamp;


    public final void setAccount(@Nullable String str) {
        this.account = str;
    }


    public final void setPassword(@Nullable String str) {
        this.password = str;
    }


    public final void setPushId(@Nullable String str) {
        this.pushId = str;
    }

    public final void setPushKey(@Nullable String str) {
        this.pushKey = str;
    }

    public final void setTimestamp(@Nullable String str) {
        this.timestamp = str;
    }


}
