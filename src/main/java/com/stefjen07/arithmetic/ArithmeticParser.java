package com.stefjen07.arithmetic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class ArithmeticParser {

    static String specialCharacterToString(SpecialCharacter c) {
        switch (c) {
            case add: return "+";
            case subtract: return "-";
            case multiply: return "*";
            case divide: return "/";
            case exponentiate: return "^";
            default: return null;
        }
    }

    int priority(SpecialCharacter character) {
        switch (character) {
            case add:
            case subtract:
                return 1;
            case multiply:
            case divide:
                return 2;
            case exponentiate:
                return 3;
            default:
                return 0;
        }
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

            boolean isUnary = false;
            switch (c) {
                case '+':
                case '-':
                    if (i == 0 || String.valueOf(expression.charAt(i - 1)).matches("[(+\\-*/^]")) {
                        isUnary = true;
                        result.add("0");
                    }
                case '*':
                case '/':
                case '^':
                    SpecialCharacter specialCharacter;
                    switch (c) {
                        case '+': specialCharacter = SpecialCharacter.add; break;
                        case '-': specialCharacter = SpecialCharacter.subtract; break;
                        case '*': specialCharacter = SpecialCharacter.multiply; break;
                        case '/': specialCharacter = SpecialCharacter.divide; break;
                        case '^': specialCharacter = SpecialCharacter.exponentiate; break;
                        default: throw new RuntimeException();
                    }

                    while (!isUnary && !operators.isEmpty() && priority(operators.peek()) >= priority(specialCharacter))
                        result.add(specialCharacterToString(operators.pop()));

                    operators.push(specialCharacter);

                    break;
                case '(':
                    operators.push(SpecialCharacter.openParentheses);
                    break;
                case ')':
                    SpecialCharacter lastOperator = operators.pop();
                    while (lastOperator != SpecialCharacter.openParentheses) {
                        result.add(specialCharacterToString(lastOperator));
                        lastOperator = operators.pop();
                    }
                    break;
                default:
                    currentOperand = currentOperand.concat(String.valueOf(c));
                    break;
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
        switch(text) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "^":
                return true;
            default: return false;
        }
    }

    static Operator operatorFromString(String text) {
        switch(text) {
            case "+": return Operator.add;
            case "-": return Operator.subtract;
            case "*": return Operator.multiply;
            case "/": return Operator.divide;
            case "^": return Operator.exponentiate;
            default: throw new RuntimeException();
        }
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
