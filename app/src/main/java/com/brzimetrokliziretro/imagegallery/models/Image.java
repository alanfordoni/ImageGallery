package com.brzimetrokliziretro.imagegallery.models;

public class Image {
    private String load_image_url;
    private String image_name;
    private String external_url;

    public Image(String load_image_url, String image_name, String external_url) {
        this.load_image_url = load_image_url;
        this.image_name = image_name;
        this.external_url = external_url;
    }

    public String getLoad_image_url() {
        return load_image_url;
    }

    public void setLoad_image_url(String load_image_url) {
        this.load_image_url = load_image_url;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getExternal_url() {
        return external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }
}
