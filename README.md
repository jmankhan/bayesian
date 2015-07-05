# Bayesian Theorem  Modeler
This program is designed to create a graphical representation of Bayes' Theorem and will hopefully be used as an educational tool for both computer science and mathematical learning. Perhaps I will port it to javascript once I learn it. I would like to post it online eventually, but japplets kind of suck.


The theorem in it's general form [can be viewed here.](https://en.wikipedia.org/wiki/Bayes%27_theorem#Extended_form "Bayes' Theorem")

The program uses the MVC design pattern, with the most basic model called BayesianModel, referring to each individual
probability. The hierarchy is as such: Main -> HypothesisHolder -> Hypothesis -> Base model (BayesianModel).
Each hierarchy component has an associated model, view, and controller.

Note: you can drag around the rectangles with your mouse. You can add new hypotheses, but not remove any (yet)

The lack of tests causes me no pain.
