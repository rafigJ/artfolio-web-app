package ru.vsu.cs.artfolio.mapper;

import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaMapper {
    public static List<MediaFileEntity> toEntityList(List<MultipartFile> mediaFiles) throws IOException {
        List<MediaFileEntity> entities = new ArrayList<>();
        int pos = 0;
        for (MultipartFile mediaFile : mediaFiles) {
            entities.add(MediaFileEntity.builder()
                    .file(mediaFile.getBytes())
                    .position(pos++)
                    .type(mediaFile.getContentType())
                    .build());
        }
        return entities;
    }
}
