package org.nietao.export.xssf.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.nietao.export.xssf.model.XLSXReadStatus;

public interface XLSXReader {
	XLSXReadStatus read(InputStream inputXLSX, Map beans) throws IOException, InvalidFormatException;
    void setSheetReaders(Map<String, XLSXSheetReader> sheetReaders);
    Map<String, XLSXSheetReader> getSheetReaders();
    void addSheetReader( String sheetName, XLSXSheetReader reader);
    void addSheetReader(XLSXSheetReader reader);
}
