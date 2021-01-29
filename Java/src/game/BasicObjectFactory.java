package game;

/**
 * Interface to create a new copy of the same object; used for buying items from the vending machine
 * */
public interface BasicObjectFactory {
    /** Method to return a new instance of the object */
    Object createNewCopy();
}
