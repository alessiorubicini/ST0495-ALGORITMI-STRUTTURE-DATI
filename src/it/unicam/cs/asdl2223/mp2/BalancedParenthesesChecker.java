package it.unicam.cs.asdl2223.mp2;

import java.util.Collections;
import java.util.Iterator;

/**
 * An object of this class is an actor that uses an ASDL2223Deque<Character> as
 * a Stack in order to check that a sequence containing the following
 * characters: '(', ')', '[', ']', '{', '}' in any order is a string of balanced
 * parentheses or not. The input is given as a String in which white spaces,
 * tabs and newlines are ignored.
 * 
 * Some examples:
 * 
 * - " (( [( {\t (\t) [ ] } ) \n ] ) ) " is a string o balanced parentheses - " " is a
 * string of balanced parentheses - "(([)])" is NOT a string of balanced
 * parentheses - "( { } " is NOT a string of balanced parentheses - "}(([]))" is
 * NOT a string of balanced parentheses - "( ( \n [(P)] \t ))" is NOT a string
 * of balanced parentheses
 * 
 * @author Template: Luca Tesei, Implementation: Alessio Rubicini alessio.rubicini@studenti.unicam.it
 *
 */
public class BalancedParenthesesChecker {

    // The stack is to be used to check the balanced parentheses
    private ASDL2223Deque<Character> stack;

    /**
     * Create a new checker.
     */
    public BalancedParenthesesChecker() {
        this.stack = new ASDL2223Deque<Character>();
    }

    /**
     * Check if a given string contains a balanced parentheses sequence of
     * characters '(', ')', '[', ']', '{', '}' by ignoring white spaces ' ',
     * tabs '\t' and newlines '\n'.
     * 
     * @param s
     *              the string to check
     * @return true if s contains a balanced parentheses sequence, false
     *         otherwise
     * @throws IllegalArgumentException
     *                                      if s contains at least a character
     *                                      different form:'(', ')', '[', ']',
     *                                      '{', '}', white space ' ', tab '\t'
     *                                      and newline '\n'
     */
    public boolean check(String s) {
        // Clears the stack
        this.stack.clear();
        // Iterate through the string starting from the first character
        for (int i = 0; i < s.length(); i++) {
            // Get the character
            char character = s.charAt(i);
            // Check if the character matches a not allowed one
            if(character != '(' && character != ')' && character != '[' && character != ']' && character != '{'
                    && character != '}' && character != ' ' && character != '\t' && character != '\n') {
                throw new IllegalArgumentException("La stringa contiene caratteri non ammessi");
            }
            // If the character is an opening bracket
            if (character == '(' || character == '[' || character == '{') {
                // Push the opening bracket to the top of the stack
                this.stack.push(character);
                // Since this is an opening bracket, move on to the next iteration
                // and ignore the closing brackets checks
                continue;
            }
            // Ignore additional characters
            if (character == ' ' || character == '\t' || character == '\n') continue;
            // If there are no opening brackets, then there are closing brackets, so the string is not balanced
            if (this.stack.isEmpty()) return false;
            // Check if the closing brackets matches the last opening
            if(character == ')') {
                if (stack.peekFirst() == '{' || stack.peekFirst() == '[') return false;
                this.stack.removeFirst();
            }
            if(character == '}') {
                if (stack.peekFirst() == '(' || stack.peekFirst() == '[') return false;
                this.stack.removeFirst();
            }
            if(character == ']') {
                if (stack.peekFirst() == '(' || stack.peekFirst() == '{') return false;
                this.stack.removeFirst();
            }
        }
        // If the stack is empty, then the string is balanced
        return (this.stack.isEmpty());
    }

}
