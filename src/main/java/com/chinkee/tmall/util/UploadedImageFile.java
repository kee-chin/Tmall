package com.chinkee.tmall.util;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {
    MultipartFile image; // MultipartFile 类型的属性，用于接受上传文件的注入
    // <input id="categoryPic" accept="image/*" type="file" name="image" />
    // 属性名称image必须和页面中的增加分类部分中的type="file"的name值保持一致。

    public void setImage(MultipartFile image){
        this.image = image;
    }

    public MultipartFile getImage(){
        return image;
    }
}
