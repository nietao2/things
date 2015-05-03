package org.nietao.export.xssf.reader.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nietao.export.xssf.reader.OffsetCellCheck;
import org.nietao.export.xssf.reader.OffsetRowCheck;

public class OffsetRowCheckImpl implements OffsetRowCheck {

	private int offset;
	private Map<Short, OffsetCellCheck> cellChecks = new HashMap<Short, OffsetCellCheck>();
	
	public OffsetRowCheckImpl(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void addCellCheck(OffsetCellCheck cellCheck) {
		this.cellChecks.put(cellCheck.getOffset(), cellCheck);
	}
	
	public boolean isCheckSuccessful(Object[] rowdDatas){
		for(Entry<Short, OffsetCellCheck> entry : cellChecks.entrySet()){
			OffsetCellCheck cellCheck = entry.getValue();
			if(cellCheck.getOffset() < rowdDatas.length
					&& !(cellCheck.getValue().equals(rowdDatas[cellCheck.getOffset()])
							||("".equals(cellCheck.getValue()) && rowdDatas[cellCheck.getOffset()] == null))){
				return false;
			}
		}
		return true;
	}

	public Map<Short, OffsetCellCheck> getCellChecks() {
		return cellChecks;
	}

	public void setCellChecks(Map<Short, OffsetCellCheck> cellChecks) {
		this.cellChecks = cellChecks;
	}

}
