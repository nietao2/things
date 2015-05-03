package org.nietao.export.xssf.reader;

import java.util.List;

public interface XLSXLoopBlockReader extends XLSXBlockReader {
	void setLoopBreakCondition(SectionCheck condition);
    SectionCheck getLoopBreakCondition();
    List getBlockReaders();
	void addBlockReader(XLSXBlockReader blockReader);
}
