package org.nietao.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.nietao.jxls.model.Department;
import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException, SAXException, InvalidFormatException {
		InputStream inputXML = new BufferedInputStream(App.class.getResourceAsStream("/department.xml"));
        XLSReader mainReader = ReaderBuilder.buildFromXML( inputXML );
        InputStream inputXLS = new BufferedInputStream(App.class.getResourceAsStream("/departmentdata.xls"));
        Department department = new Department();
//        Department hrDepartment = new Department();
//        List departments = new ArrayList();
        Map beans = new HashMap();
        beans.put("department", department);
//        beans.put("hrDepartment", hrDepartment);
//        beans.put("departments", departments);
        XLSReadStatus readStatus = mainReader.read( inputXLS, beans);
        
        System.out.println(department.getName());
        
        inputXML.close();
        inputXLS.close();
	}
}
