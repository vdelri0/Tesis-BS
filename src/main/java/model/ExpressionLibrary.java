
package model;

import java.util.TreeMap;

/**
 * this class saves all the expressions of the java languague that will be
 * used to create the OFG
 * @author victor del rio
 */
public class ExpressionLibrary {
    private TreeMap<Integer, Expression> expressions;

    public ExpressionLibrary() {
        this.expressions = new TreeMap<Integer, Expression>();
    }

    public TreeMap<Integer, Expression> getExpressions() {
        return expressions;
    }

    public void declareLibrary(){
        
        //Syntactic Expressions
          expressions.put(ConstantsManager.PACKAGE_DECLARATION_INDEX, new Expression(ConstantsManager.PACKAGE_DECLARATION, ConstantsManager.PACKAGE_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.CLASS_DECLARATION_INDEX, new Expression(ConstantsManager.CLASS_DECLARATION, ConstantsManager.CLASS_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.METHOD_DECLARATION_INDEX, new Expression(ConstantsManager.METHOD_DECLARATION, ConstantsManager.METHOD_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_INSTATIATION_INDEX, new Expression(ConstantsManager.OBJECT_INSTANTIATION, ConstantsManager.OBJECT_INSTANTIATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_VARIABLE_ASSIGNATION_INDEX, new Expression(ConstantsManager.OBJECT_VARIABLE_ASSIGNATION, ConstantsManager.OBJECT_VARIABLE_ASSIGNATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_METHOD_INVOCATION_INDEX, new Expression(ConstantsManager.OBJECT_METHOD_INVOCATION, ConstantsManager.OBJECT_METHOD_INVOCATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_METHOD_INVOCATION_LISTENER_INDEX, new Expression(ConstantsManager.OBJECT_METHOD_INVOCATION_LISTENER, ConstantsManager.OBJECT_METHOD_INVOCATION_LISTENER_TOKEN));
          expressions.put(ConstantsManager.METHOD_INVOCATION_INDEX, new Expression(ConstantsManager.METHOD_INVOCATION, ConstantsManager.METHOD_INVOCATION_TOKEN));
    }
}