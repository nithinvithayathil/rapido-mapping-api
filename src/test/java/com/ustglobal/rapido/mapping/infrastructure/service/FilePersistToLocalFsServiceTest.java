package com.ustglobal.rapido.mapping.infrastructure.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ustglobal.rapido.mapping.infrastructure.services.FilePersistToLocalFsService;

@RunWith(MockitoJUnitRunner.class)
public class FilePersistToLocalFsServiceTest {

	@Test
	public void saveMultiPartFile() throws IOException {
		FilePersistToLocalFsService filePersistToLocalFsService = new FilePersistToLocalFsService();
		FileInputStream inputFile = new FileInputStream(
				ResourceUtils.getFile(this.getClass().getResource("/Sample.xsd")).getAbsolutePath());
		MultipartFile[] multipartFiles = new MultipartFile[1];
		MockMultipartFile file = new MockMultipartFile("file", "Sample.xsd", "multipart/form-data", inputFile);
		multipartFiles[0] = file;
		ReflectionTestUtils.setField(filePersistToLocalFsService, "fileSaveDir", "rapido-saved-files");
		filePersistToLocalFsService.save(multipartFiles);
		FileUtils.forceDelete(new File("rapido-saved-files"));
	}

}
