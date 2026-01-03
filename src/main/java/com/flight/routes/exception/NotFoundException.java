package com.flight.routes.exception;

public class NotFoundException extends RuntimeException {
  private final String messageKey;
  private final Object[] args;

  public NotFoundException(String messageKey, Object... args) {
    this.messageKey = messageKey;
    this.args = args;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public Object[] getArgs() {
    return args;
  }
}
