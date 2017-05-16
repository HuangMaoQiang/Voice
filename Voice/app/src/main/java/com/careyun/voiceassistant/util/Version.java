package com.careyun.voiceassistant.util;

public class Version {
    private boolean success;
    private String error;
    private String errorCode;
    private returnInfo returnInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Version.returnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(Version.returnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public class returnInfo {
        private String patchUrl;

        private boolean needUpdate;

        private String completeUrl;

        public String getPatchUrl() {
            return patchUrl;
        }

        public void setPatchUrl(String patchUrl) {
            this.patchUrl = patchUrl;
        }

        public boolean isNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
        }

        public String getCompleteUrl() {
            return completeUrl;
        }

        public void setCompleteUrl(String completeUrl) {
            this.completeUrl = completeUrl;
        }
    }
}
