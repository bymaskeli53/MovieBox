package com.example.moviebox

/**
 * Gender
 * Gender is coming from api as integer we need to convert it to string
 * @property labelResourceId
 *
 */
enum class Gender(
    val labelResourceId: Int,
) {
    UNKNOWN(R.string.unknown),
    FEMALE(R.string.female),
    MALE(R.string.male),
    NON_BINARY(R.string.non_binary),
    NO_DATA(R.string.no_data),
    ;

    companion object {
        fun fromInt(gender: Int): Gender =
            when (gender) {
                1 -> FEMALE
                2 -> MALE
                3 -> NON_BINARY
                0 -> UNKNOWN
                else -> NO_DATA
            }
    }
}
