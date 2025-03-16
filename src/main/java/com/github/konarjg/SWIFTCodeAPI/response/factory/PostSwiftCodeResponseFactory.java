package com.github.konarjg.SWIFTCodeAPI.response.factory;

import com.github.konarjg.SWIFTCodeAPI.response.PostSwiftCodeResponse;

public class PostSwiftCodeResponseFactory {
    public static PostSwiftCodeResponse createPostSwiftCodeResponse(String message) {
        PostSwiftCodeResponse response = new PostSwiftCodeResponse();
        response.setMessage(message);

        return response;
    }
}
