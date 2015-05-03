package org.nietao.export.xssf.model;

import java.util.ArrayList;
import java.util.List;

public class XLSXReadStatus {

	private List<XLSXReadMessage> readMessages = new ArrayList<XLSXReadMessage>();

    private boolean statusOK = true;

    public XLSXReadStatus() {
    }

    public void mergeReadStatus(XLSXReadStatus status){
        if( status == null ){
            return;
        }
        if( !status.isStatusOK() ){
            statusOK = false;
        }
        addMessages( status.getReadMessages() );
    }

    public void addMessage( XLSXReadMessage errorMessage ){
        if( errorMessage != null ){
            readMessages.add( errorMessage );
        }
    }

    public void addMessages( List<XLSXReadMessage> list ){
        if( list != null ){
            readMessages.addAll( list );
        }
    }

    public void clear(){
        readMessages.clear();
    }

    public boolean isStatusOK() {
        return statusOK;
    }

    public void setStatusOK(boolean statusOK) {
        this.statusOK = statusOK;
    }

    public List<XLSXReadMessage> getReadMessages() {
        return readMessages;
    }

}
