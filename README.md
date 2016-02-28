# An application of NEAT in flappy bird - behind the scenes 
 
When my friend and me saw [this](https://www.youtube.com/watch?v=qv6UVOQ0F44) video about a neural network evolving to beat a level in super mario we were amazed and we though how cool it would be to make something similar. At the same time we were tasked at our college to make a program using some form of artificial intelligence. The game was on. 
 
For about 2 weeks we leisurely compiled data about NEAT. We read the paper and made a shared note where we could post all relevant content. On the very informal [website](https://www.cs.ucf.edu/~kstanley/neat.html) of NEAT creator Dr. Kenneth O. Stanley we found a great book: "AI Techniques for Game Programming" by Mat Buckland that had excellent chapters on NEAT and Genetic algorithms. Funny thing is even though we finished Andrew Ng's course on machine learning, we knew next to nothing of genetic algorithms and neural networks. So we implemented a simple path finding genetic algorithm to get started.  
 
After that we found another book that dealt solely with neuroevolution: "Handbook of neuroevolution through Erlang" by Gene I. Sher, we read through about half the book and picked up very useful concepts. Like: how exactly do real neuron behave? How does a recurrent neural network work? What are the benefits and disadvantages of crossover? And what types of learning there are?  
 
All of this research took about a month, and there was only one month left for the implementation. And of course the students we are, we thought that was plenty of time.  
 
After much deliberation between us and our TA we came to a conclusion that because of its simple mechanics and implementation flappy bird was the best candidate for our algorithm. We decided on Java as our language of choice because of our familiarity with it, also we already had a simple game engine made by a TA from a computer graphics course in Java.  
 
The implementation was easy, a bird and terrain updating position, checking collision and drawing in every frame. Because of other commitments it took about a week to finish. 3 weeks left.

The following week we tried to figure out what kind of architecture our neural network should take. We decided on this: (Image of Neuron and synapse classes)
We implemented five mutations:
-  Add neuron
-  Remove neuron
-  Change weight
-  Add synapse
-  Splice

The next thing we had to figure out was a way to make copies of the neural network, because it was so interconnected and random we wrote a copy method combining BFS with a hash map containing the copied elements. It worked like this: (A very cool image)
Then we had to tie it in with a genetic part that would select the best birds based on fitness and combine them into new birds. All of this took a long time because of our busy student schedules. 2 Days left.
We would give the network the birds position relative to the column, the columns position relative to the bird, and the distance from the column to the bird in X axis. Now the "signals" could propagate through the network giving an output between 0 and 1. If the value was above the threshold then it would flap, otherwise it wouldn't. All 200 birds would have to do this. Every frame. You would think this was a lot of computation, but it wasn't. It ran very fluidly in my i5 processor laptop. (why did it run so fluid). 12 Hours left.

It didn't work. We were franticly going through every part of the code trying to find our mistake. But there weren't any, the copy was fine, the mutation too, the birds would flap but after a 100 generations they couldn't even pass three columns. Maybe the problem wasn’t in our network? It wasn’t, it was the way we were passing our parameters. 3 Hours left.

We forgot to normalize them. We would pass the birds positions in pixels, and it was several orders of magnitude larger than what our network could accept (It could accept larger values if the weights connected to the inputs got sufficiently small but at the time we were changing the weights at random so it would take a lot of luck for this to happen) . We just had to normalize them between 0 and 1 and then after about 20-30 generations a single bird could pass around 50 columns. We were overjoyed. We submitted the project and got maximum points.


TODO: reci nesto o sigmoidu, dodati specijaciju iako je nismo implementirali?, bolji zakljucak
