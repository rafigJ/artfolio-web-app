package ru.vsu.cs.artfolio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    void uploadImages(List<MultipartFile> images) throws IOException;

    byte[] downloadImages(String fileName) throws IOException;
}
