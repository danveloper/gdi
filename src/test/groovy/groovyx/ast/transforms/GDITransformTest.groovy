package groovyx.ast.transforms

import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper
import org.junit.Test

/**
 * User: dwoods
 * Date: 2/17/13
 */
class GDITransformTest {
    @Test
    public void testIt() {
        def script = """
            class Any {
                static injected = [myService: Object.class]
            }
        """
        def invoker = new TransformTestHelper(new GDITransformation(), CompilePhase.CANONICALIZATION)
        def instance = invoker.parse(script)
        instance.methods.find {it.name == "getMyService"}
    }
}
