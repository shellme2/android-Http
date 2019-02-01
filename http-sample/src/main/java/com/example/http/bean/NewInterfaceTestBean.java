package com.example.http.bean;

/**
 * Created by admin on 2017/4/7.
 */

public class NewInterfaceTestBean {

    private String testMsg;

    private String testCode;

    public NewInterfaceTestBean(){

    }

    public NewInterfaceTestBean(String testMsg, String testCode){
        this.testMsg = testMsg;
        this.testCode = testCode;
    }
    public String getTestMsg() {
        return testMsg;
    }
    public void setTestMsg(String testMsg) {
        this.testMsg = testMsg;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }
}
