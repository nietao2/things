package org.nietao.export.xssf.reader;

import java.util.Map;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.reader.exception.SAXParseBlockCompleteExcetion;
import org.nietao.export.xssf.reader.exception.XLSXSAXParseException;


public interface XLSXBlockReader {
//	XLSXReadStatus read(XLSRowCursor cursor, Map beans);

    int getStartRow();

    void setStartRow(int startRow);

    int getEndRow();

    void setEndRow(int endRow);

	void setBeans(Map beans);

	XLSXReadStatus processRow(int currentRowIndex, Object[] rowDatas,
			String sheetName, XLSXReadStatus readStatus) throws SAXParseBlockCompleteExcetion, XLSXSAXParseException;
}
