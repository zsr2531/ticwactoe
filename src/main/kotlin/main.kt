import kotlin.math.max
import kotlin.math.min

fun minimax(board: Board): Int {
    // First, we check if the game is over, and return the score accordingly.
    if (board.isFinished) {
        return when(board.winner) {
            Side.Cross -> 1
            Side.Circle -> -1
            null -> 0
        }
    }

    // If the game isn't over yet, we loop over all possible moves from
    // the current position and try to find the best "value".
    var value: Int

    if (board.sideToMove == Side.Cross) {
        // Cross always tries to get the biggest score.
        value = -1000 // Effectively negative infinity, as the score of any position is in the set { -1, 0, 1 }.
        for (move in board.emptySlots())
            // If the score is bigger than anything we found previously, set the best score to the newly found one.
            value = max(value, minimax(board.makeMove(move)!!))
    } else {
        // Circle always tries to get the smallest score.
        value = 1000 // Effectively infinity, as the score of any position is in the set { -1, 0, 1 }.
        for (move in board.emptySlots())
            // If the score is lower than anything we found previously, set the best score to the newly found one.
            value = min(value, minimax(board.makeMove(move)!!))
    }

    // And now we return the score.
    return value
}

fun bestMove(board: Board): Int? {
    // There is no best move if the game is over... duh.
    if (board.isFinished)
        return null

    // Assume there is no best move yet.
    //            move  score
    var best: Pair<Int, Int>? = null

    if (board.sideToMove == Side.Cross) {
        for (move in board.emptySlots()) {
            val score = minimax(board.makeMove(move)!!)
            if (best == null || best.second < score)
                best = Pair(move, score)
        }
    } else {
        for (move in board.emptySlots()) {
            val score = minimax(board.makeMove(move)!!)
            if (best == null || best.second > score)
                best = Pair(move, score)
        }
    }

    // Return the best move that we found.
    return best!!.first
}

fun main() {
    var game = Board(Side.Cross)
    while (!game.isFinished) {
        println(game.toString())

        var move: Int

        if (game.sideToMove == Side.Circle) {
            move = bestMove(game)!!
        } else {
            println("Evaluation: " + minimax(game))
            while (true) {
                print("What's your move? ")
                val input = readLine() ?: continue
                val num = input.toIntOrNull()

                if (num is Int) {
                    move = num
                    break
                }
            }
        }

        val new = game.makeMove(move)
        if (new == null) {
            println("That's an invalid move!")
        } else {
            game = new
        }
    }

    println(game.toString())
    println(when(game.winner) {
        Side.Cross -> "Cross wins!"
        Side.Circle -> "Circle wins!"
        null -> "It's a draw!"
    })
}