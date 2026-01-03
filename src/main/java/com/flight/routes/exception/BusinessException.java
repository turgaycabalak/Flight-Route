package com.flight.routes.exception;

public class BusinessException extends RuntimeException {
  private final String messageKey;
  private final Object[] args;

  public BusinessException(String messageKey, Object... args) {
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
