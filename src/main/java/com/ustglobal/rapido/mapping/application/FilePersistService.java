package com.ustglobal.rapido.mapping.application;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service to do operations associated with file
 *
 */
public interface FilePersistService {
	
	public void save(MultipartFile[] files);

}
