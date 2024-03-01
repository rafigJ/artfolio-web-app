package ru.vsu.cs.artfolio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaFileService {

    void uploadMedia(List<MultipartFile> mediaFiles) throws IOException;

    byte[] downloadMedia(Long mediaId) throws IOException;

}
