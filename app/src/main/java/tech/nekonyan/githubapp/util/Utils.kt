package tech.nekonyan.githubapp.util

class Utils {
    companion object {
        @JvmStatic
        fun isNullOrBlank(string: String): Boolean {
            return string.isBlank()
        }
    }
}