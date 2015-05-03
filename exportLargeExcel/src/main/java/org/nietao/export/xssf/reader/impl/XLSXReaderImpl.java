package org.nietao.export.xssf.reader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.reader.XLSXReader;
import org.nietao.export.xssf.reader.XLSXSheetReader;
import org.nietao.export.xssf.reader.exception.SAXParseCompleteException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XLSXReaderImpl implements XLSXReader {

	protected final Log log = LogFactory.getLog(getClass());

	private Map<String, XLSXSheetReader> sheetReaders = new HashMap<String, XLSXSheetReader>();

	private XLSXReadStatus readStatus = new XLSXReadStatus();

	public XLSXReadStatus read(InputStream inputXLSX, Map beans)
			throws IOException, InvalidFormatException {
		readStatus.clear();
		OPCPackage pkg = OPCPackage.open(inputXLSX);
		try {
			return read(beans, pkg);
		} catch (OpenXML4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private XLSXReadStatus read(Map beans, OPCPackage pkg) throws IOException, OpenXML4JException, SAXException {
		XSSFReader xssfReader = new XSSFReader(pkg);
		SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();
		StylesTable stylesTable = xssfReader.getStylesTable();
		Iterator<InputStream> sheets = xssfReader.getSheetsData();
		XMLReader parser = fetchSheetParser();
		while(sheets.hasNext()){
			try{
				readStatus.mergeReadStatus(readSheet(sheets, parser, beans, sharedStringsTable, stylesTable));
			}catch(SAXParseCompleteException e){
				readStatus.mergeReadStatus(e.getReadStatus());
			}
		}
		return null;
	}

	private XLSXReadStatus readSheet(Iterator<InputStream> sheets,
			XMLReader parser, Map beans, SharedStringsTable sharedStringsTable,
			StylesTable stylesTable) throws SAXParseCompleteException, IOException, SAXException {
		InputStream sheet = sheets.next();
		String sheetName = ((SheetIterator) sheets).getSheetName();
		if(log.isInfoEnabled()){
			log.info("Processing sheet: " + sheetName);
		}
		InputSource sheetSource = new InputSource(sheet);
		XLSXSheetReader sheetReader = null;
		if(sheetReaders.containsKey(sheetName)){
			sheetReader = sheetReaders.get(sheetName);
			sheetReader.getReadStatus().clear();
			sheetReader.setBeans(beans);
			sheetReader.setSharedStringsTable(sharedStringsTable);
			sheetReader.setStylesTable(stylesTable);
			sheetReader.setCurrentBlockReader();
			createNestedObjects(sheetReader, beans);
			parser.setContentHandler(sheetReader);
			parser.parse(sheetSource);
		}
		return null;
	}

	private void createNestedObjects(XLSXSheetReader sheetReader, Map beans) {
		// TODO Auto-generated method stub
		
	}

	private XMLReader fetchSheetParser() throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		return parser;
	}


	public Map<String, XLSXSheetReader> getSheetReaders() {
		return sheetReaders;
	}

	public void addSheetReader(String sheetName, XLSXSheetReader reader) {
		sheetReaders.put(sheetName, reader);
	}

	public void addSheetReader(XLSXSheetReader reader) {
		addSheetReader(reader.getSheetName(), reader);
	}

	public void setSheetReaders(Map<String, XLSXSheetReader> sheetReaders) {
		this.sheetReaders = sheetReaders;
	}

}
