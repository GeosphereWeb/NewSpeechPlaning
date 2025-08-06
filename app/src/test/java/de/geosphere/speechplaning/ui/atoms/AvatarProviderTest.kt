package de.geosphere.speechplaning.ui.atoms

import de.geosphere.speechplaning.R
import de.geosphere.speechplaning.data.SpiritualStatus
import org.junit.Test
import kotlin.test.assertEquals

class AvatarProviderTest {

    @Test
    fun `getAvatar returns correct avatar for ELDER status`() {
        // Arrange
        val status = SpiritualStatus.ELDER
        // Act & Assert
        assertEquals(R.drawable.business_man_man_avatar_icon, AvatarProvider.getAvatar(status))
    }

    @Test
    fun `getAvatar returns correct avatar for MINISTERIAL SERVANT status`() {
        // Verify that when SpiritualStatus.MINISTERIAL_SERVANT is passed, the function returns
        // R.drawable.man_avatar_male_icon.
        // Arrange
        val status = SpiritualStatus.MINISTERIAL_SERVANT
        // Act & Assert
        assertEquals(R.drawable.man_avatar_male_icon, AvatarProvider.getAvatar(status))
    }

    @Test
    fun `getAvatar returns correct avatar for UKNOWN status`() {
        // Verify that when SpiritualStatus.MINISTERIAL_SERVANT is passed, the function returns
        // R.drawable.man_avatar_male_icon.
        // Arrange
        val status = SpiritualStatus.UNKNOWN
        // Act & Assert
        assertEquals(R.drawable.man_goatee_user_avatar_icon, AvatarProvider.getAvatar(status))
    }
}
