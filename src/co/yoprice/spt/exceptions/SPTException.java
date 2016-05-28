package co.yoprice.spt.exceptions;

/**
 * Created by cj on 4/25/16.
 */
public class SPTException extends Throwable {
    public enum SPTERROR{
        CORRUPT_PKT, PARSE_FAILED
    }

    private final String errorMsg;
    private SPTERROR spterror;

    public SPTException(String errorMsg, SPTERROR spterror){
        this.errorMsg = errorMsg;
        this.spterror = spterror;
    }

    @Override
    public String getMessage() {
        return spterror.name() + ": " + getErrorMsg();
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    private String getErrorMsg() {
        return errorMsg;
    }
}
