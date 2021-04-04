class Board(val sideToMove: Side, private val pieces: Array<Piece?> = Array(9) { null }) {
    val winner: Side? get() = findWinner()

    // The game is over when either the board is full, or either player gets a connect-3.
    val isFinished: Boolean get() = pieces.all { p -> p != null } || findWinner() != null

    fun makeMove(pos: Int): Board? {
        // If the game has finished, no move is legal.
        if (isFinished)
            return null

        // If the index is out of range, the move is illegal.
        if (pos < 0 || pos >= 9)
            return null

        // If there is already a piece at `pos`, the move is illegal.
        if (pieces[pos] != null)
            return null

        // We make a copy of `pieces` and place the piece at `pos`.
        val clone = pieces.clone()
        clone[pos] = Piece(sideToMove)

        // And finally, we return a new `Board` instance with the
        // side to move set to our opponent and with the newly
        // created copy of `pieces`.
        return Board(sideToMove.opposite(), clone)
    }

    fun emptySlots(): List<Int> = (0..8).filter { p -> pieces[p] == null }

    override fun equals(other: Any?): Boolean {
        return if (other is Board) {
            hashCode() == other.hashCode()
        } else {
            false
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()

        for (i in 0..8) {
            builder.append(pieces[i].prettify())
            if ((i + 1) % 3 == 0)
                builder.append('\n')
            else
                builder.append(' ')
        }

        return builder.toString()
    }

    override fun hashCode(): Int {
        var hash = 0

        for (i in 0..8) {
            val p = pieces[i] ?: continue

            val num = 337 * ((i shl 6) - (p.side.ordinal + 1))
            hash = hash xor num
        }

        return hash + sideToMove.ordinal + 1
    }

    private fun Piece?.isSide(side: Side): Boolean = this?.side == side
    private fun Piece?.prettify(): String = this?.toString() ?: "."

    private fun findWinner(): Side? {
        // There can never be a victory if both sides only made 2 or less moves.
        if (pieces.count { p -> p != null } <= 4)
            return null

        fun horizontal(side: Side): Boolean {
            val first = pieces[0].isSide(side) && pieces[1].isSide(side) && pieces[2].isSide(side)
            val second = pieces[3].isSide(side) && pieces[4].isSide(side) && pieces[5].isSide(side)
            val third = pieces[6].isSide(side) && pieces[7].isSide(side) && pieces[8].isSide(side)

            return first || second || third
        }

        fun vertical(side: Side): Boolean {
            val first = pieces[0].isSide(side) && pieces[3].isSide(side) && pieces[6].isSide(side)
            val second = pieces[1].isSide(side) && pieces[4].isSide(side) && pieces[7].isSide(side)
            val third = pieces[2].isSide(side) && pieces[5].isSide(side) && pieces[8].isSide(side)

            return first || second || third
        }

        fun diagonal(side: Side): Boolean {
            // We can return early if the side doesn't own the middle slot.
            if (!pieces[4].isSide(side))
                return false

            val diagonal = pieces[0].isSide(side) && pieces[8].isSide(side)
            val antiDiagonal = pieces[2].isSide(side) && pieces[6].isSide(side)

            return diagonal || antiDiagonal
        }

        return when {
            // First we check if Cross won.
            horizontal(Side.Cross) || vertical(Side.Cross) || diagonal(Side.Cross) -> Side.Cross

            // If Cross didn't win, we check if Circle won.
            horizontal(Side.Circle) || vertical(Side.Circle) || diagonal(Side.Circle) -> Side.Circle

            // If neither won, we return nothing.
            else -> null
        }
    }
}