package org.nietao.export.xssf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.nietao.export.xssf.reader.OffsetCellCheck;
import org.nietao.export.xssf.reader.OffsetRowCheck;
import org.nietao.export.xssf.reader.SectionCheck;
import org.nietao.export.xssf.reader.XLSXLoopBlockReader;
import org.nietao.export.xssf.reader.XLSXReader;
import org.nietao.export.xssf.reader.XLSXSheetReader;
import org.nietao.export.xssf.reader.XLSXSimpleBlockReader;
import org.nietao.export.xssf.reader.impl.BeanCellMapping;
import org.nietao.export.xssf.reader.impl.OffsetCellCheckImpl;
import org.nietao.export.xssf.reader.impl.OffsetRowCheckImpl;
import org.nietao.export.xssf.reader.impl.SimpleSectionCheck;
import org.nietao.export.xssf.reader.impl.XLSXForEachBlockReaderImpl;
import org.nietao.export.xssf.reader.impl.XLSXReaderImpl;
import org.nietao.export.xssf.reader.impl.XLSXSheetReaderImpl;
import org.nietao.export.xssf.reader.impl.XLSXSimpleBlockReaderImpl;
import org.xml.sax.SAXException;

/**
 * @author Tao Nie
 */
public class XLSXReaderBuilder {

    XLSXReader reader = new XLSXReaderImpl();
    XLSXSheetReader currentSheetReader;
    XLSXSimpleBlockReader currentSimpleBlockReader;
    XLSXLoopBlockReader currentLoopBlockReader;
    boolean lastSheetReader = false;
    SectionCheck currentSectionCheck;
    OffsetRowCheck currentRowCheck;

    public static XLSXReader buildFromXML(InputStream xmlStream) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.setValidating( false );
        digester.addObjectCreate("workbook", XLSXReaderImpl.class);
        digester.addObjectCreate("workbook/worksheet", XLSXSheetReaderImpl.class);
        digester.addSetProperties("workbook/worksheet", "name", "sheetName");
        digester.addSetProperties("workbook/worksheet", "idx", "sheetIdx");
//        digester.addSetProperty("workbook/worksheet", "sheetName", "name");
        digester.addSetNext("workbook/worksheet", "addSheetReader");
        digester.addObjectCreate("*/loop", XLSXForEachBlockReaderImpl.class);
        digester.addSetProperties("*/loop");
        digester.addSetNext("*/loop", "addBlockReader");
        digester.addObjectCreate("*/section", XLSXSimpleBlockReaderImpl.class);
        digester.addSetProperties("*/section");
        digester.addSetNext("*/section", "addBlockReader");
        digester.addObjectCreate("*/mapping", BeanCellMapping.class);
        digester.addSetProperties("*/mapping");
        digester.addCallMethod("*/mapping", "setFullPropertyName", 1);
        digester.addCallParam("*/mapping", 0);
        digester.addSetNext("*/mapping", "addMapping");
        digester.addObjectCreate("*/loop/loopbreakcondition", SimpleSectionCheck.class);
        digester.addSetNext("*/loop/loopbreakcondition", "setLoopBreakCondition");
        digester.addObjectCreate("*/loopbreakcondition/rowcheck", OffsetRowCheckImpl.class);
        digester.addSetProperties("*/loopbreakcondition/rowcheck");
        digester.addSetNext("*/loopbreakcondition/rowcheck", "addRowCheck");
        digester.addObjectCreate("*/rowcheck/cellcheck", OffsetCellCheckImpl.class);
        digester.addSetProperties("*/rowcheck/cellcheck");
        digester.addCallMethod("*/rowcheck/cellcheck", "setValue", 1);
        digester.addCallParam("*/rowcheck/cellcheck", 0);
        digester.addSetNext("*/rowcheck/cellcheck", "addCellCheck");
        return (XLSXReader) digester.parse( xmlStream );
    }

    public static XLSXReader buildFromXML(File xmlFile) throws IOException, SAXException {
        InputStream xmlStream = new BufferedInputStream( new FileInputStream(xmlFile) );
        XLSXReader reader = buildFromXML( xmlStream );
        xmlStream.close();
        return reader;
    }

    public XLSXReaderBuilder addSheetReader(String sheetName) {
        XLSXSheetReader sheetReader = new XLSXSheetReaderImpl();
        reader.addSheetReader( sheetName, sheetReader );
        currentSheetReader = sheetReader;
        lastSheetReader = true;
        return this;
    }

    public XLSXReader getReader() {
        return reader;
    }

    public XLSXReaderBuilder addSimpleBlockReader(int startRow, int endRow) {
    	XLSXSimpleBlockReader blockReader = new XLSXSimpleBlockReaderImpl( startRow, endRow );
        if( lastSheetReader ){
            currentSheetReader.addBlockReader( blockReader );
        }else{
            currentLoopBlockReader.addBlockReader( blockReader );
        }
        currentSimpleBlockReader = blockReader;
        return this;
    }

    public XLSXReaderBuilder addMapping(String cellName, String propertyName) {
        BeanCellMapping mapping = new BeanCellMapping( cellName, propertyName );
        currentSimpleBlockReader.addMapping( mapping );
        return this;
    }

    public XLSXReaderBuilder addLoopBlockReader(int startRow, int endRow, String items, String varName, Class varType) {
        XLSXLoopBlockReader loopReader = new XLSXForEachBlockReaderImpl(startRow, endRow, items, varName, varType);
        if( lastSheetReader ){
            currentSheetReader.addBlockReader( loopReader );
        }else{
            currentLoopBlockReader.addBlockReader( loopReader );
        }
        currentLoopBlockReader = loopReader;
        return this;
    }

    public XLSXReaderBuilder addLoopBreakCondition(){
        SectionCheck condition = new SimpleSectionCheck();
        currentLoopBlockReader.setLoopBreakCondition( condition );
        currentSectionCheck = condition;
        return this;
    }

    public XLSXReaderBuilder addOffsetRowCheck(int offset){
        OffsetRowCheck rowCheck = new OffsetRowCheckImpl( offset );
        currentSectionCheck.addRowCheck( rowCheck );
        currentRowCheck = rowCheck;
        return this;
    }

    public XLSXReaderBuilder addOffsetCellCheck(short offset, String value){
        OffsetCellCheck cellCheck = new OffsetCellCheckImpl( offset, value );
        currentRowCheck.addCellCheck( cellCheck );
        return this;
    }
    // todo:
    public XLSXReaderBuilder addSimpleBlockReaderToParent(){
        return this;
    }
    // todo:
    public XLSXReaderBuilder addLoopBlockReaderToParent(){
        return this;
    }
}