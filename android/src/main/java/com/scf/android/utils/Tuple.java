package com.scf.android.utils;

public class Tuple<L, R> {

    private final L result;
    private final R exception;

    public Tuple(L result, R exception) {
        this.result = result;
        this.exception = exception;
    }

    public L getResult() {
        return result;
    }

    public R getException() {
        return exception;
    }

    @Override
    public int hashCode() {
        return result.hashCode() ^ exception.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple)) return false;
        Tuple pairo = (Tuple) o;
        return this.result.equals(pairo.getResult()) &&
                this.exception.equals(pairo.getException());
    }

}