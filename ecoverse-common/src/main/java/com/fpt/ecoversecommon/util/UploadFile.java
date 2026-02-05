package com.fpt.ecoversecommon.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UploadFile {

    private final Cloudinary cloudinary;

    public UploadFile(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String imageToUrl(MultipartFile multipartFile) {
        try {
            Map<?,?> map = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            return map.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> imagesToUrl(List<MultipartFile> multipartFiles) {
        List<String> images = new ArrayList<>();
        try {
            for (MultipartFile file : multipartFiles) {
                Map<?,?> map = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String link = map.get("secure_url").toString();
                images.add(link);
            }
            return images;
        } catch (IOException e) {
            throw new RuntimeException("Upload image failed", e);
        }
    }
}
