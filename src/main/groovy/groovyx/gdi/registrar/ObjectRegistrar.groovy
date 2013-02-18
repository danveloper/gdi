package groovyx.gdi.registrar

/**
 * User: dwoods
 * Date: 2/18/13
 */
class ObjectRegistrar {
    static Map<Class, String> byClass = [:]
    static Map<String, Map<String, Object>> byName = [:]

    static prototypeInstances = Collections.synchronizedMap([:])

    private static boolean initialized = false

    static {
        ObjectRegistrar.class.declaredMethods { method ->
            if (method.isSynthetic()) return
            if (method.name.startsWith("init_")) {
                ObjectRegistrar."${method.name}"()
            }
        }
    }

    static void register(Class clazz) {
        def name = clazz.simpleName.substring(0,1).toLowerCase() + clazz.simpleName.substring(1)

        byName[name] = [scope: clazz.scope, ref: clazz]
        byClass[clazz] = name

        if (!clazz.scope || clazz.scope == InjectionScope.SINGLETON) {
            byName[name].instance = clazz.newInstance()
        }

    }

    static Object getObject(hashCode, String name) {
        def nameRegistry = byName[name]
        if (nameRegistry.scope == InjectionScope.PROTOTYPE) {
            if (!prototypeInstances[hashCode+name]) {
                prototypeInstances[hashCode+name] = nameRegistry.ref.newInstance()
            }
            return prototypeInstances[hashCode+name]
        } else {
            return nameRegistry.instance
        }
    }

    static Object getObject(hashCode, Class clazz) {
        getObject(hashCode, byClass[clazz])
    }
}
