package mateusz.oleksik.smb_projekt_1.common

import java.util.*

class Utils {
    companion object {
        fun getUUID(): String {
            return UUID.randomUUID().toString()
        }
    }
}