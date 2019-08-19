package com.example.rossinyamaral.bank.local;

public class BankCache {

    private static BankStorage storage;

    public static void setStorage(BankStorage storage) {
        BankCache.storage = storage;
    }

    public static String getLastLogin() {
        return storage.getLastLogin();
    }

    public static void setLastLogin(String username) {
        storage.setLastLogin(username);
    }
}
