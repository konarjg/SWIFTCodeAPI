package com.github.konarjg.SWIFTCodeAPI.response.factory;

import com.github.konarjg.SWIFTCodeAPI.response.DeleteSwiftCodeResponse;

public final class DeleteSwiftCodeResponseFactory {
    public static DeleteSwiftCodeResponse createDeleteSwiftCodeResponse(String message) {
        DeleteSwiftCodeResponse response = new DeleteSwiftCodeResponse();
        response.setMessage(message);

        return response;
    }
}
