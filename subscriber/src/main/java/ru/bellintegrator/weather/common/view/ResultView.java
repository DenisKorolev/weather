package ru.bellintegrator.weather.common.view;

public class ResultView {

    private String result;

    /**
     * Constructor for Jackson
     */
    public ResultView(){

    }

    public ResultView(String result){
        setResult(result);
    }

    @Override
    public String toString() {
        return "{result:" + getResult() + "}";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
