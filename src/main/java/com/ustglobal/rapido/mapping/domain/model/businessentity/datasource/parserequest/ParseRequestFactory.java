package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest;

import org.springframework.stereotype.Service;

/**
 * Factory to create parse request based on the type of import source
 */
@Service
public class ParseRequestFactory {
	public ParseRequest createParseRequest(ImportSourceType importSourceType, String dataSourceName) {
		ParseRequest request = ParseRequest.NONE;
		switch (importSourceType) {
		case DBMETA:
			request = new ParseFromDBMetaRequest(dataSourceName);
			break;
		case FILE:
			request = new ParseFromFileRequest(dataSourceName);
			break;
		}
		return request;
	}
}
