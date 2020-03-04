package com.sx.context;

/**
 * @author shengx
 * @date 2020/3/3 16:01
 */
public class PropertyValue {
    private String name;
    private String id;
    private String ref;
    private Class clazz;
    private boolean autowired = false;

    public PropertyValue() {
    }

    public PropertyValue(String name, String id, String ref, Class clazz) {
        this.name = name;
        this.id = id;
        this.ref = ref;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isAutowired() {
        return autowired;
    }

    public void setAutowired(boolean autowired) {
        this.autowired = autowired;
    }
}
