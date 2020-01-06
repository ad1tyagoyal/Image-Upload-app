package com.example.myimagedb;

public class UploadFile
{
    private String imageName;
    private String imageUrl;

    public UploadFile()
    {
    }

    public UploadFile(String imageName, String imageUrl)
    {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
