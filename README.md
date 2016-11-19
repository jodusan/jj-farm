# An application of NEAT in flappy bird
FlappyBird played by NEAT. Every bird has its own ANN and a fitness score that is measured by distance/time passed. For each generation top 50% of population is preserved and possibly mutated while the rest is discarded. Mutations can change ANN of each bird in various ways. 

 ![jj-farm](https://media.giphy.com/media/R5NDdelv6dzpe/giphy.gif)

# Install
*Windows: If you haven't already, you need to add javac to path like [here](http://stackoverflow.com/questions/37973276/how-to-run-a-java-program-in-cmd)*

```sh
git clone https://github.com/dulex123/jj-farm
cd jj-farm
javac game/FlappyBird.java
```

# Run
When running on Ubuntu there will be a little bit of tearing.
```sh
# Windows
java game.FlappyBird

# For much smoother experience on *nix run with opengl 
java -Dsun.java2d.opengl=true game.FlappyBird
```

# Notes
 - It is not guaranteed that birds will evolve to play the game successfuly
 - If you are interested in NEAT take a look at these books:
   - "Handbook of Neuroevolution Through Erlang" - Gene I. Sher
   - "Ai Techniques For Game Programming" - Mat Buckland
   
# Credits

Released under the [MIT License].<br>
Authored and maintained by Dušan Josipović & Nikola Jovičić.

> Blog [dulex123.github.io](http://dulex123.github.io) &nbsp;&middot;&nbsp;
> GitHub [@dulex123](https://github.com/dulex123) &nbsp;&middot;&nbsp;
> Twitter [@josipovicd](https://twitter.com/josipovicd)

> [nikolajovicic.com](http://jovicicnikola.com/)&nbsp;&middot;&nbsp;
> GitHub [@killx94](https://github.com/killx94) 


[MIT License]: http://mit-license.org/
