package com.stefjen07.arithmetic;

public class ArithmeticExpression {
    double value;
    boolean isConstant;

    ArithmeticExpression operand1;
    ArithmeticExpression operand2;
    Operator operator;

    ArithmeticExpression(double value) {
        this.value = value;
        this.isConstant = true;
    }

    ArithmeticExpression(ArithmeticExpression operand1, ArithmeticExpression operand2, Operator operator) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
    }

    public double getValue() {
        return isConstant ? value : switch (operator) {
            case add -> operand1.getValue() + operand2.getValue();
            case subtract -> operand1.getValue() - operand2.getValue();
            case multiply -> operand1.getValue() * operand2.getValue();
            case divide -> operand1.getValue() / operand2.getValue();
        };
    }
}
