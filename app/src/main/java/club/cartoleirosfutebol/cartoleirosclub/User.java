package club.cartoleirosfutebol.cartoleirosclub;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JP on 09/03/2017.
 */

public class User {

    @SerializedName("Name")
    private String Name;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Img")
    private String Img;
    @SerializedName("Guid")
    private String Guid;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getGuid() {
        return Guid;
    }

    public void setGuid(String guid) {
        Guid = guid;
    }
}
