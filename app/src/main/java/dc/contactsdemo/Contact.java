package dc.contactsdemo;

import com.google.gson.annotations.SerializedName;

public class Contact {
    @SerializedName("id_user")
    String id;
    String name;
    @SerializedName("nomer_user")
    String phone;
    @SerializedName("data_user")
    private String birthday;
    @SerializedName("user_url_photo")
    private String photo;
    private boolean tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public boolean isTag() {
        return tag;
    }

    public boolean getTag() {
        return tag;
    }
}
