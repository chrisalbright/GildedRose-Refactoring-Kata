My strategy was to add an Integration test for each
requirement, then to create a matching function and 
accompanying Unit test for each requirement, and
finally to replace the convoluted update function
with something much simpler.

This worked, per se, but still requires an Integration
test to ensure the application is working. I'd like
to reduce the testing surface down to only Unit tests
and something simple to ensure it is wired properly.

I'm not sure how to override an extension method in
Kotlin, or if its even possible, but perhaps it is
not a good strategy for this type of problem which
seems to require impurity. Could there be a better way?

I also don't love the implementation, it feels like too
much duplication. I will try to abstract some common
requirements better.