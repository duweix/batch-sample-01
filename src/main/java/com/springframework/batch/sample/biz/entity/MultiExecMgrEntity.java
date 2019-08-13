package com.springframework.batch.sample.biz.entity;

public class MultiExecMgrEntity {
    private Long reqId;
    private Long multiProcNo;
    private String procTarget;
    private String proc;
    private String states;
    private String errInfo;

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Long getMultiProcNo() {
        return multiProcNo;
    }

    public void setMultiProcNo(Long multiProcNo) {
        this.multiProcNo = multiProcNo;
    }

    public String getProcTarget() {
        return procTarget;
    }

    public void setProcTarget(String procTarget) {
        this.procTarget = procTarget;
    }

    public String getProc() {
        return proc;
    }

    public void setProc(String proc) {
        this.proc = proc;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    @Override
    public String toString() {
        return "MultiExecMgr [reqId=" + reqId + ", multiProcNo=" + multiProcNo + ", procTarget=" + procTarget + ", proc=" + proc + ", states=" + states + ", errInfo=" + errInfo + "]";
    }

}
