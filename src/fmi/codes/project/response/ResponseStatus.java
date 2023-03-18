package fmi.codes.project.response;

public enum ResponseStatus {
    OK("ok"),
    ERROR("error");

    private final String label;

    ResponseStatus(String label) {
        this.label = label;
    }
}