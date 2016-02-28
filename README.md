# An application of NEAT in flappy bird - behind the scenes

When my friend and me saw [this](https://www.youtube.com/watch?v=qv6UVOQ0F44) video about a neural network evolving to beat a level in super mario we were amazed so we though how cool it would be to make something similar. At the same time we were tasked at our college to make a program using some form of artificial intelligence. The game was on.

For about 2 weeks we lesurly compiled data about NEAT. We read the paper and made a shared note where we could post all relevant content. On the very informal [website](https://www.cs.ucf.edu/~kstanley/neat.html) of NEAT creator Dr. Kenneth O. Stanley we found a great book: "AI Techniques for Game Programming" by Mat Buckland that had excellent chapters on NEAT and Genetic alghorithms. Funny thing is even though we finished Andrew Ng's course on machine learning, we knew next to nothing of genetic algorithms and neural networks. So we implemented a simple pathfinding genetic algorithm to get started. 

After that we found another book that dealt solely with neuroevolution: "Handbook of neuroevolution through Erlang" by Gene I. Sher, we read through about half the book and picked up very useful concepts. Like how exactly do real neuron behave, how does a reccurent neural network work, what are the benefits and disadvanages of crosover and what types of learning there are. 

All of this reaserch took about a month, and there was only one month left for the implementation. And of course the students we are, we thought that was plenty of time. 

After much deliberation between us and our TA we came to a conclusion that because of its simple mechanics and implementation flappy bird was the best candidate for our algorithm. We decided on Java as our languge of choice because of our familiarity with it, also we already had a simple game engine made by a TA from a computer graphics course in Java. 
