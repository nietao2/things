package org.nietao.export.xssf.reader.impl;


import java.util.ArrayList;
import java.util.List;

import org.nietao.export.xssf.reader.OffsetRowCheck;
import org.nietao.export.xssf.reader.SectionCheck;

public class SimpleSectionCheck implements SectionCheck {
	
	List<OffsetRowCheck> offsetRowChecks = new ArrayList<OffsetRowCheck>();

	public SimpleSectionCheck(){}
	
	public SimpleSectionCheck(List<OffsetRowCheck> offsetRowChecks){
		this.offsetRowChecks = offsetRowChecks;
	}
	public void addRowCheck(OffsetRowCheck offsetRowCheck) {
		offsetRowChecks.add(offsetRowCheck);
	}

	public boolean isCheckSuccessful(Object[] rowDatas) {
		for(int i = 0; i < offsetRowChecks.size(); i++){
			OffsetRowCheck offsetRowCheck = offsetRowChecks.get(i);
			if(!offsetRowCheck.isCheckSuccessful(rowDatas)){
				return false;
			}
		}
		return true;
	}

	public List<OffsetRowCheck> getOffsetRowChecks() {
		return offsetRowChecks;
	}

	public void setOffsetRowChecks(List<OffsetRowCheck> offsetRowChecks) {
		this.offsetRowChecks = offsetRowChecks;
	}

	
}
