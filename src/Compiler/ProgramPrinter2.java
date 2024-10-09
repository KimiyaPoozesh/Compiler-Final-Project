package Compiler;


import gen.CListener;
import gen.CParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgramPrinter2 implements CListener {

    ArrayList<Scope> scopes = new ArrayList<>();
    private static int nested = 0;

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
        Scope scope = new Scope("program", ctx.start.getLine(), Type.PROGRAM);
        scopes.add(scope);
//        System.out.println();
        String value = "";
        String name = "main";
        List<CParser.DeclarationSpecifierContext> declSpecCtxList = ctx.mainfunctionDefinition().declarationSpecifiers().declarationSpecifier();
        String fieldType = getType(declSpecCtxList);
        if (fieldType == null) {
            fieldType = "void";
        }
        value = String.format("Method (name : %s) (return type: %s)", name, fieldType);
        scope.insert("Method_" + name, new SymbolTableItem(ctx.mainfunctionDefinition().start.getLine(), value));

        for (int i = 0; i < ctx.functionDefinition().size(); i++) {

            value = "";
            name = ctx.functionDefinition().get(i).declarator().directDeclarator().directDeclarator().Identifier().getText();
            declSpecCtxList = ctx.functionDefinition().get(i).declarationSpecifiers().declarationSpecifier();
            fieldType = getType(declSpecCtxList);
            if (fieldType == null) {
                fieldType = "void";
            }
            if (ctx.functionDefinition().get(i).declarator().directDeclarator().parameterTypeList() != null) {
                List<CParser.ParameterDeclarationContext> params = ctx.functionDefinition().get(i).declarator().directDeclarator().parameterTypeList().parameterList().parameterDeclaration();
                String para = "[parameter list: ";
                for (int param = 0; param < params.size(); param++) {
                    if (param == 0)
                        para += getParamameters(params.get(param), param);
                    else
                        para += ", " + getParamameters(params.get(param), param);
                }
                para += "]";
                value = String.format("Method (name : %s) (return type: %s)", name, fieldType) + " " + para;

                scope.insert("Method_" + name, new SymbolTableItem(ctx.functionDefinition().get(i).start.getLine(), value));
            }
        }
    }

    public String getParamameters(CParser.ParameterDeclarationContext param, int idx) {
        List<CParser.DeclarationSpecifierContext> paramdec = param.declarationSpecifiers().declarationSpecifier();
        String paramType = getType(paramdec);
        if (param.declarator().directDeclarator().LeftBracket().size() == 0)
            return String.format("[type: %s, index: %d]", paramType, idx);
        else
            return String.format("[type: %s array, index: %d]", paramType, idx);
    }

    @Override
    public void exitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        for (Scope scope : scopes) {
            System.out.println(scope.toString());
        }

    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        String name = ctx.declarator().directDeclarator().directDeclarator().Identifier().getText();
        Scope scope = new Scope(name, ctx.start.getLine(), Type.FUNCTION);
        scopes.add(scope);
        for (int i = scopes.size() - 2; i >= 0; i--) {
            if (scopes.get(i).getType() == Type.PROGRAM) {
                scope.setParent(scopes.get(i));
                scopes.get(i).getChildern().add(scope);
                break;
            }
        }
        if (ctx.declarator().directDeclarator().parameterTypeList() != null) {
            List<CParser.ParameterDeclarationContext> params = ctx.declarator().directDeclarator().parameterTypeList().parameterList().parameterDeclaration();
            for (int param = 0; param < params.size(); param++) {
                String name1 = params.get(param).declarator().directDeclarator().Identifier().getText();
                List<CParser.DeclarationSpecifierContext> paramdec = params.get(param).declarationSpecifiers().declarationSpecifier();
                String paramType = getType(paramdec);
                String value = String.format("methodParamField(name: %s) (type: %s)", name1, paramType);
                scope.insert("Field_" + name1, new SymbolTableItem(ctx.start.getLine(), value));
            }
        }
        String fieldType = null;
        String field = null;
        int length = 0;
        List<CParser.BlockItemContext> blockItemList = ctx.compoundStatement().blockItemList().blockItem();
        for (CParser.BlockItemContext blockItem : blockItemList) {
            if (blockItem.declaration() != null) {
                List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                fieldType = getType(declSpecCtxList);
                if (blockItem.declaration().initDeclaratorList() != null) {
                    List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                    CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                    field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                    if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                        List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                        length = initializerList.size();
                        length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                    }
                }
                String key = "Field_" + field;
                String value = "";
                if (length == 0)
                    value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                else
                    value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                scope.insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                break;
            }
        }
    }

    @Override
    public void exitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {

    }

    @Override
    public void enterMainfunctionDefinition(CParser.MainfunctionDefinitionContext ctx) {
        String name = "main";
        Scope scope = new Scope(name, ctx.start.getLine(), Type.MAIN);
        scopes.add(scope);
        for (int i = scopes.size() - 2; i >= 0; i--) {
            if (scopes.get(i).getType() == Type.PROGRAM) {
                scope.setParent(scopes.get(i));
                scopes.get(i).getChildern().add(scope);
                break;
            }
        }
        String fieldType = null;
        String field = null;
        int length = 0;
        List<CParser.BlockItemContext> blockItemList = ctx.compoundStatement().blockItemList().blockItem();
        for (CParser.BlockItemContext blockItem : blockItemList) {
            if (blockItem.declaration() != null) {
                List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                fieldType = getType(declSpecCtxList);
                if (blockItem.declaration().initDeclaratorList() != null) {
                    List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                    CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                    field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                    if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                        List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                        length = initializerList.size();
                        length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                    }
                }
                String key = "Field_" + field;
                String value = "";
                if (length == 0)
                    value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                else
                    value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                scope.insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                length = 0;

            }
        }

    }

    @Override
    public void exitMainfunctionDefinition(CParser.MainfunctionDefinitionContext ctx) {

    }

    @Override
    public void enterPrimaryExpression(CParser.PrimaryExpressionContext ctx) {


    }

    @Override
    public void exitPrimaryExpression(CParser.PrimaryExpressionContext ctx) {

    }

    @Override
    public void enterPostfixExpression(CParser.PostfixExpressionContext ctx) {

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

    }

    @Override
    public void exitBlockItemList(CParser.BlockItemListContext ctx) {

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
            String name = "nested";
            Scope scope = new Scope(name, ctx.start.getLine(), Type.NESTED);
            scopes.add(scope);
            for (int i = scopes.size() - 2; i >= 0; i--) {
                if (scopes.get(i).getType() == Type.FUNCTION || scopes.get(i).getType() == Type.MAIN) {
                    scope.setParent(scopes.get(i));
                    scopes.get(i).getChildern().add(scope);
                    break;
                }
            }
            String fieldType = null;
            String field = null;
            int length = 0;
            for (CParser.StatementContext statement : ctx.statement()) {
                List<CParser.BlockItemContext> blockItemList = statement.compoundStatement().blockItemList().blockItem();
                for (CParser.BlockItemContext blockItem : blockItemList) {
                    if (blockItem.declaration() != null) {
                        List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                        fieldType = getType(declSpecCtxList);
                        if (blockItem.declaration().initDeclaratorList() != null) {
                            List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                            CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                            field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                            if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                                List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                                length = initializerList.size();
                                length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                            }
                        }
                        String key = "Field_" + field;
                        String value = "";
                        if (length == 0)
                            value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                        else
                            value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                        scope.insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                        length = 0;
                    }
                }
            }
        } else {
            int i;
            for (i = scopes.size() - 1; i >= 0; i--) {
                if (scopes.get(i).getType() == Type.FUNCTION || scopes.get(i).getType() == Type.MAIN) {
                    break;
                }
            }
            String fieldType = null;
            String field = null;
            int length = 0;
            for (CParser.StatementContext statement : ctx.statement()) {
                List<CParser.BlockItemContext> blockItemList = statement.compoundStatement().blockItemList().blockItem();
                for (CParser.BlockItemContext blockItem : blockItemList) {
                    if (blockItem.declaration() != null) {
                        List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                        fieldType = getType(declSpecCtxList);
                        if (blockItem.declaration().initDeclaratorList() != null) {
                            List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                            CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                            field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                            if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                                List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                                length = initializerList.size();
                                length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                            }
                        }
                        String key = "Field_" + field;
                        String value = "";
                        if (length == 0)
                            value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                        else
                            value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                        scopes.get(i).insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                        length = 0;
                    }
                }
            }
        }
        nested++;

    }

    @Override
    public void exitSelectionStatement(CParser.SelectionStatementContext ctx) {
        nested--;
    }

    @Override
    public void enterIterationStatement(CParser.IterationStatementContext ctx) {
        if (nested >= 1) {
            String name = "nested";
            Scope scope = new Scope(name, ctx.start.getLine(), Type.NESTED);
            scopes.add(scope);
            for (int i = scopes.size() - 2; i >= 0; i--) {
                if (scopes.get(i).getType() == Type.FUNCTION || scopes.get(i).getType() == Type.MAIN) {
                    scope.setParent(scopes.get(i));
                    scopes.get(i).getChildern().add(scope);
                    break;
                }
            }
            String fieldType = null;
            String field = null;
            int length = 0;
            List<CParser.BlockItemContext> blockItemList = ctx.statement().compoundStatement().blockItemList().blockItem();
            for (CParser.BlockItemContext blockItem : blockItemList) {
                if (blockItem.declaration() != null) {
                    List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                    fieldType = getType(declSpecCtxList);
                    if (blockItem.declaration().initDeclaratorList() != null) {
                        List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                        CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                        field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                        if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                            List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                            length = initializerList.size();
                            length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                        }
                    }
                    String key = "Field_" + field;
                    String value = "";
                    if (length == 0)
                        value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                    else
                        value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                    scope.insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                    length = 0;
                }
            }
        } else {
            int i;
            for (i = scopes.size() - 1; i >= 0; i--) {
                if (scopes.get(i).getType() == Type.FUNCTION || scopes.get(i).getType() == Type.MAIN) {
                    break;
                }
            }
            String fieldType = null;
            String field = null;
            int length = 0;
            List<CParser.BlockItemContext> blockItemList = ctx.statement().compoundStatement().blockItemList().blockItem();
            for (CParser.BlockItemContext blockItem : blockItemList) {
                if (blockItem.declaration() != null) {
                    List<CParser.DeclarationSpecifierContext> declSpecCtxList = blockItem.declaration().declarationSpecifiers().declarationSpecifier();
                    fieldType = getType(declSpecCtxList);
                    if (blockItem.declaration().initDeclaratorList() != null) {
                        List<CParser.InitDeclaratorContext> initDeclaratorList = blockItem.declaration().initDeclaratorList().initDeclarator();
                        CParser.InitDeclaratorContext initDeclarator = initDeclaratorList.get(0);
                        field = initDeclarator.declarator().directDeclarator().Identifier().getText();
                        if (initDeclarator.declarator().directDeclarator().LeftBracket().size() > 0) {
//                            List<CParser.InitializerContext> initializerList = initDeclarator.initializer().initializerList().initializer();
//                            length = initializerList.size();
                            length = Integer.parseInt(initDeclarator.declarator().directDeclarator().Constant().get(0).getText());
                        }
                    }
                    String key = "Field_" + field;
                    String value = "";
                    if (length == 0)
                        value = String.format("methodField(name: %s) (type: %s)", field, fieldType);
                    else
                        value = String.format("methodField(name: %s) (type: %s array, length= %d)", field, fieldType, length);
                    scopes.get(i).insert(key, new SymbolTableItem(blockItem.start.getLine(), value));
                    length = 0;
                }
            }
        }
        nested++;
    }

    @Override
    public void exitIterationStatement(CParser.IterationStatementContext ctx) {
        nested--;
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
