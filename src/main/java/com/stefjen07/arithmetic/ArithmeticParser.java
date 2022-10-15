package com.stefjen07.arithmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ArithmeticParser {

    static String specialCharacterToString(SpecialCharacter c) {
        return switch (c) {
            case add -> "+";
            case subtract -> "-";
            case multiply -> "*";
            case divide -> "/";
            default -> null;
        };
    }

    int priority(SpecialCharacter character) {
        return switch (character) {
            case add, subtract -> 1;
            case multiply, divide -> 2;
            default -> 0;
        };
    }

    List<String> infixToPostfix(String expression) {
        List<String> result = new ArrayList<>();
        Stack<SpecialCharacter> operators = new Stack<>();
        String currentOperand = "";

        for(int i=0;i<expression.length();i++) {
            char c = expression.charAt(i);

            if(i > 0 && String.valueOf(c).matches("[0-9(]") && expression.charAt(i-1) == ')') {
                while(!operators.isEmpty() && priority(operators.peek()) >= priority(SpecialCharacter.multiply))
                    result.add(specialCharacterToString(operators.pop()));

                operators.push(SpecialCharacter.multiply);
            }

            if(!String.valueOf(c).matches("[0-9]") && !currentOperand.isEmpty()) {
                result.add(currentOperand);
                currentOperand = "";
            }

            switch (c) {
                case '+', '-', '*', '/' -> {
                    if (c == '-' && (i == 0 || String.valueOf(c).matches("[0-9)]"))) {
                        result.add("0");
                    }

                    SpecialCharacter specialCharacter = switch (c) {
                        case '+' -> SpecialCharacter.add;
                        case '-' -> SpecialCharacter.subtract;
                        case '*' -> SpecialCharacter.multiply;
                        case '/' -> SpecialCharacter.divide;
                        default -> throw new RuntimeException();
                    };
                    while (!operators.isEmpty() && priority(operators.peek()) >= priority(specialCharacter))
                        result.add(specialCharacterToString(operators.pop()));

                    operators.push(specialCharacter);
                }
                case '(' -> operators.push(SpecialCharacter.openParentheses);
                case ')' -> {
                    SpecialCharacter lastOperator = operators.pop();
                    while (lastOperator != SpecialCharacter.openParentheses) {
                        result.add(specialCharacterToString(lastOperator));
                        lastOperator = operators.pop();
                    }
                }
                default -> currentOperand = currentOperand.concat(String.valueOf(c));
            }
        }

        if(!currentOperand.isEmpty()) {
            result.add(currentOperand);
        }

        while (!operators.isEmpty()) {
            if(operators.peek() == SpecialCharacter.openParentheses) {
                operators.pop();
                continue;
            }

            result.add(specialCharacterToString(operators.pop()));
        }

        return result;
    }

    static boolean isOperator(String text) {
        return switch(text) {
            case "+", "-", "*", "/" -> true;
            default -> false;
        };
    }

    static Operator operatorFromString(String text) {
        return switch(text) {
            case "+" -> Operator.add;
            case "-" -> Operator.subtract;
            case "*" -> Operator.multiply;
            case "/" -> Operator.divide;
            default -> throw new RuntimeException();
        };
    }

    ArithmeticExpression getOperand(List<String> postfix) {
        if(isOperator(postfix.get(postfix.size() - 1))) {
            Operator operator = operatorFromString(postfix.get(postfix.size()-1));
            postfix.remove(postfix.size()-1);

            ArithmeticExpression operand2 = getOperand(postfix);
            ArithmeticExpression operand1 = getOperand(postfix);

            return new ArithmeticExpression(operand1, operand2, operator);
        } else {
            double value = Double.parseDouble(postfix.get(postfix.size()-1));
            postfix.remove(postfix.size()-1);

            return new ArithmeticExpression(value);
        }
    }

    public double parse(String infix) {
        List<String> postfix = infixToPostfix(infix.replaceAll("\\s+",""));

        return getOperand(postfix).getValue();
    }
}
