package org.nietao.export.xssf.model;

public class XLSXReadMessage {
	private String message;
    private Exception exception;

    public XLSXReadMessage(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }


    public XLSXReadMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
