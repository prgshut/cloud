package ru.cloud;

import java.io.IOException;

public interface Callback {
    void call() throws IOException;
}