fun main(args: Array<String>) {

//---------------------------------------------Lambda Expressions-------------------------------------------------------

    //val lambdaName : Type = { argumentList -> codeBody }

    // no return 1
    val lambda: (Int) -> Unit = { x -> println(x)}
    println("log 1: (no return1) -> lambda:")
    println(5)

    //no return 2
    val lambda1: (String) -> Unit = { name -> println("Hello, $name")}
    println("log 2: (no return 2) -> lambda1:")
    lambda1("Your Name")

    //inferred type 1
    val lambda2 = { name: String -> println("Hello, $name")}
    println("log 3: (inferred type1) -> lambda2:")
    lambda2("Your Name")

    //inferred type 2
    val lambda3 = { x: Int, y: Int ->
        println(x)
        println(y)
        x + y
    }
    println("log 4: (inferred type2) -> lambda3: ")
    println(lambda3(2,3))

    //inferred tupe 3
    val square = { number: Int -> number * number }
    val nine = square(3)
    println("log 5: (inferred type 3): ")
    println(nine)

   //returning from lambda
    val calculateGrade1 = { grade : Int ->
        when(grade) {
            in 0..40 -> "Fail"
            in 41..70 -> "Pass"
            in 71..100 -> "Distinction"
            else -> false
        }
    }
    println("log 6: (returning from lambda): ")
    println(calculateGrade1(42))

    val calculateGrade2 = fun(grade: Int): String {
        if (grade < 0 || grade > 100) {
            return "Error"
        } else if (grade < 40) {
            return "Fail"
        } else if (grade < 70) {
            return "Pass"
        }

        return "Distinction"
    }
    println("log 7: (returning from lambda): ")
    println(calculateGrade2(72))

    //it
    val array = arrayOf(1, 2, 3, 4 ,5, 6)

    println("log 8: (it): ")
    array.forEach { item -> println(item * 2) }
    array.forEach { println(it * 4) }

}
