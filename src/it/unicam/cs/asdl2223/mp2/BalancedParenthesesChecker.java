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
        // Pulisce lo stack
        this.stack.clear();
        // Itera sulla stringa dal primo carattere
        for (int i = 0; i < s.length(); i++) {
            char character = s.charAt(i);
            // Controlla la presenza di caratteri non ammessi
            if(character != '(' && character != ')' && character != '[' && character != ']' && character != '{'
                    && character != '}' && character != ' ' && character != '\t' && character != '\n') {
                throw new IllegalArgumentException("La stringa contiene caratteri non ammessi");
            }
            // Se il carattere è una parentesi aperta
            if (character == '(' || character == '[' || character == '{') {
                // Push della parentesi aperta in testa allo stack
                this.stack.push(character);
                // Trattandosi di una parentesi aperta, ignora i controlli delle parentesi chiuse
                continue;
            }
            // Ignora caratteri aggiuntivi
            if (character == ' ' || character == '\t' || character == '\n') continue;
            // Se non ci sono parentesi aperte, allora ci sono parentesi chiuse, quindi la stringa non è bilanciata
            if (this.stack.isEmpty()) return false;
            // Controlla se la parentesi chiusa combacia con l'ultima aperta
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
        // Se lo stack è vuoto, allora la stringa è bilanciata
        return (this.stack.isEmpty());
    }

}
