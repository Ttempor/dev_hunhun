package com.hun.api.sys;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class s {
    public static void main(String[] args) {
        MapperFacade mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();
    }
}
