/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author victor del rio
 */
public class ConstantsManager {
    //INDEX 
//    public static final int LCOMMENT_INDEX = 1;
//    public static final int RCOMMENT_INDEX = 2;
//    public static final int PACKAGE_INDEX = 3;
//    public static final int CLASS_INDEX = 4;
//    public static final int ACCESS_MODIFIER_INDEX = 5;
//    public static final int STATIC_INDEX = 6;
//    public static final int LPAREN_INDEX = 7;
//    public static final int RPAREN_INDEX = 8;
//    public static final int LBRACE_INDEX = 9;
//    public static final int RBRACE_INDEX = 10;
//    public static final int OBJECT_NAME_INDEX = 11;
//    public static final int NEW_INDEX = 12;
//    public static final int EQUAL_INDEX = 13;
//    public static final int TOKEN_INDEX = 14;
      public static final int PACKAGE_DECLARATION_INDEX = 1;
      public static final int CLASS_DECLARATION_INDEX = 2;
      public static final int METHOD_DECLARATION_INDEX = 3;
      public static final int OBJECT_INSTATIATION_INDEX = 4;
      public static final int OBJECT_VARIABLE_ASSIGNATION_INDEX = 5;
      public static final int OBJECT_METHOD_INVOCATION_INDEX = 6;
      public static final int METHOD_INVOCATION_INDEX = 7;
    
    
    
    
    //LEXICAL EXPRESSIONS
    public static final String LINE_ENDING = "(\\r\\n?|\\n|.*)";
    public static final String LCOMMENT = "(.*)/\\*(.*)";
    public static final String RCOMMENT = "\\*/";
    public static final String PACKAGE = "package";
    public static final String CLASS = "class";
    public static final String VOID = "void";
    public static final String ACCESS_MODIFIER = "(public|private|protected)";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String STATIC = "static";
    public static final String LPAREN = "\\(";
    public static final String RPAREN = "\\)";
    public static final String LBRACE = "\\{";
    public static final String RBRACE = "\\}";
    public static final String SEMICOLON = ";";
    public static final String COMMA = ",";
    public static final String DOT = "\\.";
    public static final String EQUAL = "=";
    public static final String NEW = "new";
    public static final String IDENTIFIER = "([a-zA-Z_$][a-zA-Z\\d_$]*)";
    public static final String STRING =  "\".*\"";
    public static final String DIGIT = "\\d";
    
    //LEXICAL TOKENS
    public static final String LCOMMENT_TOKEN = "leftComment";
    public static final String RCOMMENT_TOKEN = "rightComment";
    public static final String PACKAGE_TOKEN = "package";
    public static final String CLASS_TOKEN = "class";
    public static final String ACCESS_MODIFIER_TOKEN = "accessModifier";
    public static final String EXTENDS_TOKEN = "extends";
    public static final String IMPLEMENTS_TOKEN = "implements";
    public static final String STATIC_TOKEN = "static";
    public static final String LPAREN_TOKEN = "leftParen";
    public static final String RPAREN_TOKEN = "rightParen";
    public static final String LBRACE_TOKEN = "leftBrace";
    public static final String RBRACE_TOKEN = "rightBrace";
    public static final String OBJECT_NAME_TOKEN = "objectName";
    public static final String NEW_TOKEN = "new";
    public static final String EQUAL_TOKEN = "equal";
    public static final String IDENTIFIER_TOKEN = "identifier";
    public static final String STRING_TOKEN = "string";
    public static final String DIGIT_TOKEN = "digit";
    
    //SYNTACTIC EXPRESSIONS
    public static final String VARIABLE = DIGIT+"|"+STRING;
    public static final String PARAMETER = "("+VARIABLE+"|"+IDENTIFIER+")";
    public static final String PACKAGE_DECLARATION = "\\s*"+PACKAGE+"\\s*("+IDENTIFIER+"("+DOT+IDENTIFIER+")*)"+"\\s*"+SEMICOLON+"\\s*"+LINE_ENDING;
    public static final String CLASS_DECLARATION = "\\s*"+ACCESS_MODIFIER+"\\s+"+CLASS+"\\s+"+IDENTIFIER+"\\S+(("+EXTENDS+"|"+IMPLEMENTS+")\\s+"+IDENTIFIER+"(\\s*"+COMMA+"\\s*"+IDENTIFIER+"\\s*)*)?"+"\\s*"+"("+LBRACE+")?"+"\\s*"+LINE_ENDING;
    public static final String METHOD_DECLARATION = "\\s*"+ACCESS_MODIFIER+"\\s+("+STATIC+"\\s+)?"+IDENTIFIER+"\\s+"+IDENTIFIER+"\\s*"+LPAREN+"\\s*("+IDENTIFIER+"\\s+"+IDENTIFIER+"(\\s*"+COMMA+"\\s*"+IDENTIFIER+"\\s+"+IDENTIFIER+"\\s*)*)*"+RPAREN+"\\s*("+LBRACE+")?\\s*"+LINE_ENDING;
    public static final String OBJECT_INSTANTIATION= "\\s*"+IDENTIFIER+"\\s+"+IDENTIFIER+"\\s*"+EQUAL+"\\s*"+NEW+"\\s+"+IDENTIFIER+"\\s*"+LPAREN+"\\s*("+PARAMETER+"(\\s*"+COMMA+"\\s*"+PARAMETER+"\\s*)*)*"+RPAREN+"\\s*"+SEMICOLON+"\\s*"+LINE_ENDING;
    public static final String OBJECT_VARIABLE_ASSIGNATION = "\\s*"+IDENTIFIER+"\\s*"+EQUAL+"\\s*"+PARAMETER+"\\s*"+SEMICOLON+"\\s*"+LINE_ENDING;
    public static final String OBJECT_METHOD_INVOCATION = "\\s*"+IDENTIFIER+"("+DOT+IDENTIFIER+")+"+LPAREN+"(\\s*"+PARAMETER+"(\\s*"+COMMA+"\\s*"+PARAMETER+"\\s*)*)*"+RPAREN+"\\s*"+SEMICOLON+"\\s*"+LINE_ENDING;
    public static final String METHOD_INVOCATION = "\\s*"+IDENTIFIER+LPAREN+"(\\s*"+PARAMETER+"\\s*("+COMMA+"\\s*"+PARAMETER+"\\s*)*)*"+RPAREN+"\\s*"+SEMICOLON+"\\s*"+LINE_ENDING;
    
    //SYNTACTIC TOKENS
    public static final String PACKAGE_DECLARATION_TOKEN = "packageDeclaration";
    public static final String CLASS_DECLARATION_TOKEN = "classDeclaration";
    public static final String METHOD_DECLARATION_TOKEN = "methodDeclaration";
    public static final String OBJECT_INSTANTIATION_TOKEN = "objectInstantiation";
    public static final String OBJECT_VARIABLE_ASSIGNATION_TOKEN = "objectVariableAssignation";
    public static final String OBJECT_METHOD_INVOCATION_TOKEN = "objectMethodInvocation";
    public static final String METHOD_INVOCATION_TOKEN = "methodInvocation";
}