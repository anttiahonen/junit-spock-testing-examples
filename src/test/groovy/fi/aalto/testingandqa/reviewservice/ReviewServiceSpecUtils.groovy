package fi.aalto.testingandqa.reviewservice

import fi.aalto.testingandqa.review.models.Review

class ReviewServiceSpecUtils {

    static Review buildDefaultReview_groovy25(){
        new Review().tap { //tap is a closure that groups actions on the object that it is called and returns the object
            title = "Movie of the century" //in groovy, setters can be accessed with dot-notation -> this is the same as setTitle("Movie of the century")
            body = "So good movie!"
        }
    }

    static Review buildDefaultReview_groovy24(){
        new Review().with { //with is a closure that groups actions on the object and returns the return value of last line
            title = "Movie of the century"
            body = "So good movie!"

            it //this is needed for .with closure to return the object itself
        }
    }

    static Review buildDefaultReview_objectGraphBuilder(){
        ObjectGraphBuilder builder = new ObjectGraphBuilder(classNameResolver: "fi.aalto.testingandqa.review.models") //set the package where the builded classes are with classNameResolver
        builder.review(title: "Movie of the century", body: "So good movie!") //uses a map to set fields, check for more complicated examples here: http://mrhaki.blogspot.com/2009/09/groovy-goodness-building-object-graphs.html
    }

}