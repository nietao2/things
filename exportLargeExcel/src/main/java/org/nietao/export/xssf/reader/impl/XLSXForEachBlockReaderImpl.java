package org.nietao.export.xssf.reader.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.util.CellReference;
import org.nietao.export.xssf.model.XLSXReadMessage;
import org.nietao.export.xssf.model.XLSXReadStatus;
import org.nietao.export.xssf.model.XLSXSheetHeader;
import org.nietao.export.xssf.reader.SectionCheck;
import org.nietao.export.xssf.reader.XLSXBlockReader;
import org.nietao.export.xssf.reader.XLSXLoopBlockReader;
import org.nietao.export.xssf.reader.exception.SAXParseBlockCompleteExcetion;
import org.nietao.export.xssf.reader.exception.XLSXSAXParseException;
import org.nietao.export.xssf.reader.utils.ExpressionCollectionParser;
import org.nietao.export.xssf.reader.utils.ReaderConfig;

public class XLSXForEachBlockReaderImpl extends XLSXBlockReaderImpl implements XLSXLoopBlockReader {
	protected final Log log = LogFactory.getLog(getClass());

	private String items;
	private String var;
	private Class varType;
	private Map<Integer, BeanCellMapping> beanCellMap = new HashMap<Integer, BeanCellMapping>();
	private Map<Integer, XLSXSheetHeader> headers = new HashMap<Integer, XLSXSheetHeader>();

    SectionCheck loopBreakCheck;

	private Collection itemsCollection;

    public XLSXForEachBlockReaderImpl() {
    }

    public XLSXForEachBlockReaderImpl(int startRow, int endRow, String items, String var, Class varType) {
        setStartRow(startRow);
        setEndRow(endRow);
        this.items = items;
        this.var = var;
        this.varType = varType;
    }
    
    private boolean isInLoop(Object[] rowDatas){
    	return !this.loopBreakCheck.isCheckSuccessful(rowDatas);
    }
    
    

    public XLSXReadStatus processRow(int currentRowNum, Object[] rowDatas, String sheetName, XLSXReadStatus readStatus) throws XLSXSAXParseException, SAXParseBlockCompleteExcetion {
        if(!headers.isEmpty()){
        	checkHeader(currentRowNum, rowDatas, sheetName, readStatus);
        	checkStatus(readStatus);
        }else{
        	if(isInLoop(rowDatas)){
        		createNewCollectionItem(currentRowNum, itemsCollection, getBeans(), readStatus);
        		for(int i = 0; i < rowDatas.length; i++){
        			if(beanCellMap.containsKey(i)){
        				try{
        					beanCellMap.get(i).populateBean(rowDatas[i], getBeans());
        				}catch(Exception e){
        					String message = "Can't read cell " + getCellName(currentRowNum, i) + " on " + sheetName + " spreadsheet";
        	                readStatus.addMessage(new XLSXReadMessage(message, e));
        	                if (ReaderConfig.getInstance().isSkipErrors()) {
        	                    if (log.isWarnEnabled()) {
        	                        log.warn(message);
        	                    }
        	                } else {
        	                    readStatus.setStatusOK(false);
        	                    throw new XLSXSAXParseException(getCellName(currentRowNum, i), "Can't read cell " + getCellName(currentRowNum, i) + " on " + sheetName + " spreadsheet", readStatus);
        	                } 
        				}
        			}
        		}
        	}else{
        		getBeans().remove(var);
        		throw new SAXParseBlockCompleteExcetion(readStatus);
        	}
        }
        return readStatus;

    }

    private void checkStatus(XLSXReadStatus readStatus) throws XLSXSAXParseException {
    	if(headers.isEmpty() && !readStatus.isStatusOK()){
    		String message = "Header checking fail!";
    		throw new XLSXSAXParseException(message, readStatus);
    	}
	}

	private void checkHeader(int currentRowNum, Object[] rowDatas,
			String sheetName, XLSXReadStatus readStatus) {
    	for(int i = 0; i < rowDatas.length; i++){
    		String cell = getCellName(currentRowNum, i);
    		if(headers.containsKey(cell)){
    			String title = headers.get(cell).getTitle();
    			if(!title.equals(rowDatas[i])){
    				String message = "Imcompatible sheet name is found in sheet [" + sheetName + "] cell [" + cell + "], expect [" + title + "], actual header is [" + rowDatas[i] + "]";
    				if(log.isWarnEnabled()){
    					log.warn(message);
    				}
    				readStatus.setStatusOK(false);
    				readStatus.addMessage(new XLSXReadMessage(message));
    			}
    			headers.remove(cell);
    		}
    	}
	}

	private String getCellName(int currentRowNum, int col) {
		CellReference currentCellRef = new CellReference(currentRowNum, col, false, false);
        return currentCellRef.formatAsString();
	}

	private void createNewCollectionItem(int currentRowNum, Collection itemsCollection, Map beans, XLSXReadStatus readStatus) throws XLSXSAXParseException {
        Object obj = null;
        try {
            obj = varType.newInstance();
        } catch (Exception e) {
            String message = "Can't create a new collection item for " + items + ". varType = " + varType.getName();
            readStatus.addMessage(new XLSXReadMessage(message));
            if (!ReaderConfig.getInstance().isSkipErrors()) {
                readStatus.setStatusOK(false);
                throw new XLSXSAXParseException(message, readStatus);
            }
            if (log.isWarnEnabled()) {
                log.warn(message);
            }
        }
        itemsCollection.add(obj);
        beans.put(var, obj);
    }

	public void setBeans(Map beans){
		super.setBeans(beans);
		JexlContext context = new MapContext(getBeans());
		ExpressionCollectionParser parser = new ExpressionCollectionParser(context, this.items + ";", true);
		this.itemsCollection = parser.getCollection();
	}
//    private void readInnerBlocks(XLSRowCursor cursor, Map beans) {
//        for (int i = 0; i < innerBlockReaders.size(); i++) {
//            XLSBlockReader xlsBlockReader = (XLSBlockReader) innerBlockReaders.get(i);
//            readStatus.mergeReadStatus(xlsBlockReader.read(cursor, beans));
//            cursor.moveForward();
//        }
//    }
//
//    public void addBlockReader(XLSBlockReader reader) {
//        innerBlockReaders.add(reader);
//    }
//
//    public List getBlockReaders() {
//        return innerBlockReaders;
//    }

    public SectionCheck getLoopBreakCondition() {
        return loopBreakCheck;
    }

    public void setLoopBreakCondition(SectionCheck sectionCheck) {
        this.loopBreakCheck = sectionCheck;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setVarType(Class varType) {
        this.varType = varType;
    }

    public String getItems() {
        return items;
    }

    public String getVar() {
        return var;
    }

    public Class getVarType() {
        return varType;
    }

	@Override
	public List getBlockReaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addBlockReader(XLSXBlockReader blockReader) {
		// TODO Auto-generated method stub
		
	}

}
