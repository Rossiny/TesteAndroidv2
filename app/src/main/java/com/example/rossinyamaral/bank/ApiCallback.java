package com.example.rossinyamaral.bank;

public interface ApiCallback<T> {
    void onSuccess(T object);

    void onError(ErrorResponse message);
}
