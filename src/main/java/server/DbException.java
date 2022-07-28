package server;

public class DbException extends Exception {
    public DbException(String message) {
        super(message);
    }

    public DbException() {
        super("ERROR");
    }
}

class ExitException extends Exception {
    public ExitException(String message) {
        super(message);
    }
}
