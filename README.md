# An application of NEAT in flappy bird - behind the scenes 
 
When my friend and me saw [this](https://www.youtube.com/watch?v=qv6UVOQ0F44) video about a neural network evolving to beat a level in super mario we were amazed and we though how cool it would be to make something similar. At the same time we were tasked at our college to make a program using some form of artificial intelligence. The game was on. 
 
For about 2 weeks we leisurely compiled data about NEAT. We read the paper and made a shared note where we could post all relevant content. On the very informal [website](https://www.cs.ucf.edu/~kstanley/neat.html) of NEAT creator Dr. Kenneth O. Stanley we found a great book: "AI Techniques for Game Programming" by Mat Buckland that had excellent chapters on NEAT and Genetic algorithms. Funny thing is even though we finished Andrew Ng's course on machine learning, we knew next to nothing of genetic algorithms and neural networks. So we implemented a simple path finding genetic algorithm to get started.  
 
After that we found another book that dealt solely with neuroevolution: "Handbook of neuroevolution through Erlang" by Gene I. Sher, we read through about half the book and picked up very useful concepts. Like how exactly do real neuron behave, how does a recurrent neural network work, what are the benefits and disadvantages of crossover and what types of learning there are.  
 
All of this research took about a month, and there was only one month left for the implementation. And of course the students we are, we thought that was plenty of time.  
 
After much deliberation between us and our TA we came to a conclusion that because of its simple mechanics and implementation flappy bird was the best candidate for our algorithm. We decided on Java as our language of choice because of our familiarity with it, also we already had a simple game engine made by a TA from a computer graphics course in Java.  
 
The implementation was easy, a bird and terrain updating position, checking collision and drawing in every frame. Because of school schedule it took about a week to finish. 3 weeks left.

The following week we tried to figure out what kind of architecture our neural network should take. We decided on this: (Image of Neuron and synapse classes)
We implemented five mutations:- Add neuron
                              - Remove neuron
                              - Change weigth
                              - Add synapse
                              - Splice

