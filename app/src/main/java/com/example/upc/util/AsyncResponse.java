package com.example.upc.util;

public interface AsyncResponse {
    void onDataReceivedSuccess(String msg);
    void onDataReceivedFailed(String msg);
}