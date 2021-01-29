package com.ustglobal.rapido.mapping.infrastructure.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db.DatabaseQueryService;

@Service
public class JDBCDatabaseQueryRestService implements DatabaseQueryService {

	private static final String RAPIDO_CORRELATION_ID = "rapidoCorrelationId";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${connector.service.baseurl}")
	private String connectorBaseUrl;

	@Override
	public Collection<String> getColumnsForTable(String schema, String tableName, String dbConfigId) {
		HttpHeaders headers = addDefaultHeaders();
		Map<String, Object> params = new HashMap<>();
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
		String fecthTableFieldsURL = String.join("/%s", connectorBaseUrl, "/schemas", "/tables", "/columns");
		String requestURL = String.format(fecthTableFieldsURL, dbConfigId,
				StringUtils.isEmpty(schema) ? "none" : schema, tableName);
		ResponseEntity<List<String>> response = restTemplate.exchange(requestURL, HttpMethod.GET, request,
				new ParameterizedTypeReference<List<String>>() {
				});
		return response.getBody();
	}

	private HttpHeaders addDefaultHeaders() {
		String rapidoCorrelationId = MDC.get(RAPIDO_CORRELATION_ID);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(RAPIDO_CORRELATION_ID, rapidoCorrelationId);
		return headers;
	}

}
