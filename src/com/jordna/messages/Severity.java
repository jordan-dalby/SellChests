package com.jordna.messages;

public enum Severity
{
    INFO("info"),
    DEBUG("debug"),
    ERROR("error");

    public final String name;

    Severity(String name)
    {
        this.name = name;
    }
}
