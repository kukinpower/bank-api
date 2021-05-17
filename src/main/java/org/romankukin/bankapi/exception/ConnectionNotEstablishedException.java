package org.romankukin.bankapi.exception;

public class ConnectionNotEstablishedException extends IllegalArgumentException {
    private String sqlExceptionForEachTrace;

    public ConnectionNotEstablishedException(String trace, String message, Throwable cause) {
        super(message, cause);
        this.sqlExceptionForEachTrace = trace;
    }

    public ConnectionNotEstablishedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getSqlExceptionTrace() {
        return sqlExceptionForEachTrace == null ? super.getMessage() : sqlExceptionForEachTrace;
    }
}
