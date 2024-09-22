package ma.zouhir.ebankingbackend.exceptions;

public class BalanceNotEnoughException extends Exception {
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}
