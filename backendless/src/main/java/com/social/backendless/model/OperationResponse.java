package com.social.backendless.model;

/**
 * Wrapper for the operation response when an operation to the backend is requested
 */

public class OperationResponse {
    private String mOpCode;
    private String mError;

    public OperationResponse(String mOpCode, String mError) {
        this.mOpCode = mOpCode;
        this.mError = mError;
    }

    public String getOpCode() {
        return mOpCode;
    }

    public String getError() {
        return mError;
    }
}
