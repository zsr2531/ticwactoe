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
        return when(side) {
            Side.Cross -> "X"
            Side.Circle -> "O"
        }
    }

    override fun hashCode(): Int = side.ordinal + 1
}
