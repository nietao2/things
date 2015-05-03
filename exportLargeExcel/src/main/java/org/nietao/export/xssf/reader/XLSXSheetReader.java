package org.nietao.export.xssf.reader;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.nietao.export.xssf.model.XLSXReadStatus;
import org.xml.sax.ContentHandler;

public interface XLSXSheetReader extends ContentHandler {

    Queue<XLSXBlockReader> getBlockReaders();
    void setBlockReaders(Queue<XLSXBlockReader> blockReaders);
    void addBlockReader(XLSXBlockReader blockReader);

    String getSheetName();
    void setSheetName(String sheetName);
	XLSXReadStatus getReadStatus();
	void setBeans(Map beans);
	void setSharedStringsTable(SharedStringsTable sharedStringsTable);
	void setStylesTable(StylesTable stylesTable);
	void setCurrentBlockReader();

}
