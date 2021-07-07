Sharing resources between multiple threads can cause:

1. Race conditions

This occurs when the result depends on the order of operations and the order is not guaranteed.

2. Change visibility

When a thread changes value of a shared variable, it would only be written in a local cache of that thread; 
other threads will not have access to that new value (they will see the old value). 