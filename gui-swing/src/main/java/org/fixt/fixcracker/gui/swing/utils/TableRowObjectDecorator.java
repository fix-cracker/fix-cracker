package org.fixt.fixcracker.gui.swing.utils;

import java.util.function.Function;

public class TableRowObjectDecorator<A> {
    private final A obj;
    private Function<A, String> func;

    public TableRowObjectDecorator(A obj, Function<A, String> func) {
        this.obj = obj;
        this.func = func;
    }

    public A getObj() {
        return obj;
    }

    @Override
    public String toString() {
        return func.apply(obj);
    }
}
