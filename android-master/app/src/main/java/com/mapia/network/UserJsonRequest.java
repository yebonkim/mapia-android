package com.mapia.network;

/**
 * Created by daehyun on 15. 6. 8..
 */
class UserJsonRequest {
    final String username;
    final String password;

    UserJsonRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}