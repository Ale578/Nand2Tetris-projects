// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

// Initialize variable that stores whether there is a keyboard press or not


(CHECK)
    // Jump to CLEAR if no key is pressed
    @KBD
    D=M
    @CLEAR
    D;JEQ
    
    // Jump to BLACKEN if a key is pressed
    @KBD
    D=M
    @BLACKEN
    D;JGT

    @LOOP
    0;JMP

(CLEAR)
    // Make screen white
    @color
    M=0
    @CHANGE
    0;JMP    

(BLACKEN)
    // Make screen black
    @color
    M=-1
    @CHANGE
    0;JMP

(CHANGE)
    // Change all the pixels on the screen to the colour variable
    @SCREEN
    D=A
    @address
    M=D

    (LOOP)
        // If adress == 24576 go to CHECK
        @KBD
        D=A
        @address
        D=D-M
        @CHECK
        D;JEQ        

        // Else change pixels
        @color
        D=M
        @address
        A=M
        M=D

        //Update address
        @address
        M=M+1           

        @LOOP
        0;JMP

