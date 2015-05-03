package org.nietao.export.xssf.reader.impl;

import org.nietao.export.xssf.reader.OffsetCellCheck;

public class OffsetCellCheckImpl implements OffsetCellCheck {

	private short offset;
	private String value;
	public OffsetCellCheckImpl(short offset, String value) {
		this.offset = offset;
		this.value = value;
	}
	public short getOffset() {
		return offset;
	}
	public void setOffset(short offset) {
		this.offset = offset;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


}
