package ru.vsu.cs.artfolio.service;

import ru.vsu.cs.artfolio.entity.MediaFileEntity;

public interface MediaService {

    MediaFileEntity downloadMedia(Long mediaId);
}
