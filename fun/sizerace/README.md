sizerace
========

You are given a finite set of elements, each of given size (which takes one of the predefined positive values).An element is removed with probability equal to its size divided by the total size of all elements in the set. This process is then repeated until the set is empty.

We now ask: in what order do we run out of elements of any given size? What is the most probable outcome? The program here investigates the problem for the simplest case when there are only two possible sizes. It appears that the cases when either of the sizes has approximately equal probability of being eliminated first follow a quadratic relation between the initial number of elements of either size.
