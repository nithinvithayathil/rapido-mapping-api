package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest;

public class ParseFromFileRequest extends ParseRequest {

  /**
   * A master file path in case there are multiple files to be parsed and a root has to be identified.
   */
	private String masterFilePath;

	public ParseFromFileRequest(String dataSourceName) {
		super(dataSourceName);
	}

	public String getMasterFilePath() {
		return masterFilePath;
	}

	public void setMasterFilePath(String masterFilePath) {
		this.masterFilePath = masterFilePath;
	}
}
