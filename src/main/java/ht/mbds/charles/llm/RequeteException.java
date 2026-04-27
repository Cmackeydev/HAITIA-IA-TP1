package ht.mbds.charles.llm;

public class RequeteException extends Exception {
    private String detail;

    public RequeteException(String message,String detail) {
        super(message);
        setDetail(detail);
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getDetail() {
        return detail;
    }
}


//
