package com.dhamova.fvr.DTO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JuanCruz on 2/19/17.
 */

public class News {

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("text_content")
    private String text_content;


    public News(String title, String text_content, String image_url) {

        setTitle(title);
        setImage_url(image_url);
        setText_content(text_content);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }
}
