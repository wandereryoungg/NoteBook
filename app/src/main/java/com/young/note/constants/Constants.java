package com.young.note.constants;

public class Constants {

    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public interface EventFlag {
        int IMPORTANT = 0;
        int NORMAL = 1;
    }

    public interface IconTag {
        int FIRST = 1;
        int OTHER = 2;
    }

    public static final int HANDLER_SUCCESS = 0x0001;
    public static final int HANDLER_FAILED = 0x0000;

}
