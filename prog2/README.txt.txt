done alone
in package parse the parseExp() parse the expression and calls the parseRest() if 
it is a cons node starting with (. the parseRest() returns a cons Node. 

for the special package I only define regular form.

The readToken and parse works correctly but the pretty print prints on every 
single line and not indented.