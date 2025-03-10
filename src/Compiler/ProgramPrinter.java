package Compiler;

import gen.CListener;
import gen.CParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

public class ProgramPrinter implements CListener {
    public static int nested = 0;
    public static int entry = 0;
    public static String tab = "    ";
    public static int tabCount = 0;
    public static String[] functions = new String[10];

    public static String funcName=null;
    // repeat a given word count time
    public static String repeat(int count, String word) {
        return new String(new char[count]).replace("\0", word);
    }

    public String getType(List<CParser.DeclarationSpecifierContext> declSpecCtxList) {
        String fieldType = null;

        for (CParser.DeclarationSpecifierContext declSpecCtx : declSpecCtxList) {
            CParser.TypeSpecifierContext typeSpecCtx = declSpecCtx.typeSpecifier();
            if (typeSpecCtx != null) {
                fieldType = typeSpecCtx.getText();
                break; // if you only want to get the first typeSpecifier
            }
        }
        return fieldType;
    }

    @Override
    public void enterExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        System.out.println("Program start {");
    }

    @Override
    public void exitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        System.out.println("}");
    }

    @Override
    public void enterPrimaryExpression(CParser.PrimaryExpressionContext ctx) {


    }

    @Override
    public void exitPrimaryExpression(CParser.PrimaryExpressionContext ctx) {

    }
    public static boolean contains(String[] array, String target) {
        for (String s : array) {
            if (s != null && s.equals(target)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void enterPostfixExpression(CParser.PostfixExpressionContext ctx) {
        String nTab = repeat(tabCount+1, tab);
        if(funcName!=null) {
            String params=null;
            CParser.PrimaryExpressionContext temp = ctx.primaryExpression();
            if(temp.Identifier()!=null){
                params=temp.Identifier().getText();
                if(!contains(functions, params))
                    System.out.println(nTab+"function call: "+funcName+"/ params: "+params);
            }

        }
        if(ctx.LeftParen()!=null){
            if(ctx.argumentExpressionList()!=null){
                CParser.PrimaryExpressionContext temp = ctx.primaryExpression();
                if(temp.Identifier()!=null){
                    if(contains(functions, temp.Identifier().getText())) {
                        funcName = temp.Identifier().getText();
                    }
                }
            }
        }


//        System.out.println("function call: "+funcName+"/ params: ");
    }

    @Override
    public void exitPostfixExpression(CParser.PostfixExpressionContext ctx) {

    }

    @Override
    public void enterArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {

    }

    @Override
    public void exitArgumentExpressionList(CParser.ArgumentExpressionListContext ctx) {

    }

    @Override
    public void enterUnaryExpression(CParser.UnaryExpressionContext ctx) {

    }

    @Override
    public void exitUnaryExpression(CParser.UnaryExpressionContext ctx) {

    }

    @Override
    public void enterUnaryOperator(CParser.UnaryOperatorContext ctx) {

    }

    @Override
    public void exitUnaryOperator(CParser.UnaryOperatorContext ctx) {

    }

    @Override
    public void enterCastExpression(CParser.CastExpressionContext ctx) {

    }

    @Override
    public void exitCastExpression(CParser.CastExpressionContext ctx) {

    }

    @Override
    public void enterMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {

    }

    @Override
    public void exitMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {

    }

    @Override
    public void enterAdditiveExpression(CParser.AdditiveExpressionContext ctx) {

    }

    @Override
    public void exitAdditiveExpression(CParser.AdditiveExpressionContext ctx) {

    }

    @Override
    public void enterShiftExpression(CParser.ShiftExpressionContext ctx) {

    }

    @Override
    public void exitShiftExpression(CParser.ShiftExpressionContext ctx) {

    }

    @Override
    public void enterRelationalExpression(CParser.RelationalExpressionContext ctx) {

    }

    @Override
    public void exitRelationalExpression(CParser.RelationalExpressionContext ctx) {

    }

    @Override
    public void enterEqualityExpression(CParser.EqualityExpressionContext ctx) {

    }

    @Override
    public void exitEqualityExpression(CParser.EqualityExpressionContext ctx) {

    }

    @Override
    public void enterAndExpression(CParser.AndExpressionContext ctx) {

    }

    @Override
    public void exitAndExpression(CParser.AndExpressionContext ctx) {

    }

    @Override
    public void enterExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {

    }

    @Override
    public void exitExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {

    }

    @Override
    public void enterInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {

    }

    @Override
    public void exitInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {

    }

    @Override
    public void enterLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {

    }

    @Override
    public void exitLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {

    }

    @Override
    public void enterLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {

    }

    @Override
    public void exitLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {

    }

    @Override
    public void enterConditionalExpression(CParser.ConditionalExpressionContext ctx) {

    }

    @Override
    public void exitConditionalExpression(CParser.ConditionalExpressionContext ctx) {

    }

    @Override
    public void enterAssignmentExpression(CParser.AssignmentExpressionContext ctx) {

    }

    @Override
    public void exitAssignmentExpression(CParser.AssignmentExpressionContext ctx) {

    }

    @Override
    public void enterAssignmentOperator(CParser.AssignmentOperatorContext ctx) {

    }

    @Override
    public void exitAssignmentOperator(CParser.AssignmentOperatorContext ctx) {

    }

    @Override
    public void enterExpression(CParser.ExpressionContext ctx) {

    }

    @Override
    public void exitExpression(CParser.ExpressionContext ctx) {

    }

    @Override
    public void enterConstantExpression(CParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void exitConstantExpression(CParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void enterDeclaration(CParser.DeclarationContext ctx) {

    }

    @Override
    public void exitDeclaration(CParser.DeclarationContext ctx) {

    }

    @Override
    public void enterDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {

    }

    @Override
    public void exitDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx) {

    }

    @Override
    public void enterDeclarationSpecifiers2(CParser.DeclarationSpecifiers2Context ctx) {

    }

    @Override
    public void exitDeclarationSpecifiers2(CParser.DeclarationSpecifiers2Context ctx) {

    }

    @Override
    public void enterDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {

    }

    @Override
    public void exitDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {

    }

    @Override
    public void enterInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {

    }

    @Override
    public void exitInitDeclaratorList(CParser.InitDeclaratorListContext ctx) {

    }

    @Override
    public void enterInitDeclarator(CParser.InitDeclaratorContext ctx) {

    }

    @Override
    public void exitInitDeclarator(CParser.InitDeclaratorContext ctx) {

    }

    @Override
    public void enterStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {

    }

    @Override
    public void exitStorageClassSpecifier(CParser.StorageClassSpecifierContext ctx) {

    }

    @Override
    public void enterTypeSpecifier(CParser.TypeSpecifierContext ctx) {

    }

    @Override
    public void exitTypeSpecifier(CParser.TypeSpecifierContext ctx) {

    }

    @Override
    public void enterStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {

    }

    @Override
    public void exitStructOrUnionSpecifier(CParser.StructOrUnionSpecifierContext ctx) {

    }

    @Override
    public void enterStructOrUnion(CParser.StructOrUnionContext ctx) {

    }

    @Override
    public void exitStructOrUnion(CParser.StructOrUnionContext ctx) {

    }

    @Override
    public void enterStructDeclarationList(CParser.StructDeclarationListContext ctx) {

    }

    @Override
    public void exitStructDeclarationList(CParser.StructDeclarationListContext ctx) {

    }

    @Override
    public void enterStructDeclaration(CParser.StructDeclarationContext ctx) {

    }

    @Override
    public void exitStructDeclaration(CParser.StructDeclarationContext ctx) {

    }

    @Override
    public void enterSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {

    }

    @Override
    public void exitSpecifierQualifierList(CParser.SpecifierQualifierListContext ctx) {

    }

    @Override
    public void enterStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {

    }

    @Override
    public void exitStructDeclaratorList(CParser.StructDeclaratorListContext ctx) {

    }

    @Override
    public void enterStructDeclarator(CParser.StructDeclaratorContext ctx) {

    }

    @Override
    public void exitStructDeclarator(CParser.StructDeclaratorContext ctx) {

    }

    @Override
    public void enterEnumSpecifier(CParser.EnumSpecifierContext ctx) {

    }

    @Override
    public void exitEnumSpecifier(CParser.EnumSpecifierContext ctx) {

    }

    @Override
    public void enterEnumeratorList(CParser.EnumeratorListContext ctx) {

    }

    @Override
    public void exitEnumeratorList(CParser.EnumeratorListContext ctx) {

    }

    @Override
    public void enterEnumerator(CParser.EnumeratorContext ctx) {

    }

    @Override
    public void exitEnumerator(CParser.EnumeratorContext ctx) {

    }

    @Override
    public void enterEnumerationConstant(CParser.EnumerationConstantContext ctx) {

    }

    @Override
    public void exitEnumerationConstant(CParser.EnumerationConstantContext ctx) {

    }

    @Override
    public void enterTypeQualifier(CParser.TypeQualifierContext ctx) {

    }

    @Override
    public void exitTypeQualifier(CParser.TypeQualifierContext ctx) {

    }

    @Override
    public void enterDeclarator(CParser.DeclaratorContext ctx) {

    }

    @Override
    public void exitDeclarator(CParser.DeclaratorContext ctx) {

    }

    @Override
    public void enterMaindeclarator(CParser.MaindeclaratorContext ctx) {

    }

    @Override
    public void exitMaindeclarator(CParser.MaindeclaratorContext ctx) {

    }

    @Override
    public void enterDirectDeclarator(CParser.DirectDeclaratorContext ctx) {

    }

    @Override
    public void exitDirectDeclarator(CParser.DirectDeclaratorContext ctx) {

    }

    @Override
    public void enterMaindirectDeclarator(CParser.MaindirectDeclaratorContext ctx) {

    }

    @Override
    public void exitMaindirectDeclarator(CParser.MaindirectDeclaratorContext ctx) {

    }

    @Override
    public void enterNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {

    }

    @Override
    public void exitNestedParenthesesBlock(CParser.NestedParenthesesBlockContext ctx) {

    }

    @Override
    public void enterPointer(CParser.PointerContext ctx) {

    }

    @Override
    public void exitPointer(CParser.PointerContext ctx) {

    }

    @Override
    public void enterTypeQualifierList(CParser.TypeQualifierListContext ctx) {

    }

    @Override
    public void exitTypeQualifierList(CParser.TypeQualifierListContext ctx) {

    }

    @Override
    public void enterParameterTypeList(CParser.ParameterTypeListContext ctx) {

    }

    @Override
    public void exitParameterTypeList(CParser.ParameterTypeListContext ctx) {

    }

    @Override
    public void enterParameterList(CParser.ParameterListContext ctx) {

    }

    @Override
    public void exitParameterList(CParser.ParameterListContext ctx) {

    }

    @Override
    public void enterParameterDeclaration(CParser.ParameterDeclarationContext ctx) {

    }

    @Override
    public void exitParameterDeclaration(CParser.ParameterDeclarationContext ctx) {

    }

    @Override
    public void enterIdentifierList(CParser.IdentifierListContext ctx) {

    }

    @Override
    public void exitIdentifierList(CParser.IdentifierListContext ctx) {

    }

    @Override
    public void enterTypeName(CParser.TypeNameContext ctx) {

    }

    @Override
    public void exitTypeName(CParser.TypeNameContext ctx) {

    }

    @Override
    public void enterTypedefName(CParser.TypedefNameContext ctx) {

    }

    @Override
    public void exitTypedefName(CParser.TypedefNameContext ctx) {

    }

    @Override
    public void enterInitializer(CParser.InitializerContext ctx) {

    }

    @Override
    public void exitInitializer(CParser.InitializerContext ctx) {

    }

    @Override
    public void enterInitializerList(CParser.InitializerListContext ctx) {

    }

    @Override
    public void exitInitializerList(CParser.InitializerListContext ctx) {

    }

    @Override
    public void enterDesignation(CParser.DesignationContext ctx) {

    }

    @Override
    public void exitDesignation(CParser.DesignationContext ctx) {

    }

    @Override
    public void enterDesignatorList(CParser.DesignatorListContext ctx) {

    }

    @Override
    public void exitDesignatorList(CParser.DesignatorListContext ctx) {

    }

    @Override
    public void enterDesignator(CParser.DesignatorContext ctx) {

    }

    @Override
    public void exitDesignator(CParser.DesignatorContext ctx) {

    }

    @Override
    public void enterStatement(CParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(CParser.StatementContext ctx) {

    }

    @Override
    public void enterLabeledStatement(CParser.LabeledStatementContext ctx) {

    }

    @Override
    public void exitLabeledStatement(CParser.LabeledStatementContext ctx) {

    }

    @Override
    public void enterCompoundStatement(CParser.CompoundStatementContext ctx) {

    }

    @Override
    public void exitCompoundStatement(CParser.CompoundStatementContext ctx) {

    }

    @Override
    public void enterBlockItemList(CParser.BlockItemListContext ctx) {

        String fieldType = null;
        String field = null;
        int length = 0;
        List<CParser.BlockItemContext> blockItemList = ctx.blockItem();

        for (CParser.BlockItemContext blockItem : blockItemList) {
            if (blockItem.declaration() != null) {

                tabCount++;
                String nTab = repeat(tabCount, tab);
                List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                fieldType = getType(declSpecCtxList);
                if (blockItem.declaration().initDeclaratorList() != null) {
                    List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                    CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                    field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                    if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
                        length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                    }
                } else {
                    field = blockItem.declaration().declarationSpecifiers().declarationSpecifier().get(1).getText();
                }

                if (length != 0) {
                    System.out.println(nTab + "field: " + field + "/ type: " + fieldType+ "/ length: " + length);
                    tabCount--;

                }
                else {
                    System.out.println(nTab + "field: " + field + "/ type: " + fieldType);
                    tabCount--;

                }
            }


        }


    }

    @Override
    public void exitBlockItemList(CParser.BlockItemListContext ctx) {
//    tabCount--;
    }

    @Override
    public void enterBlockItem(CParser.BlockItemContext ctx) {

    }

    @Override
    public void exitBlockItem(CParser.BlockItemContext ctx) {

    }

    @Override
    public void enterExpressionStatement(CParser.ExpressionStatementContext ctx) {

    }

    @Override
    public void exitExpressionStatement(CParser.ExpressionStatementContext ctx) {

    }

    @Override
    public void enterSelectionStatement(CParser.SelectionStatementContext ctx) {
        if (nested >= 1) {
            entry++;
            tabCount++;
            String nTab = repeat(tabCount, tab);
            System.out.println(nTab + "nested statement: {");

        } else {
            nested++;
        }
    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        if (entry >= 1) {
            entry--;
            String nTab = repeat(tabCount, tab);
            System.out.println(nTab + "}");
            nested--;
            tabCount--;
            if (entry == 0) {
                nested = 0;
            }
        }

    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        if (nested >= 1) {
            entry++;
            tabCount++;
            String nTab = repeat(tabCount, tab);
            System.out.println(nTab + "nested statement: {");

        } else {
            nested++;
        }
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        if (entry >= 1) {
            entry--;
            String nTab = repeat(tabCount, tab);
            System.out.println(nTab + "}");
            tabCount--;
            nested--;
            if (entry == 0) {
                nested = 0;
            }
        }

    }

    @Override
    public void enterForCondition(CParser.ForConditionContext ctx) {

    }

    @Override
    public void exitForCondition(CParser.ForConditionContext ctx) {

    }

    @Override
    public void enterForDeclaration(CParser.ForDeclarationContext ctx) {

    }

    @Override
    public void exitForDeclaration(CParser.ForDeclarationContext ctx) {

    }

    @Override
    public void enterForExpression(CParser.ForExpressionContext ctx) {

    }

    @Override
    public void exitForExpression(CParser.ForExpressionContext ctx) {

    }

    @Override
    public void enterJumpStatement(CParser.JumpStatementContext ctx) {

    }

    @Override
    public void exitJumpStatement(CParser.JumpStatementContext ctx) {

    }


    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        tabCount++;

        String nTab = repeat(tabCount, tab);
        List<CParser.DeclarationSpecifierContext> declSpecCtxList = ctx.declarationSpecifiers().declarationSpecifier();
        String fieldType = getType(declSpecCtxList);
        if (fieldType == null || fieldType == "void") {
            fieldType = "void(no return)";
        }
        //function CALL
        String name = ctx.declarator().directDeclarator().directDeclarator().Identifier().getText();
        System.out.println(nTab + "normal method: name: " + name + "/ return type: " + fieldType + "{");
        if(ctx.declarator().directDeclarator().parameterTypeList()!=null) {
            List<CParser.ParameterDeclarationContext> params = ctx.declarator().directDeclarator().parameterTypeList().parameterList().parameterDeclaration();
            for (int i = 0; i < functions.length; i++) {
                if (functions[i] == null) {
                    functions[i] = name;

                    break;
                }
            }
            //fin PARAMETERS
            StringBuilder paramsString = new StringBuilder();
            for (CParser.ParameterDeclarationContext param : params) {
                String paramName = param.declarator().directDeclarator().Identifier().getText();
                List<CParser.DeclarationSpecifierContext> paramdec = param.declarationSpecifiers().declarationSpecifier();
                String paramType = getType(paramdec);
                paramsString.append(paramName + " " + paramType + ", ");

            }
            // Remove the last ", " from the string
            paramsString.setLength(paramsString.length() - 2);
            tabCount++;
            System.out.println(repeat(tabCount, tab) + "parameter list: [" + paramsString.toString() + "]");
            tabCount--;
        }





    }


    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        String nTab = repeat(tabCount, tab);
        System.out.println(nTab + "}");
        tabCount--;
    }


    @Override
    public void enterMainfunctionDefinition(CParser.MainfunctionDefinitionContext ctx) {
        tabCount++;
        String nTab = repeat(tabCount, tab);
        //for the type
        List<CParser.DeclarationSpecifierContext> declSpecCtxList = ctx.declarationSpecifiers().declarationSpecifier();
        String fieldType = getType(declSpecCtxList);
        if (fieldType == null || fieldType == "void") {
            fieldType = "void(no return)";
        }
        System.out.println(nTab + "main method: return type: " + fieldType + "{");


    }

    @Override
    public void exitMainfunctionDefinition(CParser.MainfunctionDefinitionContext ctx) {

        String nTab = repeat(tabCount, tab);
        System.out.println(nTab + "}");
        tabCount--;
    }

    @Override
    public void enterDeclarationList(CParser.DeclarationListContext ctx) {

    }

    @Override
    public void exitDeclarationList(CParser.DeclarationListContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
