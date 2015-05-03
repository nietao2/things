package org.nietao.export.xssf.reader;


public interface SectionCheck {
    void addRowCheck(OffsetRowCheck offsetRowCheck);

	boolean isCheckSuccessful(Object[] rowDatas);
}
