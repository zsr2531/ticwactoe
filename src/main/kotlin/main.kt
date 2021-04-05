import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun minimax(board: Board): Int {
    // First, we check if the game is over, and return the score accordingly.
    if (board.isFinished) {
        return when (board.winner) {
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
    var best: Int? = null
    val moves = MutableList(0) { 0 }

    // We loop over every possible move.
    for (move in board.emptySlots()) {
        // Calculate the score of the move using our minimax function.
        val score = minimax(board.makeMove(move)!!)

        // If there isn't a move yet, we don't need to check if
        // it's better than anything we found previously.
        if (best == null) {
            best = score
            moves.add(move)
            continue
        }

        // Check whether the current score is better than the best
        // score we found so far.
        val isBetter = when (board.sideToMove) {
            Side.Cross -> best < score
            Side.Circle -> best > score
        }

        // Set `best` accordingly.
        best = when {
            // If we have a new best score, clear the `moves` and add the newly found best move.
            isBetter -> {
                moves.clear()
                moves.add(move)
                score
            }
            // If the move is as good as the `best` score, add it as an alternative.
            best == score -> {
                moves.add(move)
                best
            }
            // Otherwise (the move is worse), don't do anything.
            else -> {
                best
            }
        }
    }

    // Return one of the best moves that we found.
    return moves[Random.nextInt(moves.size)]
}

fun main() {
    val side: Side
    while (true) {
        print("Cross or circle? [x/o] ")
        val input = readLine() ?: continue
        if (input != "x" && input != "o")
            continue

        side = when (input) {
            "x" -> Side.Cross
            "o" -> Side.Circle
            else -> throw Exception("Unreachable.")
        }
        break
    }

    var game = Board(Side.Cross)
    while (!game.isFinished) {
        var move: Int

        if (game.sideToMove != side) {
            move = bestMove(game)!!
        } else {
            println(game.toString())
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
    println(
        when (game.winner) {
            Side.Cross -> "Cross wins!"
            Side.Circle -> "Circle wins!"
            null -> "It's a draw!"
        }
    )
}
