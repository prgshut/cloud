package ru.cloud;

public class Command {
    public static final byte COMMAND_GET_FILE_LIST = 10;
    public static final byte COMMAND_SEND_FILE = 20;
    public static final byte COMMAND_RECIPIENT_FILE = 30;
    public static final byte COMMAND_AUTH = 5;
    public static final byte COMMAND_GET_HOME_DIR = 40;

    public static byte DIR = 11;
    public static byte FILE = 12;
    public static byte END_LIST = 13;
}
