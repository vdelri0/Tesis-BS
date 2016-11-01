
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
        //Lexical Expressions
//        expressions.put(ConstantsManager.LCOMMENT_INDEX, new Expression(ConstantsManager.LCOMMENT,ConstantsManager.LCOMMENT_IDENTIFIER));
//        expressions.put(ConstantsManager.RCOMMENT_INDEX, new Expression(ConstantsManager.RCOMMENT,ConstantsManager.RCOMMENT_IDENTIFIER));
//        expressions.put(ConstantsManager.PACKAGE_INDEX, new Expression(ConstantsManager.PACKAGE,ConstantsManager.PACKAGE_IDENTIFIER));
//        expressions.put(ConstantsManager.CLASS_INDEX, new Expression(ConstantsManager.CLASS,ConstantsManager.CLASS_IDENTIFIER));
//        expressions.put(ConstantsManager.ACCESS_MODIFIER_INDEX, new Expression(ConstantsManager.ACCESS_MODIFIER,ConstantsManager.ACCESS_MODIFIER_IDENTIFIER));
//        expressions.put(ConstantsManager.STATIC_INDEX, new Expression(ConstantsManager.STATIC,ConstantsManager.STATIC_IDENTIFIER));
//        expressions.put(ConstantsManager.LPAREN_INDEX, new Expression(ConstantsManager.LPAREN,ConstantsManager.LPAREN_IDENTIFIER));
//        expressions.put(ConstantsManager.RPAREN_INDEX, new Expression(ConstantsManager.RPAREN,ConstantsManager.RPAREN_IDENTIFIER));
//        expressions.put(ConstantsManager.LBRACE_INDEX, new Expression(ConstantsManager.LBRACE,ConstantsManager.LBRACE_IDENTIFIER));
//        expressions.put(ConstantsManager.RBRACE_INDEX, new Expression(ConstantsManager.RBRACE,ConstantsManager.RBRACE_IDENTIFIER));
        
        //Syntactic Expressions
          expressions.put(ConstantsManager.PACKAGE_DECLARATION_INDEX, new Expression(ConstantsManager.PACKAGE_DECLARATION, ConstantsManager.PACKAGE_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.CLASS_DECLARATION_INDEX, new Expression(ConstantsManager.CLASS_DECLARATION, ConstantsManager.CLASS_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.METHOD_DECLARATION_INDEX, new Expression(ConstantsManager.METHOD_DECLARATION, ConstantsManager.METHOD_DECLARATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_INSTATIATION_INDEX, new Expression(ConstantsManager.OBJECT_INSTANTIATION, ConstantsManager.OBJECT_INSTANTIATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_VARIABLE_ASSIGNATION_INDEX, new Expression(ConstantsManager.OBJECT_VARIABLE_ASSIGNATION, ConstantsManager.OBJECT_VARIABLE_ASSIGNATION_TOKEN));
          expressions.put(ConstantsManager.OBJECT_METHOD_INVOCATION_INDEX, new Expression(ConstantsManager.OBJECT_METHOD_INVOCATION, ConstantsManager.OBJECT_METHOD_INVOCATION_TOKEN));
          expressions.put(ConstantsManager.METHOD_INVOCATION_INDEX, new Expression(ConstantsManager.METHOD_INVOCATION, ConstantsManager.METHOD_INVOCATION_TOKEN));
    }
}