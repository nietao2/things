package org.nietao.export.xssf.reader;



public interface OffsetRowCheck {
	int getOffset();
    void setOffset(int offset);
//    boolean isCheckSuccessful(Row row);
//    boolean isCheckSuccessful(XLSRowCursor cursor);
    void addCellCheck(OffsetCellCheck cellCheck);
	boolean isCheckSuccessful(Object[] rowDatas);
}
