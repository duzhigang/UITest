package com.ggec.uitest.ui.eventbus;

public class ProgressEvent {
    private int progress;

    public ProgressEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }
}
