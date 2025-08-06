package de.geosphere.speechplaning.ui.atoms

import androidx.annotation.DrawableRes
import de.geosphere.speechplaning.R
import de.geosphere.speechplaning.data.SpiritualStatus

/**
 * Provides avatar resources based on spiritual status.
 */
object AvatarProvider {

    /**
     * Retrieves the appropriate avatar drawable resource ID based on the provided spiritual status.
     *
     * @param spiritualStatus The spiritual status of the individual.  This should be an enum
     *                        `SpiritualStatus` which can be one of `ELDER`, `MINISTERIAL_SERVANT`, or other values.
     * @return An integer representing the drawable resource ID for the corresponding avatar.
     *         - `R.drawable.business_man_man_avatar_icon` for `SpiritualStatus.ELDER`
     *         - `R.drawable.man_avatar_male_icon` for `SpiritualStatus.MINISTERIAL_SERVANT`
     *         - `R.drawable.man_goatee_user_avatar_icon` for any other `SpiritualStatus` value (default).
     */
    @DrawableRes
    fun getAvatar(spiritualStatus: SpiritualStatus): Int {
        return when (spiritualStatus) {
            SpiritualStatus.ELDER -> R.drawable.business_man_man_avatar_icon
            SpiritualStatus.MINISTERIAL_SERVANT -> R.drawable.man_avatar_male_icon
            else -> R.drawable.man_goatee_user_avatar_icon
        }
    }
}
