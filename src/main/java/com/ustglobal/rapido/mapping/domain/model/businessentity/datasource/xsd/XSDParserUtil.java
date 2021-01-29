package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

@SuppressWarnings("restriction")
@Component
public class XSDParserUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(XSDParserUtil.class);

	/**
	 * This method identifies the root element of xsd file.
	 * 
	 * @param filePath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static String getRootElementName(String filePath)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new File(filePath));
		NodeList elementList = document.getElementsByTagNameNS("*", "element");
		Element element = (Element) elementList.item(0);
		NamedNodeMap node = element.getAttributes();
		if (ObjectUtils.isEmpty(node)) {
			throw new XSDParseException("Error while retrieving root element.");
		}
		return node.item(0).getNodeValue();
	}

	/**
	 * Recursive method which helps to add the field definition for the supplied
	 * datasource.
	 * 
	 * @param elementInfos
	 * @param typeInfos
	 * @param rootElementName
	 * @param childElementName
	 * @param xpath
	 * @param dataSourceDefinition
	 * @param dataSourceFactory
	 * @return
	 */
	public DataSourceDefinition addFieldDefinitionsToDataSourceFromParsedJSFile(List<Object> elementInfos,
			List<Object> typeInfos, String rootElementName, String childElementName, String xpath,
			DataSourceDefinition dataSourceDefinition, DataSourceFactory dataSourceFactory) {
		if (!StringUtils.isEmpty(rootElementName)) {
			return addRootElementDefinitionsToDatasourceFromElementInfos(elementInfos, typeInfos, rootElementName,
					xpath, dataSourceDefinition, dataSourceFactory);
		} else if (!StringUtils.isEmpty(childElementName)) {
			addElementFieldDefinitionToDataSourceFromTypeInfos(elementInfos, typeInfos, childElementName, xpath,
					dataSourceDefinition, dataSourceFactory);
		}
		return dataSourceDefinition;

	}

	/**
	 * Add child element datasource field definition extracted from parsed js file
	 * typeInfos
	 * 
	 * @param elementInfos
	 * @param typeInfos
	 *            - Having the child elements informations
	 * @param elementTypeName
	 * @param xpath
	 * @param dataSourceDefinition
	 * @param dataSourceFactory
	 */
	@SuppressWarnings("unchecked")
	private void addElementFieldDefinitionToDataSourceFromTypeInfos(List<Object> elementInfos, List<Object> typeInfos,
			String elementTypeName, String xpath, DataSourceDefinition dataSourceDefinition,
			DataSourceFactory dataSourceFactory) {
		Optional<Object> optionalTypeInfo = typeInfos.stream().filter(e -> isLocalNameEqual(e, elementTypeName))
				.findFirst();
		if (optionalTypeInfo.isPresent()) {
			Map<String, Object> matchingTypeInfoObject = (Map<String, Object>) optionalTypeInfo.get();
			List<Object> propertyInfos = (List<Object>) matchingTypeInfoObject.get("propertyInfos");
			if (!CollectionUtils.isEmpty(propertyInfos)) {
				for (Object propertInfo : propertyInfos) {
					Map<String, Object> propertInfoMap = (Map<String, Object>) propertInfo;
					if (propertInfoMap.containsKey("elementTypeInfos")) {
						for (Object elementTypeInfo : (List<Object>) propertInfoMap.get("elementTypeInfos")) {
							Map<String, Object> elementInfoMap = (Map<String, Object>) elementTypeInfo;
							String elementTypeInfoName = extratctElementName(elementInfoMap);
							xpath = buildFieldXpath(xpath, elementTypeInfoName);
							XSDDataFieldDefinition xsdDataFieldDefinition = createXSDDataFieldDefinition(
									elementTypeInfoName, xpath, dataSourceDefinition, dataSourceFactory);
							dataSourceDefinition.addField(xsdDataFieldDefinition);
							String elementTypeInfoType = getType((String) elementInfoMap.get("typeInfo"));
							if (!StringUtils.isEmpty(elementTypeInfoType)
									&& !XSDDataTypeValidator.isDefaultType(elementTypeInfoType)) {
								addFieldDefinitionsToDataSourceFromParsedJSFile(elementInfos, typeInfos, null,
										elementTypeInfoType, xpath, dataSourceDefinition, dataSourceFactory);
							}
							xpath = resetXpath(xpath);
						}
					} else {
						String propertyInfoName = extratctElementName(propertInfoMap);
						xpath = buildFieldXpath(xpath, propertyInfoName);
						String propertyInfoType = getType((String) propertInfoMap.get("typeInfo"));
						XSDDataFieldDefinition xsdDataFieldDefinition = createXSDDataFieldDefinition(propertyInfoName,
								xpath, dataSourceDefinition, dataSourceFactory);
						dataSourceDefinition.addField(xsdDataFieldDefinition);
						if (!StringUtils.isEmpty(propertyInfoType)
								&& !XSDDataTypeValidator.isDefaultType(propertyInfoType)) {
							addFieldDefinitionsToDataSourceFromParsedJSFile(elementInfos, typeInfos, null,
									propertyInfoType, xpath, dataSourceDefinition, dataSourceFactory);
						}
						xpath = resetXpath(xpath);
					}
				}
			} else {
				String localName = (String) matchingTypeInfoObject.get("localName");
				String baseTypeInfo = getType((String) matchingTypeInfoObject.get("baseTypeInfo"));
				xpath = buildFieldXpath(xpath, localName);
				XSDDataFieldDefinition xsdDataFieldDefinition = createXSDDataFieldDefinition(localName, xpath,
						dataSourceDefinition, dataSourceFactory);
				dataSourceDefinition.addField(xsdDataFieldDefinition);
				if (!StringUtils.isEmpty(baseTypeInfo) && !XSDDataTypeValidator.isDefaultType(baseTypeInfo)) {
					addFieldDefinitionsToDataSourceFromParsedJSFile(elementInfos, typeInfos, null, baseTypeInfo, xpath,
							dataSourceDefinition, dataSourceFactory);
				}
				xpath = resetXpath(xpath);
			}
		}
	}

	/**
	 * Add root element datasource field definition extracted from parsed js file
	 * elementInfos
	 * 
	 * @param elementInfos
	 * @param typeInfos
	 * @param rootElementName
	 * @param xpath
	 * @param dataSourceDefinition
	 * @param dataSourceFactory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataSourceDefinition addRootElementDefinitionsToDatasourceFromElementInfos(List<Object> elementInfos,
			List<Object> typeInfos, String rootElementName, String xpath, DataSourceDefinition dataSourceDefinition,
			DataSourceFactory dataSourceFactory) {
		Optional<Object> optionalRootElement = elementInfos.stream().filter(e -> isElementNameEqual(e, rootElementName))
				.findFirst();
		if (optionalRootElement.isPresent()) {
			Map<String, String> elementInfoFromJSFile = (Map<String, String>) optionalRootElement.get();
			xpath = buildFieldXpath(xpath, rootElementName);
			dataSourceDefinition.addField(
					createXSDDataFieldDefinition(rootElementName, xpath, dataSourceDefinition, dataSourceFactory));
			String type = getType(elementInfoFromJSFile.get("typeInfo"));
			if (!StringUtils.isEmpty(type) && !XSDDataTypeValidator.isDefaultType(type)) {
				addFieldDefinitionsToDataSourceFromParsedJSFile(elementInfos, typeInfos, null, type, xpath,
						dataSourceDefinition, dataSourceFactory);
			}
		}
		return dataSourceDefinition;
	}

	@SuppressWarnings("unchecked")
	private String extratctElementName(Map<String, Object> xsdElementMap) {
		String elementName;
		if (!ObjectUtils.isEmpty(xsdElementMap.get("elementName"))) {
			if (xsdElementMap.get("elementName") instanceof String) {
				elementName = (String) xsdElementMap.get("elementName");
			} else {
				Map<String, Object> elementMap = (Map<String, Object>) xsdElementMap.get("elementName");
				elementName = (String) elementMap.get("localPart");
			}
		} else {
			elementName = (String) xsdElementMap.get("name");
		}
		return elementName;
	}

	/**
	 * This method create XSD data field definition
	 * 
	 * @param elementName
	 * @param xpath
	 * @param dataSourceDefinition
	 * @return
	 */
	private XSDDataFieldDefinition createXSDDataFieldDefinition(String elementName, String xpath,
			DataSourceDefinition dataSourceDefinition, DataSourceFactory dataSourceFactory) {
		return (XSDDataFieldDefinition) dataSourceFactory
				.createDataFieldDefinition(createDataFieldDefinitionRequest(elementName, dataSourceDefinition, xpath));
	}

	/**
	 * This method creates new DataFieldDefinitionRequest
	 * 
	 * @param elementName
	 * @param dataSourceDefinition
	 * @return
	 */
	private CreateDataFieldDefinitionRequest createDataFieldDefinitionRequest(String elementName,
			DataSourceDefinition dataSourceDefinition, String xpath) {
		CreateDataFieldDefinitionRequest dataFieldDefinitionRequest = new CreateDataFieldDefinitionRequest(
				new FieldDefinitionId(UUID.randomUUID().toString(), dataSourceDefinition.getSourceDefinitionId()),
				elementName);
		dataFieldDefinitionRequest.setXpath(xpath);
		return dataFieldDefinitionRequest;
	}

	/**
	 * This method validates the element name matches with rootNode name
	 * 
	 * @param object
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isElementNameEqual(Object object, String elementName) {
		Map<String, Object> elementInfo = (Map<String, Object>) object;
		if (elementInfo.get("elementName") instanceof String) {
			return elementName.equalsIgnoreCase((String) elementInfo.get("elementName"));
		} else {
			Map<String, Object> rootNameElementInfo = (Map<String, Object>) elementInfo.get("elementName");
			return elementName.equalsIgnoreCase((String) rootNameElementInfo.get("localPart"));
		}
	}

	/**
	 * This method validates the element type name matches with local name
	 * 
	 * @param object
	 * @param elementTypeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isLocalNameEqual(Object object, String elementTypeName) {
		Map<String, String> typeInfos = (Map<String, String>) object;
		return elementTypeName.equalsIgnoreCase(typeInfos.get("localName"));

	}

	/**
	 * This method is used to build the xpath for leaf nodes
	 * 
	 * @param xpath
	 * @param fieldName
	 * @return
	 */
	private String buildFieldXpath(String xpath, String fieldName) {
		return StringUtils.isEmpty(xpath) ? "/" + fieldName : xpath + "/" + fieldName;
	}

	private String resetXpath(String xpath) {
		return xpath.substring(0, xpath.lastIndexOf('/'));
	}

	public static String getType(String type) {
		if (XSDDataTypeValidator.isDefaultType(type)) {
			return StringUtils.isEmpty(type) ? "String" : type;
		} else {
			return StringUtils.isEmpty(type) ? "String" : type.substring(1);
		}

	}

	/**
	 * This method gives the value of scriptobject mirror as Map
	 * 
	 * @param object
	 * @return
	 */
	public static Object getScriptObjectMirrorValue(Object object) {
		if (object instanceof ScriptObjectMirror) {
			ScriptObjectMirror mirror = (ScriptObjectMirror) object;
			if (mirror.isFunction()) {
				return object.toString();
			} else if (mirror.isArray()) {
				return convertScriptObjectToList(mirror);
			} else {
				return convertScriptObjectToMap(mirror);
			}
		}
		return object;
	}

	/**
	 * This method convert the ScriptObjectMirror node to Hashmap
	 * 
	 * @param node
	 * @return
	 */
	public static Map<String, Object> convertScriptObjectToMap(ScriptObjectMirror node) {
		Map<String, Object> resultMap = new HashMap<>();
		for (Entry<String, Object> entry : node.entrySet()) {
			Object entryValue = entry.getValue();
			if (entryValue instanceof ScriptObjectMirror) {
				ScriptObjectMirror tempValue = (ScriptObjectMirror) entryValue;
				if (tempValue.isArray()) {
					resultMap.put(entry.getKey(), convertScriptObjectToList(tempValue));
				} else {
					resultMap.put(entry.getKey(), convertScriptObjectToMap(tempValue));
				}
			} else if (entryValue instanceof String) {
				resultMap.put(entry.getKey(), (String) entry.getValue());
			} else if (entryValue instanceof Integer) {
				resultMap.put(entry.getKey(), (Integer) entry.getValue());
			} else {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		return resultMap;
	}

	/**
	 * This method convert the ScriptObjectMirror node to ArrayList
	 * 
	 * @param node
	 * @return
	 */
	public static List<Object> convertScriptObjectToList(ScriptObjectMirror node) {
		List<Object> resultList = new ArrayList<>();
		for (Object each : node.values()) {
			if (each instanceof ScriptObjectMirror) {
				ScriptObjectMirror tempValue = (ScriptObjectMirror) each;
				if (tempValue.isArray()) {
					resultList.add(convertScriptObjectToList(tempValue));
				} else {
					resultList.add(convertScriptObjectToMap(tempValue));
				}
			} else if (each instanceof String) {
				resultList.add((String) each);
			} else if (each instanceof Integer) {
				resultList.add((Integer) each);
			} else {
				resultList.add(each);
			}
		}
		return resultList;
	}

}
