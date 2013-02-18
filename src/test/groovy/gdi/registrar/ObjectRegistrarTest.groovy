package gdi.registrar

import org.junit.Test
import groovyx.gdi.registrar.ObjectRegistrar

/**
 * User: dwoods
 * Date: 2/18/13
 */
class ObjectRegistrarTest {
    @Test
    void testSingletonRegistrar() {
        ObjectRegistrar.register(MyTestSingleton)
        def myTestSingleton = ObjectRegistrar.getObject(1, MyTestSingleton)
        assert myTestSingleton && myTestSingleton instanceof MyTestSingleton
        def myTestSingleton2 = ObjectRegistrar.getObject(2, MyTestSingleton)
        assert myTestSingleton2 && myTestSingleton == myTestSingleton2
    }
    @Test
    void testPrototypeRegistrar() {
        ObjectRegistrar.register(MyTestPrototype)
        def myTestPrototype = ObjectRegistrar.getObject(1, MyTestPrototype)
        assert myTestPrototype && myTestPrototype instanceof MyTestPrototype
        def myTestPrototype2 = ObjectRegistrar.getObject(2, MyTestPrototype)
        assert myTestPrototype2 && myTestPrototype != myTestPrototype2
    }
}
