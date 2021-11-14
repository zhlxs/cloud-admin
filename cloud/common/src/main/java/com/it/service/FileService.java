package com.it.service;

import com.it.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService
{

	FileInfo save(MultipartFile file) throws IOException;

	void delete(String id);

}
