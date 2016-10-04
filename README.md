# An application of NEAT in flappy bird
FlappyBird played by NEAT. Every bird has its own ANN and a fitness score that is measured by distance/time passed.

For each generation top 50% of population is preserved and possibly mutated while the rest is discarded.

Mutations can change ANN of each bird in various ways. 

 ![jj-farm](https://media.giphy.com/media/R5NDdelv6dzpe/giphy.gif)

# Installation and execution
*Windows: If you haven't already, you need to add javac to path like [here](http://stackoverflow.com/questions/37973276/how-to-run-a-java-program-in-cmd)*

```sh
git clone https://github.com/dulex123/jj-farm
cd jj-farm
javac game/FlappyBird.java
java game.FlappyBird
```

# Notes
 - It is not guaranteed that birds will evolve to play the game successfuly 
 - If you are interested in NEAT take a look at :
   - "Handbook of Neuroevolution Through Erlang" - Gene I. Sher
   - "Ai Techniques For Game Programming" - Mat Buckland
