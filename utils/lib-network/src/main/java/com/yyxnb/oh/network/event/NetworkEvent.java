package com.yyxnb.oh.network.event;

public class NetworkEvent {

    private boolean available;

    public NetworkEvent(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
