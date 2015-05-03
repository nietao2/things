package org.nietao.export.xssf.reader.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.nietao.export.xssf.model.XLSXDataType;
import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.reader.XLSXBlockReader;
import org.nietao.export.xssf.reader.XLSXSheetReader;
import org.nietao.export.xssf.reader.exception.SAXParseBlockCompleteExcetion;
import org.nietao.export.xssf.reader.exception.SAXParseCompleteException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XLSXSheetReaderImpl extends DefaultHandler implements
		XLSXSheetReader {

	private XLSXReadStatus readStatus = new XLSXReadStatus();

	protected final Log log = LogFactory.getLog(getClass());

	private Map beans;
	private SharedStringsTable sharedStringsTable;
	private StylesTable stylesTable;
	private String readValue;
	private XLSXDataType dataType;
	private Object[] rowDatas;
	private int colIdx;
	private Queue<XLSXBlockReader> blockReaders = new LinkedList<XLSXBlockReader>();

	private short formatIndex;
	private String formatString;
	private String sheetName;
	private XLSXBlockReader currentBlockReader;

	private int currentRowIndex;

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (this.currentRowIndex >= currentBlockReader.getStartRow()) {
			if (name.equals("c")) {// c > 单元格
				colIdx = getColumn(attributes);
				String cellType = attributes.getValue("t");
				String cellStyle = attributes.getValue("s");

				this.dataType = XLSXDataType.NUMBER;
				if ("b".equals(cellType)) {
					this.dataType = XLSXDataType.BOOL;
				} else if ("e".equals(cellType)) {
					this.dataType = XLSXDataType.ERROR;
				} else if ("inlineStr".equals(cellType)) {
					this.dataType = XLSXDataType.INLINESTR;
				} else if ("s".equals(cellType)) {
					this.dataType = XLSXDataType.SSTINDEX;
				} else if ("str".equals(cellType)) {
					this.dataType = XLSXDataType.FORMULA;
				} else if (cellStyle != null) {
					int styleIndex = Integer.parseInt(cellStyle);
					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
					this.formatIndex = style.getDataFormat();
					this.formatString = style.getDataFormatString();
				} else {
					this.formatIndex = 0;
					this.formatString = "";
				}
			} else if (name.equals("row")) {
				int cols = getColsNum(attributes);
				rowDatas = new Object[cols];
			}
			readValue = "";
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (this.currentRowIndex >= currentBlockReader.getStartRow()) {
			if (name.equals("v")) {
				switch (this.dataType) {
				case BOOL: {
					char first = readValue.charAt(0);
					rowDatas[colIdx] = first == '0' ? "FALSE" : "TRUE";
					break;
				}
				case ERROR: {
					rowDatas[colIdx] = "ERROR:" + readValue.toString();
					break;
				}
				case INLINESTR: {
					rowDatas[colIdx] = new XSSFRichTextString(readValue)
							.toString();
					break;
				}
				case SSTINDEX: {
					int idx = Integer.parseInt(readValue);
					rowDatas[colIdx] = new XSSFRichTextString(
							sharedStringsTable.getEntryAt(idx)).toString();
					break;
				}
				case FORMULA: {
					rowDatas[colIdx] = readValue;
					break;
				}
				case NUMBER: {
					if (HSSFDateUtil.isADateFormat(formatIndex, formatString)) {
						Double d = Double.parseDouble(readValue);
						Date date = HSSFDateUtil.getJavaDate(d);
						rowDatas[colIdx] = date;
					} else {
						rowDatas[colIdx] = readValue;
					}
					break;
				}
				}
			} else if (name.equals("row")) {
				try {
					currentBlockReader.processRow(
							this.currentRowIndex, rowDatas, this.sheetName, readStatus);
				} catch (SAXParseBlockCompleteExcetion e) {
					currentBlockReader = blockReaders.poll();
					if (currentBlockReader != null) {
						currentBlockReader.setBeans(beans);
					} else {
						throw new SAXParseCompleteException(this.readStatus);
					}
				}
				this.currentRowIndex++;
			}
		}else if (name.equals("row")) {
			this.currentRowIndex++;
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		readValue += new String(ch, start, length);
	}

	private int getColumn(Attributes attrubuts) {
		String name = attrubuts.getValue("r");
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			if (Character.isDigit(name.charAt(i))) {
				break;
			}
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}

	private int getColsNum(Attributes attrubuts) {
		String spans = attrubuts.getValue("spans");
		String cols = spans.substring(spans.indexOf(":") + 1);
		return Integer.parseInt(cols);
	}

	public void setReadStatus(XLSXReadStatus readStatus) {
		this.readStatus = readStatus;
	}

	public Queue<XLSXBlockReader> getBlockReaders() {
		return this.blockReaders;
	}

	public void setBlockReaders(Queue<XLSXBlockReader> blockReaders) {
		this.blockReaders = blockReaders;
	}

	public void addBlockReader(XLSXBlockReader blockReader) {
		this.blockReaders.offer(blockReader);
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public XLSXReadStatus getReadStatus() {
		return this.readStatus;
	}

	public void setBeans(Map beans) {
		this.beans = beans;
	}

	public void setSharedStringsTable(SharedStringsTable sharedStringsTable) {
		this.sharedStringsTable = sharedStringsTable;
	}

	public void setStylesTable(StylesTable stylesTable) {
		this.stylesTable = stylesTable;
	}

	public void setCurrentBlockReader() {
		this.currentBlockReader = this.blockReaders.poll();
	}

	public String getReadValue() {
		return readValue;
	}

	public void setReadValue(String readValue) {
		this.readValue = readValue;
	}

	public XLSXDataType getDataType() {
		return dataType;
	}

	public void setDataType(XLSXDataType dataType) {
		this.dataType = dataType;
	}

	public Object[] getRowDatas() {
		return rowDatas;
	}

	public void setRowDatas(Object[] rowDatas) {
		this.rowDatas = rowDatas;
	}

	public int getColIdx() {
		return colIdx;
	}

	public void setColIdx(int colIdx) {
		this.colIdx = colIdx;
	}

	public short getFormatIndex() {
		return formatIndex;
	}

	public void setFormatIndex(short formatIndex) {
		this.formatIndex = formatIndex;
	}

	public String getFormatString() {
		return formatString;
	}

	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public Map getBeans() {
		return beans;
	}

	public SharedStringsTable getSharedStringsTable() {
		return sharedStringsTable;
	}

	public StylesTable getStylesTable() {
		return stylesTable;
	}

	public XLSXBlockReader getCurrentBlockReader() {
		return currentBlockReader;
	}

}
