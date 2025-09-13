package com.job.jportal.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {
    private final Path root = Paths.get("uploads");

    public FileService() throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);
    }

    public String store(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) throw new IOException("Invalid file path");
        Path target = root.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public Path load(String filename) {
        return root.resolve(filename);
    }
}
