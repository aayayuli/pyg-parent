package bean;

import java.io.Serializable;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/9  18:27
 * @description TODO
 **/
public class Result  implements Serializable {
    private  Boolean success;
    private  String message;

    public Result() {
    }

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
