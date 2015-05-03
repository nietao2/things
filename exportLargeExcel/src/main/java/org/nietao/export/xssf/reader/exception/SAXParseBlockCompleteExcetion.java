package org.nietao.export.xssf.reader.exception;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.xml.sax.SAXException;

public class SAXParseBlockCompleteExcetion extends SAXException {
	
	private static final long serialVersionUID = 1918402710152843043L;
	private XLSXReadStatus readStatus;
	
	public SAXParseBlockCompleteExcetion(XLSXReadStatus readStatus){
		this.readStatus = readStatus;
	}

	public XLSXReadStatus getReadStatus() {
		return this.readStatus;
	}

	public void setReadStatus(XLSXReadStatus readStatus) {
		this.readStatus = readStatus;
	}

}
