package com.circuitguard.utils;


public class SRBaseEndpoint<S, T> {
    private AbstractConverter<S, T> converter = new AbstractConverter<>();

    public AbstractConverter<S, T> getConverter(Populator<S, T> populator, String target) {
        converter.setPopulator(populator);
        converter.setTarget(target);
        return converter;
    }
}
