package com.ustglobal.rapido.mapping.infrastructure.services;

import com.ustglobal.rapido.mapping.domain.shared.FilePersistenceException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ustglobal.rapido.mapping.application.FilePersistService;

@Service
public class FilePersistToLocalFsService implements FilePersistService {

  @Value("${rapido.file.save.dir}")
  private String fileSaveDir;

  public void save(MultipartFile[] files) {
    createOutputDirectoryIfNotExist();

    for (MultipartFile multiPartFile : files) {
      Path filePath = getNewFilePath(multiPartFile);

      try {
        multiPartFile.transferTo(filePath);
      } catch (IllegalStateException | IOException e) {
        throw new FilePersistenceException("Exception occured while saving multipart file", e);
      }
    }
  }

  private Path getNewFilePath(MultipartFile multiPartFile) {
    String pathToGet = String.join(File.separator,fileSaveDir,multiPartFile.getOriginalFilename());
    Path filePath = Paths.get(pathToGet);
    if (ObjectUtils.isEmpty(filePath)) {
      throw new FilePersistenceException(String.format("File Path %s not available.", pathToGet));
    }
    return filePath;
  }

  private void createOutputDirectoryIfNotExist() {
    File destination = new File(fileSaveDir);
    if (!destination.exists()) {
      if(!destination.mkdir()){
        throw new FilePersistenceException("Could not create output directory.");
      }
    }
  }

}
