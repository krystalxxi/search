package com.ecommerce.search.exception;

/**
 * 搜索异常
 */
public class ElasticSearchSdkException extends RuntimeException{
    public ElasticSearchSdkException(){

    }
    public ElasticSearchSdkException(String errorMsg){
        super(errorMsg);
    }
}
