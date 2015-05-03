package org.nietao.export.xssf.reader.exception;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.xml.sax.SAXException;

public class SAXParseCompleteException extends SAXException {

	private static final long serialVersionUID = 5197692683291328088L;
	
	private XLSXReadStatus readStatus;
	
	public SAXParseCompleteException(XLSXReadStatus readStatus){
		this.readStatus = readStatus;
	}

	public XLSXReadStatus getReadStatus() {
		return this.readStatus;
	}

	public void setReadStatus(XLSXReadStatus readStatus) {
		this.readStatus = readStatus;
	}

}
