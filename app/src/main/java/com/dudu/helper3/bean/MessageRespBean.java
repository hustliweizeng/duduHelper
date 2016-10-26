package com.dudu.helper3.bean;

/**
 * Created with IntelliJ IDEA.
 * User: hao
 * Date: 13-12-22
 * Time: ����4:56
 * To change this template use File | Settings | File Templates.
 */
public class MessageRespBean {
    private String returnCode;
    private String returnDescr;
    private String content;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDescr() {
        return returnDescr;
    }

    public void setReturnDescr(String returnDescr) {
        this.returnDescr = returnDescr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
