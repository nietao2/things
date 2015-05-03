package org.nietao.export.xssf.reader.exception;

import org.nietao.export.xssf.model.XLSXReadStatus;
import org.xml.sax.SAXException;

public class XLSXSAXParseException extends SAXException {
	private static final long serialVersionUID = -1369334728059458112L;
	private String cell;
	private String message;
	private XLSXReadStatus readStatus;
	
	
	public XLSXSAXParseException(String cell, String message,
			XLSXReadStatus readStatus) {
		this.cell = cell;
		this.message = message;
		this.readStatus = readStatus;
	}
	public XLSXSAXParseException(String message, XLSXReadStatus readStatus) {
		this.message = message;
		this.readStatus = readStatus;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public XLSXReadStatus getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(XLSXReadStatus readStatus) {
		this.readStatus = readStatus;
	}



}
