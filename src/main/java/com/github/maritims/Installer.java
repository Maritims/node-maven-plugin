package com.github.maritims;

import java.io.IOException;

public interface Installer {
    boolean download() throws IOException;
    boolean extract() throws IOException;
}
