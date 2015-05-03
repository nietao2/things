package org.nietao.export.xssf.reader.impl;

import java.util.Map;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.reader.XLSXBlockReader;
import org.nietao.export.xssf.reader.exception.SAXParseBlockCompleteExcetion;

public abstract class XLSXBlockReaderImpl implements XLSXBlockReader {
	private int startRow;
    private int endRow;
    private Map beans;

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

	public Map getBeans() {
		return beans;
	}

	public void setBeans(Map beans) {
		this.beans = beans;
	}

    
    

}
