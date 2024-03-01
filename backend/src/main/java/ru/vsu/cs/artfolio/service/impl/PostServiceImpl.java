package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;


}
