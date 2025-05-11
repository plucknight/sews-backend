package com.example.sews.utils.bputils;

public class BPModelInstance {
    private Object instance;
    private ClassLoader classLoader;

    public BPModelInstance(Object instance, ClassLoader classLoader) {
        this.instance = instance;
        this.classLoader = classLoader;
    }

    public Object getInstance() {
        return instance;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
