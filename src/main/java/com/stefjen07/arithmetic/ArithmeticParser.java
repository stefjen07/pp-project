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
            default -> "";
        };
    }

    List<String> infixToPostfix(String expression) {
        List<String> result = new ArrayList<>();
        Stack<SpecialCharacter> operators = new Stack<>();
        String currentOperand = "";

        for(int i=0;i<expression.length();i++) {
            char c = expression.charAt(i);

            if(!String.valueOf(c).matches("[0-9]")) {
                result.add(currentOperand);
                currentOperand = "";
            }
            switch (c) {
                case '+' -> operators.push(SpecialCharacter.add);
                case '-' -> operators.push(SpecialCharacter.subtract);
                case '*' -> {
                    while(operators.peek() == SpecialCharacter.add || operators.peek() == SpecialCharacter.subtract)
                        result.add(specialCharacterToString(operators.pop()));

                    operators.push(SpecialCharacter.multiply);
                }
                case '/' -> {
                    while(operators.peek() == SpecialCharacter.add || operators.peek() == SpecialCharacter.subtract)
                        result.add(specialCharacterToString(operators.pop()));

                    operators.push(SpecialCharacter.divide);
                }
                case '(' -> operators.push(SpecialCharacter.openParentheses);
                case ')' -> {
                    SpecialCharacter lastOperator = operators.pop();
                    while (lastOperator != SpecialCharacter.openParentheses) {

                        lastOperator = operators.pop();
                    }
                }
                default -> currentOperand = currentOperand.concat(String.valueOf(c));
            }
        }

        while (!operators.isEmpty()) {
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
        };
    }

    ArithmeticExpression getOperand(List<String> postfix) {
        if(isOperator(postfix.get(postfix.size() - 1))) {
            Operator operator = operatorFromString(postfix.get(postfix.size()-1));
            postfix.remove(postfix.size()-1);

            return new ArithmeticExpression(getOperand(postfix), getOperand(postfix), operator);
        } else {
            int value = Integer.parseInt(postfix.get(postfix.size()-1));
            postfix.remove(postfix.size()-1);

            return new ArithmeticExpression(value);
        }
    }

    public int parse(String infix) {
        List<String> postfix = infixToPostfix(infix);

        return getOperand(postfix).getValue();
    }
}
