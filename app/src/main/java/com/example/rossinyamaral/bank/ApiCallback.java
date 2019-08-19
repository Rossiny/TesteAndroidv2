package com.example.rossinyamaral.bank;

import com.example.rossinyamaral.bank.remote.ErrorResponse;

public interface ApiCallback<T> {
    void onSuccess(T object);

    void onError(ErrorResponse message);
}
