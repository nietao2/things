package org.nietao.export.xssf.reader.impl;

import java.util.List;
import java.util.Map;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.reader.XLSXSimpleBlockReader;
import org.nietao.export.xssf.reader.exception.SAXParseBlockCompleteExcetion;

public class XLSXSimpleBlockReaderImpl implements XLSXSimpleBlockReader {

	public XLSXSimpleBlockReaderImpl(int startRow, int endRow) {
		// TODO Auto-generated constructor stub
	}

	public void addMapping(BeanCellMapping mapping) {
		// TODO Auto-generated method stub

	}

	public List getMappings() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStartRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setStartRow(int startRow) {
		// TODO Auto-generated method stub
		
	}

	public int getEndRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setEndRow(int endRow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBeans(Map beans) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public XLSXReadStatus processRow(int currentRowIndex, Object[] rowDatas,
			String sheetName, XLSXReadStatus readStatus)
			throws SAXParseBlockCompleteExcetion {
		// TODO Auto-generated method stub
		return null;
	}

}
