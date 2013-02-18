package groovyx.ast.transforms;


import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * User: dwoods
 * Date: 2/17/13
 */
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class GDITransformation implements ASTTransformation {
    private static final String INJECTED_FIELD_NAME = "injected"

    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        source.ast.classes.each { ClassNode classNode ->
            FieldNode fieldNode = classNode.fields.find { it.name == INJECTED_FIELD_NAME }
            if (!fieldNode) return

            def fieldValues = ((MapExpression)fieldNode.initialValueExpression).mapEntryExpressions
            fieldValues.each { MapEntryExpression mapEntryExpression ->
                def key = mapEntryExpression.keyExpression.value
                def value = mapEntryExpression.valueExpression.type
                classNode.addMethod(createDIGetter(key, value.name))
            }
        }
    }

    private MethodNode createDIGetter(propertyName, clazzName) {
        def newName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1)

        def clazzParts = clazzName.split("\\.")
        def packageName = clazzParts[0..clazzParts.size()-2]
        def shortName = clazzParts[-1]

        try {
            def ast = new AstBuilder().buildFromString(CompilePhase.INSTRUCTION_SELECTION, false, """
                package ${packageName.join(".")}

                class ${shortName} {
                    public $clazzName get${newName}() {
                        ($clazzName)groovyx.gdi.registrar.ObjectRegistrar.getObject(System.identityHashCode(this), ${clazzName}.class)
                    }
                }
            """)

            return ast[1].methods.find { it.name == "get${newName}" }
        } catch (e) {
            throw e
        }
    }
}
