package org.nietao.export.xssf.reader;

import java.util.List;


import org.nietao.export.xssf.reader.impl.BeanCellMapping;


public interface XLSXSimpleBlockReader extends XLSXBlockReader {
	void addMapping(BeanCellMapping mapping);

    List getMappings();
}
