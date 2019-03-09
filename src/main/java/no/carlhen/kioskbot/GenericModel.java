package no.carlhen.kioskbot;

abstract class GenericModel<T extends GenericModel<T>> {
    public String id;
    public boolean isSame(T object){
        return this.equals(object);
    }
}
