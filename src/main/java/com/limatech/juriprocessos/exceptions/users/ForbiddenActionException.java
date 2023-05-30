package com.limatech.juriprocessos.exceptions.users;

public class ForbiddenActionException extends RuntimeException{

    public ForbiddenActionException() {
        super("User is not allowed to perform this action");
    }
}
