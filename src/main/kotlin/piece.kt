enum class Side {
    Cross,
    Circle;

    fun opposite(): Side = when(this) {
        Cross -> Circle
        Circle -> Cross
    }
}

class Piece(val side: Side) {
    override fun equals(other: Any?): Boolean {
        return if (other is Piece) {
            other.side == side
        } else {
            false
        }
    }

    override fun toString(): String {
        return when (side) {
            Side.Cross -> "X"
            Side.Circle -> "O"
        }
    }

    // Not the best hashcode, but it's good enough for
    // what we need. Most of the "noise" will come
    // from the position of the piece.
    override fun hashCode(): Int = side.ordinal + 1
}
