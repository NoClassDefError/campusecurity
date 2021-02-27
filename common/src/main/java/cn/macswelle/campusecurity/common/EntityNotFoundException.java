package cn.macswelle.campusecurity.common;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String entityId, Class type) {
        super("Can not find type " + type + " id = " + entityId + " in database");
    }
}
