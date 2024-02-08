package com.imooc.myapp;

import com.squareup.otto.Bus;

public class BusProvider {
    private BusProvider(){

    }

    private static final Bus BUS = new Bus();

    public static Bus getBus(){
        return BUS;
    }
}
