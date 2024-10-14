// Accesing the textArea by its id i.e outArea as display.
const display = document.getElementById("outArea");

// Adding user inputs in display. Also adding Scroll effect in the display if inputs extends.
function appendToDisplay(input){
    display.value += input;
    display.scrollLeft = display.scrollWidth;
}

// To Clear all the user inputs from display.
function clearDisplay(){
    display.value = "";
}

// To delete a user inputs from display.
function clearAnInput(){
    display.value = display.value.slice(0, -1);
}

// Using the In-Built eval(String) function for calculation. (Learned on MDN doc)
// As eval() may give some error caused by wrong input so i'm wraping it into try-catch block.
function Result(){
    try {
        display.value = eval?.(`"use strict";(${display.value})`); //Indirect eval.
    }
    catch(err){
        display.value = "Error";
    }
}

// hidding the virtual mouse for display.
display.addEventListener("mousedown", function(event) {
    event.preventDefault();
});